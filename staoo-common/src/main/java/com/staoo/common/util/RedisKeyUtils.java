package com.staoo.common.util;



/**
 * Redis缓存键工具类
 * 用于生成支持多租户的缓存键
 */
public class RedisKeyUtils {

    /**
     * 租户ID前缀
     */
    private static final String TENANT_PREFIX = "tenant:";
    
    /**
     * 分隔符
     */
    private static final String SEPARATOR = ":";

    /**
     * 生成带租户ID的缓存键
     * @param module 模块名称
     * @param key 原始键名
     * @return 带租户ID的缓存键
     */
    public static String getTenantKey(String module, String key) {
        Long tenantId = TenantContext.getTenantId();
        if (tenantId != null) {
            return TENANT_PREFIX + tenantId + SEPARATOR + module + SEPARATOR + key;
        }
        return module + SEPARATOR + key;
    }

    /**
     * 生成带租户ID和用户ID的缓存键
     * @param module 模块名称
     * @param key 原始键名
     * @param userId 用户ID
     * @return 带租户ID和用户ID的缓存键
     */
    public static String getTenantUserKey(String module, String key, Long userId) {
        Long tenantId = TenantContext.getTenantId();
        if (tenantId != null && userId != null) {
            return TENANT_PREFIX + tenantId + SEPARATOR + module + SEPARATOR + userId + SEPARATOR + key;
        } else if (userId != null) {
            return module + SEPARATOR + userId + SEPARATOR + key;
        }
        return module + SEPARATOR + key;
    }

    /**
     * 生成带租户ID的列表缓存键
     * @param module 模块名称
     * @param keyPrefix 键前缀
     * @return 带租户ID的列表缓存键
     */
    public static String getTenantListKey(String module, String keyPrefix) {
        Long tenantId = TenantContext.getTenantId();
        if (tenantId != null) {
            return TENANT_PREFIX + tenantId + SEPARATOR + module + SEPARATOR + keyPrefix;
        }
        return module + SEPARATOR + keyPrefix;
    }

    /**
     * 从缓存键中提取租户ID
     * @param key 缓存键
     * @return 租户ID，如果不存在则返回null
     */
    public static Long extractTenantId(String key) {
        if (key != null && key.startsWith(TENANT_PREFIX)) {
            try {
                int firstSeparatorIndex = key.indexOf(SEPARATOR, TENANT_PREFIX.length());
                if (firstSeparatorIndex > TENANT_PREFIX.length()) {
                    String tenantIdStr = key.substring(TENANT_PREFIX.length(), firstSeparatorIndex);
                    return Long.parseLong(tenantIdStr);
                }
            } catch (NumberFormatException e) {
                // 解析失败，返回null
            }
        }
        return null;
    }

    /**
     * 移除缓存键中的租户ID部分
     * @param key 带租户ID的缓存键
     * @return 移除租户ID后的缓存键
     */
    public static String removeTenantPrefix(String key) {
        if (key != null && key.startsWith(TENANT_PREFIX)) {
            int firstSeparatorIndex = key.indexOf(SEPARATOR, TENANT_PREFIX.length());
            if (firstSeparatorIndex > TENANT_PREFIX.length()) {
                return key.substring(firstSeparatorIndex + 1);
            }
        }
        return key;
    }
}