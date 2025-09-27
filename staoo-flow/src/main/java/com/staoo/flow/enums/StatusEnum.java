package com.staoo.flow.enums;

/**
 * 状态枚举类
 * 定义系统中各种状态值
 */
public enum StatusEnum {
    // 表单和流程相关状态
    DRAFT("DRAFT", "草稿"),
    PUBLISHED("PUBLISHED", "已发布"),
    SUBMITTED("SUBMITTED", "已提交"),
    APPROVED("APPROVED", "已审批"),
    REJECTED("REJECTED", "已拒绝"),
    
    // 流程任务相关状态
    CLAIM("CLAIM", "认领"),
    ASSIGN("ASSIGN", "指派"),
    APPROVE("APPROVE", "审批通过"),
    REJECT("REJECT", "拒绝");
    
    private final String code;
    private final String desc;
    
    StatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDesc() {
        return desc;
    }
    
    /**
     * 根据状态码获取枚举实例
     * @param code 状态码
     * @return 枚举实例
     */
    public static StatusEnum getByCode(String code) {
        for (StatusEnum status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}