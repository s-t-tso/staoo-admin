package com.staoo.system.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JWT令牌提供者
 * 负责JWT令牌的生成、验证和解码
 */
@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh-expiration}")
    private long jwtRefreshExpiration;

    @Value("${jwt.issuer}")
    private String jwtIssuer;

    @Value("${system.login.max-sessions}")
    private int maxSessions;

    /**
     * 获取JWT过期时间
     */
    public long getJwtExpiration() {
        return jwtExpiration;
    }

    // 存储用户会话信息，用于多端登录控制
    // key: username + deviceId, value: token
    private final Map<String, String> userSessions = new ConcurrentHashMap<>();

    // 存储用户当前活跃会话数
    private final Map<String, Integer> userActiveSessions = new ConcurrentHashMap<>();

    /**
     * 获取JWT密钥
     */
    private SecretKey getSignInKey() {
        try {
            // 首先尝试使用标准的Base64解码
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        } catch (Exception e) {
            try {
                // 如果标准解码失败，尝试使用URL安全的Base64解码
                return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtSecret));
            } catch (Exception ex) {
                // 如果仍然失败，记录错误并使用密钥的字节数组直接生成密钥
                logger.warn("JWT密钥无法通过Base64解码，直接使用原始字节数组: {}", ex.getMessage());
                return Keys.hmacShaKeyFor(jwtSecret.getBytes());
            }
        }
    }

    /**
     * 生成访问令牌
     * @param username 用户名
     * @param deviceId 设备ID
     * @param claims 自定义声明
     * @return JWT访问令牌
     */
    public String generateAccessToken(String username, String deviceId, Map<String, Object> claims) {
        // 检查用户会话数是否超过限制
        checkAndCleanSessions(username, deviceId);

        // 生成新令牌
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        String token = Jwts.builder()
                .setClaims(claims != null ? claims : new HashMap<>())
                .setSubject(username)
                .setIssuer(jwtIssuer)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();

        // 存储会话信息
        String sessionKey = getSessionKey(username, deviceId);
        userSessions.put(sessionKey, token);
        userActiveSessions.put(username, userActiveSessions.getOrDefault(username, 0) + 1);

        return token;
    }

    /**
     * 生成刷新令牌
     * @param username 用户名
     * @return JWT刷新令牌
     */
    public String generateRefreshToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtRefreshExpiration);

        return Jwts.builder()
                .setSubject(username)
                .setIssuer(jwtIssuer)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * 从令牌中获取用户名
     * @param token JWT令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * 验证令牌
     * @param token JWT令牌
     * @param deviceId 设备ID
     * @return 令牌是否有效
     */
    public boolean validateToken(String token, String deviceId) {
        try {
            // 解析令牌
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            String sessionKey = getSessionKey(username, deviceId);

            // 检查令牌是否在会话中
//            String storedToken = userSessions.get(sessionKey);
//            if (storedToken == null || !storedToken.equals(token)) {
//                logger.warn("令牌不在有效会话中: {}", username);
//                return false;
//            }

            // 检查令牌是否过期
            Date expiration = claims.getExpiration();
            if (expiration != null && expiration.before(new Date())) {
                logger.warn("令牌已过期: {}", username);
                // 移除过期会话
                removeSession(username, deviceId);
                return false;
            }

            return true;
        } catch (SecurityException e) {
            logger.error("无效的JWT签名", e);
        } catch (MalformedJwtException e) {
            logger.error("无效的JWT令牌", e);
        } catch (ExpiredJwtException e) {
            logger.error("JWT令牌已过期", e);
        } catch (UnsupportedJwtException e) {
            logger.error("不支持的JWT令牌", e);
        } catch (IllegalArgumentException e) {
            logger.error("JWT令牌为空或格式错误", e);
        }
        return false;
    }

    /**
     * 从令牌中获取所有声明
     * @param token JWT令牌
     * @return 声明Map
     */
    public Map<String, Object> getAllClaimsFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

    /**
     * 注销指定用户的指定设备会话
     * @param username 用户名
     * @param deviceId 设备ID
     */
    public void logout(String username, String deviceId) {
        removeSession(username, deviceId);
    }

    /**
     * 注销指定用户的所有会话
     * @param username 用户名
     */
    public void logoutAll(String username) {
        // 遍历并移除所有用户会话
        userSessions.keySet().removeIf(key -> key.startsWith(username + ":"));
        // 重置用户活跃会话数
        userActiveSessions.remove(username);
        logger.info("用户所有会话已注销: {}", username);
    }

    /**
     * 检查并清理用户会话
     * @param username 用户名
     * @param deviceId 设备ID
     */
    private void checkAndCleanSessions(String username, String deviceId) {
        String sessionKey = getSessionKey(username, deviceId);
        Integer currentSessions = userActiveSessions.getOrDefault(username, 0);

        // 如果用户会话数已达到最大限制，且新设备不是已有会话，则需要清理
        if (currentSessions >= maxSessions && !userSessions.containsKey(sessionKey)) {
            // 这里可以根据业务需求决定清理策略
            // 简单策略：清理最早的会话（实际应用中可能需要记录会话创建时间）
            int sessionsToRemove = currentSessions - maxSessions + 1;
            int removedCount = 0;

            for (String key : userSessions.keySet()) {
                if (key.startsWith(username + ":")) {
                    userSessions.remove(key);
                    removedCount++;
                    if (removedCount >= sessionsToRemove) {
                        break;
                    }
                }
            }

            // 更新会话计数
            userActiveSessions.put(username, maxSessions);
            logger.info("用户会话数已达到上限，已清理旧会话: {}, 剩余会话数: {}", username, maxSessions);
        }
    }

    /**
     * 移除指定会话
     * @param username 用户名
     * @param deviceId 设备ID
     */
    private void removeSession(String username, String deviceId) {
        String sessionKey = getSessionKey(username, deviceId);
        if (userSessions.containsKey(sessionKey)) {
            userSessions.remove(sessionKey);
            int currentSessions = userActiveSessions.getOrDefault(username, 0);
            if (currentSessions > 0) {
                userActiveSessions.put(username, currentSessions - 1);
            } else {
                userActiveSessions.remove(username);
            }
            logger.info("用户会话已注销: {}, 设备ID: {}", username, deviceId);
        }
    }

    /**
     * 获取会话键
     * @param username 用户名
     * @param deviceId 设备ID
     * @return 会话键
     */
    private String getSessionKey(String username, String deviceId) {
        return username + ":" + deviceId;
    }
}
