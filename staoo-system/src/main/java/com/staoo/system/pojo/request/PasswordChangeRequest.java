package com.staoo.system.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 密码修改请求类
 * 用于用户密码修改操作的请求参数
 */
public class PasswordChangeRequest {
    /**
     * 用户ID
     */
    @NotBlank(message = "用户ID不能为空")
    private Long userId;

    /**
     * 旧密码
     */
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    /**
     * 新密码
     * 密码必须包含至少8个字符，且包含大小写字母、数字和特殊字符
     */
    @NotBlank(message = "新密码不能为空")
    @Size(min = 8, message = "密码长度不能少于8个字符")
    private String newPassword;

    // getter and setter methods
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}