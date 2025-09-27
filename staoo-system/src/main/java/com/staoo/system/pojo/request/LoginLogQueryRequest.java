package com.staoo.system.pojo.request;

import com.staoo.common.domain.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 登录日志查询请求类
 * 用于登录日志的分页查询条件
 */
@Schema(description = "登录日志查询请求")
public class LoginLogQueryRequest extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 租户ID
     */
    @Schema(description = "租户ID")
    private Long tenantId;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    @Size(max = 50, message = "用户名长度不能超过50个字符")
    private String username;

    /**
     * 登录IP地址
     */
    @Schema(description = "登录IP地址")
    @Size(max = 50, message = "IP地址长度不能超过50个字符")
    private String ip;

    /**
     * 登录地点
     */
    @Schema(description = "登录地点")
    @Size(max = 100, message = "登录地点长度不能超过100个字符")
    private String location;

    /**
     * 浏览器
     */
    @Schema(description = "浏览器")
    @Size(max = 50, message = "浏览器信息长度不能超过50个字符")
    private String browser;

    /**
     * 操作系统
     */
    @Schema(description = "操作系统")
    @Size(max = 50, message = "操作系统信息长度不能超过50个字符")
    private String os;

    /**
     * 登录状态
     * 0-失败，1-成功
     */
    @Schema(description = "登录状态（0-失败，1-成功）")
    @Pattern(regexp = "^[01]$", message = "登录状态只能是0或1")
    private String status;

    /**
     * 登录时间开始
     */
    @Schema(description = "登录时间开始")
    private LocalDateTime loginTimeStart;

    /**
     * 登录时间结束
     */
    @Schema(description = "登录时间结束")
    private LocalDateTime loginTimeEnd;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getLoginTimeStart() {
        return loginTimeStart;
    }

    public void setLoginTimeStart(LocalDateTime loginTimeStart) {
        this.loginTimeStart = loginTimeStart;
    }

    public LocalDateTime getLoginTimeEnd() {
        return loginTimeEnd;
    }

    public void setLoginTimeEnd(LocalDateTime loginTimeEnd) {
        this.loginTimeEnd = loginTimeEnd;
    }
}