package com.staoo.system.mapper;

import com.staoo.system.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper接口
 * 提供用户相关的数据库操作方法
 */
@Mapper
public interface UserMapper {
    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户信息
     */
    User getById(@Param("id") Long id);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    User getByUsername(@Param("username") String username);

    /**
     * 查询用户列表
     * @param user 查询条件
     * @return 用户列表
     */
    List<User> getList(User user);

    /**
     * 根据部门ID查询用户列表
     * @param user 查询条件
     * @return 用户列表
     */
    List<User> getListByDeptId(User user);

    /**
     * 新增用户
     * @param user 用户信息
     * @return 影响行数
     */
    int insert(User user);

    /**
     * 更新用户
     * @param user 用户信息
     * @return 影响行数
     */
    int update(User user);

    /**
     * 删除用户
     * @param id 用户ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 批量删除用户
     * @param ids 用户ID列表
     * @return 影响行数
     */
    int deleteByIds(@Param("ids") List<Long> ids);

    /**
     * 批量更新用户状态
     * @param ids 用户ID列表
     * @param status 状态
     * @return 影响行数
     */
    int updateStatusByIds(@Param("ids") List<Long> ids, @Param("status") Integer status);

    /**
     * 查询用户总数
     * @param user 查询条件
     * @return 用户总数
     */
    int getCount(User user);

    /**
     * 根据用户ID查询角色ID列表
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Long> getRoleIdsByUserId(@Param("userId") Long userId);

    /**
     * 保存用户角色关系
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 影响行数
     */
    int saveUserRoles(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);

    /**
     * 删除用户角色关系
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteUserRoles(@Param("userId") Long userId);
}