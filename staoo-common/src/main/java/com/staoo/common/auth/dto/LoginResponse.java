package com.staoo.common.auth.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 登录响应DTO
 */
public class LoginResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 访问令牌
     */
    private String accessToken;
    
    /**
     * 刷新令牌
     */
    private String refreshToken;
    
    /**
     * 令牌类型
     */
    private String tokenType = "Bearer";
    
    /**
     * 过期时间（秒）
     */
    private Long expiresIn;
    
    /**
     * 用户信息
     */
    private UserInfo userInfo;
    
    // Getters and Setters
    public String getAccessToken() {
        return accessToken;
    }
    
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    
    public String getRefreshToken() {
        return refreshToken;
    }
    
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    public String getTokenType() {
        return tokenType;
    }
    
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
    
    public Long getExpiresIn() {
        return expiresIn;
    }
    
    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
    
    public UserInfo getUserInfo() {
        return userInfo;
    }
    
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
    
    /**
     * 用户信息类
     */
    public static class UserInfo {
        
        /**
         * 用户ID
         */
        private Long id;
        
        /**
         * 用户名
         */
        private String username;
        
        /**
         * 昵称
         */
        private String nickname;
        
        /**
         * 头像
         */
        private String avatar;
        
        /**
         * 租户ID
         */
        private Long tenantId;
        
        /**
         * 租户代码
         */
        private String tenantCode;
        
        /**
         * 角色列表
         */
        private List<String> roles;
        
        /**
         * 权限列表
         */
        private List<String> permissions;
        
        /**
         * 部门信息
         */
        private Map<String, Object> department;
        
        /**
         * 额外信息
         */
        private Map<String, Object> extraInfo;
        
        // Getters and Setters
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
        
        public String getNickname() {
            return nickname;
        }
        
        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
        
        public String getAvatar() {
            return avatar;
        }
        
        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
        
        public Long getTenantId() {
            return tenantId;
        }
        
        public void setTenantId(Long tenantId) {
            this.tenantId = tenantId;
        }
        
        public String getTenantCode() {
            return tenantCode;
        }
        
        public void setTenantCode(String tenantCode) {
            this.tenantCode = tenantCode;
        }
        
        public List<String> getRoles() {
            return roles;
        }
        
        public void setRoles(List<String> roles) {
            this.roles = roles;
        }
        
        public List<String> getPermissions() {
            return permissions;
        }
        
        public void setPermissions(List<String> permissions) {
            this.permissions = permissions;
        }
        
        public Map<String, Object> getDepartment() {
            return department;
        }
        
        public void setDepartment(Map<String, Object> department) {
            this.department = department;
        }
        
        public Map<String, Object> getExtraInfo() {
            return extraInfo;
        }
        
        public void setExtraInfo(Map<String, Object> extraInfo) {
            this.extraInfo = extraInfo;
        }
    }
}