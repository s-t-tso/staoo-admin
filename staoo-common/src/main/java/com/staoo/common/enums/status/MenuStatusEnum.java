/**
 * 菜单状态枚举类
 * 定义菜单的各种状态
 */
package com.staoo.common.enums.status;

public enum MenuStatusEnum {
    /**
     * 禁用状态
     */
    DISABLED(0, "禁用"),
    
    /**
     * 启用状态
     */
    ENABLED(1, "启用");
    
    private final Integer code;
    private final String name;
    
    MenuStatusEnum(Integer code, String name) {
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
    public static MenuStatusEnum getByCode(Integer code) {
        for (MenuStatusEnum status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}