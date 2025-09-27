package com.staoo.system.service;

import com.staoo.system.domain.UserDept;

import java.util.List;

/**
 * 用户部门关联服务接口
 * 提供用户部门关联相关的业务逻辑操作
 */
public interface IUserDeptService {
    /**
     * 根据用户ID获取部门ID列表
     * @param userId 用户ID
     * @return 部门ID列表
     */
    List<Long> getDeptIdsByUserId(Long userId);

    /**
     * 根据部门ID获取用户ID列表
     * @param deptId 部门ID
     * @return 用户ID列表
     */
    List<Long> getUserIdsByDeptId(Long deptId);

    /**
     * 保存用户与部门的关联
     * @param userId 用户ID
     * @param deptId 部门ID
     * @return 是否成功
     */
    boolean saveUserDept(Long userId, Long deptId);

    /**
     * 批量保存用户与部门的关联
     * @param userId 用户ID
     * @param deptIds 部门ID列表
     * @return 是否成功
     */
    boolean batchSaveUserDept(Long userId, List<Long> deptIds);

    /**
     * 更新用户与部门的关联
     * @param userId 用户ID
     * @param deptIds 新的部门ID列表
     * @return 是否成功
     */
    boolean updateUserDept(Long userId, List<Long> deptIds);

    /**
     * 删除用户与部门的关联
     * @param userId 用户ID
     * @param deptId 部门ID
     * @return 是否成功
     */
    boolean deleteUserDept(Long userId, Long deptId);

    /**
     * 根据用户ID删除所有关联
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean deleteByUserId(Long userId);

    /**
     * 根据部门ID删除所有关联
     * @param deptId 部门ID
     * @return 是否成功
     */
    boolean deleteByDeptId(Long deptId);

    /**
     * 批量根据用户ID删除所有关联
     * @param userIds 用户ID列表
     * @return 是否成功
     */
    boolean batchDeleteByUserIds(List<Long> userIds);

    /**
     * 批量根据部门ID删除所有关联
     * @param deptIds 部门ID列表
     * @return 是否成功
     */
    boolean batchDeleteByDeptIds(List<Long> deptIds);

    /**
     * 检查用户是否属于指定部门
     * @param userId 用户ID
     * @param deptId 部门ID
     * @return 是否属于
     */
    boolean checkUserBelongsToDept(Long userId, Long deptId);

    /**
     * 获取用户的主要部门ID
     * @param userId 用户ID
     * @return 主要部门ID
     */
    Long getPrimaryDeptId(Long userId);
}