package com.staoo.system.mapper;

import com.staoo.system.domain.UserTenant;
import com.staoo.system.pojo.request.UserTenantQueryRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户-租户关联Mapper接口
 * 提供用户与租户关联关系的数据库操作方法
 */
@Mapper
public interface UserTenantMapper {
    /**
     * 新增用户-租户关联
     * @param userTenant 用户-租户关联信息
     * @return 影响行数
     */
    int insert(UserTenant userTenant);

    /**
     * 根据ID删除用户-租户关联
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 根据用户ID和租户ID删除关联
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 影响行数
     */
    int deleteByUserAndTenant(@Param("userId") Long userId, @Param("tenantId") Long tenantId);

    /**
     * 更新用户-租户关联信息
     * @param userTenant 用户-租户关联信息
     * @return 影响行数
     */
    int update(UserTenant userTenant);

    /**
     * 更新用户在租户中的角色类型
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @param roleType 角色类型
     * @return 影响行数
     */
    int updateRoleType(@Param("userId") Long userId, @Param("tenantId") Long tenantId, @Param("roleType") Integer roleType);

    /**
     * 更新用户在租户中的状态
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(@Param("userId") Long userId, @Param("tenantId") Long tenantId, @Param("status") Integer status);

    /**
     * 根据ID查询用户-租户关联信息
     * @param id 主键ID
     * @return 用户-租户关联信息
     */
    UserTenant selectById(Long id);

    /**
     * 根据用户ID和租户ID查询关联信息
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 用户-租户关联信息
     */
    UserTenant selectByUserAndTenant(@Param("userId") Long userId, @Param("tenantId") Long tenantId);

    /**
     * 根据用户ID查询其所有租户关联
     * @param userId 用户ID
     * @return 用户-租户关联列表
     */
    List<UserTenant> selectByUserId(Long userId);

    /**
     * 根据租户ID查询其所有用户关联
     * @param tenantId 租户ID
     * @return 用户-租户关联列表
     */
    List<UserTenant> selectByTenantId(Long tenantId);

    /**
     * 查询用户在指定租户中的角色类型
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 角色类型
     */
    Integer selectRoleTypeByUserAndTenant(@Param("userId") Long userId, @Param("tenantId") Long tenantId);

    /**
     * 查询用户是否属于指定租户
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 是否存在关联
     */
    boolean existsByUserAndTenant(@Param("userId") Long userId, @Param("tenantId") Long tenantId);
    
    /**
     * 根据查询请求条件查询用户-租户关联列表
     * @param request 查询请求对象
     * @return 用户-租户关联列表
     */
    List<UserTenant> getListByRequest(UserTenantQueryRequest request);
}