package com.staoo.framework.auth.filter;

import com.staoo.common.enums.StatusCodeEnum;
import com.staoo.common.exception.BusinessException;
import com.staoo.common.util.TenantContext;
import com.staoo.common.util.UserUtils;
import com.staoo.common.domain.UserInfo;
import com.staoo.system.auth.jwt.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * JWT认证过滤器
 * 用于拦截请求并验证JWT令牌
 * @author shitao
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // 获取JWT令牌
            String jwt = getJwtFromRequest(request);
            String deviceId = getDeviceIdFromRequest(request);

            // 如果令牌存在且有效
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt, deviceId)) {
                // 从令牌中获取用户名
                String username = tokenProvider.getUsernameFromToken(jwt);

                // 加载用户信息
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 从令牌中获取租户信息
                Map<String, Object> claims = tokenProvider.getAllClaimsFromToken(jwt);
                
                // 设置Spring Security上下文
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.debug("已设置Spring Security上下文: {}", username);

                // 设置当前用户到UserUtils
                UserInfo userInfo = new UserInfo();
                userInfo.setUsername(username);
                if (claims.containsKey("userId")) {
                    userInfo.setId(Long.valueOf(claims.get("userId").toString()));
                }
                if (claims.containsKey("tenantId")) {
                    userInfo.setTenantId(Long.valueOf(claims.get("tenantId").toString()));
                    // 设置租户信息到TenantContext
                    TenantContext.setTenantId(userInfo.getTenantId());
                    logger.debug("已设置租户ID: {}", userInfo.getTenantId());
                }
                if (claims.containsKey("tenantCode")) {
                    userInfo.setTenantCode(claims.get("tenantCode").toString());
                    // 设置租户编码到TenantContext
                    TenantContext.setTenantCode(userInfo.getTenantCode());
                    logger.debug("已设置租户编码: {}", userInfo.getTenantCode());
                }
                if (claims.containsKey("roles")) {
                    userInfo.setRoles((List<String>) claims.get("roles"));
                }
                if (claims.containsKey("permissions")) {
                    userInfo.setPermissions((List<String>) claims.get("permissions"));
                }
                UserUtils.setCurrentUser(userInfo);
                logger.debug("已设置当前用户到UserUtils: {}", username);
            }
        } catch (Exception ex) {
            logger.error("无法设置用户认证: {}", ex.getMessage());
            // 不抛出异常，继续过滤链，让后续的安全检查处理未认证的请求
        }

        // 继续过滤链
        filterChain.doFilter(request, response);
    }

    /**
     * 从请求头中提取JWT令牌
     * @param request HTTP请求
     * @return JWT令牌
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    /**
     * 从请求中提取设备ID
     * @param request HTTP请求
     * @return 设备ID
     */
    private String getDeviceIdFromRequest(HttpServletRequest request) {
        // 从请求头或参数中获取设备ID
        String deviceId = request.getHeader("Device-Id");
        if (!StringUtils.hasText(deviceId)) {
            deviceId = request.getParameter("deviceId");
        }
        // 如果没有提供设备ID，默认为unknown
        return StringUtils.hasText(deviceId) ? deviceId : "unknown";
    }

    /**
     * 配置哪些请求不需要经过过滤器
     * @param request HTTP请求
     * @return 是否应该跳过过滤
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // 排除登录、注册、刷新令牌等公开接口，包含带前缀和不带前缀的版本
        return path.startsWith("/api/auth/") ||
               path.startsWith("/auth/") ||
               path.startsWith("/api/public/") ||
               path.contains("/swagger") ||
               path.contains("/v3/api-docs") ||
               path.contains("/webjars/") ||
               path.equals("/");
    }
}
