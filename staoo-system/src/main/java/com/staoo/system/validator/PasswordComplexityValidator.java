package com.staoo.system.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * 密码复杂度验证策略
 * 实现密码强度的验证逻辑
 */
@Component
public class PasswordComplexityValidator implements ValidationStrategy<String>, ConstraintValidator<PasswordComplexity, String> {
    
    private PasswordComplexity constraintAnnotation;
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("\\d");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");
    
    @Override
    public void initialize(PasswordComplexity constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }
    
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }
        
        // 使用注解中的参数进行验证
        if (password.length() < constraintAnnotation.minLength() || password.length() > constraintAnnotation.maxLength()) {
            return false;
        }
        
        boolean hasUppercase = !constraintAnnotation.requireUpperCase() || UPPERCASE_PATTERN.matcher(password).find();
        boolean hasLowercase = !constraintAnnotation.requireLowerCase() || LOWERCASE_PATTERN.matcher(password).find();
        boolean hasDigit = !constraintAnnotation.requireDigit() || DIGIT_PATTERN.matcher(password).find();
        boolean hasSpecialChar = !constraintAnnotation.requireSpecialChar() || SPECIAL_CHAR_PATTERN.matcher(password).find();
        
        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }
    
    @Override
    public boolean validate(String password) {
        // 对于ValidationStrategy接口的实现，使用默认参数
        if (password == null || password.length() < 8 || password.length() > 20) {
            return false;
        }
        
        boolean hasUppercase = UPPERCASE_PATTERN.matcher(password).find();
        boolean hasLowercase = LOWERCASE_PATTERN.matcher(password).find();
        boolean hasDigit = DIGIT_PATTERN.matcher(password).find();
        boolean hasSpecialChar = SPECIAL_CHAR_PATTERN.matcher(password).find();
        
        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }
    
    @Override
    public String getErrorMessage() {
        return "密码复杂度不符合要求，至少包含大小写字母、数字和特殊字符，长度8-20位";
    }
}