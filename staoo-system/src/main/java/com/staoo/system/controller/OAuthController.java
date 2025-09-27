package com.staoo.system.controller;

import com.staoo.common.domain.AjaxResult;
import com.staoo.system.service.impl.OAuthServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * OAuth认证Controller
 */
@RestController
@RequestMapping("/system/oauth")
@Tag(name = "OAuth认证", description = "第三方应用认证接口")
public class OAuthController {

    @Autowired
    private OAuthServiceImpl oAuthService;

    @Operation(summary = "获取访问令牌", description = "通过应用密钥获取访问令牌")
    @PostMapping("/token")
    public AjaxResult<Map<String, String>> getToken(@RequestParam String appKey, @RequestParam String appSecret) {
        String token = oAuthService.generateToken(appKey, appSecret);
        Map<String, String> result = new java.util.HashMap<>();
        result.put("access_token", token);
        result.put("token_type", "Bearer");
        // 假设令牌有效期为1小时
        result.put("expires_in", "3600");
        return AjaxResult.success(result);
    }

    @Operation(summary = "验证令牌", description = "验证访问令牌是否有效")
    @PostMapping("/validate")
    public AjaxResult<Map<String, Object>> validateToken(@RequestParam String token) {
        Map<String, Object> claims = oAuthService.validateToken(token);
        return AjaxResult.success(claims);
    }

    @Operation(summary = "刷新令牌", description = "使用刷新令牌获取新的访问令牌")
    @PostMapping("/refresh")
    public AjaxResult<Map<String, String>> refreshToken(@RequestParam String refreshToken) {
        String newToken = oAuthService.refreshToken(refreshToken);
        Map<String, String> result = new java.util.HashMap<>();
        result.put("access_token", newToken);
        result.put("token_type", "Bearer");
        result.put("expires_in", "3600");
        return AjaxResult.success(result);
    }

    @Operation(summary = "授权码模式", description = "OAuth授权码模式接口")
    @GetMapping("/authorize")
    public void authorize(
            @RequestParam String response_type,
            @RequestParam String client_id,
            @RequestParam String redirect_uri,
            @RequestParam(required = false) String scope,
            @RequestParam(required = false) String state) {
        // 实际项目中，这里应该重定向到登录页面，让用户授权
        // 这里简化处理，直接重定向到回调地址，带上授权码
        String redirectUrl = redirect_uri + "?code=mock_code&state=" + (state != null ? state : "");
        try {
            // 实际代码中应该使用HttpServletResponse进行重定向
            // response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            throw new RuntimeException("重定向失败", e);
        }
    }

    @Operation(summary = "回调处理", description = "第三方应用回调处理接口")
    @PostMapping("/callback/{appKey}")
    public AjaxResult<String> callback(@PathVariable String appKey, @RequestBody Map<String, Object> data) {
        // 处理第三方应用的回调请求
        // 实际项目中应该验证回调的签名，确保请求来自可信的第三方应用
        return AjaxResult.success("回调处理成功");
    }
}
