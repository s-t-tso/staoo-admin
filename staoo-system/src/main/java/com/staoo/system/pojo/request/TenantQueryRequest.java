package com.staoo.system.pojo.request;

import com.staoo.common.domain.PageQuery;
import jakarta.validation.constraints.Size;

/**
 * 租户查询请求对象
 */
public class TenantQueryRequest extends PageQuery {
    /**
     * 租户ID
     */
    private Long id;
    
    /**
     * 租户名称
     */
    @Size(max = 50, message = "租户名称不能超过50个字符")
    private String tenantName;
    
    /**
     * 状态（0:禁用，1:启用）
     */
    private Integer status;
    
    /**
     * 联系人
     */
    @Size(max = 20, message = "联系人不能超过20个字符")
    private String contactPerson;
    
    /**
     * 联系电话
     */
    @Size(max = 20, message = "联系电话不能超过20个字符")
    private String contactPhone;
    
    /**
     * 联系邮箱
     */
    @Size(max = 50, message = "联系邮箱不能超过50个字符")
    private String contactEmail;
    
    /**
     * 排序字段
     */
    private Integer sort;
    
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
}