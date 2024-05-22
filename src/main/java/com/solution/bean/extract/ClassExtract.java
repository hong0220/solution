package com.solution.bean.extract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.solution.bean.extract.entity.BeanInfo;
import com.solution.bean.extract.entity.DataTypeEnum;
import com.solution.bean.extract.entity.FieldInfo;
import com.solution.bean.extract.entity.TypeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: hongyihui
 * @date: 2021/9/16 下午2:27
 */
public class ClassExtract {

    private static final Logger LOG = LoggerFactory.getLogger(ClassExtract.class);

    public static void getParameterTypeClassInfoList(Method method) {
        int parameterCount = method.getParameterCount();
        Parameter[] parameters = method.getParameters();
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        for (int index = 0; index < parameterCount; ++index) {
            Type genericParameterType = genericParameterTypes[index];
            Parameter parameter = parameters[index];
            Class<?> clazz = parameter.getType();
            ClassExtract.classExtract(clazz, genericParameterType);
        }
    }

    public static void getReturnTypeClasInfo(Method method) {
        Type genericReturnType = method.getGenericReturnType();
        Class<?> returnClass = method.getReturnType();
        ClassExtract.classExtract(returnClass, genericReturnType);
    }

    /**
     * 类型信息抽取
     */
    public static void classExtract(Class<?> clazz, Type type) {
        DataTypeEnum dataTypeEnum = DataTypeEnum.judgeDataType(clazz, type);
        // 终止递归条件
        if (dataTypeEnum == DataTypeEnum.BASIC_TYPE) { // 基本类型，包装类，Object类
            return;
        }
        // 终止递归条件
        if (dataTypeEnum == DataTypeEnum.VOID) { // 枚举
            return;
        }
        // 终止递归条件
        if (dataTypeEnum == DataTypeEnum.TYPE_VARIABLE) { // 泛型变量
            return;
        }

        if (dataTypeEnum != DataTypeEnum.GENERIC_TYPE) {
            // 避免循环依赖
            if (clazz != null) {
                if (MetaDataExtract.getBeanMap().get(clazz.getTypeName()) != null) {
                    return;
                }
            }
        }

        if (dataTypeEnum == DataTypeEnum.INTERFACE) { // 接口
            handleInterface(clazz);
        } else if (dataTypeEnum == DataTypeEnum.ENUM) { // 枚举
            handleEnum(clazz);
        } else if (dataTypeEnum == DataTypeEnum.ARRAY) { // 数组
            handleArray(type);
        } else if (dataTypeEnum == DataTypeEnum.GENERIC_TYPE) { // 泛型
            handleGenericType((ParameterizedType) type);
        } else if (dataTypeEnum == DataTypeEnum.OBJECT_TYPE) { // 对象类型
            handleObjectType(clazz);
        }
    }

    public static void handleInterface(Class<?> clazz) {
        // 过滤
        if (!clazz.getTypeName().contains(TypeConstants.FILTER_PACKAGE)) {
            return;
        }
        MetaDataExtract.getBeanMap().put(clazz.getTypeName(), new BeanInfo());

        List<FieldInfo> fieldInfoList = new LinkedList<>();
        // 获取继承的所有字段
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            Class<?> fieldClass = field.getType();
            Type fieldType = field.getGenericType();

            FieldInfo fieldInfo = new FieldInfo();
            // 字段名
            fieldInfo.setFieldName(getFieldName(field));
            // 字段类型
            fieldInfo.setFieldType(fieldType.getTypeName());
            // 字段数据类型
            DataTypeEnum dataTypeEnum = DataTypeEnum.judgeDataType(fieldClass, fieldType);
            fieldInfo.setDataType(dataTypeEnum.getType());
            if (dataTypeEnum == DataTypeEnum.ARRAY) {
                fieldInfo.setArrayDimension(getArrayDimension(fieldClass.getName()));
            }

            // 递归
            classExtract(fieldClass, fieldType);

            fieldInfoList.add(fieldInfo);
        }

        BeanInfo beanInfo = new BeanInfo();
        beanInfo.setClassName(clazz.getTypeName());
        beanInfo.setGenericType(getGenericString(clazz));
        beanInfo.setFieldInfoList(fieldInfoList);
        MetaDataExtract.getBeanMap().put(beanInfo.getClassName(), beanInfo);
    }

    /**
     * 枚举不用递归
     */
    public static void handleEnum(Class<?> clazz) {
        // 过滤
        if (!clazz.getTypeName().contains(TypeConstants.FILTER_PACKAGE)) {
            return;
        }
        MetaDataExtract.getBeanMap().put(clazz.getTypeName(), new BeanInfo());

        List<FieldInfo> fieldInfoList = new LinkedList<>();
        // 枚举没有继承字段
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Class<?> fieldClass = field.getType();
            Type fieldType = field.getGenericType();

            FieldInfo fieldInfo = new FieldInfo();
            // 字段名
            fieldInfo.setFieldName(getFieldName(field));
            // 字段类型
            fieldInfo.setFieldType(fieldType.getTypeName());
            // 字段数据类型
            DataTypeEnum dataTypeEnum = DataTypeEnum.judgeDataType(fieldClass, fieldType);
            fieldInfo.setDataType(dataTypeEnum.getType());
            if (dataTypeEnum == DataTypeEnum.ARRAY) {
                fieldInfo.setArrayDimension(getArrayDimension(fieldClass.getName()));
            }

            // 递归
            classExtract(fieldClass, fieldType);

            fieldInfoList.add(fieldInfo);
        }

        BeanInfo beanInfo = new BeanInfo();
        beanInfo.setClassName(clazz.getTypeName());
        beanInfo.setFieldInfoList(fieldInfoList);
        MetaDataExtract.getBeanMap().put(beanInfo.getClassName(), beanInfo);
    }

    private static void handleArray(Type type) {
        // 泛型数组
        // 比如 java.util.List<java.lang.Integer>[]，T[][]
        if (type instanceof GenericArrayType) {
            // 取到最内部的类，防止出现多级数组，多级解析
            // int arrayDimension = 0;
            Type realType = type;
            while (realType instanceof GenericArrayType &&
                ((GenericArrayType) realType).getGenericComponentType() != null) {
                realType = ((GenericArrayType) realType).getGenericComponentType();
                // arrayDimension++;
            }
            if (realType instanceof ParameterizedType) {
                // 递归
                handleGenericType((ParameterizedType) realType);
            } else if (realType instanceof TypeVariable) {
                // 不处理
            }
        } else {  // 基本类型数组，枚举数组，接口数组，对象类型数组
            // 取到最内部的类，防止出现多级数组，多级解析
            // 只能使用Class.forName创建Class
            String arrayName = ((Class) type).getName();
            // int arrayDimension = getArrayDimension(arrayName);
            Class<?> realClazz = getClass(arrayName);

            // 递归
            classExtract(realClazz, null);
        }
    }

    private static void handleGenericType(ParameterizedType genericType) {
        // 里层数组单独解析
        Type[] actualTypeArray = genericType.getActualTypeArguments();
        for (Type actualType : actualTypeArray) {
            if (actualType instanceof ParameterizedType) { // 泛型
                // 递归
                handleGenericType((ParameterizedType) actualType);
            } else if (actualType instanceof TypeVariable) { // 处理 Map<T, java.lang.String>
                // 不处理
            } else if (actualType instanceof WildcardType) { // 处理 java.lang.ref.ReferenceQueue<? super T>
                // 不处理
            } else if (actualType instanceof GenericArrayType ||
                ((actualType instanceof Class) && ((Class) actualType).isArray())) { // 泛型数组和非泛型数组
                // 递归
                handleArray(actualType);
            } else {
                // Class<?> actualClass = Class.forName(actualType.getTypeName());  等价下面
                Class<?> actualClass = (Class<?>) actualType;

                // 递归
                classExtract(actualClass, actualType);
            }
        }

        // 外层单独解析
        // Class<?> realClazz = Class.forName(genericType.getRawType().getTypeName()); 等价下面
        Class<?> realClazz = (Class) (genericType.getRawType());

        // 递归
        classExtract(realClazz, null);
    }

    private static void handleObjectType(Class<?> clazz) {
        // 过滤
        if (!clazz.getTypeName().contains(TypeConstants.FILTER_PACKAGE)) {
            return;
        }
        MetaDataExtract.getBeanMap().put(clazz.getTypeName(), new BeanInfo());

        List<FieldInfo> fieldInfoList = new LinkedList<>();
        // 获取继承的父类字段，不包括Object
        List<Field> fieldList = new LinkedList<>();
        Class tempClass = clazz;
        while (tempClass != null && !"java.lang.object".equals(tempClass.getName().toLowerCase())) {
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass();
        }
        for (Field field : fieldList) {
            Class<?> fieldClass = field.getType();
            Type fieldType = field.getGenericType();

            FieldInfo fieldInfo = new FieldInfo();
            // 字段名
            fieldInfo.setFieldName(getFieldName(field));
            // 字段类型
            fieldInfo.setFieldType(fieldType.getTypeName());
            // 字段数据类型
            DataTypeEnum dataTypeEnum = DataTypeEnum.judgeDataType(fieldClass, fieldType);
            fieldInfo.setDataType(dataTypeEnum.getType());
            if (dataTypeEnum == DataTypeEnum.ARRAY) {
                fieldInfo.setArrayDimension(getArrayDimension(fieldClass.getName()));
            }

            // 递归
            classExtract(fieldClass, fieldType);

            fieldInfoList.add(fieldInfo);
        }

        BeanInfo beanInfo = new BeanInfo();
        beanInfo.setClassName(clazz.getTypeName());
        beanInfo.setGenericType(getGenericString(clazz));
        beanInfo.setFieldInfoList(fieldInfoList);
        MetaDataExtract.getBeanMap().put(beanInfo.getClassName(), beanInfo);
    }

    private static Class<?> getClass(String className) {
        int index = 0;
        for (; index < className.length(); index++) {
            if (className.charAt(index) == '[') {
                continue;
            }
            if (className.charAt(index) == 'B') {
                return byte.class;
            }
            if (className.charAt(index) == 'C') {
                return char.class;
            }
            if (className.charAt(index) == 'D') {
                return double.class;
            }
            if (className.charAt(index) == 'F') {
                return float.class;
            }
            if (className.charAt(index) == 'I') {
                return int.class;
            }
            if (className.charAt(index) == 'J') {
                return long.class;
            }
            if (className.charAt(index) == 'S') {
                return short.class;
            }
            if (className.charAt(index) == 'Z') {
                return boolean.class;
            }
            if (className.charAt(index) == 'V') {
                return void.class;
            }
            // 对象类型，比如：[[Lcom.qunar.framework.bean.Address;
            if (className.charAt(index) == 'L') {
                className = className.substring(index + 1, className.length() - 1);
                try {
                    return Class.forName(className);
                } catch (Exception e) {
                    LOG.error("Class.forName error, className={}", className, e);
                }
            }
        }
        return null;
    }

    /**
     * 获取数组维度，一维数组，二维数组
     */
    private static int getArrayDimension(String className) {
        int index = 0;
        for (; index < className.length(); index++) {
            if (className.charAt(index) != '[') {
                break;
            }
        }
        return index;
    }

    public static List<String> getGenericString(Class<?> clazz) {
        List<String> genericType = new LinkedList<>();
        TypeVariable<?>[] typeparms = clazz.getTypeParameters();
        if (typeparms != null && typeparms.length != 0) {
            for (TypeVariable<?> typeparm : typeparms) {
                genericType.add(typeparm.getTypeName());
            }
        }
        return genericType;
    }

    private static final String JSON_PROPERTY = "com.fasterxml.jackson.annotation.JsonProperty";

    private static String getFieldName(Field field) {
        Annotation[] annotations = field.getAnnotations();
        if (annotations != null && annotations.length != 0) {
            for (Annotation annotation : annotations) {
                if (JSON_PROPERTY.equals(annotation.annotationType().getName())) {
                    return ((JsonProperty) annotation).value();
                }
            }
        }
        return field.getName();
    }
}