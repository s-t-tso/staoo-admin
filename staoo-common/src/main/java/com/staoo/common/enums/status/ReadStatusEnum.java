/**
 * 阅读状态枚举类
 * 定义通知、消息等的阅读状态
 */
package com.staoo.common.enums.status;

public enum ReadStatusEnum {
    /**
     * 未读状态
     */
    UNREAD(0, "未读"),
    
    /**
     * 已读状态
     */
    READ(1, "已读");
    
    private final Integer code;
    private final String name;
    
    ReadStatusEnum(Integer code, String name) {
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
    public static ReadStatusEnum getByCode(Integer code) {
        for (ReadStatusEnum status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}