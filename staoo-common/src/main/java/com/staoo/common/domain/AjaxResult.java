package com.staoo.common.domain;

import java.io.Serializable;

/**
 * AJAX统一响应实体类
 * 用于封装所有API接口的响应数据
 */
public class AjaxResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 数据
     */
    private T data;

    /**
     * 消息
     */
    private String message;

    /**
     * 错误代码
     */
    private String errcode;

    /**
     * 错误消息
     */
    private String errmsg;

    // 无参构造方法
    public AjaxResult() {
    }

    // 带参构造方法
    public AjaxResult(Integer code, T data, String message, String errcode, String errmsg) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    // getter and setter methods
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    /**
     * 成功响应
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 响应对象
     */
    public static <T> AjaxResult<T> success(T data) {
        return new AjaxResult<>(200, data, "操作成功", null, null);
    }

    /**
     * 成功响应
     * @param <T> 数据类型
     * @return 响应对象
     */
    public static <T> AjaxResult<T> success() {
        return new AjaxResult<>(200, null, "操作成功", null, null);
    }

    /**
     * 失败响应
     * @param code 状态码
     * @param message 消息
     * @param errcode 错误代码
     * @param errmsg 错误消息
     * @param <T> 数据类型
     * @return 响应对象
     */
    public static <T> AjaxResult<T> error(Integer code, String message, String errcode, String errmsg) {
        return new AjaxResult<>(code, null, message, errcode, errmsg);
    }

    /**
     * 失败响应
     * @param code 状态码
     * @param message 消息
     * @param <T> 数据类型
     * @return 响应对象
     */
    public static <T> AjaxResult<T> error(Integer code, String message) {
        return new AjaxResult<>(code, null, message, "SERVER_ERROR", message);
    }

    /**
     * 失败响应
     * @param message 消息
     * @param <T> 数据类型
     * @return 响应对象
     */
    public static <T> AjaxResult<T> error(String message) {
        return new AjaxResult<>(500, null, message, "SERVER_ERROR", "服务器内部错误");
    }

    @Override
    public String toString() {
        return "AjaxResult{" +
                "code=" + code +
                ", data=" + data +
                ", message='" + message + '\'' +
                ", errcode='" + errcode + '\'' +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }
}