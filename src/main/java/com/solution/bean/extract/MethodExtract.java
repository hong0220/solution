package com.solution.bean.extract;

import com.solution.bean.extract.entity.AnnotationInfo;
import com.solution.bean.extract.entity.ParameterInfo;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: hongyihui
 * @date: 2021/9/16 下午2:29
 */
public class MethodExtract {

    /**
     * 获取JsonBody注解
     */
    public static boolean hasJsonBody(Method method) {
        Annotation[] annotations = method.getAnnotations();
        if (annotations != null && annotations.length != 0) {
            for (Annotation annotation : annotations) {
                if ("qunar.web.spring.annotation.JsonBody".equals(annotation.annotationType().getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取RequestBody注解
     */
    public static boolean hasRequestBody(Method method) {
        Annotation[][] annotationArray = method.getParameterAnnotations();
        if (annotationArray != null) {
            for (Annotation[] annotations : annotationArray) {
                for (Annotation annotation : annotations) {
                    if ("org.springframework.web.bind.annotation.RequestBody".equals(
                        annotation.annotationType().getName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static List<ParameterInfo> buildNameNotEssentialParameterInfos(Method method) {
        List<ParameterInfo> parameterInfoList = new ArrayList<>();

        final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
        final String[] parameterNames = nameDiscoverer.getParameterNames(method);
        final Type[] types = method.getGenericParameterTypes();
        if (types.length > 0) {
            for (Type type : types) {
                final ParameterInfo parameterInfo = new ParameterInfo();
                parameterInfo.setType(type.getTypeName());
                parameterInfoList.add(parameterInfo);
            }

            if (parameterNames != null && parameterNames.length == types.length) {
                for (int i = 0; i < types.length; ++i) {
                    parameterInfoList.get(i).setName(parameterNames[i]);
                }
            }
        }

        return parameterInfoList;
    }

    /**
     * 构建参数类型列表
     */
    public static List<ParameterInfo> buildParameterInfos(Method method) {
        List<ParameterInfo> parameterInfoList = new ArrayList<>();

        LocalVariableTableParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        // 参数名
        String[] paramArray = nameDiscoverer.getParameterNames(method);
        // 参数注解
        Annotation[][] annotationArray = method.getParameterAnnotations();
        // 参数类型
        Type[] parameterTypes = method.getGenericParameterTypes();

        if (paramArray != null && paramArray.length != 0) {
            for (int index = 0; index < paramArray.length; ++index) {
                ParameterInfo parameterInfo = new ParameterInfo();
                String name = paramArray[index];
                Annotation[] annotations = annotationArray[index];
                if (annotations != null) {
                    for (Annotation annotation : annotations) {
                        AnnotationInfo annotationInfo = AnnotationExtract.parameterAnnotationExtract(annotation);
                        if (annotationInfo != null) {
                            parameterInfo.addAnnotation(annotationInfo);
                        }
                    }
                }

                parameterInfo.setName(name);
                parameterInfo.setType(parameterTypes[index].getTypeName());
                parameterInfoList.add(parameterInfo);
            }
        }
        return parameterInfoList;
    }
}