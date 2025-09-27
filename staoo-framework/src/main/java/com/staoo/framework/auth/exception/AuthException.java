package com.staoo.framework.auth.exception;

import com.staoo.common.enums.StatusCodeEnum;

/**
 * 认证异常类
 * 用于处理认证过程中可能出现的各种异常情况
 */
public class AuthException extends RuntimeException {
    private final int code;
    private final String message;

    /**
     * 构造函数
     * @param code 错误码
     * @param message 错误消息
     */
    public AuthException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    /**
     * 构造函数，使用状态码枚举
     * @param statusCodeEnum 状态码枚举
     */
    public AuthException(StatusCodeEnum statusCodeEnum) {
        super(statusCodeEnum.getMessage());
        this.code = statusCodeEnum.getCode();
        this.message = statusCodeEnum.getMessage();
    }

    /**
     * 构造函数，带原始异常
     * @param code 错误码
     * @param message 错误消息
     * @param cause 原始异常
     */
    public AuthException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    /**
     * 构造函数，使用状态码枚举和原始异常
     * @param statusCodeEnum 状态码枚举
     * @param cause 原始异常
     */
    public AuthException(StatusCodeEnum statusCodeEnum, Throwable cause) {
        super(statusCodeEnum.getMessage(), cause);
        this.code = statusCodeEnum.getCode();
        this.message = statusCodeEnum.getMessage();
    }
    
    public int getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
}