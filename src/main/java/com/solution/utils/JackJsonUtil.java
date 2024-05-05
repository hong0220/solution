package com.solution.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.Locale;
import java.util.TimeZone;

public class JackJsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 解决jpa/hibernate字段加上FetchType.LAZY注解序列化json数据异常
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // 属性值为null的不参与序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // json多字段，实体少字段，避免缺少某个字段属性出现问题序列化
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

//        // json进行换行缩进等操作
//        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        objectMapper.setLocale(Locale.CHINA);
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    }

    public static <T> String getJson(T obj) {
        String jsonStr = null;
        try {
            jsonStr = objectMapper.writeValueAsString(obj);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return jsonStr;
    }

    public static <T> T getObject(String json, TypeReference valueTypeRef) {
        T obj = null;
        try {
            obj = (T) objectMapper.readValue(json, valueTypeRef);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return obj;
    }
}