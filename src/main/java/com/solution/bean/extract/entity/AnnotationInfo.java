package com.solution.bean.extract.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: hongyihui
 * @date: 2021/10/21 上午11:42
 */
public class AnnotationInfo {

    private String key;
    private Map<String, Object> content = new HashMap<>();

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, Object> getContent() {
        return content;
    }

    public void setContent(Map<String, Object> content) {
        this.content = content;
    }
}