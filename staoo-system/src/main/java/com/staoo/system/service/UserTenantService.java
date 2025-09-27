package com.staoo.system.service;

import com.staoo.system.domain.UserTenant;
import java.util.List;

/**
 * 用户-租户关联Service接口
 * 提供用户与租户关联关系的业务操作方法
 */
public interface UserTenantService {
    /**
     * 新增用户-租户关联
     * @param userTenant 用户-租户关联信息
     * @return 结果
     */
    boolean addUserTenant(UserTenant userTenant);

    /**
     * 批量新增用户-租户关联
     * @param userTenantList 用户-租户关联列表
     * @return 结果
     */
    boolean batchAddUserTenant(List<UserTenant> userTenantList);

    /**
     * 删除用户-租户关联
     * @param id 主键ID
     * @return 结果
     */
    boolean deleteUserTenantById(Long id);

    /**
     * 根据用户ID和租户ID删除关联
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 结果
     */
    boolean deleteUserTenantByUserAndTenant(Long userId, Long tenantId);

    /**
     * 批量删除用户-租户关联
     * @param ids 主键ID列表
     * @return 结果
     */
    boolean batchDeleteUserTenantByIds(Long[] ids);

    /**
     * 批量删除用户的所有租户关联
     * @param userIds 用户ID列表
     * @return 结果
     */
    boolean batchDeleteUserTenantByUserIds(Long[] userIds);

    /**
     * 批量删除租户的所有用户关联
     * @param tenantIds 租户ID列表
     * @return 结果
     */
    boolean batchDeleteUserTenantByTenantIds(Long[] tenantIds);

    /**
     * 更新用户-租户关联信息
     * @param userTenant 用户-租户关联信息
     * @return 结果
     */
    boolean updateUserTenant(UserTenant userTenant);

    /**
     * 更新用户在租户中的角色类型
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @param roleType 角色类型
     * @return 结果
     */
    boolean updateUserTenantRoleType(Long userId, Long tenantId, Integer roleType);

    /**
     * 更新用户在租户中的状态
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @param status 状态
     * @return 结果
     */
    boolean updateUserTenantStatus(Long userId, Long tenantId, Integer status);

    /**
     * 根据ID查询用户-租户关联信息
     * @param id 主键ID
     * @return 用户-租户关联信息
     */
    UserTenant getUserTenantById(Long id);

    /**
     * 根据用户ID和租户ID查询关联信息
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 用户-租户关联信息
     */
    UserTenant getUserTenantByUserAndTenant(Long userId, Long tenantId);

    /**
     * 根据用户ID查询其所有租户关联
     * @param userId 用户ID
     * @return 用户-租户关联列表
     */
    List<UserTenant> getUserTenantsByUserId(Long userId);

    /**
     * 根据租户ID查询其所有用户关联
     * @param tenantId 租户ID
     * @return 用户-租户关联列表
     */
    List<UserTenant> getUserTenantsByTenantId(Long tenantId);

    /**
     * 查询用户在指定租户中的角色类型
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 角色类型
     */
    Integer getUserTenantRoleType(Long userId, Long tenantId);

    /**
     * 检查用户是否属于指定租户
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 是否属于
     */
    boolean checkUserBelongsToTenant(Long userId, Long tenantId);

    /**
     * 检查用户在租户中是否具有指定角色类型
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @param roleType 角色类型
     * @return 是否具有
     */
    boolean checkUserHasRoleTypeInTenant(Long userId, Long tenantId, Integer roleType);

    /**
     * 检查用户在租户中是否具有创建者权限
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 是否具有
     */
    boolean checkUserIsCreatorInTenant(Long userId, Long tenantId);

    /**
     * 检查用户在租户中是否具有管理者权限
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 是否具有
     */
    boolean checkUserIsManagerInTenant(Long userId, Long tenantId);

    /**
     * 检查用户在租户中是否具有普通用户权限
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 是否具有
     */
    boolean checkUserIsNormalInTenant(Long userId, Long tenantId);
}