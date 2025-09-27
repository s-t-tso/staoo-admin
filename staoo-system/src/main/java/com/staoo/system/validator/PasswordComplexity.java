package com.staoo.system.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 密码复杂度验证注解
 * 用于验证密码是否符合指定的复杂度要求
 */
@Documented
@Constraint(validatedBy = PasswordComplexityValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordComplexity {
    // 默认错误消息
    String message() default "密码复杂度不符合要求，至少包含大小写字母、数字和特殊字符，长度8-20位";

    // 是否需要大写字母
    boolean requireUpperCase() default true;

    // 是否需要小写字母
    boolean requireLowerCase() default true;

    // 是否需要数字
    boolean requireDigit() default true;

    // 是否需要特殊字符
    boolean requireSpecialChar() default true;

    // 最小长度
    int minLength() default 8;

    // 最大长度
    int maxLength() default 20;

    // 验证组
    Class<?>[] groups() default {};

    // 负载
    Class<? extends Payload>[] payload() default {};
}