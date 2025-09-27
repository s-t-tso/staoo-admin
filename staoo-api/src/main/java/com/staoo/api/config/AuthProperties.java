package com.staoo.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 认证配置属性类
 * 用于从配置文件中读取JWT等认证相关的配置项
 */
@Configuration
@ConfigurationProperties(prefix = "staoo.auth")
public class AuthProperties {
    /**
     * JWT配置
     */
    private Jwt jwt = new Jwt();

    /**
     * 登录配置
     */
    private Login login = new Login();

    /**
     * 会话配置
     */
    private Session session = new Session();

    /**
     * CORS配置
     */
    private Cors cors = new Cors();

    public Jwt getJwt() {
        return jwt;
    }

    public void setJwt(Jwt jwt) {
        this.jwt = jwt;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Cors getCors() {
        return cors;
    }

    public void setCors(Cors cors) {
        this.cors = cors;
    }

    public static class Jwt {
        /**
         * JWT密钥
         */
        private String secret = "your-secret-key-change-in-production";

        /**
         * 访问令牌过期时间(毫秒)
         */
        private long expiration = 3600000; // 1小时

        /**
         * 刷新令牌过期时间(毫秒)
         */
        private long refreshExpiration = 86400000; // 24小时

        /**
         * 令牌前缀
         */
        private String tokenPrefix = "Bearer ";

        /**
         * 令牌头部
         */
        private String header = "Authorization";

        /**
         * 刷新令牌头部
         */
        private String refreshHeader = "Refresh-Token";

        /**
         * 允许的时钟偏差(秒)
         */
        private long allowedClockSkewSeconds = 60;

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public long getExpiration() {
            return expiration;
        }

        public void setExpiration(long expiration) {
            this.expiration = expiration;
        }

        public long getRefreshExpiration() {
            return refreshExpiration;
        }

        public void setRefreshExpiration(long refreshExpiration) {
            this.refreshExpiration = refreshExpiration;
        }

        public String getTokenPrefix() {
            return tokenPrefix;
        }

        public void setTokenPrefix(String tokenPrefix) {
            this.tokenPrefix = tokenPrefix;
        }

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public String getRefreshHeader() {
            return refreshHeader;
        }

        public void setRefreshHeader(String refreshHeader) {
            this.refreshHeader = refreshHeader;
        }

        public long getAllowedClockSkewSeconds() {
            return allowedClockSkewSeconds;
        }

        public void setAllowedClockSkewSeconds(long allowedClockSkewSeconds) {
            this.allowedClockSkewSeconds = allowedClockSkewSeconds;
        }
    }

    public static class Login {
        /**
         * 是否启用验证码
         */
        private boolean captchaEnabled = true;

        /**
         * 登录失败次数限制
         */
        private int maxLoginAttempts = 5;

        /**
         * 登录失败锁定时间(分钟)
         */
        private int lockTimeMinutes = 30;

        /**
         * 允许的登录IP列表，为空表示不限制
         */
        private List<String> allowedIpList = List.of();

        /**
         * 登录成功跳转URL
         */
        private String successUrl = "/";

        /**
         * 登录失败跳转URL
         */
        private String failureUrl = "/login?error";

        public boolean isCaptchaEnabled() {
            return captchaEnabled;
        }

        public void setCaptchaEnabled(boolean captchaEnabled) {
            this.captchaEnabled = captchaEnabled;
        }

        public int getMaxLoginAttempts() {
            return maxLoginAttempts;
        }

        public void setMaxLoginAttempts(int maxLoginAttempts) {
            this.maxLoginAttempts = maxLoginAttempts;
        }

        public int getLockTimeMinutes() {
            return lockTimeMinutes;
        }

        public void setLockTimeMinutes(int lockTimeMinutes) {
            this.lockTimeMinutes = lockTimeMinutes;
        }

        public List<String> getAllowedIpList() {
            return allowedIpList;
        }

        public void setAllowedIpList(List<String> allowedIpList) {
            this.allowedIpList = allowedIpList;
        }

        public String getSuccessUrl() {
            return successUrl;
        }

        public void setSuccessUrl(String successUrl) {
            this.successUrl = successUrl;
        }

        public String getFailureUrl() {
            return failureUrl;
        }

        public void setFailureUrl(String failureUrl) {
            this.failureUrl = failureUrl;
        }
    }

    public static class Session {
        /**
         * 最大会话数，0表示不限制
         */
        private int maxSessions = 0;

        /**
         * 当达到最大会话数时，是拒绝新登录还是踢出旧登录
         */
        private boolean maxSessionsPreventsLogin = false;

        /**
         * 会话过期时间(分钟)
         */
        private int timeoutMinutes = 30;

        public int getMaxSessions() {
            return maxSessions;
        }

        public void setMaxSessions(int maxSessions) {
            this.maxSessions = maxSessions;
        }

        public boolean isMaxSessionsPreventsLogin() {
            return maxSessionsPreventsLogin;
        }

        public void setMaxSessionsPreventsLogin(boolean maxSessionsPreventsLogin) {
            this.maxSessionsPreventsLogin = maxSessionsPreventsLogin;
        }

        public int getTimeoutMinutes() {
            return timeoutMinutes;
        }

        public void setTimeoutMinutes(int timeoutMinutes) {
            this.timeoutMinutes = timeoutMinutes;
        }
    }

    public static class Cors {
        /**
         * 是否启用CORS
         */
        private boolean enabled = true;

        /**
         * 允许的来源 - 明确指定，因为allowCredentials为true时不能使用通配符*
         */
        private List<String> allowedOrigins = List.of(
            "http://localhost:8080", 
            "http://localhost:5173", 
            "http://127.0.0.1:8080", 
            "http://127.0.0.1:5173"
        );

        /**
         * 允许的方法
         */
        private List<String> allowedMethods = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS");

        /**
         * 允许的头
         */
        private List<String> allowedHeaders = List.of("*");

        /**
         * 允许凭证
         */
        private boolean allowCredentials = true;

        /**
         * 最大缓存时间(秒)
         */
        private long maxAge = 3600;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public List<String> getAllowedOrigins() {
            return allowedOrigins;
        }

        public void setAllowedOrigins(List<String> allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }

        public List<String> getAllowedMethods() {
            return allowedMethods;
        }

        public void setAllowedMethods(List<String> allowedMethods) {
            this.allowedMethods = allowedMethods;
        }

        public List<String> getAllowedHeaders() {
            return allowedHeaders;
        }

        public void setAllowedHeaders(List<String> allowedHeaders) {
            this.allowedHeaders = allowedHeaders;
        }

        public boolean isAllowCredentials() {
            return allowCredentials;
        }

        public void setAllowCredentials(boolean allowCredentials) {
            this.allowCredentials = allowCredentials;
        }

        public long getMaxAge() {
            return maxAge;
        }

        public void setMaxAge(long maxAge) {
            this.maxAge = maxAge;
        }
    }
}
