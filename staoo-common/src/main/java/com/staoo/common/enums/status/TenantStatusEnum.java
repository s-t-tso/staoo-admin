/**
 * 租户状态枚举类
 * 定义租户的各种状态
 */
package com.staoo.common.enums.status;

public enum TenantStatusEnum {
    /**
     * 禁用状态
     */
    DISABLED(0, "禁用"),
    
    /**
     * 启用状态
     */
    ENABLED(1, "启用"),
    
    /**
     * 审核中状态
     */
    PENDING(2, "审核中"),
    
    /**
     * 过期状态
     */
    EXPIRED(3, "过期");
    
    private final Integer code;
    private final String name;
    
    TenantStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getName() {
        return name;
    }
    
    /**
     * 根据状态码获取枚举对象
     * @param code 状态码
     * @return 枚举对象
     */
    public static TenantStatusEnum getByCode(Integer code) {
        for (TenantStatusEnum status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}