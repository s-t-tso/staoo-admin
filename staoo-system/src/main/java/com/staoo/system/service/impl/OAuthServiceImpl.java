package com.staoo.system.service.impl;

import com.staoo.system.auth.jwt.JwtTokenProvider;
import com.staoo.system.domain.ThirdPartyApp;
import com.staoo.system.service.ThirdPartyAppService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.Map;

/**
 * OAuth认证服务实现类
 */
@Service
public class OAuthServiceImpl {

    @Autowired
    private ThirdPartyAppService thirdPartyAppService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * 根据应用密钥生成访问令牌
     * @param appKey 应用标识
     * @param appSecret 应用密钥
     * @return 访问令牌
     */
    public String generateToken(String appKey, String appSecret) {
        // 验证应用信息
        ThirdPartyApp app = thirdPartyAppService.getByAppKey(appKey);
        if (app == null || !"0".equals(app.getStatus())) {
            throw new RuntimeException("无效的应用信息");
        }

        // 验证应用密钥
        if (!appSecret.equals(app.getAppSecret())) {
            throw new RuntimeException("应用密钥错误");
        }

        // 构建令牌载荷
        Map<String, Object> claims = new HashMap<>();
        claims.put("appId", app.getId());
        claims.put("appKey", app.getAppKey());
        claims.put("appName", app.getAppName());
        claims.put("permissions", app.getPermissions());

        // 生成JWT令牌
        String deviceId = "third_party_app";
        return jwtTokenProvider.generateAccessToken(appKey, deviceId, claims);
    }

    /**
     * 验证令牌
     * @param token 访问令牌
     * @return 令牌验证结果
     */
    public Map<String, Object> validateToken(String token) {
        String deviceId = "third_party_app";
        boolean isValid = jwtTokenProvider.validateToken(token, deviceId);
        if (isValid) {
            return jwtTokenProvider.getAllClaimsFromToken(token);
        }
        throw new RuntimeException("无效的令牌");
    }

    /**
     * 刷新令牌
     * @param refreshToken 刷新令牌
     * @return 新的访问令牌
     */
    public String refreshToken(String refreshToken) {
        // 从刷新令牌中获取应用信息
        Map<String, Object> claims = jwtTokenProvider.getAllClaimsFromToken(refreshToken);
        String appKey = (String) claims.get("appKey");
        
        // 验证应用是否存在且有效
        ThirdPartyApp app = thirdPartyAppService.getByAppKey(appKey);
        if (app == null || !"0".equals(app.getStatus())) {
            throw new RuntimeException("无效的应用信息");
        }
        
        // 构建新的令牌载荷
        Map<String, Object> newClaims = new HashMap<>();
        newClaims.put("appId", app.getId());
        newClaims.put("appKey", app.getAppKey());
        newClaims.put("appName", app.getAppName());
        newClaims.put("permissions", app.getPermissions());
        
        // 生成新的访问令牌
        String deviceId = "third_party_app";
        return jwtTokenProvider.generateAccessToken(appKey, deviceId, newClaims);
    }

    /**
     * 从令牌中获取应用信息
     * @param token 访问令牌
     * @return 应用信息
     */
    public ThirdPartyApp getAppFromToken(String token) {
        Map<String, Object> claims = jwtTokenProvider.getAllClaimsFromToken(token);
        if (claims != null && claims.containsKey("appKey")) {
            String appKey = (String) claims.get("appKey");
            return thirdPartyAppService.getByAppKey(appKey);
        }
        return null;
    }
}