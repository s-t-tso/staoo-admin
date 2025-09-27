package com.staoo.system.validator;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 验证管理器
 * 使用策略模式管理和执行各种验证策略
 * @param <T> 验证对象的类型
 */
@Component
public class ValidationManager<T> {
    
    private final List<ValidationStrategy<T>> validationStrategies = new ArrayList<>();
    
    /**
     * 添加验证策略
     * @param strategy 验证策略
     */
    public void addValidationStrategy(ValidationStrategy<T> strategy) {
        if (strategy != null) {
            validationStrategies.add(strategy);
        }
    }
    
    /**
     * 执行所有验证策略
     * @param object 需要验证的对象
     * @return 验证结果对象，包含是否通过和错误信息
     */
    public ValidationResult validate(T object) {
        List<String> errorMessages = new ArrayList<>();
        
        for (ValidationStrategy<T> strategy : validationStrategies) {
            if (!strategy.validate(object)) {
                errorMessages.add(strategy.getErrorMessage());
            }
        }
        
        return new ValidationResult(errorMessages.isEmpty(), errorMessages);
    }
    
    /**
     * 验证结果类
     * 包含验证是否通过和错误信息列表
     */
    public static class ValidationResult {
        private final boolean isValid;
        private final List<String> errorMessages;
        
        public ValidationResult(boolean isValid, List<String> errorMessages) {
            this.isValid = isValid;
            this.errorMessages = errorMessages != null ? errorMessages : new ArrayList<>();
        }
        
        public boolean isValid() {
            return isValid;
        }
        
        public List<String> getErrorMessages() {
            return errorMessages;
        }
        
        public String getFirstErrorMessage() {
            return errorMessages.isEmpty() ? null : errorMessages.get(0);
        }
    }
}