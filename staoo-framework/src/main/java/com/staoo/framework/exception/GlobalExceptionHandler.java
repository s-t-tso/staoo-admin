package com.staoo.framework.exception;

import com.staoo.common.domain.AjaxResult;
import com.staoo.common.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 * 用于统一捕获和处理系统中的所有异常
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理业务异常
     * @param e 业务异常
     * @return 统一响应
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public AjaxResult<String> handleBusinessException(BusinessException e) {
        logger.error("业务异常: {}", e.getMessage(), e);
        return AjaxResult.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数校验异常
     * @param e 参数校验异常
     * @return 统一响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AjaxResult<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error("参数校验异常: {}", e.getMessage(), e);
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        AjaxResult<Map<String, String>> result = AjaxResult.error(400, "参数校验失败");
        result.setData(errors);
        return result;
    }

    /**
     * 处理绑定异常
     * @param e 绑定异常
     * @return 统一响应
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AjaxResult<Map<String, String>> handleBindException(BindException e) {
        logger.error("绑定异常: {}", e.getMessage(), e);
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        AjaxResult<Map<String, String>> result = AjaxResult.error(400, "参数绑定失败");
        result.setData(errors);
        return result;
    }

    /**
     * 处理缺少请求参数异常
     * @param e 缺少请求参数异常
     * @return 统一响应
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AjaxResult<String> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        logger.error("缺少请求参数异常: {}", e.getMessage(), e);
        return AjaxResult.error(400, "缺少请求参数: " + e.getParameterName());
    }

    /**
     * 处理请求方法不支持异常
     * @param e 请求方法不支持异常
     * @return 统一响应
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public AjaxResult<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        logger.error("请求方法不支持异常: {}", e.getMessage(), e);
        return AjaxResult.error(405, "不支持的请求方法: " + e.getMethod());
    }

    /**
     * 处理参数类型不匹配异常
     * @param e 参数类型不匹配异常
     * @return 统一响应
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AjaxResult<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        logger.error("参数类型不匹配异常: {}", e.getMessage(), e);
        return AjaxResult.error(400, "参数类型不匹配: " + e.getName() + " 应为 " + e.getRequiredType().getSimpleName());
    }

    /**
     * 处理请求路径不存在异常
     * @param e 请求路径不存在异常
     * @return 统一响应
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public AjaxResult<String> handleNoHandlerFoundException(NoHandlerFoundException e) {
        logger.error("请求路径不存在异常: {}", e.getMessage(), e);
        return AjaxResult.error(404, "请求资源不存在: " + e.getRequestURL());
    }

    /**
     * 处理系统异常
     * @param e 系统异常
     * @param request HTTP请求
     * @return 统一响应
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AjaxResult<String> handleException(Exception e, HttpServletRequest request) {
        logger.error("系统异常: 请求路径={}", request.getRequestURI(), e);
        return AjaxResult.error(500, "服务器内部错误，请稍后再试");
    }

    /**
     * 处理运行时异常
     * @param e 运行时异常
     * @param request HTTP请求
     * @return 统一响应
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AjaxResult<String> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        logger.error("运行时异常: 请求路径={}", request.getRequestURI(), e);
        return AjaxResult.error(500, "服务器内部错误，请稍后再试");
    }
}