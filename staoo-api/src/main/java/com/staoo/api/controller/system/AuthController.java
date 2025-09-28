package com.staoo.api.controller.system;

import com.staoo.common.auth.dto.LoginRequest;
import com.staoo.common.auth.dto.LoginResponse;
import com.staoo.common.domain.AjaxResult;
import com.staoo.common.domain.UserInfo;
import com.staoo.common.util.UserUtils;
import com.staoo.system.auth.service.LoginService;
import com.staoo.system.auth.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 提供登录、登出、获取用户信息等认证相关接口
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "认证管理", description = "用户认证相关接口")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private LoginService loginService;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * 用户登录接口
     * @param request 登录请求参数
     * @param httpRequest HTTP请求对象
     * @return 登录响应
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户通过账号密码登录系统")
    public AjaxResult<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest httpRequest) {
        try {
            logger.info("用户登录请求: {}", loginRequest);

            // 由服务端直接获取客户端信息，不依赖前端提交
            loginRequest.setIp(getClientIp(httpRequest));
            loginRequest.setUserAgent(httpRequest.getHeader("User-Agent"));
            if (loginRequest.getLoginType() == null || loginRequest.getLoginType().isEmpty()) {
                loginRequest.setLoginType("PASSWORD");
            }

            // 执行登录逻辑
            LoginResponse loginResponse = loginService.login(loginRequest);

            logger.info("用户登录成功: {}", loginRequest.getUsername());
            return AjaxResult.success(loginResponse);
        } catch (Exception e) {
            logger.error("用户登录失败: {}", e.getMessage(), e);
            return AjaxResult.error(403, "登录失败: " + e.getMessage());
        }
    }

    /**
     * 用户登出接口
     * @return 登出响应
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户退出登录系统")
    public AjaxResult<String> logout() {
        try {
            // 由于使用JWT无状态认证，服务端不需要额外处理，主要由客户端清除token
            logger.info("用户登出成功");
            return AjaxResult.success("登出成功");
        } catch (Exception e) {
            logger.error("用户登出失败: {}", e.getMessage(), e);
            return AjaxResult.error(500, "登出失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户信息接口
     * @param request HTTP请求对象
     * @return 用户信息响应
     */
    @GetMapping("/info")
    @Operation(summary = "获取用户信息", description = "获取当前登录用户的详细信息")
    public AjaxResult<Map<String, Object>> getUserInfo(HttpServletRequest request) {
        try {
            // 从UserUtils获取当前登录用户信息
            UserInfo currentUser = UserUtils.getCurrentUser();
            
            if (currentUser == null) {
                logger.warn("未获取到当前登录用户信息");
                return AjaxResult.error(401, "用户未登录");
            }
            
            // 构建用户信息响应，只包含非敏感信息
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("name", currentUser.getUsername());
            userInfo.put("nickname", currentUser.getNickname());
            userInfo.put("avatar", currentUser.getAvatar() != null ? currentUser.getAvatar() : "");
            userInfo.put("roles", currentUser.getRoles() != null ? currentUser.getRoles().toArray() : new String[0]);
            userInfo.put("permissions", currentUser.getPermissions() != null ? currentUser.getPermissions().toArray() : new String[0]);
            userInfo.put("isSuperAdmin", currentUser.getIsSuperAdmin() != null && currentUser.getIsSuperAdmin());
            
            logger.info("获取用户信息成功: {}", currentUser.getUsername());
            return AjaxResult.success(userInfo);
        } catch (Exception e) {
            logger.error("获取用户信息失败: {}", e.getMessage(), e);
            return AjaxResult.error(500, "获取用户信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取客户端IP地址
     * @param request HTTP请求对象
     * @return IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多级代理情况下，取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
    
    /**
     * 刷新令牌接口
     * @param refreshToken 刷新令牌
     * @return 包含新令牌的响应
     */
    @PostMapping("/refresh")
    @Operation(summary = "刷新令牌", description = "使用刷新令牌获取新的访问令牌")
    public AjaxResult<LoginResponse> refreshToken(@RequestParam String refreshToken) {
        try {
            logger.info("刷新令牌请求");
            
            // 从请求头中获取设备ID（如果存在）
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String deviceId = request.getHeader("Device-ID");
            if (deviceId == null || deviceId.isEmpty()) {
                deviceId = "web"; // 默认设备ID
            }
            
            // 验证刷新令牌的有效性
            boolean isRefreshTokenValid = validateRefreshToken(refreshToken);
            if (!isRefreshTokenValid) {
                logger.warn("无效的刷新令牌");
                return AjaxResult.error(401, "无效的刷新令牌");
            }
            
            // 获取用户名
            String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
            
            // 生成新的访问令牌和刷新令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("username", username);
            claims.put("deviceId", deviceId);
            
            String newAccessToken = jwtTokenProvider.generateAccessToken(username, deviceId, claims);
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(username);
            
            // 构建响应
            LoginResponse response = new LoginResponse();
            response.setAccessToken(newAccessToken);
            response.setRefreshToken(newRefreshToken);
            response.setTokenType("Bearer");
            response.setExpiresIn(jwtTokenProvider.getJwtExpiration() / 1000); // 转换为秒
            
            logger.info("刷新令牌成功: {}", username);
            return AjaxResult.success(response);
        } catch (Exception e) {
            logger.error("刷新令牌失败: {}", e.getMessage(), e);
            return AjaxResult.error(401, "刷新令牌失败: " + e.getMessage());
        }
    }
    
    /**
     * 验证刷新令牌的有效性
     * 刷新令牌的验证与访问令牌略有不同，主要验证其签名和过期时间
     */
    private boolean validateRefreshToken(String refreshToken) {
        try {
            // 尝试从刷新令牌中获取用户名，如果能成功获取，说明令牌签名有效
            // 注意：getUsernameFromToken方法内部会验证令牌的签名和有效性
            // 但刷新令牌可能没有与设备ID相关的会话信息，所以不能使用validateToken方法
            jwtTokenProvider.getUsernameFromToken(refreshToken);
            
            return true;
        } catch (Exception e) {
            logger.error("验证刷新令牌失败: {}", e.getMessage());
            return false;
        }
    }
}
