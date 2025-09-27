package com.staoo.system.mapper;

import com.staoo.system.domain.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色Mapper接口
 * 提供角色相关的数据库操作方法
 */
@Mapper
public interface RoleMapper {
    /**
     * 根据ID查询角色
     * @param id 角色ID
     * @return 角色信息
     */
    Role getById(@Param("id") Long id);

    /**
     * 根据角色名称查询角色
     * @param roleName 角色名称
     * @return 角色信息
     */
    Role getByRoleName(@Param("roleName") String roleName);

    /**
     * 查询角色列表
     * @param role 查询条件
     * @return 角色列表
     */
    List<Role> getList(Role role);

    /**
     * 新增角色
     * @param role 角色信息
     * @return 影响行数
     */
    int insert(Role role);

    /**
     * 更新角色
     * @param role 角色信息
     * @return 影响行数
     */
    int update(Role role);

    /**
     * 删除角色
     * @param id 角色ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 批量删除角色
     * @param ids 角色ID列表
     * @return 影响行数
     */
    int deleteByIds(@Param("ids") List<Long> ids);

    /**
     * 批量更新角色状态
     * @param ids 角色ID列表
     * @param status 状态
     * @return 影响行数
     */
    int updateStatusByIds(@Param("ids") List<Long> ids, @Param("status") Integer status);

    /**
     * 查询角色总数
     * @param role 查询条件
     * @return 角色总数
     */
    int getCount(Role role);

    /**
     * 根据角色ID查询菜单ID列表
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    List<Long> getMenuIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 保存角色菜单关系
     * @param roleId 角色ID
     * @param menuIds 菜单ID列表
     * @return 影响行数
     */
    int saveRoleMenus(@Param("roleId") Long roleId, @Param("menuIds") List<Long> menuIds);

    /**
     * 删除角色菜单关系
     * @param roleId 角色ID
     * @return 影响行数
     */
    int deleteRoleMenus(@Param("roleId") Long roleId);

    /**
     * 根据用户ID查询角色列表
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Role> getRolesByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询权限标识列表
     * @param userId 用户ID
     * @return 权限标识列表
     */
    List<String> getPermissionsByUserId(@Param("userId") Long userId);
}