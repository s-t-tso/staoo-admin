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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * IAM登录服务实现
 * 处理IAM认证登录逻辑
 */
@Service
public class IAMLoginServiceImpl implements ThirdPartyLoginService {
    private static final Logger logger = LoggerFactory.getLogger(IAMLoginServiceImpl.class);
    private static final String LOGIN_TYPE = "IAM";
    
    @Autowired
    private ThirdPartyAppService thirdPartyAppService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    private RestTemplate restTemplate;
    
    // IAM服务器地址，实际项目中应从配置中读取
    private String iamServerUrl = "http://localhost:8081/iam";
    
    @Override
    public LoginResponse login(ThirdPartyLoginRequest request) {
        logger.info("IAM登录请求: {}", request);
        
        // 1. 验证第三方应用信息
        ThirdPartyApp app = thirdPartyAppService.getByAppKey(request.getAppId());
        if (app == null || !"0".equals(app.getStatus())) {
            throw new BusinessException(StatusCodeEnum.AUTHENTICATION_FAILED, "无效的应用信息");
        }
        
        // 2. 验证应用是否有权限使用IAM登录
        if (!app.getPermissions().contains("IAM_LOGIN")) {
            throw new BusinessException(StatusCodeEnum.PERMISSION_DENIED, "应用无IAM登录权限");
        }
        
        // 3. 调用IAM服务验证授权码并获取用户信息
        Map<String, Object> iamUserInfo = validateIAMAuthCode(app, request.getAuthCode());
        
        // 4. 根据IAM返回的用户信息查询或创建系统用户
        User user = getUserByIAMInfo(iamUserInfo);
        
        // 5. 生成JWT令牌
        return generateLoginResponse(user, request);
    }
    
    @Override
    public String getLoginType() {
        return LOGIN_TYPE;
    }
    
    /**
     * 调用IAM服务验证授权码并获取用户信息
     * @param app 第三方应用信息
     * @param authCode IAM授权码
     * @return IAM用户信息
     */
    private Map<String, Object> validateIAMAuthCode(ThirdPartyApp app, String authCode) {
        try {
            // 构建IAM认证请求
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);
            
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("client_id", app.getAppKey());
            params.add("client_secret", app.getAppSecret());
            params.add("code", authCode);
            params.add("grant_type", "authorization_code");
            
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            
            // 调用IAM服务
            ResponseEntity<Map> response = restTemplate.exchange(
                iamServerUrl + "/oauth2/token",
                HttpMethod.POST,
                request,
                Map.class
            );
            
            // 这里简化处理，实际项目中应该根据IAM返回的token获取用户信息
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("username", response.getBody().get("username"));
            userInfo.put("email", response.getBody().get("email"));
            userInfo.put("nickname", response.getBody().get("nickname"));
            
            return userInfo;
        } catch (Exception e) {
            logger.error("IAM认证失败: {}", e.getMessage(), e);
            throw new BusinessException(StatusCodeEnum.AUTHENTICATION_FAILED, "IAM认证失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据IAM用户信息查询或创建系统用户
     * @param iamUserInfo IAM用户信息
     * @return 系统用户
     */
    private User getUserByIAMInfo(Map<String, Object> iamUserInfo) {
        String username = (String) iamUserInfo.get("username");
        
        // 先查询是否存在该用户
        User user = userService.getByUsername(username);
        
        if (user == null) {
            // 创建新用户
            user = new User();
            user.setUsername(username);
            user.setNickname((String) iamUserInfo.get("nickname"));
            user.setEmail((String) iamUserInfo.get("email"));
            user.setStatus(1); // 启用状态
            
            // 保存用户
            userService.save(user);
        }
        
        return user;
    }
    
    /**
     * 生成登录响应
     * @param user 用户信息
     * @param request 登录请求
     * @return 登录响应
     */
    private LoginResponse generateLoginResponse(User user, ThirdPartyLoginRequest request) {
        // 构建JWT令牌载荷
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put("tenantId", request.getTenantId());
        claims.put("loginType", LOGIN_TYPE);
        
        // 生成令牌
        String deviceId = request.getDeviceId() != null ? request.getDeviceId() : "iam_device";
        String token = jwtTokenProvider.generateAccessToken(user.getUsername(), deviceId, claims);
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