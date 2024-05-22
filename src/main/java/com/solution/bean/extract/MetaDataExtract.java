package com.solution.bean.extract;

import com.solution.bean.extract.entity.AnnotationInfo;
import com.solution.bean.extract.entity.BeanInfo;
import com.solution.bean.extract.entity.DubboMethodInfo;
import com.solution.bean.extract.entity.HttpMethodInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: hongyihui
 * @date: 2021/9/16 下午2:28
 */
public class MetaDataExtract {

    private static final Logger LOG = LoggerFactory.getLogger(AnnotationExtract.class);
    private static Map<String, BeanInfo> beanMap = new ConcurrentHashMap<>();
    private static Map<String, String> returnTypeMap = new ConcurrentHashMap<>();

    public static void httpClassExtract(HttpMethodInfo httpMethodInfo, Method method) {
        Annotation[] classAnnotation = method.getDeclaringClass().getAnnotations();
        if (classAnnotation != null) {
            for (Annotation annotation : classAnnotation) {
                AnnotationInfo annotationInfo = AnnotationExtract.classAnnotationExtract(annotation);
                if (annotationInfo != null) {
                    httpMethodInfo.addAnnotation(annotationInfo);
                }
            }
        }

        httpMethodInfo.setClassName(method.getDeclaringClass().getCanonicalName());
        httpMethodInfo.setMethodName(method.getName());

        Annotation[] annotations = method.getAnnotations();
        if (annotations != null) {
            for (Annotation annotation : annotations) {
                AnnotationInfo annotationInfo = AnnotationExtract.methodAnnotationExtract(annotation);
                if (annotationInfo != null) {
                    httpMethodInfo.addAnnotation(annotationInfo);
                }
            }
        }

        httpMethodInfo.setParameterInfoList(MethodExtract.buildParameterInfos(method));
        ClassExtract.getParameterTypeClassInfoList(method);

        String key = getMethodSignature(method);
        String returnType = MetaDataExtract.getReturnTypeMap().get(key);
        String genericSignature = method.getGenericReturnType().getTypeName();
        if (returnType != null) {
            if (genericSignature != null && genericSignature.length() < returnType.length()) {
                httpMethodInfo.setReturnType(returnType);
            } else {
                httpMethodInfo.setReturnType(genericSignature);
            }
        } else {
            httpMethodInfo.setReturnType(genericSignature);
        }

        ClassExtract.getReturnTypeClasInfo(method);
    }

    public static DubboMethodInfo dubboClassExtrace(String className, Method method) {
        DubboMethodInfo dubboMethodInfo = new DubboMethodInfo();
        dubboMethodInfo.setClassName(className);
        dubboMethodInfo.setMethodName(method.getName());

        dubboMethodInfo.setParameterInfoList(MethodExtract.buildNameNotEssentialParameterInfos(method));
        ClassExtract.getParameterTypeClassInfoList(method);

        dubboMethodInfo.setReturnType(method.getGenericReturnType().getTypeName());
        ClassExtract.getReturnTypeClasInfo(method);
        return dubboMethodInfo;
    }

    public static Map<String, BeanInfo> getBeanMap() {
        return beanMap;
    }

    public static Map<String, String> getReturnTypeMap() {
        return returnTypeMap;
    }

    public static String getMethodSignature(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        StringBuilder parameterStr = new StringBuilder();
        if (parameterTypes != null && parameterTypes.length != 0) {
            for (Class parameterType : parameterTypes) {
                parameterStr.append(parameterType.getTypeName());
            }
        }
        return method.getDeclaringClass().getTypeName() + "." + method.getName() + "." + parameterStr.toString();
    }

    private static ExecutorService executor = new ThreadPoolExecutor(1, 1, 60L,
            TimeUnit.SECONDS, new ArrayBlockingQueue(10), new ThreadPoolExecutor.DiscardPolicy());

    public static void setReturnTypeMap(HandlerMethodReturnValueHandler returnValueHandler, Object[] args) {
        try {
            if ("org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor".equals(
                    returnValueHandler.getClass().getTypeName())) {
                Object returnValue = args[0];
                MethodParameter returnType = (MethodParameter) args[1];
                setReturnTypeMap(returnValue, returnType);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void setReturnTypeMap(Object returnValue, MethodParameter returnType) {
        try {
//            if (returnValue instanceof JsonV2) {
//                JsonV2 jsonV2 = (JsonV2) returnValue;
//
//                // 如果status不为0，不需要解析JsonV2详细的泛型返回值
//                if (jsonV2.status != 0) {
//                    return;
//                }
//
//                executor.submit(new Thread(() -> {
//                    Method requestMappingMethod = returnType.getMethod();
//                    String key = MetaDataExtract.getMethodSignature(requestMappingMethod);
//                    StringBuilder sb = new StringBuilder();
//                    if (jsonV2.data != null) {
//                        sb.append("qunar.api.pojo.json.JsonV2<").append(jsonV2.data.getClass().getTypeName()).append(">");
//
//                        // LOG.info("处理数据:key=" + key + ", value=" + sb.toString());
//
//                        if (!returnTypeMap.containsKey(key)) {
//                            returnTypeMap.put(key, sb.toString());
//                        }
//                    }
//                }));
//            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}