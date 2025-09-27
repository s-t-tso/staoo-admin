package com.staoo.framework.interceptor;

import com.staoo.common.util.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 用户上下文拦截器
 * 在请求处理完成后清理ThreadLocal中的用户信息，防止内存泄漏
 */
@Component
public class UserContextInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(UserContextInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 请求开始前不需要特别处理，用户信息由JwtAuthenticationFilter设置
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求完成后清理用户上下文，防止ThreadLocal内存泄漏
        UserUtils.clear();
        if (logger.isDebugEnabled()) {
            logger.debug("已清理请求[{}]的用户ThreadLocal信息", request.getRequestURI());
        }
    }
}