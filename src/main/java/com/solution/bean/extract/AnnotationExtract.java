package com.solution.bean.extract;

import com.solution.bean.extract.entity.AnnotationInfo;

import java.lang.annotation.Annotation;

/**
 * @author: hongyihui
 * @date: 2021/10/21 下午2:49
 */
public class AnnotationExtract {

    private static final String RestController = "org.springframework.web.bind.annotation.RestController";
    private static final String JsonBody = "qunar.web.spring.annotation.JsonBody";
    private static final String GetMapping = "org.springframework.web.bind.annotation.GetMapping";
    private static final String PostMapping = "org.springframework.web.bind.annotation.PostMapping";
    private static final String PutMapping = "org.springframework.web.bind.annotation.PutMapping";
    private static final String DeleteMapping = "org.springframework.web.bind.annotation.DeleteMapping";
    private static final String ResponseBody = "org.springframework.web.bind.annotation.ResponseBody";
    private static final String RequestMapping = "org.springframework.web.bind.annotation.RequestMapping";
    private static final String ModelAttribute = "org.springframework.web.bind.annotation.ModelAttribute";
    private static final String RequestParam = "org.springframework.web.bind.annotation.RequestParam";
    private static final String RequestBody = "org.springframework.web.bind.annotation.RequestBody";
    private static final String PathVariable = "org.springframework.web.bind.annotation.PathVariable";
    private static final String CookieValue = "org.springframework.web.bind.annotation.CookieValue";
    private static final String RequestHeader = "org.springframework.web.bind.annotation.RequestHeader";

    /**
     * 处理类上的注解
     */
    public static AnnotationInfo classAnnotationExtract(Annotation annotation) {
        if (RestController.equals(annotation.annotationType().getName())) {
            AnnotationInfo annotationInfo = new AnnotationInfo();
            annotationInfo.setKey(annotation.annotationType().getName());
            return annotationInfo;
        }
        return null;
    }

    /**
     * 处理方法上的注解
     */
    public static AnnotationInfo methodAnnotationExtract(Annotation annotation) {
        if (GetMapping.equals(annotation.annotationType().getName())) {
            org.springframework.web.bind.annotation.GetMapping getMapping = (org.springframework.web.bind.annotation.GetMapping) annotation;

            AnnotationInfo annotationInfo = new AnnotationInfo();
            annotationInfo.setKey(annotation.annotationType().getName());
            annotationInfo.getContent().put("name", getMapping.name());
            annotationInfo.getContent().put("value", getMapping.value());
            annotationInfo.getContent().put("path", getMapping.path());
            annotationInfo.getContent().put("params", getMapping.params());
            annotationInfo.getContent().put("headers", getMapping.headers());
            try {
                annotationInfo.getContent().put("consumes", getMapping.consumes());
            } catch (Throwable t) {
                // spring低版本不支持
            }
            annotationInfo.getContent().put("produces", getMapping.produces());
            return annotationInfo;
        } else if (PostMapping.equals(annotation.annotationType().getName())) {
            org.springframework.web.bind.annotation.PostMapping postMapping = (org.springframework.web.bind.annotation.PostMapping) annotation;

            AnnotationInfo annotationInfo = new AnnotationInfo();
            annotationInfo.setKey(annotation.annotationType().getName());
            annotationInfo.getContent().put("name", postMapping.name());
            annotationInfo.getContent().put("value", postMapping.value());
            annotationInfo.getContent().put("path", postMapping.path());
            annotationInfo.getContent().put("params", postMapping.params());
            annotationInfo.getContent().put("headers", postMapping.headers());
            annotationInfo.getContent().put("consumes", postMapping.consumes());
            annotationInfo.getContent().put("produces", postMapping.produces());
            return annotationInfo;
        } else if (PutMapping.equals(annotation.annotationType().getName())) {
            org.springframework.web.bind.annotation.PutMapping putMapping = (org.springframework.web.bind.annotation.PutMapping) annotation;

            AnnotationInfo annotationInfo = new AnnotationInfo();
            annotationInfo.setKey(annotation.annotationType().getName());
            annotationInfo.getContent().put("name", putMapping.name());
            annotationInfo.getContent().put("value", putMapping.value());
            annotationInfo.getContent().put("path", putMapping.path());
            annotationInfo.getContent().put("params", putMapping.params());
            annotationInfo.getContent().put("headers", putMapping.headers());
            annotationInfo.getContent().put("consumes", putMapping.consumes());
            annotationInfo.getContent().put("produces", putMapping.produces());
            return annotationInfo;
        } else if (DeleteMapping.equals(annotation.annotationType().getName())) {
            org.springframework.web.bind.annotation.DeleteMapping deleteMapping = (org.springframework.web.bind.annotation.DeleteMapping) annotation;

            AnnotationInfo annotationInfo = new AnnotationInfo();
            annotationInfo.setKey(annotation.annotationType().getName());
            annotationInfo.getContent().put("name", deleteMapping.name());
            annotationInfo.getContent().put("value", deleteMapping.value());
            annotationInfo.getContent().put("path", deleteMapping.path());
            annotationInfo.getContent().put("params", deleteMapping.params());
            annotationInfo.getContent().put("headers", deleteMapping.headers());
            annotationInfo.getContent().put("consumes", deleteMapping.consumes());
            annotationInfo.getContent().put("produces", deleteMapping.produces());
            return annotationInfo;
        } else if (ResponseBody.equals(annotation.annotationType().getName())) {
            AnnotationInfo annotationInfo = new AnnotationInfo();
            annotationInfo.setKey(annotation.annotationType().getName());
            return annotationInfo;
        } else if (JsonBody.equals(annotation.annotationType().getName())) {
            AnnotationInfo annotationInfo = new AnnotationInfo();
            annotationInfo.setKey(JsonBody);
            return annotationInfo;
        } else if (RequestMapping.equals(annotation.annotationType().getName())) {
            org.springframework.web.bind.annotation.RequestMapping requestMapping = (org.springframework.web.bind.annotation.RequestMapping) annotation;

            AnnotationInfo annotationInfo = new AnnotationInfo();
            annotationInfo.setKey(annotation.annotationType().getName());
            try {
                annotationInfo.getContent().put("name", requestMapping.name());
                annotationInfo.getContent().put("path", requestMapping.path());
            } catch (Throwable t) {
                // spring低版本不支持
            }
            annotationInfo.getContent().put("value", requestMapping.value());
            annotationInfo.getContent().put("method", requestMapping.method());
            annotationInfo.getContent().put("params", requestMapping.params());
            annotationInfo.getContent().put("headers", requestMapping.headers());
            annotationInfo.getContent().put("consumes", requestMapping.consumes());
            annotationInfo.getContent().put("produces", requestMapping.produces());
            return annotationInfo;
        } else if (ModelAttribute.equals(annotation.annotationType().getName())) {
            org.springframework.web.bind.annotation.ModelAttribute modelAttribute = (org.springframework.web.bind.annotation.ModelAttribute) annotation;

            AnnotationInfo annotationInfo = new AnnotationInfo();
            annotationInfo.setKey(annotation.annotationType().getName());
            try {
                annotationInfo.getContent().put("name", modelAttribute.name());
            } catch (Throwable t) {
                // spring低版本不支持
            }
            annotationInfo.getContent().put("value", modelAttribute.value());
            return annotationInfo;
        }
        return null;
    }

    /**
     * 处理方法参数上的注解
     */
    public static AnnotationInfo parameterAnnotationExtract(Annotation annotation) {
        if (RequestParam.equals(annotation.annotationType().getName())) {
            org.springframework.web.bind.annotation.RequestParam requestParam = (org.springframework.web.bind.annotation.RequestParam) annotation;

            AnnotationInfo annotationInfo = new AnnotationInfo();
            annotationInfo.setKey(annotation.annotationType().getName());
            try {
                annotationInfo.getContent().put("name", requestParam.name());
            } catch (Throwable t) {
                // spring低版本不支持
            }
            annotationInfo.getContent().put("value", requestParam.value());
            return annotationInfo;
        } else if (RequestBody.equals(annotation.annotationType().getName())) {
            org.springframework.web.bind.annotation.RequestBody requestBody = (org.springframework.web.bind.annotation.RequestBody) annotation;

            AnnotationInfo annotationInfo = new AnnotationInfo();
            annotationInfo.setKey(annotation.annotationType().getName());
            return annotationInfo;
        } else if (PathVariable.equals(annotation.annotationType().getName())) {
            org.springframework.web.bind.annotation.PathVariable pathVariable = (org.springframework.web.bind.annotation.PathVariable) annotation;

            AnnotationInfo annotationInfo = new AnnotationInfo();
            annotationInfo.setKey(annotation.annotationType().getName());
            try {
                annotationInfo.getContent().put("name", pathVariable.name());
            } catch (Throwable t) {
                // spring低版本不支持
            }
            annotationInfo.getContent().put("value", pathVariable.value());
            return annotationInfo;
        } else if (CookieValue.equals(annotation.annotationType().getName())) {
            org.springframework.web.bind.annotation.CookieValue cookieValue = (org.springframework.web.bind.annotation.CookieValue) annotation;

            AnnotationInfo annotationInfo = new AnnotationInfo();
            annotationInfo.setKey(annotation.annotationType().getName());
            try {
                annotationInfo.getContent().put("name", cookieValue.name());
            } catch (Throwable t) {
                // spring低版本不支持
            }
            annotationInfo.getContent().put("value", cookieValue.value());
            return annotationInfo;
        } else if (RequestHeader.equals(annotation.annotationType().getName())) {
            org.springframework.web.bind.annotation.RequestHeader requestHeader = (org.springframework.web.bind.annotation.RequestHeader) annotation;

            AnnotationInfo annotationInfo = new AnnotationInfo();
            annotationInfo.setKey(annotation.annotationType().getName());
            try {
                annotationInfo.getContent().put("name", requestHeader.name());
            } catch (Throwable t) {
                // spring低版本不支持
            }
            annotationInfo.getContent().put("value", requestHeader.value());
            return annotationInfo;
        } else if (ModelAttribute.equals(annotation.annotationType().getName())) {
            org.springframework.web.bind.annotation.ModelAttribute modelAttribute = (org.springframework.web.bind.annotation.ModelAttribute) annotation;

            AnnotationInfo annotationInfo = new AnnotationInfo();
            annotationInfo.setKey(annotation.annotationType().getName());
            try {
                annotationInfo.getContent().put("name", modelAttribute.name());
            } catch (Throwable t) {
                // spring低版本不支持
            }
            annotationInfo.getContent().put("value", modelAttribute.value());
            return annotationInfo;
        }
        return null;
    }
}