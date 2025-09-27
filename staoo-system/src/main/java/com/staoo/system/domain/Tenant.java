package com.staoo.system.domain;

import com.staoo.common.domain.BaseEntity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 租户实体类
 * 对应数据库中的sys_tenant表
 */
public class Tenant extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 租户ID
     */
    private Long id;

    /**
     * 租户名称
     */
    private String tenantName;

    /**
     * 租户备注
     */
    private String remark;

    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 租户描述
     */
    private String description;

    /**
     * 租户到期时间
     */
    private LocalDateTime expireTime;

    /**
     * 最大用户数
     */
    private Integer maxUsers;

    /**
     * 最大数据存储空间（MB）
     */
    private Long maxStorage;

    /**
     * 已使用存储空间（MB）
     */
    private Long usedStorage;

    /**
     * 租户配置JSON
     */
    private String configJson;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 租户状态（0：正常，1：停用）
     */
    private Integer status;

    /**
     * 租户排序
     */
    private Integer sort;

    /**
     * 租户参数
     */
    private String tenantParams;

    /**
     * 租户联系电话
     */
    private String contactPhone;

    /**
     * 租户联系邮箱
     */
    private String contactEmail;

    /**
     * 租户联系地址
     */
    private String contactAddress;

    /**
     * 租户联系人
     */
    private String contactPerson;

    /**
     * 租户联系人电话
     */
    private String personPhone;

    /**
     * 租户联系人邮箱
     */
    private String personEmail;

    /**
     * 租户联系人地址
     */
    private String personAddress;

    /**
     * 租户联系人参数
     */
    private String personParams;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除标志（0:未删除 1:已删除）
     */
    private Integer delFlag;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPersonPhone() {
        return personPhone;
    }

    public void setPersonPhone(String personPhone) {
        this.personPhone = personPhone;
    }

    public String getPersonEmail() {
        return personEmail;
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail = personEmail;
    }

    public String getPersonAddress() {
        return personAddress;
    }

    public void setPersonAddress(String personAddress) {
        this.personAddress = personAddress;
    }

    public String getPersonParams() {
        return personParams;
    }

    public void setPersonParams(String personParams) {
        this.personParams = personParams;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
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

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(Integer maxUsers) {
        this.maxUsers = maxUsers;
    }

    public Long getMaxStorage() {
        return maxStorage;
    }

    public void setMaxStorage(Long maxStorage) {
        this.maxStorage = maxStorage;
    }

    public Long getUsedStorage() {
        return usedStorage;
    }

    public void setUsedStorage(Long usedStorage) {
        this.usedStorage = usedStorage;
    }

    public String getConfigJson() {
        return configJson;
    }

    public void setConfigJson(String configJson) {
        this.configJson = configJson;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}