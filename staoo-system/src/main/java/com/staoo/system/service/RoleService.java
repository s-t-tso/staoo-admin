package com.staoo.system.service;

import com.staoo.common.domain.TableResult;
import com.staoo.common.domain.PageQuery;
import com.staoo.system.domain.Role;

import java.util.List;
import java.util.Set;

/**
 * 角色服务接口
 * 定义角色相关的业务操作方法
 */
public interface RoleService {
    /**
     * 根据ID查询角色
     * @param id 角色ID
     * @return 角色信息
     */
    Role getById(Long id);

    /**
     * 根据角色名称查询角色
     * @param roleName 角色名称
     * @return 角色信息
     */
    Role getByRoleName(String roleName);

    /**
     * 查询角色列表
     * @param role 查询条件
     * @return 角色列表
     */
    List<Role> getList(Role role);

    /**
     * 分页查询角色
     * @param query 分页查询参数
     * @return 分页结果
     */
    TableResult<Role> getPage(PageQuery query);

    /**
     * 新增角色
     * @param role 角色信息
     * @return 是否成功
     */
    boolean save(Role role);

    /**
     * 更新角色
     * @param role 角色信息
     * @return 是否成功
     */
    boolean update(Role role);

    /**
     * 删除角色
     * @param id 角色ID
     * @return 是否成功
     */
    boolean deleteById(Long id);

    /**
     * 批量删除角色
     * @param ids 角色ID列表
     * @return 是否成功
     */
    boolean deleteByIds(List<Long> ids);

    /**
     * 批量更新角色状态
     * @param ids 角色ID列表
     * @param status 状态
     * @return 是否成功
     */
    boolean updateStatusByIds(List<Long> ids, Integer status);

    /**
     * 根据用户ID查询角色列表
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Role> getRolesByUserId(Long userId);

    /**
     * 根据用户ID查询权限标识列表
     * @param userId 用户ID
     * @return 权限标识列表
     */
    Set<String> getPermissionsByUserId(Long userId);

    /**
     * 根据角色ID查询菜单ID列表
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    List<Long> getMenuIdsByRoleId(Long roleId);

    /**
     * 保存角色菜单关系
     * @param roleId 角色ID
     * @param menuIds 菜单ID列表
     * @return 是否成功
     */
    boolean saveRoleMenus(Long roleId, List<Long> menuIds);

    /**
     * 检查角色名称是否唯一
     * @param role 角色信息
     * @return 是否唯一
     */
    boolean checkRoleNameUnique(Role role);

    /**
     * 检查角色权限是否唯一
     * @param role 角色信息
     * @return 是否唯一
     */
    boolean checkRoleKeyUnique(Role role);
}