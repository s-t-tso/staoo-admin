package com.staoo.system.auth.service.impl;

import com.staoo.common.enums.StatusCodeEnum;
import com.staoo.common.exception.BusinessException;
import com.staoo.common.auth.dto.LoginResponse;
import com.staoo.system.auth.jwt.JwtTokenProvider;
import com.staoo.system.auth.service.ThirdPartyLoginService;
import com.staoo.system.domain.ThirdPartyApp;
import com.staoo.system.domain.ThirdPartyLoginRequest;
import com.staoo.system.domain.User;
import com.staoo.system.service.ThirdPartyAppService;
import com.staoo.system.service.UserService;
import com.staoo.system.service.impl.OAuthServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * OAuth2登录服务实现
 * 处理OAuth2认证登录逻辑
 */
@Service
public class OAuth2LoginServiceImpl implements ThirdPartyLoginService {
    private static final Logger logger = LoggerFactory.getLogger(OAuth2LoginServiceImpl.class);
    private static final String LOGIN_TYPE = "OAUTH2";
    
    @Autowired
    private ThirdPartyAppService thirdPartyAppService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    private OAuthServiceImpl oAuthService;
    
    @Override
    public LoginResponse login(ThirdPartyLoginRequest request) {
        logger.info("OAuth2登录请求: {}", request);
        
        // 1. 验证第三方应用信息
        ThirdPartyApp app = thirdPartyAppService.getByAppKey(request.getAppId());
        if (app == null || !"0".equals(app.getStatus())) {
            throw new BusinessException(StatusCodeEnum.AUTHENTICATION_FAILED, "无效的应用信息");
        }
        
        // 2. 验证应用是否有权限使用OAuth2登录
        if (!app.getPermissions().contains("OAUTH2_LOGIN")) {
            throw new BusinessException(StatusCodeEnum.PERMISSION_DENIED, "应用无OAuth2登录权限");
        }
        
        try {
            // 3. 调用已有的OAuthService处理令牌生成
            String token = oAuthService.generateToken(request.getAppId(), (String) request.getCustomParams().get("appSecret"));
            
            // 4. 从令牌中获取应用信息
            Map<String, Object> claims = jwtTokenProvider.getAllClaimsFromToken(token);
            String username = (String) claims.get("appKey");
            
            // 5. 查询或创建对应的系统用户
            User user = getUserByOAuthInfo(claims);
            
            // 6. 生成标准登录响应
            return generateLoginResponse(user, token, request);
        } catch (Exception e) {
            logger.error("OAuth2认证失败: {}", e.getMessage(), e);
            throw new BusinessException(StatusCodeEnum.AUTHENTICATION_FAILED, "OAuth2认证失败: " + e.getMessage());
        }
    }
    
    @Override
    public String getLoginType() {
        return LOGIN_TYPE;
    }
    
    /**
     * 根据OAuth信息查询或创建系统用户
     * @param claims OAuth令牌中的声明信息
     * @return 系统用户
     */
    private User getUserByOAuthInfo(Map<String, Object> claims) {
        String appKey = (String) claims.get("appKey");
        String username = "oauth2_" + appKey;
        
        // 先查询是否存在该用户
        User user = userService.getByUsername(username);
        
        if (user == null) {
            // 创建新用户
            user = new User();
            user.setUsername(username);
            user.setNickname((String) claims.get("appName"));
            user.setStatus(1); // 启用状态
            
            // 保存用户
            userService.save(user);
        }
        
        return user;
    }
    
    /**
     * 生成登录响应
     * @param user 用户信息
     * @param token 访问令牌
     * @param request 登录请求
     * @return 登录响应
     */
    private LoginResponse generateLoginResponse(User user, String token, ThirdPartyLoginRequest request) {
        // 构建JWT令牌载荷
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put("tenantId", request.getTenantId());
        claims.put("loginType", LOGIN_TYPE);
        
        // 生成刷新令牌
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());
        
        // 构建登录响应
        LoginResponse response = new LoginResponse();
        response.setAccessToken(token);
        response.setRefreshToken(refreshToken);
        
        // 创建并设置用户信息
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setNickname(user.getNickname());
        userInfo.setTenantId(request.getTenantId());
        userInfo.setAvatar(user.getAvatar());
        response.setUserInfo(userInfo);
        
        return response;
    }
}