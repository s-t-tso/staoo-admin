package com.staoo.common.util;

import com.staoo.common.domain.UserInfo;
import org.springframework.util.StringUtils;

/**
 * 用户工具类
 * 提供获取当前用户信息、租户信息等功能
 */
public class UserUtils {
    private static final ThreadLocal<UserInfo> CURRENT_USER = new ThreadLocal<>();

    /**
     * 设置当前登录用户信息
     * @param userInfo 用户信息
     */
    public static void setCurrentUser(UserInfo userInfo) {
        CURRENT_USER.set(userInfo);
    }

    /**
     * 获取当前登录用户信息
     * @return 用户信息
     */
    public static UserInfo getCurrentUser() {
        return CURRENT_USER.get();
    }

    /**
     * 获取当前登录用户ID
     * @return 用户ID
     */
    public static Long getCurrentUserId() {
        UserInfo userInfo = getCurrentUser();
        return userInfo != null ? userInfo.getId() : null;
    }

    /**
     * 获取当前登录用户名
     * @return 用户名
     */
    public static String getCurrentUsername() {
        UserInfo userInfo = getCurrentUser();
        return userInfo != null ? userInfo.getUsername() : null;
    }

    /**
     * 获取当前登录用户的租户ID
     * 优先从TenantContext获取，其次从UserInfo获取
     * @return 租户ID
     */
    public static Long getCurrentTenantId() {
        if (TenantContext.hasTenantId()) {
            return TenantContext.getTenantId();
        }
        
        UserInfo userInfo = getCurrentUser();
        return userInfo != null ? userInfo.getTenantId() : null;
    }

    /**
     * 获取当前登录用户的租户编码
     * @return 租户编码
     */
    public static String getCurrentTenantCode() {
        if (TenantContext.hasTenantCode()) {
            return TenantContext.getTenantCode();
        }
        
        UserInfo userInfo = getCurrentUser();
        return userInfo != null ? userInfo.getTenantCode() : null;
    }

    /**
     * 判断当前是否有登录用户
     * @return 是否有登录用户
     */
    public static boolean hasLoginUser() {
        return getCurrentUser() != null;
    }

    /**
     * 检查当前用户是否是超级管理员
     * @return 是否是超级管理员
     */
    public static boolean isSuperAdmin() {
        UserInfo userInfo = getCurrentUser();
        return userInfo != null && userInfo.getIsSuperAdmin() != null && userInfo.getIsSuperAdmin();
    }

    /**
     * 检查当前用户是否具有指定角色
     * @param roleCode 角色编码
     * @return 是否具有指定角色
     */
    public static boolean hasRole(String roleCode) {
        UserInfo userInfo = getCurrentUser();
        if (userInfo == null || userInfo.getRoles() == null || !StringUtils.hasText(roleCode)) {
            return false;
        }
        return userInfo.getRoles().contains(roleCode);
    }

    /**
     * 检查当前用户是否具有指定权限
     * @param permission 权限编码
     * @return 是否具有指定权限
     */
    public static boolean hasPermission(String permission) {
        UserInfo userInfo = getCurrentUser();
        if (userInfo == null || userInfo.getPermissions() == null || !StringUtils.hasText(permission)) {
            return false;
        }
        return userInfo.getPermissions().contains(permission);
    }

    /**
     * 清理当前线程的用户上下文
     * 防止ThreadLocal内存泄漏
     */
    public static void clear() {
        CURRENT_USER.remove();
    }
}