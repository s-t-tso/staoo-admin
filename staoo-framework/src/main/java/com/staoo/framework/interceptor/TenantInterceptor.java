package com.staoo.framework.interceptor;

import com.staoo.common.util.TenantContext;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 租户拦截器
 * 从请求头中获取租户ID并设置到线程上下文中
 */
public class TenantInterceptor implements HandlerInterceptor {
    
    private static final String TENANT_ID_HEADER = "X-Tenant-Id";
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 从请求头中获取租户ID
        String tenantIdStr = request.getHeader(TENANT_ID_HEADER);
        if (Objects.nonNull(tenantIdStr) && !tenantIdStr.isEmpty()) {
            try {
                Long tenantId = Long.parseLong(tenantIdStr);
                TenantContext.setTenantId(tenantId);
            } catch (NumberFormatException e) {
                // 租户ID格式不正确，记录日志但继续请求
                // 在实际应用中应该添加日志记录
            }
        }
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求完成后清理租户上下文，防止ThreadLocal内存泄漏
        TenantContext.clear();
    }
}