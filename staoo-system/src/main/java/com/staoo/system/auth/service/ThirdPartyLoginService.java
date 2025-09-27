package com.staoo.system.auth.service;

import com.staoo.common.auth.dto.LoginResponse;
import com.staoo.system.domain.ThirdPartyLoginRequest;

/**
 * 第三方登录服务接口
 * 定义统一的第三方登录入口
 */
public interface ThirdPartyLoginService {
    /**
     * 处理第三方登录请求
     * @param request 第三方登录请求
     * @return 登录响应
     */
    LoginResponse login(ThirdPartyLoginRequest request);
    
    /**
     * 获取登录类型
     * @return 登录类型（如"IAM", "OAUTH2"）
     */
    String getLoginType();
}