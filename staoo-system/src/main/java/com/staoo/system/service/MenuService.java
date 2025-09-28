package com.staoo.system.service;

import com.staoo.common.domain.TableResult;
import com.staoo.system.domain.Menu;
import com.staoo.system.pojo.request.MenuQueryRequest;

import java.util.List;
import java.util.Set;

/**
 * 菜单服务接口
 * 定义菜单相关的业务操作方法
 */
public interface MenuService {
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
     * 根据请求参数查询菜单列表
     * @param request 查询请求参数
     * @return 菜单列表
     */
    List<Menu> getList(MenuQueryRequest request);

    /**
     * 分页查询菜单
     * @param request 查询条件和分页参数
     * @return 菜单分页结果
     */
    TableResult<Menu> getPage(MenuQueryRequest request);

    /**
     * 新增菜单
     * @param menu 菜单信息
     * @return 操作结果
     */
    boolean save(Menu menu);

    /**
     * 更新菜单
     * @param menu 菜单信息
     * @return 操作结果
     */
    boolean update(Menu menu);

    /**
     * 根据ID删除菜单
     * @param id 菜单ID
     * @return 操作结果
     */
    boolean deleteById(Long id);



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
     * 检查菜单名称唯一性
     * @param menuName 菜单名称
     * @param parentId 父菜单ID
     * @param id 菜单ID（用于排除自身）
     * @return 检查结果
     */
    boolean checkMenuNameUnique(String menuName, Long parentId, Long id);

    /**
     * 检查菜单权限唯一性
     * @param perms 菜单权限
     * @param id 菜单ID（用于排除自身）
     * @return 检查结果
     */
    boolean checkMenuPermsUnique(String perms, Long id);

    /**
     * 检查菜单路由唯一性
     * @param path 菜单路由
     * @param id 菜单ID（用于排除自身）
     * @return 检查结果
     */
    boolean checkMenuPathUnique(String path, Long id);

    /**
     * 检查菜单组件唯一性
     * @param component 菜单组件
     * @param id 菜单ID（用于排除自身）
     * @return 检查结果
     */
    boolean checkMenuComponentUnique(String component, Long id);
}
