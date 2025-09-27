package com.staoo.system.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户请求类
 * 用于用户新增和更新操作的请求参数
 */
public class UserRequest {
    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名称
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 30, message = "用户名长度必须在2-30个字符之间")
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户昵称
     */
    @Size(max = 50, message = "用户昵称不能超过50个字符")
    private String nickname;

    /**
     * 用户手机号
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String phone;

    /**
     * 用户邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 用户状态
     * 0-禁用，1-启用，2-离职
     */
    @NotNull(message = "用户状态不能为空")
    private Integer status;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 工号
     */
    @Size(max = 20, message = "工号不能超过20个字符")
    private String workNo;

    /**
     * 备注
     */
    @Size(max = 200, message = "备注信息不能超过200个字符")
    private String remark;

    /**
     * 头像
     */
    @Size(max = 200, message = "头像URL不能超过200个字符")
    private String avatar;

    /**
     * 角色ID列表
     */
    private List<Long> roleIds;

    /**
     * 部门ID列表
     */
    private List<Long> deptIds;
    
    /**
     * 租户ID
     */
    private Long tenantId;

    // getter and setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getWorkNo() {
        return workNo;
    }

    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public List<Long> getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(List<Long> deptIds) {
        this.deptIds = deptIds;
    }
    
    public Long getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}