package com.solution.bean.extract.entity;

import java.util.List;

/**
 * @author: hongyihui
 * @date: 2021/9/16 下午2:44
 */
public class BeanInfo {

    /**
     * 类的类型
     */
    private String className;
    /**
     * 泛型类型列表
     */
    private List<String> genericType;
    /**
     * 字段类型列表
     */
    private List<FieldInfo> fieldInfoList;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<String> getGenericType() {
        return genericType;
    }

    public void setGenericType(List<String> genericType) {
        this.genericType = genericType;
    }

    public List<FieldInfo> getFieldInfoList() {
        return fieldInfoList;
    }

    public void setFieldInfoList(List<FieldInfo> fieldInfoList) {
        this.fieldInfoList = fieldInfoList;
    }
}