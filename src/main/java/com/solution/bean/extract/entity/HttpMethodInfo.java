package com.solution.bean.extract.entity;

import java.util.LinkedList;
import java.util.List;

/**
 * DispatcherServlet相关mapping描述
 */
public class HttpMethodInfo {

    /**
     * 类名
     */
    private String className;
    /**
     * 函数名
     */
    private String methodName;
    /**
     * 注解数组
     */
    private List<AnnotationInfo> annotations;
    /**
     * url地址
     */
    private String url;
    /**
     * 参数类型列表
     */
    private List<ParameterInfo> parameterInfoList;
    /**
     * 返回类型
     */
    private String returnType;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<AnnotationInfo> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<AnnotationInfo> annotations) {
        this.annotations = annotations;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<ParameterInfo> getParameterInfoList() {
        return parameterInfoList;
    }

    public void setParameterInfoList(List<ParameterInfo> parameterInfoList) {
        this.parameterInfoList = parameterInfoList;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public void addAnnotation(AnnotationInfo annotationInfo) {
        if (this.annotations == null) {
            this.annotations = new LinkedList<>();
        }
        this.annotations.add(annotationInfo);
    }
}