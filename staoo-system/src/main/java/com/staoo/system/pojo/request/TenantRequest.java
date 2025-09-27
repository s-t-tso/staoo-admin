package com.staoo.system.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * 租户请求类
 * 用于租户管理相关接口的参数验证和接收
 */
public class TenantRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 租户ID
     */
    private Long id;

    /**
     * 租户名称
     */
    @NotBlank(message = "租户名称不能为空")
    @Size(max = 100, message = "租户名称不能超过100个字符")
    private String tenantName;

    /**
     * 租户编码
     */
    @Size(max = 50, message = "租户编码不能超过50个字符")
    private String tenantCode;

    /**
     * 租户描述
     */
    @Size(max = 500, message = "租户描述不能超过500个字符")
    private String description;

    /**
     * 租户状态：0-禁用，1-启用
     */
    @NotNull(message = "租户状态不能为空")
    private Integer status;

    /**
     * 联系人
     */
    @Size(max = 50, message = "联系人姓名不能超过50个字符")
    private String contactPerson;

    /**
     * 联系电话
     */
    @Size(max = 20, message = "联系电话不能超过20个字符")
    private String contactPhone;

    /**
     * 联系邮箱
     */
    @Size(max = 100, message = "联系邮箱不能超过100个字符")
    private String contactEmail;

    /**
     * 租户排序
     */
    private Integer sort;

    /**
     * 租户参数
     */
    @Size(max = 2000, message = "租户参数不能超过2000个字符")
    private String tenantParams;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注不能超过500个字符")
    private String remark;

    // getter and setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getTenantParams() {
        return tenantParams;
    }

    public void setTenantParams(String tenantParams) {
        this.tenantParams = tenantParams;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}