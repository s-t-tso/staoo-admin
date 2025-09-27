package com.staoo.system.controller;

import com.staoo.common.auth.dto.LoginResponse;
import com.staoo.common.domain.AjaxResult;
import com.staoo.system.auth.factory.ThirdPartyLoginFactory;
import com.staoo.system.auth.service.ThirdPartyLoginService;
import com.staoo.system.domain.ThirdPartyLoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 第三方登录控制器
 * 提供统一的第三方登录入口
 * @author shitao
 */
@RestController
@RequestMapping("/system/third-party/login")
@Tag(name = "第三方登录", description = "第三方应用登录接口")
public class ThirdPartyLoginController {
    private static final Logger logger = LoggerFactory.getLogger(ThirdPartyLoginController.class);

    @Autowired
    private ThirdPartyLoginFactory thirdPartyLoginFactory;

    /**
     * 第三方登录接口
     * @param request 第三方登录请求
     * @param httpRequest HTTP请求对象，用于获取客户端IP和User-Agent
     * @return 登录响应
     */
    @PostMapping
    @Operation(summary = "第三方登录", description = "支持IAM和OAuth2等多种第三方登录方式")
    public AjaxResult<LoginResponse> thirdPartyLogin(
            @Validated @RequestBody ThirdPartyLoginRequest request,
            HttpServletRequest httpRequest) {
        logger.info("第三方登录请求: {}", request);

        // 补充请求信息
        if (request.getClientIp() == null) {
            request.setClientIp(getClientIp(httpRequest));
        }
        if (request.getUserAgent() == null) {
            request.setUserAgent(httpRequest.getHeader("User-Agent"));
        }

        // 根据登录类型获取对应的登录服务
        ThirdPartyLoginService loginService = thirdPartyLoginFactory.getLoginService(request.getLoginType());

        // 调用具体的登录服务处理登录请求
        LoginResponse loginResponse = loginService.login(request);

        logger.info("第三方登录成功: {}, 登录类型: {}", loginResponse.getUserInfo().getUsername(), request.getLoginType());

        return AjaxResult.success(loginResponse);
    }

    /**
     * 获取客户端真实IP地址
     * @param request HTTP请求对象
     * @return 客户端IP地址
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

        // 多级代理的情况下，第一个IP为客户端真实IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }
}
