package com.staoo.system.security;

import com.staoo.system.domain.User;
import com.staoo.system.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 用户认证服务实现类
 * 实现Spring Security的UserDetailsService接口
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("尝试加载用户信息: {}", username);
        
        if (!StringUtils.hasText(username)) {
            throw new UsernameNotFoundException("用户名为空");
        }
        
        try {
            // 通过UserService获取用户信息
            User user = userService.getByUsername(username);
            
            if (user == null) {
                logger.warn("用户不存在: {}", username);
                throw new UsernameNotFoundException("用户不存在: " + username);
            }
            
            // 检查用户状态
            if (user.getStatus() == null || user.getStatus() != 1) {
                logger.warn("用户状态异常: {}, 状态: {}", username, user.getStatus());
                throw new UsernameNotFoundException("用户状态异常");
            }
            
            // 构建UserDetails对象返回
            // 暂时传入null作为roles列表，实际项目中应该从数据库获取用户角色
            return new UserDetailsImpl(user, null);
        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("加载用户信息失败: {}", e.getMessage());
            throw new UsernameNotFoundException("加载用户信息失败", e);
        }
    }
}