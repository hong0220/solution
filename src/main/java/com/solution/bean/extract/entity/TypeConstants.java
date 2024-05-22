package com.solution.bean.extract.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: hongyihui
 * @date: 2021/9/16 下午2:15
 */
public class TypeConstants {

    public static final Set<String> BASIC_CLASS_STRING = new HashSet<String>() {{
        add("byte");
        add("java.lang.Byte");
        add("short");
        add("java.lang.Short");
        add("int");
        add("java.lang.Integer");
        add("long");
        add("java.lang.Long");
        add("float");
        add("java.lang.Float");
        add("double");
        add("java.lang.Double");
        add("char");
        add("java.lang.Character");
        add("java.lang.String");
        add("boolean");
        add("java.lang.Boolean");
        add("java.lang.Object");
    }};

    public static final String FILTER_PACKAGE = "qunar";
}