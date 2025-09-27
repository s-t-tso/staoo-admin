package com.staoo.system.pojo.request;

import com.staoo.common.domain.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

/**
 * 用户-租户关联查询请求
 * 用于分页查询用户与租户关联关系
 */
@Schema(description = "用户-租户关联查询请求")
public class UserTenantQueryRequest extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 租户ID
     */
    @Schema(description = "租户ID")
    private Long tenantId;

    /**
     * 角色类型（1：创建者，2：管理者，3：普通用户）
     */
    @Schema(description = "角色类型（1：创建者，2：管理者，3：普通用户）")
    private Integer roleType;

    /**
     * 状态（1：正常，0：禁用）
     */
    @Schema(description = "状态（1：正常，0：禁用）")
    private Integer status;

    /**
     * 关键词搜索（用户ID、租户ID）
     */
    @Schema(description = "关键词搜索（用户ID、租户ID）")
    @Size(max = 200, message = "关键词不能超过200个字符")
    private String keyword;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
    
    public Integer getRoleType() {
        return roleType;
    }
    
    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public String getKeyword() {
        return keyword;
    }
    
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}