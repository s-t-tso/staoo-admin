package com.staoo.system.auth.strategy.impl;

import com.staoo.common.enums.StatusCodeEnum;
import com.staoo.common.exception.BusinessException;
import com.staoo.common.auth.dto.LoginRequest;
import com.staoo.system.auth.strategy.AbstractLoginStrategy;
import com.staoo.system.domain.LoginLog;
import com.staoo.system.domain.User;
import com.staoo.system.service.LoginLogService;
import com.staoo.system.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 手机验证码登录策略实现
 * 处理手机验证码登录逻辑
 */
@Component
public class SmsLoginStrategy extends AbstractLoginStrategy {

    private static final Logger logger = LoggerFactory.getLogger(SmsLoginStrategy.class);
    private static final String LOGIN_TYPE = "SMS";

    // 验证码有效期（分钟）
    private static final int SMS_CODE_EXPIRATION = 5;


    // 验证码长度
    private static final int SMS_CODE_LENGTH = 6;

    // 存储验证码信息的Map
    private final Map<String, SmsCodeInfo> smsCodeMap = new ConcurrentHashMap<>();

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private SmsService smsService;

    @Override
    public String getLoginType() {
        return LOGIN_TYPE;
    }

    @Override
    public void validateRequest(LoginRequest request) {
        if (request == null || !StringUtils.hasText(request.getUsername())) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "手机号码不能为空");
        }

        if (!StringUtils.hasText(request.getPassword())) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "验证码不能为空");
        }

        // 简单的手机号码格式验证
        if (!request.getUsername().matches("^1[3-9]\\d{9}$")) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "手机号码格式不正确");
        }
    }

    @Override
    protected User authenticate(LoginRequest request) {
        String phoneNumber = request.getUsername();
        // 在短信登录中，密码字段用于存储验证码
        String code = request.getPassword();
        String ip = request.getIp();
        String userAgent = request.getUserAgent();

        try {
            // 验证验证码
            if (!validateSmsCode(phoneNumber, code)) {
                recordLoginLog(phoneNumber, false, "验证码错误或已过期", ip, userAgent);
                throw new BusinessException(StatusCodeEnum.AUTHENTICATION_FAILED, "验证码错误或已过期");
            }

            // 查询用户信息（假设username字段存储手机号）
            User user = userService.getByUsername(phoneNumber);
            if (user == null) {
                recordLoginLog(phoneNumber, false, "用户不存在", ip, userAgent);
                throw new BusinessException(StatusCodeEnum.AUTHENTICATION_FAILED, "用户不存在");
            }

            // 更新用户最后登录时间和IP
            updateUserLoginInfo(user, ip);

            // 记录登录日志
            recordLoginLog(phoneNumber, true, "手机验证码登录成功", ip, userAgent);

            // 验证码使用后移除
            smsCodeMap.remove(phoneNumber);

            return user;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.error("手机验证码登录异常: {}", e.getMessage(), e);
            recordLoginLog(phoneNumber, false, "登录异常: " + e.getMessage(), ip, userAgent);
            throw new BusinessException(StatusCodeEnum.INTERNAL_SERVER_ERROR, "登录失败，请稍后重试");
        }
    }

    /**
     * 发送短信验证码
     * @param phoneNumber 手机号码
     * @return 是否发送成功
     */
    public boolean sendSmsCode(String phoneNumber) {
        // 验证手机号码格式
        if (!phoneNumber.matches("^1[3-9]\\d{9}$")) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "手机号码格式不正确");
        }

        // 检查发送频率
        if (!smsService.checkSendFrequency(phoneNumber)) {
            // 频率检查失败，这里已经在checkSendFrequency方法中抛出异常
            return false;
        }

        // 生成验证码
        String code = smsService.generateVerificationCode(SMS_CODE_LENGTH);

        // 存储验证码信息
        SmsCodeInfo smsCodeInfo = new SmsCodeInfo();
        smsCodeInfo.setCode(code);
        smsCodeInfo.setCreateTime(LocalDateTime.now());
        smsCodeMap.put(phoneNumber, smsCodeInfo);

        // 发送验证码短信
        return smsService.sendVerificationCode(phoneNumber, code);
    }

    /**
     * 验证短信验证码
     * @param phoneNumber 手机号码
     * @param code 验证码
     * @return 是否验证通过
     */
    private boolean validateSmsCode(String phoneNumber, String code) {
        if (!StringUtils.hasText(phoneNumber) || !StringUtils.hasText(code)) {
            return false;
        }

        SmsCodeInfo smsCodeInfo = smsCodeMap.get(phoneNumber);
        if (smsCodeInfo == null) {
            return false;
        }

        // 检查验证码是否过期
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireTime = smsCodeInfo.getCreateTime().plusMinutes(SMS_CODE_EXPIRATION);
        if (now.isAfter(expireTime)) {
            smsCodeMap.remove(phoneNumber);
            return false;
        }

        // 验证验证码是否匹配
        return code.equals(smsCodeInfo.getCode());
    }



    /**
     * 更新用户最后登录时间和IP
     */
    private void updateUserLoginInfo(User user, String ip) {
        try {
            user.setLastLoginTime(LocalDateTime.now());
            user.setLastLoginIp(ip);
            userService.update(user);
        } catch (Exception e) {
            logger.error("更新用户登录信息失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 记录登录日志
     */
    private void recordLoginLog(String username, boolean status, String message, String ip, String userAgent) {
        try {
            LoginLog loginLog = new LoginLog();
            loginLog.setUsername(username);
            loginLog.setStatus(status ? 1 : 0);
            loginLog.setErrorMsg(message);
            loginLog.setIp(ip);

            // 获取浏览器和操作系统信息
            String browser = getBrowser(userAgent);
            String os = getOs(userAgent);
            loginLog.setBrowser(browser);
            loginLog.setOs(os);

            // 保存登录日志
            loginLogService.addLoginLog(loginLog);
        } catch (Exception e) {
            logger.error("记录登录日志失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 获取浏览器信息
     */
    private String getBrowser(String userAgent) {
        if (!StringUtils.hasText(userAgent)) {
            return "Unknown";
        }

        if (userAgent.contains("MSIE")) {
            return "Internet Explorer";
        } else if (userAgent.contains("Firefox")) {
            return "Firefox";
        } else if (userAgent.contains("Chrome")) {
            return "Chrome";
        } else if (userAgent.contains("Safari")) {
            return "Safari";
        } else if (userAgent.contains("Opera")) {
            return "Opera";
        } else {
            return "Other";
        }
    }

    /**
     * 获取操作系统信息
     */
    private String getOs(String userAgent) {
        if (!StringUtils.hasText(userAgent)) {
            return "Unknown";
        }

        if (userAgent.contains("Windows")) {
            return "Windows";
        } else if (userAgent.contains("Macintosh")) {
            return "Mac OS";
        } else if (userAgent.contains("Linux")) {
            return "Linux";
        } else if (userAgent.contains("Android")) {
            return "Android";
        } else if (userAgent.contains("iOS")) {
            return "iOS";
        } else {
            return "Other";
        }
    }

    /**
     * 短信验证码信息内部类
     */
    private static class SmsCodeInfo {
        private String code;
        private LocalDateTime createTime;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public LocalDateTime getCreateTime() {
            return createTime;
        }

        public void setCreateTime(LocalDateTime createTime) {
            this.createTime = createTime;
        }
    }
}
