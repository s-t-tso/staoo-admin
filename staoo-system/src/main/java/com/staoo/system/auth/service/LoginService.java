package com.staoo.system.auth.service;

import com.staoo.common.auth.dto.LoginRequest;
import com.staoo.common.auth.dto.LoginResponse;

/**
 * 登录服务接口
 * 处理登录相关的业务逻辑
 */
public interface LoginService {
    /**
     * 用户登录
     * @param request 登录请求参数
     * @return 登录响应
     */
    LoginResponse login(LoginRequest request);
    
    /**
     * 处理登录失败
     * @param username 用户名
     * @return 登录失败信息
     */
    void handleLoginFailure(String username);
    
    /**
     * 检查用户是否被锁定
     * @param username 用户名
     * @return 是否被锁定
     */
    boolean isUserLocked(String username);
    
    /**
     * 记录登录日志
     * @param username 用户名
     * @param status 登录状态
     * @param message 登录消息
     * @param ip IP地址
     * @param userAgent 用户代理
     */
    void recordLoginLog(String username, boolean status, String message, String ip, String userAgent);
}