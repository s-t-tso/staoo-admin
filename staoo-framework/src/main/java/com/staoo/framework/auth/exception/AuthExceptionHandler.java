package com.staoo.framework.auth.exception;

import com.staoo.common.domain.AjaxResult;
import com.staoo.common.enums.StatusCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证异常处理器
 * 捕获和处理认证相关的异常，返回统一格式的错误响应
 */
@RestControllerAdvice
public class AuthExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(AuthExceptionHandler.class);

    /**
     * 处理认证异常
     */
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<AjaxResult<?>> handleAuthException(AuthException ex, HttpServletRequest request) {
        logger.error("认证异常: {}", ex.getMessage(), ex);
        AjaxResult<?> response = AjaxResult.error(ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 处理凭据错误异常
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<AjaxResult<?>> handleBadCredentialsException(BadCredentialsException ex, HttpServletRequest request) {
        logger.error("凭据错误异常: {}", ex.getMessage(), ex);
        AjaxResult<?> response = AjaxResult.error(StatusCodeEnum.PASSWORD_ERROR.getCode(), StatusCodeEnum.PASSWORD_ERROR.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 处理用户禁用异常
     */
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<AjaxResult<?>> handleDisabledException(DisabledException ex, HttpServletRequest request) {
        logger.error("用户禁用异常: {}", ex.getMessage(), ex);
        AjaxResult<?> response = AjaxResult.error(StatusCodeEnum.AUTHENTICATION_FAILED.getCode(), StatusCodeEnum.AUTHENTICATION_FAILED.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    /**
     * 处理用户锁定异常
     */
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<AjaxResult<?>> handleLockedException(LockedException ex, HttpServletRequest request) {
        logger.error("用户锁定异常: {}", ex.getMessage(), ex);
        AjaxResult<?> response = AjaxResult.error(StatusCodeEnum.ACCOUNT_LOCKED.getCode(), StatusCodeEnum.ACCOUNT_LOCKED.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    /**
     * 处理访问拒绝异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<AjaxResult<?>> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        logger.error("访问拒绝异常: {}", ex.getMessage(), ex);
        AjaxResult<?> response = AjaxResult.error(StatusCodeEnum.PERMISSION_DENIED.getCode(), StatusCodeEnum.PERMISSION_DENIED.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    /**
     * 处理其他所有异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<AjaxResult<?>> handleGlobalException(Exception ex, HttpServletRequest request) {
        logger.error("全局异常: {}", ex.getMessage(), ex);
        AjaxResult<?> response = AjaxResult.error(StatusCodeEnum.INTERNAL_SERVER_ERROR.getCode(), StatusCodeEnum.INTERNAL_SERVER_ERROR.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 构建错误详情
     */
    private Map<String, Object> buildErrorDetails(Exception ex, WebRequest request) {
        Map<String, Object> details = new HashMap<>();
        details.put("timestamp", new Date());
        details.put("message", ex.getMessage());
        details.put("details", request.getDescription(false));
        return details;
    }
}