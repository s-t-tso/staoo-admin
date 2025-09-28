package com.staoo.system.auth.strategy.impl;

import com.staoo.common.enums.StatusCodeEnum;
import com.staoo.common.exception.BusinessException;
import com.staoo.common.auth.dto.LoginRequest;
import com.staoo.common.auth.dto.LoginResponse;
import com.staoo.system.auth.service.impl.IAMLoginServiceImpl;
import com.staoo.system.auth.strategy.LoginStrategy;
import com.staoo.system.domain.ThirdPartyLoginRequest;
import com.staoo.system.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * IAM登录策略适配器
 * 将现有的IAM登录实现适配到策略模式框架中
 */
@Component
public class IAMLoginStrategy implements LoginStrategy {
    
    private static final String LOGIN_TYPE = "IAM";
    
    @Autowired
    private IAMLoginServiceImpl iamLoginService;
    
    @Override
    public LoginResponse login(LoginRequest request) {
        // 转换为第三方登录请求
        ThirdPartyLoginRequest thirdPartyLoginRequest = new ThirdPartyLoginRequest();
        thirdPartyLoginRequest.setAppId(request.getUsername());
        thirdPartyLoginRequest.setAuthCode(request.getPassword());
        thirdPartyLoginRequest.setUserAgent(request.getUserAgent());
        
        // 调用现有的IAM登录服务
        return iamLoginService.login(thirdPartyLoginRequest);
    }
    
    @Override
    public String getLoginType() {
        return LOGIN_TYPE;
    }
    
    @Override
    public void validateRequest(LoginRequest request) {
        if (request == null || !StringUtils.hasText(request.getPassword())) {
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR, "IAM授权码不能为空");
        }
        
        if (!StringUtils.hasText(request.getUsername())) {
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR, "应用ID不能为空");
        }
    }
    
    /**
     * 转换为第三方登录请求对象
     * @param request 登录请求
     * @return 第三方登录请求
     */
    private ThirdPartyLoginRequest convertToThirdPartyRequest(LoginRequest request) {
        ThirdPartyLoginRequest thirdPartyRequest = new ThirdPartyLoginRequest();
        // 在IAM登录中，username字段用于存储appId
        thirdPartyRequest.setAppId(request.getUsername());
        // 在IAM登录中，password字段用于存储授权码
        thirdPartyRequest.setAuthCode(request.getPassword());
        thirdPartyRequest.setUserAgent(request.getUserAgent());
        
        // 设置自定义参数
        Map<String, Object> customParams = new HashMap<>();
        if (StringUtils.hasText(request.getDeviceId())) {
            customParams.put("deviceId", request.getDeviceId());
        }
        
        thirdPartyRequest.setCustomParams(customParams);
        return thirdPartyRequest;
    }
}