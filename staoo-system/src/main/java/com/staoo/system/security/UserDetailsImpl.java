package com.staoo.system.security;

import com.staoo.system.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * UserDetails实现类
 * 将系统用户实体转换为Spring Security可用的UserDetails对象
 */
public class UserDetailsImpl implements UserDetails {
    private final User user;
    private final List<GrantedAuthority> authorities;

    public UserDetailsImpl(User user, List<String> roles) {
        this.user = user;
        this.authorities = new ArrayList<>();
        if (roles != null && !roles.isEmpty()) {
            for (String role : roles) {
                this.authorities.add(new SimpleGrantedAuthority(role));
            }
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        // 这里可以根据实际业务需求判断账号是否过期
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 用户状态为1表示启用
        return user.getStatus() != null && user.getStatus() == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 这里可以根据实际业务需求判断凭证是否过期
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 用户状态为1表示启用
        return user.getStatus() != null && user.getStatus() == 1;
    }

    /**
     * 获取原始用户对象
     */
    public User getUser() {
        return user;
    }
}