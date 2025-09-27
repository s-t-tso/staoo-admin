package com.staoo.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作日志注解
 * 用于标记需要记录操作日志的方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogOperation {
    /**
     * 操作模块
     */
    String module() default "";

    /**
     * 操作类型
     */
    String operationType() default "";

    /**
     * 操作内容
     */
    String content() default "";

    /**
     * 是否记录请求参数
     */
    boolean recordRequestParams() default true;

    /**
     * 是否记录请求体
     */
    boolean recordRequestBody() default false;

    /**
     * 是否记录响应结果
     */
    boolean recordResponseResult() default false;

    /**
     * 是否忽略异常（异常时不记录日志）
     */
    boolean ignoreException() default false;
}