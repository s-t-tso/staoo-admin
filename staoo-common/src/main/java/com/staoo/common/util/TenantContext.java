package com.staoo.common.util;

/**
 * 租户上下文管理类
 * 使用ThreadLocal存储当前线程的租户ID和租户编码，实现线程隔离
 */
public class TenantContext {
    private static final ThreadLocal<Long> CURRENT_TENANT = new ThreadLocal<>();
    private static final ThreadLocal<String> CURRENT_TENANT_CODE = new ThreadLocal<>();
    
    /**
     * 设置当前线程的租户ID
     * @param tenantId 租户ID
     */
    public static void setTenantId(Long tenantId) {
        CURRENT_TENANT.set(tenantId);
    }
    
    /**
     * 获取当前线程的租户ID
     * @return 租户ID
     */
    public static Long getTenantId() {
        return CURRENT_TENANT.get();
    }
    
    /**
     * 设置当前线程的租户编码
     * @param tenantCode 租户编码
     */
    public static void setTenantCode(String tenantCode) {
        CURRENT_TENANT_CODE.set(tenantCode);
    }
    
    /**
     * 获取当前线程的租户编码
     * @return 租户编码
     */
    public static String getTenantCode() {
        return CURRENT_TENANT_CODE.get();
    }
    
    /**
     * 清理当前线程的租户上下文
     * 防止ThreadLocal内存泄漏
     */
    public static void clear() {
        CURRENT_TENANT.remove();
        CURRENT_TENANT_CODE.remove();
    }
    
    /**
     * 判断当前是否有租户ID设置
     * @return 是否有租户ID
     */
    public static boolean hasTenantId() {
        return CURRENT_TENANT.get() != null;
    }
    
    /**
     * 判断当前是否有租户编码设置
     * @return 是否有租户编码
     */
    public static boolean hasTenantCode() {
        return CURRENT_TENANT_CODE.get() != null;
    }
}