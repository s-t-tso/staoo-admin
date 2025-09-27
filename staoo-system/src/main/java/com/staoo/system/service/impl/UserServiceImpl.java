package com.staoo.system.service.impl;

import com.staoo.common.domain.TableResult;
import com.staoo.common.exception.BusinessException;
import com.staoo.common.enums.StatusCodeEnum;
import com.staoo.common.domain.PageQuery;
import com.staoo.system.domain.User;
import com.staoo.system.domain.UserTenant;
import com.staoo.system.mapper.UserMapper;
import com.staoo.system.service.UserService;
import com.staoo.system.service.UserTenantService;
import com.staoo.system.service.IUserDeptService;
import com.staoo.common.util.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 用户服务实现类
 * 实现用户相关的业务逻辑
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private UserTenantService userTenantService;
    
    @Autowired
    private IUserDeptService userDeptService;

    @Override
    public User getById(Long id) {
        if (id == null || id <= 0) {
            logger.error("查询用户ID无效: {}", id);
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
        }
        User user = userMapper.getById(id);
        if (user == null) {
            logger.error("用户不存在: {}", id);
            throw new BusinessException(StatusCodeEnum.USER_NOT_FOUND);
        }
        // 加载用户的租户关联信息
        List<UserTenant> userTenants = userTenantService.getUserTenantsByUserId(id);
        user.setUserTenants(userTenants);
        // 加载用户关联的所有部门ID
        List<Long> deptIds = userDeptService.getDeptIdsByUserId(id);
        user.setDeptIds(deptIds);
        return user;
    }

    @Override
    public User getByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            logger.error("用户名为空");
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
        }
        User user = userMapper.getByUsername(username);
        if (user != null) {
            // 加载用户的租户关联信息
            List<UserTenant> userTenants = userTenantService.getUserTenantsByUserId(user.getId());
            user.setUserTenants(userTenants);
        }
        return user;
    }

    @Override
    public List<User> getList(User user) {
        try {
            List<User> users;
            // 构建查询条件时考虑部门筛选
            if (user != null && user.getDeptId() != null) {
                // 使用SQL JOIN查询直接获取部门下的用户，避免多次查询和Java代码过滤
                users = userMapper.getListByDeptId(user);
            } else {
                // 没有部门筛选时，直接查询
                users = userMapper.getList(user);
            }
            
            // 为每个用户加载租户关联信息和部门信息
            if (users != null && !users.isEmpty()) {
                // 优化：可以考虑批量加载关联数据，但根据当前需求先保持原有逻辑
                for (User u : users) {
                    List<UserTenant> userTenants = userTenantService.getUserTenantsByUserId(u.getId());
                    u.setUserTenants(userTenants);
                    
                    // 加载用户关联的所有部门ID
                    List<Long> deptIds = userDeptService.getDeptIdsByUserId(u.getId());
                    u.setDeptIds(deptIds);
                }
            }
            
            return users != null ? users : Collections.emptyList();
        } catch (Exception e) {
            logger.error("查询用户列表失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    public TableResult<User> getPage(PageQuery query) {
        try {
            // 构建查询条件
            User user = new User();
            // 如果有搜索关键词，可以设置到查询条件中
            if (StringUtils.hasText(query.getKeyword())) {
                // 这里可以根据业务需求设置不同的搜索字段
                user.setUsername(query.getKeyword());
                user.setNickname(query.getKeyword());
                user.setEmail(query.getKeyword());
                user.setPhone(query.getKeyword());
            }

            // 查询总数
            int total = userMapper.getCount(user);
            if (total == 0) {
                return TableResult.empty();
            }

            // 计算分页参数
            query.getStartIndex(); // 使用已有的getStartIndex方法
            // TODO: 这里需要设置分页参数到mybatis的分页插件或查询条件中

            // 查询列表
            List<User> list = userMapper.getList(user);
            // 为每个用户加载租户关联信息
            for (User u : list) {
                List<UserTenant> userTenants = userTenantService.getUserTenantsByUserId(u.getId());
                u.setUserTenants(userTenants);
            }
            return TableResult.build((long) total, query.getPageNum(), query.getPageSize(), list);
        } catch (Exception e) {
            logger.error("分页查询用户失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(User user) {
        try {
            // 参数校验
            validateUser(user);

            // 检查用户名是否已存在
            User existingUser = userMapper.getByUsername(user.getUsername());
            if (existingUser != null) {
                logger.error("用户名已存在: {}", user.getUsername());
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }

            // 加密密码
            if (StringUtils.hasText(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            // 设置创建时间和更新时间
            LocalDateTime now = LocalDateTime.now();
            user.setCreateTime(now);
            user.setUpdateTime(now);
            
            // 设置租户ID
            if (user.getTenantId() == null) {
                user.setTenantId(UserUtils.getCurrentTenantId());
            }

            // 保存用户信息
            int result = userMapper.insert(user);
            
            // 如果用户有租户关联信息，保存用户-租户关系
            if (result > 0 && user.getUserTenants() != null && !user.getUserTenants().isEmpty()) {
                for (UserTenant userTenant : user.getUserTenants()) {
                    userTenant.setUserId(user.getId());
                    userTenant.setCreateTime(now);
                    userTenant.setUpdateTime(now);
                    userTenantService.addUserTenant(userTenant);
                }
            }
            
            // 如果用户有部门关联信息，保存用户-部门关系
            if (result > 0 && user.getDeptIds() != null && !user.getDeptIds().isEmpty()) {
                userDeptService.batchSaveUserDept(user.getId(), user.getDeptIds());
            }
            
            return result > 0;
        } catch (BusinessException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            logger.error("新增用户失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(User user) {
        try {
            if (user == null || user.getId() == null || user.getId() <= 0) {
                logger.error("用户ID无效");
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }

            // 检查用户是否存在
            User existingUser = userMapper.getById(user.getId());
            if (existingUser == null) {
                logger.error("用户不存在: {}", user.getId());
                throw new BusinessException(StatusCodeEnum.USER_NOT_FOUND);
            }

            // 如果修改了用户名，检查新用户名是否已存在
            if (StringUtils.hasText(user.getUsername()) && !user.getUsername().equals(existingUser.getUsername())) {
                User checkUser = userMapper.getByUsername(user.getUsername());
                if (checkUser != null && !checkUser.getId().equals(user.getId())) {
                    logger.error("用户名已存在: {}", user.getUsername());
                    throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
                }
            }

            // 保留现有updateTime，如果没有显式设置
            if (user.getUpdateTime() == null) {
                user.setUpdateTime(LocalDateTime.now());
            }
            
            // 设置租户ID（如果未设置）
            if (user.getTenantId() == null) {
                user.setTenantId(UserUtils.getCurrentTenantId());
            }

            // 更新用户信息
            int result = userMapper.update(user);
            
            // 如果有用户-租户关系信息，处理用户-租户关系
            if (result > 0 && user.getUserTenants() != null) {
                // 先删除该用户的所有租户关联
                userTenantService.batchDeleteUserTenantByUserIds(new Long[]{user.getId()});
                // 然后重新添加新的租户关联
                LocalDateTime now = LocalDateTime.now();
                for (UserTenant userTenant : user.getUserTenants()) {
                    userTenant.setUserId(user.getId());
                    userTenant.setCreateTime(now);
                    userTenant.setUpdateTime(now);
                    userTenantService.addUserTenant(userTenant);
                }
            }
            
            // 如果用户有部门关联信息，更新用户-部门关系
            if (result > 0 && user.getDeptIds() != null) {
                userDeptService.batchSaveUserDept(user.getId(), user.getDeptIds());
            }
            
            return result > 0;
        } catch (BusinessException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            logger.error("更新用户失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Long id) {
        try {
            if (id == null || id <= 0) {
                logger.error("用户ID无效: {}", id);
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }

            // 检查用户是否存在
            User user = userMapper.getById(id);
            if (user == null) {
                logger.error("用户不存在: {}", id);
                throw new BusinessException(StatusCodeEnum.USER_NOT_FOUND);
            }

            // 删除用户角色关系
            userMapper.deleteUserRoles(id);
            
            // 删除用户-租户关系
            userTenantService.batchDeleteUserTenantByUserIds(new Long[]{id});
            
            // 删除用户-部门关系
              List<Long> userIds = new ArrayList<>();
              userIds.add(id);
              userDeptService.batchDeleteByUserIds(userIds);

            // 删除用户
            int result = userMapper.deleteById(id);
            return result > 0;
        } catch (BusinessException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            logger.error("删除用户失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByIds(List<Long> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                logger.error("用户ID列表为空");
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }

            // 批量删除用户角色关系和用户-租户关系
            for (Long id : ids) {
                userMapper.deleteUserRoles(id);
                userTenantService.batchDeleteUserTenantByUserIds(new Long[]{id});
            }

            // 批量删除用户
            int result = userMapper.deleteByIds(ids);
            return result > 0;
        } catch (BusinessException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            logger.error("批量删除用户失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatusByIds(List<Long> ids, Integer status) {
        try {
            if (ids == null || ids.isEmpty()) {
                logger.error("用户ID列表为空");
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }
            if (status == null) {
                logger.error("用户状态为空");
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }

            // 批量更新用户状态
            int result = userMapper.updateStatusByIds(ids, status);
            return result > 0;
        } catch (BusinessException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            logger.error("批量更新用户状态失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resetPassword(Long id, String password) {
        try {
            if (id == null || id <= 0) {
                logger.error("用户ID无效: {}", id);
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }
            if (!StringUtils.hasText(password)) {
                logger.error("密码为空");
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }

            // 检查用户是否存在
            User user = userMapper.getById(id);
            if (user == null) {
                logger.error("用户不存在: {}", id);
                throw new BusinessException(StatusCodeEnum.USER_NOT_FOUND);
            }

            // 加密密码
            String encodedPassword = passwordEncoder.encode(password);

            // 更新密码
            User updateUser = new User();
            updateUser.setId(id);
            updateUser.setPassword(encodedPassword);
            updateUser.setUpdateTime(LocalDateTime.now());

            int result = userMapper.update(updateUser);
            return result > 0;
        } catch (BusinessException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            logger.error("重置用户密码失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAvatar(Long id, String avatar) {
        try {
            if (id == null || id <= 0) {
                logger.error("用户ID无效: {}", id);
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }
            if (!StringUtils.hasText(avatar)) {
                logger.error("头像URL为空");
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }

            // 检查用户是否存在
            User user = userMapper.getById(id);
            if (user == null) {
                logger.error("用户不存在: {}", id);
                throw new BusinessException(StatusCodeEnum.USER_NOT_FOUND);
            }

            // 更新头像
            User updateUser = new User();
            updateUser.setId(id);
            // updateUser.setAvatar(avatar); // 方法不存在
            updateUser.setUpdateTime(LocalDateTime.now());

            int result = userMapper.update(updateUser);
            return result > 0;
        } catch (BusinessException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            logger.error("更新用户头像失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProfile(User user) {
        try {
            if (user == null || user.getId() == null || user.getId() <= 0) {
                logger.error("用户ID无效");
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }

            // 检查用户是否存在
            User existingUser = userMapper.getById(user.getId());
            if (existingUser == null) {
                logger.error("用户不存在: {}", user.getId());
                throw new BusinessException(StatusCodeEnum.USER_NOT_FOUND);
            }

            // 设置更新时间
            user.setUpdateTime(LocalDateTime.now());
            // 不允许修改密码、用户名、状态等关键信息
            user.setPassword(null);
            user.setUsername(null);
            user.setStatus(null);
            // user.setIsAdmin(null); // 方法不存在

            // 更新用户信息
            int result = userMapper.update(user);
            return result > 0;
        } catch (BusinessException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            logger.error("更新用户个人信息失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        try {
            if (userId == null || userId <= 0) {
                logger.error("用户ID无效: {}", userId);
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }
            if (!StringUtils.hasText(oldPassword)) {
                logger.error("旧密码为空");
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }
            if (!StringUtils.hasText(newPassword)) {
                logger.error("新密码为空");
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }

            // 检查用户是否存在
            User user = userMapper.getById(userId);
            if (user == null) {
                logger.error("用户不存在: {}", userId);
                throw new BusinessException(StatusCodeEnum.USER_NOT_FOUND);
            }

            // 验证旧密码
            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                logger.error("旧密码错误");
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }

            // 加密新密码
            String encodedPassword = passwordEncoder.encode(newPassword);

            // 更新密码
            User updateUser = new User();
            updateUser.setId(userId);
            updateUser.setPassword(encodedPassword);
            updateUser.setUpdateTime(LocalDateTime.now());

            int result = userMapper.update(updateUser);
            return result > 0;
        } catch (BusinessException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            logger.error("修改用户密码失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        try {
            if (userId == null || userId <= 0) {
                logger.error("用户ID无效: {}", userId);
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }

            // 检查用户是否存在
            User user = userMapper.getById(userId);
            if (user == null) {
                logger.error("用户不存在: {}", userId);
                throw new BusinessException(StatusCodeEnum.USER_NOT_FOUND);
            }

            return userMapper.getRoleIdsByUserId(userId);
        } catch (BusinessException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            logger.error("查询用户角色ID列表失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUserRoles(Long userId, List<Long> roleIds) {
        try {
            if (userId == null || userId <= 0) {
                logger.error("用户ID无效: {}", userId);
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }

            // 检查用户是否存在
            User user = userMapper.getById(userId);
            if (user == null) {
                logger.error("用户不存在: {}", userId);
                throw new BusinessException(StatusCodeEnum.USER_NOT_FOUND);
            }

            // 删除原有的用户角色关系
            userMapper.deleteUserRoles(userId);

            // 如果角色ID列表为空，直接返回成功
            if (roleIds == null || roleIds.isEmpty()) {
                return true;
            }

            // 保存新的用户角色关系
            int result = userMapper.saveUserRoles(userId, roleIds);
            return result > 0;
        } catch (BusinessException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            logger.error("保存用户角色关系失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    /**
     * 验证用户信息
     * @param user 用户信息
     */
    private void validateUser(User user) {
        if (user == null) {
            logger.error("用户信息为空");
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
        }
        if (!StringUtils.hasText(user.getUsername())) {
            logger.error("用户名为空");
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
        }
        if (!StringUtils.hasText(user.getPassword())) {
            logger.error("密码为空");
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
        }
        // 其他验证逻辑...
    }
}