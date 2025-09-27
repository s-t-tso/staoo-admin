package com.staoo.common.enums;

/**
 * 状态码枚举类
 * 遵循接口设计文档中的状态码规范
 */
public enum StatusCodeEnum {
    // 成功状态码
    SUCCESS(200, "操作成功"),

    // 客户端错误状态码
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权，请登录"),
    FORBIDDEN(403, "权限不足，拒绝访问"),
    NOT_FOUND(404, "请求资源不存在"),
    METHOD_NOT_ALLOWED(405, "不支持的请求方法"),
    TOO_MANY_REQUESTS(429, "请求过于频繁，请稍后再试"),

    // 服务端错误状态码
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),
    GATEWAY_TIMEOUT(504, "网关超时"),

    // 业务错误状态码
    BUSINESS_ERROR(10001, "业务处理失败"),
    DATA_VALIDATION_ERROR(10002, "数据校验失败"),
    PARAM_VALIDATION_ERROR(10003, "参数校验失败"),
    USER_NOT_FOUND(10004, "用户不存在"),
    USER_EXISTS(10005, "用户已存在"),
    PASSWORD_ERROR(10006, "密码错误"),
    ACCOUNT_LOCKED(10007, "账号已锁定"),
    TENANT_NOT_FOUND(10008, "租户不存在"),
    ROLE_NOT_FOUND(10009, "角色不存在"),
    MENU_NOT_FOUND(10010, "菜单不存在"),
    DEPARTMENT_NOT_FOUND(10011, "部门不存在"),
    NOTICE_NOT_FOUND(10012, "通知不存在"),
    DICT_NOT_FOUND(10013, "字典不存在"),
    FILE_UPLOAD_ERROR(10014, "文件上传失败"),
    FILE_DOWNLOAD_ERROR(10015, "文件下载失败"),
    FILE_TYPE_ERROR(10016, "文件类型错误"),
    FILE_SIZE_ERROR(10017, "文件大小超出限制"),
    DATA_NOT_FOUND(10018, "数据不存在"),
    DATA_EXISTS(10019, "数据已存在"),
    AUTHENTICATION_FAILED(10020, "认证失败"),
    JWT_EXPIRED(10021, "令牌已过期"),
    JWT_INVALID(10022, "令牌无效"),
    PERMISSION_DENIED(10023, "无此操作权限"),
    OPERATION_NOT_ALLOWED(10024, "不允许的操作"),
    DATA_INTEGRITY_VIOLATION(10025, "数据完整性冲突"),
    DATABASE_ERROR(10026, "数据库操作失败"),
    CACHE_ERROR(10027, "缓存操作失败"),
    NETWORK_ERROR(10028, "网络请求失败"),
    THIRD_PARTY_SERVICE_ERROR(10029, "第三方服务调用失败"),
    FLOWABLE_ERROR(10030, "工作流处理失败"),
    SUBSCRIPTION_CREATE_FAILED(10031, "订阅创建失败"),
    SUBSCRIPTION_NOT_FOUND(10032, "订阅不存在");

    private final Integer code;
    private final String message;

    StatusCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 根据状态码获取枚举对象
     * @param code 状态码
     * @return 枚举对象
     */
    public static StatusCodeEnum getByCode(Integer code) {
        for (StatusCodeEnum status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}