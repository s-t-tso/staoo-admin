package com.staoo.api.controller.system;

import com.staoo.common.auth.dto.LoginRequest;
import com.staoo.common.auth.dto.LoginResponse;
import com.staoo.common.domain.AjaxResult;
import com.staoo.system.auth.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
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
            // 这里应该从认证上下文中获取用户信息
            // 简化实现，实际应该从SecurityContextHolder中获取已认证用户信息
            Map<String, Object> userInfo = Map.of(
                    "name", "admin",
                    "avatar", "",
                    "roles", new String[]{"admin"},
                    "permissions", new String[]{"*:*:*"}
            );
            logger.info("获取用户信息成功");
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
}
