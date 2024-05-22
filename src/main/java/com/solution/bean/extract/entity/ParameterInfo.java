package com.solution.bean.extract.entity;

import java.util.LinkedList;
import java.util.List;

/**
 * @author: hongyihui
 * @date: 2021/10/15 下午2:27
 */
public class ParameterInfo {

    /**
     * 参数名
     */
    private String name;
    /**
     * 参数类型
     */
    private String type;
    /**
     * 注解数组
     */
    private List<AnnotationInfo> annotations;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<AnnotationInfo> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<AnnotationInfo> annotations) {
        this.annotations = annotations;
    }

    public void addAnnotation(AnnotationInfo annotationInfo) {
        if (this.annotations == null) {
            this.annotations = new LinkedList<>();
        }
        this.annotations.add(annotationInfo);
    }
}