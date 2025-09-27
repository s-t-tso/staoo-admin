package com.staoo.system.auth.strategy;

import com.staoo.common.auth.dto.LoginRequest;
import com.staoo.common.auth.dto.LoginResponse;

/**
 * 登录策略接口
 * 定义各种登录方式的通用行为
 */
public interface LoginStrategy {
    
    /**
     * 执行登录逻辑
     * @param request 登录请求
     * @return 登录响应
     */
    LoginResponse login(LoginRequest request);
    
    /**
     * 获取登录策略类型
     * @return 登录类型
     */
    String getLoginType();
    
    /**
     * 验证登录请求参数
     * @param request 登录请求
     * @throws IllegalArgumentException 参数验证失败时抛出
     */
    void validateRequest(LoginRequest request);
}