package com.solution.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginCheckInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 设置拦截器拦截的请求路径(/** 表示拦截所有请求)
        registry.addInterceptor(loginCheckInterceptor).addPathPatterns("/**");
    }
}