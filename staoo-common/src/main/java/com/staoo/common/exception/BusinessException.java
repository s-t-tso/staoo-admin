package com.staoo.common.exception;

import com.staoo.common.enums.StatusCodeEnum;

/**
 * 自定义业务异常类
 * 用于统一处理系统中的业务异常
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误消息
     */
    private String message;

    /**
     * 错误数据
     */
    private Object data;

    // 无参构造方法
    public BusinessException() {
        super();
    }

    // 带参构造方法
    public BusinessException(String message) {
        super(message);
        this.code = 500;
        this.message = message;
    }

    // 带参构造方法
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    // 带参构造方法
    public BusinessException(Integer code, String message, Object data) {
        super(message);
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 带参构造方法
    public BusinessException(StatusCodeEnum statusCode) {
        super(statusCode.getMessage());
        this.code = statusCode.getCode();
        this.message = statusCode.getMessage();
    }

    // 带参构造方法
    public BusinessException(StatusCodeEnum statusCode, Object data) {
        super(statusCode.getMessage());
        this.code = statusCode.getCode();
        this.message = statusCode.getMessage();
        this.data = data;
    }

    // 带参构造方法
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = 500;
        this.message = message;
    }

    // 带参构造方法
    public BusinessException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    // getter and setter methods
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BusinessException{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}