package com.staoo.system.auth.strategy.impl;

import com.staoo.common.enums.StatusCodeEnum;
import com.staoo.common.exception.BusinessException;
import com.staoo.common.auth.dto.LoginRequest;
import com.staoo.common.auth.dto.LoginResponse;
import com.staoo.system.auth.service.impl.OAuth2LoginServiceImpl;
import com.staoo.system.auth.strategy.LoginStrategy;
import com.staoo.system.domain.ThirdPartyLoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * OAuth2登录策略适配器
 * 将现有的OAuth2登录实现适配到策略模式框架中
 */
@Component
public class OAuth2LoginStrategy implements LoginStrategy {
    
    private static final String LOGIN_TYPE = "OAUTH2";
    
    @Autowired
    private OAuth2LoginServiceImpl oauth2LoginService;
    
    @Override
    public LoginResponse login(LoginRequest request) {
        // 转换为第三方登录请求
        ThirdPartyLoginRequest thirdPartyLoginRequest = convertToThirdPartyRequest(request);
        
        // 调用现有的OAuth2登录服务
        return oauth2LoginService.login(thirdPartyLoginRequest);
    }
    
    @Override
    public String getLoginType() {
        return LOGIN_TYPE;
    }
    
    @Override
    public void validateRequest(LoginRequest request) {
        if (request == null || !StringUtils.hasText(request.getPassword())) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "OAuth2授权码不能为空");
        }
        
        if (!StringUtils.hasText(request.getUsername())) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "OAuth2提供商不能为空");
        }
        
        // 支持的OAuth2提供商检查
        String provider = request.getUsername().toLowerCase();
        if (!provider.equals("github") && !provider.equals("google") && !provider.equals("wechat")) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "不支持的OAuth2提供商: " + provider);
        }
    }
    
    /**
     * 转换为第三方登录请求对象
     * @param request 登录请求
     * @return 第三方登录请求
     */
    private ThirdPartyLoginRequest convertToThirdPartyRequest(LoginRequest request) {
        ThirdPartyLoginRequest thirdPartyRequest = new ThirdPartyLoginRequest();
        thirdPartyRequest.setAppId(request.getUsername()); // 在OAuth2登录中，username字段用于存储提供商名称
        thirdPartyRequest.setAuthCode(request.getPassword()); // 在OAuth2登录中，password字段用于存储授权码
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