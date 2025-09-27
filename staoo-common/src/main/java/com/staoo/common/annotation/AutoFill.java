package com.staoo.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动填充注解
 * 用于标记需要自动填充创建人、创建时间、更新人和更新时间的实体类
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    /**
     * 是否填充创建人字段
     */
    boolean fillCreateBy() default true;
    
    /**
     * 是否填充创建时间字段
     */
    boolean fillCreateTime() default true;
    
    /**
     * 是否填充更新人字段
     */
    boolean fillUpdateBy() default true;
    
    /**
     * 是否填充更新时间字段
     */
    boolean fillUpdateTime() default true;
}