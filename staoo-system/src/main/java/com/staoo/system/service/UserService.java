package com.staoo.system.service;

import com.github.pagehelper.Page;
import com.staoo.common.domain.TableResult;
import com.staoo.common.domain.PageQuery;
import com.staoo.system.domain.User;
import com.staoo.system.pojo.request.UserQueryRequest;

import java.util.List;

/**
 * 用户服务接口
 * 定义用户相关的业务操作方法
 */
public interface UserService {
    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户信息
     */
    User getById(Long id);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    User getByUsername(String username);

    /**
     * 查询用户列表
     * @param user 查询条件
     * @return 用户列表
     */
    List<User> getList(UserQueryRequest user);

    /**
     * 分页查询用户
     * @param query 分页查询参数
     * @return 分页结果
     */
    Page<User> getPage(UserQueryRequest query);

    /**
     * 新增用户
     * @param user 用户信息
     * @return 是否成功
     */
    boolean save(User user);

    /**
     * 更新用户
     * @param user 用户信息
     * @return 是否成功
     */
    boolean update(User user);

    /**
     * 删除用户
     * @param id 用户ID
     * @return 是否成功
     */
    boolean deleteById(Long id);

    /**
     * 批量删除用户
     * @param ids 用户ID列表
     * @return 是否成功
     */
    boolean deleteByIds(List<Long> ids);

    /**
     * 批量更新用户状态
     * @param ids 用户ID列表
     * @param status 状态
     * @return 是否成功
     */
    boolean updateStatusByIds(List<Long> ids, Integer status);

    /**
     * 重置用户密码
     * @param id 用户ID
     * @param password 新密码
     * @return 是否成功
     */
    boolean resetPassword(Long id, String password);

    /**
     * 更新用户头像
     * @param id 用户ID
     * @param avatar 头像URL
     * @return 是否成功
     */
    boolean updateAvatar(Long id, String avatar);

    /**
     * 更新用户个人信息
     * @param user 用户信息
     * @return 是否成功
     */
    boolean updateProfile(User user);

    /**
     * 修改用户密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 根据用户ID查询角色ID列表
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Long> getRoleIdsByUserId(Long userId);

    /**
     * 保存用户角色关系
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 是否成功
     */
    boolean saveUserRoles(Long userId, List<Long> roleIds);
}
