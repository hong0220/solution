package com.solution.bean.extract.entity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * @author: hongyihui
 * @date: 2021/9/16 下午6:06
 */
public enum DataTypeEnum {
    VOID("Void"),
    ENUM("Enum"), // 枚举
    INTERFACE("Interface"), // 接口
    ARRAY("Array"), // 数组
    GENERIC_TYPE("GenericType"), // 泛型
    TYPE_VARIABLE("TypeVariable"), // 泛型变量
    BASIC_TYPE("BasicType"), // 基本类型，包装类，Object类
    OBJECT_TYPE("ObjectType"); // 对象类型

    private final String type;

    DataTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static DataTypeEnum judgeDataType(Class<?> clazz, Type type) {
        try {
            // 先判断type，再判断class。因为clazz有时候不能准确判断类型，直接是java.lang.Object
            if (type instanceof ParameterizedType) {
                return DataTypeEnum.GENERIC_TYPE;
            }
            if (type instanceof TypeVariable) {
                return DataTypeEnum.TYPE_VARIABLE;
            }

            if (clazz.getName().equals("void")) {
                return VOID;
            }
            if (clazz.isEnum()) {
                return DataTypeEnum.ENUM;
            }
            if (clazz.isInterface()) {
                return DataTypeEnum.INTERFACE;
            }
            if (clazz.isArray()) {
                return DataTypeEnum.ARRAY;
            }
            if (TypeConstants.BASIC_CLASS_STRING.contains(clazz.getName())) {
                return DataTypeEnum.BASIC_TYPE;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DataTypeEnum.OBJECT_TYPE;
    }
}