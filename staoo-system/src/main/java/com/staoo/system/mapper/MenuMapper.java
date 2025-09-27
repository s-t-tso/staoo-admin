package com.staoo.system.mapper;

import com.staoo.system.domain.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 菜单Mapper接口
 * 提供菜单相关的数据访问操作
 */
public interface MenuMapper {
    /**
     * 根据ID查询菜单
     * @param id 菜单ID
     * @return 菜单信息
     */
    Menu getById(Long id);

    /**
     * 查询菜单列表
     * @param menu 查询条件
     * @return 菜单列表
     */
    List<Menu> getList(Menu menu);

    /**
     * 查询菜单总数
     * @param menu 查询条件
     * @return 菜单总数
     */
    int getCount(Menu menu);

    /**
     * 新增菜单
     * @param menu 菜单信息
     * @return 影响行数
     */
    int insert(Menu menu);

    /**
     * 更新菜单
     * @param menu 菜单信息
     * @return 影响行数
     */
    int update(Menu menu);

    /**
     * 根据ID删除菜单
     * @param id 菜单ID
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 批量删除菜单
     * @param ids 菜单ID列表
     * @return 影响行数
     */
    int deleteByIds(List<Long> ids);

    /**
     * 批量更新菜单状态
     * @param ids 菜单ID列表
     * @param status 状态
     * @return 影响行数
     */
    int updateStatusByIds(@Param("ids") List<Long> ids, @Param("status") Integer status);

    /**
     * 根据角色ID查询菜单列表
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<Menu> getMenusByRoleId(Long roleId);

    /**
     * 根据用户ID查询菜单列表
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<Menu> getMenusByUserId(Long userId);

    /**
     * 根据用户ID查询权限标识列表
     * @param userId 用户ID
     * @return 权限标识列表
     */
    Set<String> getPermissionsByUserId(Long userId);

    /**
     * 查询所有菜单权限标识
     * @return 权限标识列表
     */
    Set<String> getAllPermissions();

    /**
     * 查询菜单树结构
     * @param menu 查询条件
     * @return 菜单树结构
     */
    List<Menu> getMenuTree(Menu menu);

    /**
     * 查询角色菜单树结构
     * @param roleId 角色ID
     * @return 菜单树结构
     */
    List<Menu> getRoleMenuTree(Long roleId);

    /**
     * 查询用户菜单树结构
     * @param userId 用户ID
     * @return 菜单树结构
     */
    List<Menu> getUserMenuTree(Long userId);

    /**
     * 根据父ID查询菜单数量
     * @param parentId 父菜单ID
     * @return 菜单数量
     */
    int getCountByParentId(Long parentId);

    /**
     * 检查菜单名称唯一性
     * @param menuName 菜单名称
     * @param parentId 父菜单ID
     * @param id 菜单ID（用于排除自身）
     * @return 菜单信息
     */
    Menu checkMenuNameUnique(@Param("menuName") String menuName, @Param("parentId") Long parentId, @Param("id") Long id);

    /**
     * 检查菜单权限唯一性
     * @param perms 菜单权限
     * @param id 菜单ID（用于排除自身）
     * @return 菜单信息
     */
    Menu checkMenuPermsUnique(@Param("perms") String perms, @Param("id") Long id);

    /**
     * 检查菜单路由唯一性
     * @param path 菜单路由
     * @param id 菜单ID（用于排除自身）
     * @return 菜单信息
     */
    Menu checkMenuPathUnique(@Param("path") String path, @Param("id") Long id);

    /**
     * 检查菜单组件唯一性
     * @param component 菜单组件
     * @param id 菜单ID（用于排除自身）
     * @return 菜单信息
     */
    Menu checkMenuComponentUnique(@Param("component") String component, @Param("id") Long id);
}