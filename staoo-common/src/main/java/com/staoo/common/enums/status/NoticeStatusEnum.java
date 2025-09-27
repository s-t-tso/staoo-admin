/**
 * 通知状态枚举类
 * 定义通知的各种状态
 */
package com.staoo.common.enums.status;

public enum NoticeStatusEnum {
    /**
     * 草稿状态
     */
    DRAFT(0, "草稿"),
    
    /**
     * 已发布状态
     */
    PUBLISHED(1, "已发布"),
    
    /**
     * 已撤回状态
     */
    RECALLED(2, "已撤回");
    
    private final Integer code;
    private final String name;
    
    NoticeStatusEnum(Integer code, String name) {
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
    public static NoticeStatusEnum getByCode(Integer code) {
        for (NoticeStatusEnum status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}