package com.staoo.system.auth.strategy;

import com.staoo.common.enums.StatusCodeEnum;
import com.staoo.common.exception.BusinessException;
import com.staoo.common.auth.dto.LoginRequest;
import com.staoo.common.auth.dto.LoginResponse;
import com.staoo.common.auth.dto.LoginResponse.UserInfo;
import com.staoo.system.auth.jwt.JwtTokenProvider;
import com.staoo.system.domain.User;
import com.staoo.system.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录策略抽象基类
 * 提供通用的登录功能，具体的登录策略需要继承此类并实现抽象方法
 */
public abstract class AbstractLoginStrategy implements LoginStrategy {
    
    protected Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    protected UserService userService;
    
    @Autowired
    protected JwtTokenProvider jwtTokenProvider;
    
    /**
     * 检查用户状态是否有效
     * @param user 用户对象
     * @throws BusinessException 当用户状态无效时抛出异常
     */
    protected void checkUserStatus(User user) {
        if (user == null) {
            throw new BusinessException(StatusCodeEnum.AUTHENTICATION_FAILED, "用户不存在");
        }
        
        // 根据User类中status字段的定义，0表示禁用，1表示启用，2表示离职
        if (user.getStatus() == 0) {
            throw new BusinessException(StatusCodeEnum.AUTHENTICATION_FAILED, "用户已禁用");
        } else if (user.getStatus() == 2) {
            throw new BusinessException(StatusCodeEnum.AUTHENTICATION_FAILED, "用户已离职");
        }
    }
    
    /**
     * 构建登录响应对象
     * @param user 用户对象
     * @param request 登录请求
     * @return 登录响应
     */
    protected LoginResponse buildLoginResponse(User user, LoginRequest request) {
        // 创建用户信息对象
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setNickname(user.getNickname());
        userInfo.setAvatar(user.getAvatar());
        // 用户没有直接的tenantId字段，设置为null
        userInfo.setTenantId(null);
        // common包的UserInfo类没有deptId字段，使用department字段存储部门信息
        if (user.getDeptId() != null) {
            Map<String, Object> department = new HashMap<>();
            department.put("id", user.getDeptId());
            userInfo.setDepartment(department);
        }
        
        // 创建登录响应
        LoginResponse response = new LoginResponse();
        response.setUserInfo(userInfo);
        
        // 生成token相关信息
        String deviceId = request.getDeviceId();
        if (deviceId == null) {
            deviceId = "default_device";
        }
        
        // 构建令牌载荷
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put("nickname", user.getNickname());
        
        // 生成accessToken和refreshToken
        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername(), deviceId, claims);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());
        
        // 设置令牌信息
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        // 将毫秒转换为秒
        response.setExpiresIn(jwtTokenProvider.getJwtExpiration() / 1000);
        
        return response;
    }
    
    @Override
    public LoginResponse login(LoginRequest request) {
        logger.info("执行[{}]登录请求", getLoginType());
        
        // 1. 验证请求参数
        validateRequest(request);
        
        // 2. 执行具体的登录逻辑（由子类实现）
        User user = authenticate(request);
        
        // 3. 检查用户状态
        checkUserStatus(user);
        
        // 4. 构建并返回登录响应
        return buildLoginResponse(user, request);
    }
    
    /**
     * 执行具体的认证逻辑
     * @param request 登录请求
     * @return 认证成功的用户对象
     */
    protected abstract User authenticate(LoginRequest request);
}