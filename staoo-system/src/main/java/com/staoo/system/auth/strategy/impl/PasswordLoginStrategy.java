package com.staoo.system.auth.strategy.impl;

import com.staoo.common.enums.StatusCodeEnum;
import com.staoo.common.exception.BusinessException;
import com.staoo.common.auth.dto.LoginRequest;
import com.staoo.system.auth.strategy.AbstractLoginStrategy;
import com.staoo.system.auth.strategy.LoginStrategy;
import com.staoo.system.domain.LoginLog;
import com.staoo.system.domain.User;
import com.staoo.system.service.LoginLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 账号密码登录策略实现
 * 处理传统的用户名密码登录逻辑
 */
@Component
public class PasswordLoginStrategy extends AbstractLoginStrategy {
    
    private static final Logger logger = LoggerFactory.getLogger(PasswordLoginStrategy.class);
    private static final String LOGIN_TYPE = "PASSWORD";
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private LoginLogService loginLogService;
    
    @Value("${system.login.max-retry-count}")
    private int maxRetryCount;
    
    @Value("${system.login.lock-time}")
    private int lockTime;
    
    // 存储登录失败信息的Map
    private final Map<String, LoginFailureInfo> loginFailureMap = new ConcurrentHashMap<>();
    
    @Override
    public String getLoginType() {
        return LOGIN_TYPE;
    }
    
    @Override
    public void validateRequest(LoginRequest request) {
        if (request == null || !StringUtils.hasText(request.getUsername()) || !StringUtils.hasText(request.getPassword())) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "用户名或密码不能为空");
        }
    }
    
    @Override
    protected User authenticate(LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        String ip = request.getIp();
        String userAgent = request.getUserAgent();
        
        // 检查用户是否被锁定
        if (isUserLocked(username)) {
            String message = "用户已被锁定，请" + lockTime + "分钟后重试";
            recordLoginLog(username, false, message, ip, userAgent);
            throw new BusinessException(StatusCodeEnum.AUTHENTICATION_FAILED, message);
        }
        
        try {
            // 查询用户信息
            User user = userService.getByUsername(username);
            if (user == null) {
                handleLoginFailure(username);
                recordLoginLog(username, false, "用户不存在", ip, userAgent);
                throw new BusinessException(StatusCodeEnum.AUTHENTICATION_FAILED, "用户名或密码错误");
            }
            
            // 验证密码
            if (!passwordEncoder.matches(password, user.getPassword())) {
                handleLoginFailure(username);
                recordLoginLog(username, false, "密码错误", ip, userAgent);
                throw new BusinessException(StatusCodeEnum.AUTHENTICATION_FAILED, "用户名或密码错误");
            }
            
            // 清除登录失败记录
            loginFailureMap.remove(username);
            
            // 更新用户最后登录时间和IP
            updateUserLoginInfo(user, ip);
            
            // 记录登录日志
            recordLoginLog(username, true, "登录成功", ip, userAgent);
            
            return user;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.error("登录异常: {}", e.getMessage(), e);
            recordLoginLog(username, false, "登录异常: " + e.getMessage(), ip, userAgent);
            throw new BusinessException(StatusCodeEnum.INTERNAL_SERVER_ERROR, "登录失败，请稍后重试");
        }
    }
    
    /**
     * 处理登录失败
     */
    private void handleLoginFailure(String username) {
        LoginFailureInfo info = loginFailureMap.computeIfAbsent(username, k -> new LoginFailureInfo());
        info.incrementCount();
        
        // 检查是否达到最大重试次数
        if (info.getCount() >= maxRetryCount) {
            info.setLocked(true);
            info.setLockTime(LocalDateTime.now());
            logger.warn("用户 [{}] 登录失败次数过多，已被锁定", username);
        }
    }
    
    /**
     * 检查用户是否被锁定
     */
    private boolean isUserLocked(String username) {
        LoginFailureInfo info = loginFailureMap.get(username);
        if (info == null || !info.isLocked()) {
            return false;
        }
        
        // 检查锁定时间是否已过
        LocalDateTime lockTime = info.getLockTime();
        if (lockTime == null) {
            return false;
        }
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime unlockTime = lockTime.plusMinutes(this.lockTime);
        
        // 如果锁定时间已过，解除锁定
        if (now.isAfter(unlockTime)) {
            loginFailureMap.remove(username);
            return false;
        }
        
        return true;
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
     * 登录失败信息内部类
     */
    private static class LoginFailureInfo {
        private int count = 0;
        private boolean locked = false;
        private LocalDateTime lockTime;
        
        public int getCount() {
            return count;
        }
        
        public void incrementCount() {
            this.count++;
        }
        
        public boolean isLocked() {
            return locked;
        }
        
        public void setLocked(boolean locked) {
            this.locked = locked;
        }
        
        public LocalDateTime getLockTime() {
            return lockTime;
        }
        
        public void setLockTime(LocalDateTime lockTime) {
            this.lockTime = lockTime;
        }
    }
}