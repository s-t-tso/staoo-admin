package com.staoo.system.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.staoo.common.domain.PageQuery;
import com.staoo.common.domain.TableResult;
import com.staoo.common.enums.StatusCodeEnum;
import com.staoo.common.exception.BusinessException;
import com.staoo.common.util.UserUtils;
import com.staoo.system.domain.User;
import com.staoo.system.domain.UserTenant;
import com.staoo.system.mapper.UserMapper;
import com.staoo.system.pojo.request.UserQueryRequest;
import com.staoo.system.service.IUserDeptService;
import com.staoo.system.service.UserService;
import com.staoo.system.service.UserTenantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
        User user = userMapper.getByUsername(username);
        if (user != null) {
            // 加载用户的租户关联信息
            List<UserTenant> userTenants = userTenantService.getUserTenantsByUserId(user.getId());
            user.setUserTenants(userTenants);
        }
        return user;
    }

    @Override
    public List<User> getList(UserQueryRequest user) {
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
    public Page<User> getPage(UserQueryRequest query) {
        try {

            // TODO: 这里需要设置分页参数到mybatis的分页插件或查询条件中
            PageHelper.startPage(query.getPageNum(), query.getPageSize());
            List<User> userList = userMapper.getList(query);
            // 查询列表
            Page<User> list = (Page<User>)  userList;
            // 为每个用户加载租户关联信息
            for (User u : list) {
                List<UserTenant> userTenants = userTenantService.getUserTenantsByUserId(u.getId());
                u.setUserTenants(userTenants);
            }
            return list;
        } catch (Exception e) {
            logger.error("分页查询用户失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(User user) {
        try {
            // 加密密码
            if (StringUtils.hasText(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            // 注意：createTime和updateTime字段将由MyBatis拦截器自动填充
            
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
                    // 时间字段由MyBatis拦截器自动填充，无需手动设置
                    // userTenant.setCreateTime(now);
                    // userTenant.setUpdateTime(now);
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
                    throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR, "用户名已存在");
                }
            }

            // 注意：updateTime字段将由MyBatis拦截器自动填充

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
                for (UserTenant userTenant : user.getUserTenants()) {
                    userTenant.setUserId(user.getId());
                    // 时间字段由MyBatis拦截器自动填充，无需手动设置
                    // userTenant.setCreateTime(now);
                    // userTenant.setUpdateTime(now);
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
            // 注意：updateTime字段将由MyBatis拦截器自动填充

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
            // 注意：updateTime字段将由MyBatis拦截器自动填充

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
            // 检查用户是否存在
            User existingUser = userMapper.getById(user.getId());
            if (existingUser == null) {
                logger.error("用户不存在: {}", user.getId());
                throw new BusinessException(StatusCodeEnum.USER_NOT_FOUND);
            }

            // 注意：updateTime字段将由MyBatis拦截器自动填充
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
            // 检查用户是否存在
            User user = userMapper.getById(userId);
            if (user == null) {
                logger.error("用户不存在: {}", userId);
                throw new BusinessException(StatusCodeEnum.USER_NOT_FOUND);
            }

            // 验证旧密码
            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                logger.error("旧密码错误");
                throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR, "旧密码错误");
            }

            // 加密新密码
            String encodedPassword = passwordEncoder.encode(newPassword);

            // 更新密码
            User updateUser = new User();
            updateUser.setId(userId);
            updateUser.setPassword(encodedPassword);
            // 注意：updateTime字段将由MyBatis拦截器自动填充

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

}
