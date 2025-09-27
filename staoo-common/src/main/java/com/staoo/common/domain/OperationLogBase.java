package com.staoo.common.domain;

import java.time.LocalDateTime;

/**
 * 操作日志实体类
 * 记录系统用户的操作行为
 */
public class OperationLogBase extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 操作人姓名
     */
    private String username;

    /**
     * 模块名称
     */
    private String module;

    /**
     * 操作类型
     */
    private String operationType;

    /**
     * 操作内容
     */
    private String content;

    /**
     * 请求URL
     */
    private String requestUrl;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 请求体
     */
    private String requestBody;

    /**
     * 响应结果
     */
    private String responseResult;

    /**
     * 操作状态（0-失败，1-成功）
     */
    private Integer status;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 操作时间
     */
    private LocalDateTime operationTime;

    /**
     * IP地址
     */
    private String ip;

    /**
     * IP地址所在地区
     */
    private String ipLocation;

    /**
     * 浏览器信息
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 执行时间（毫秒）
     */
    private Long executionTime;

    // getter and setter methods
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponseResult() {
        return responseResult;
    }

    public void setResponseResult(String responseResult) {
        this.responseResult = responseResult;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(LocalDateTime operationTime) {
        this.operationTime = operationTime;
    }

    public String getIpLocation() {
        return ipLocation;
    }

    public void setIpLocation(String ipLocation) {
        this.ipLocation = ipLocation;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    @Override
    public String toString() {
        return "OperationLogBase{" +
                "id=" + id +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", module='" + module + '\'' +
                ", operationType='" + operationType + '\'' +
                ", content='" + content + '\'' +
                ", requestUrl='" + requestUrl + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                ", requestParams='" + requestParams + '\'' +
                ", requestBody='" + requestBody + '\'' +
                ", responseResult='" + responseResult + '\'' +
                ", status=" + status +
                ", errorMessage='" + errorMessage + '\'' +
                ", operationTime=" + operationTime +
                ", ip='" + ip + '\'' +
                ", ipLocation='" + ipLocation + '\'' +
                ", browser='" + browser + '\'' +
                ", os='" + os + '\'' +
                ", executionTime=" + executionTime +
                '}';
    }
}
