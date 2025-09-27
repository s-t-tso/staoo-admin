package com.staoo.system.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 密码重置请求类
 * 用于用户密码重置操作的请求参数
 */
public class PasswordResetRequest {
    /**
     * 新密码
     * 密码必须包含至少8个字符，且包含大小写字母、数字和特殊字符
     */
    @NotBlank(message = "新密码不能为空")
    @Size(min = 8, message = "密码长度不能少于8个字符")
    private String password;

    // getter and setter methods
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}