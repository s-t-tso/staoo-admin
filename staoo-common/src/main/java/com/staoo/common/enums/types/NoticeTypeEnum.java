/**
 * 通知类型枚举类
 * 定义通知的各种类型
 */
package com.staoo.common.enums.types;

public enum NoticeTypeEnum {
    /**
     * 系统通知
     */
    SYSTEM(1, "系统通知"),
    
    /**
     * 业务通知
     */
    BUSINESS(2, "业务通知"),
    
    /**
     * 公告通知
     */
    ANNOUNCEMENT(3, "公告通知"),
    
    /**
     * 提醒通知
     */
    REMINDER(4, "提醒通知");
    
    private final Integer code;
    private final String name;
    
    NoticeTypeEnum(Integer code, String name) {
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
     * 根据类型码获取枚举对象
     * @param code 类型码
     * @return 枚举对象
     */
    public static NoticeTypeEnum getByCode(Integer code) {
        for (NoticeTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}