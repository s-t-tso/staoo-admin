package com.staoo.system.validator;

/**
 * 验证策略接口
 * 所有自定义验证策略都需要实现此接口
 * @param <T> 验证对象的类型
 */
public interface ValidationStrategy<T> {
    
    /**
     * 执行验证逻辑
     * @param object 需要验证的对象
     * @return 验证是否通过
     */
    boolean validate(T object);
    
    /**
     * 获取验证失败的错误信息
     * @return 错误信息
     */
    String getErrorMessage();
}