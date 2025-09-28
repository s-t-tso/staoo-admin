package com.staoo.system.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.staoo.common.domain.TableResult;
import com.staoo.common.enums.StatusCodeEnum;
import com.staoo.common.exception.BusinessException;
import com.staoo.common.util.TreeUtils;
import com.staoo.common.util.UserUtils;
import com.staoo.system.domain.Menu;
import com.staoo.system.mapper.MenuMapper;
import com.staoo.system.pojo.request.MenuQueryRequest;
import com.staoo.system.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 菜单服务实现类
 * 实现菜单相关的业务逻辑
 */
@Service
public class MenuServiceImpl implements MenuService {
    private static final Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public Menu getById(Long id) {
        Menu menu = menuMapper.getById(id);
        if (menu == null) {
            logger.error("菜单不存在: {}", id);
            throw new BusinessException(StatusCodeEnum.MENU_NOT_FOUND);
        }
        return menu;
    }

    @Override
    public List<Menu> getList(Menu menu) {
        try {
            return menuMapper.getList(menu);
        } catch (Exception e) {
            logger.error("查询菜单列表失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    public List<Menu> getList(MenuQueryRequest request) {
        try {
            return menuMapper.getListByRequest(request);
        } catch (Exception e) {
            logger.error("根据请求参数查询菜单列表失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    public TableResult<Menu> getPage(MenuQueryRequest request) {
        try {
            // 设置分页参数
            PageHelper.startPage(request.getPageNum(), request.getPageSize());

            // 查询列表
            List<Menu> list = getList(request);
            Page<Menu> pageList = (Page<Menu>) list;

            // 构建返回结果
            return TableResult.build(pageList.getTotal(), request.getPageNum(), request.getPageSize(), list);
        } catch (Exception e) {
            logger.error("分页查询菜单失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(Menu menu) {
        try {
            // 执行验证
            validateMenu(menu, null);

            // 保存菜单信息
            int result = menuMapper.insert(menu);
            return result > 0;
        } catch (BusinessException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            logger.error("新增菜单失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Menu menu) {
        try {
            // 检查菜单是否存在
            Menu existingMenu = menuMapper.getById(menu.getId());
            if (existingMenu == null) {
                logger.error("菜单不存在: {}", menu.getId());
                throw new BusinessException(StatusCodeEnum.MENU_NOT_FOUND);
            }

            // 执行验证
            validateMenu(menu, existingMenu);

            // 更新菜单信息
            int result = menuMapper.update(menu);
            return result > 0;
        } catch (BusinessException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            logger.error("更新菜单失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Long id) {
        try {
            // 检查菜单是否存在
            Menu menu = menuMapper.getById(id);
            if (menu == null) {
                logger.error("菜单不存在: {}", id);
                throw new BusinessException(StatusCodeEnum.MENU_NOT_FOUND);
            }

            // 检查是否有子菜单
            int childCount = menuMapper.getCountByParentId(id);
            if (childCount > 0) {
                logger.error("菜单下存在子菜单，不能删除: {}", id);
                throw new BusinessException(StatusCodeEnum.OPERATION_NOT_ALLOWED);
            }

            // 删除菜单
            int result = menuMapper.deleteById(id);
            return result > 0;
        } catch (BusinessException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            logger.error("删除菜单失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    public List<Menu> getMenusByRoleId(Long roleId) {
        try {
            return menuMapper.getMenusByRoleId(roleId);
        } catch (Exception e) {
            logger.error("根据角色ID查询菜单列表失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    public List<Menu> getMenusByUserId(Long userId) {
        try {
            return menuMapper.getMenusByUserId(userId);
        } catch (Exception e) {
            logger.error("根据用户ID查询菜单列表失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    public Set<String> getPermissionsByUserId(Long userId) {
        try {
            // 查询用户拥有的菜单权限
            Set<String> permsSet = menuMapper.getPermissionsByUserId(userId);
            if (CollectionUtils.isEmpty(permsSet)) {
                return Collections.emptySet();
            }
            return permsSet;
        } catch (Exception e) {
            logger.error("根据用户ID查询菜单权限失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    public Set<String> getAllPermissions() {
        try {
            // 查询所有菜单权限
            Set<String> permsSet = menuMapper.getAllPermissions();
            if (CollectionUtils.isEmpty(permsSet)) {
                return Collections.emptySet();
            }
            return permsSet;
        } catch (Exception e) {
            logger.error("查询所有菜单权限失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    public List<Menu> getMenuTree(Menu menu) {
        try {
            // 查询菜单列表
            List<Menu> menus = menuMapper.getList(menu);
            if (CollectionUtils.isEmpty(menus)) {
                return Collections.emptyList();
            }

            // 构建菜单树
            List<Menu> menuTree = buildMenuTree(menus, 0L);
            return menuTree;
        } catch (Exception e) {
            logger.error("构建菜单树失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    public List<Menu> getRoleMenuTree(Long roleId) {
        try {
            return menuMapper.getRoleMenuTree(roleId);
        } catch (Exception e) {
            logger.error("构建角色菜单树失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    public List<Menu> getUserMenuTree(Long userId) {
        // 获取当前登录用户
        Long currentUserId = userId;
        if (currentUserId == null) {
            currentUserId = UserUtils.getCurrentUserId();
        }

        // 超级管理员拥有所有菜单
        if (UserUtils.isSuperAdmin()) {
            Menu menu = new Menu();
            menu.setStatus(1); // 启用状态
            return getMenuTree(menu);
        }

        try {
            // 查询用户拥有的菜单
            List<Menu> userMenus = getMenusByUserId(currentUserId);
            if (CollectionUtils.isEmpty(userMenus)) {
                return Collections.emptyList();
            }

            // 查询所有启用状态的菜单
            Menu queryMenu = new Menu();
            queryMenu.setStatus(1);
            List<Menu> allMenus = menuMapper.getList(queryMenu);
            if (CollectionUtils.isEmpty(allMenus)) {
                return Collections.emptyList();
            }

            // 筛选用户拥有的菜单
            Set<Long> userMenuIds = userMenus.stream().map(Menu::getId).collect(Collectors.toSet());
            List<Menu> menus = allMenus.stream()
                                       .filter(m -> userMenuIds.contains(m.getId()))
                                       .collect(Collectors.toList());

            // 构建菜单树
            List<Menu> menuTree = buildMenuTree(menus, 0L);
            return menuTree;
        } catch (Exception e) {
            logger.error("构建用户菜单树失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    /**
     * 构建菜单树
     *
     * @param menus    菜单列表
     * @param parentId 父菜单ID
     * @return 菜单树列表
     */
    private List<Menu> buildMenuTree(List<Menu> menus, Long parentId) {
        // 直接使用TreeUtils构建菜单树
        return TreeUtils.buildTree(menus, parentId);
    }

    /**
     * 检查菜单名称是否唯一
     *
     * @param menuName 菜单名称
     * @param parentId 父菜单ID
     * @param menuId   菜单ID
     * @return true-存在 false-不存在
     */
    @Override
    public boolean checkMenuNameUnique(String menuName, Long parentId, Long menuId) {
        try {
            Menu menu = menuMapper.checkMenuNameUnique(menuName, parentId, menuId);
            return menu != null;
        } catch (Exception e) {
            logger.error("检查菜单名称唯一性失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    /**
     * 检查菜单权限是否唯一
     *
     * @param perms  菜单权限
     * @param menuId 菜单ID
     * @return true-存在 false-不存在
     */
    @Override
    public boolean checkMenuPermsUnique(String perms, Long menuId) {
        try {
            Menu menu = menuMapper.checkMenuPermsUnique(perms, menuId);
            return menu != null;
        } catch (Exception e) {
            logger.error("检查菜单权限唯一性失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    /**
     * 检查菜单路由是否唯一
     *
     * @param path   菜单路由
     * @param menuId 菜单ID
     * @return true-存在 false-不存在
     */
    @Override
    public boolean checkMenuPathUnique(String path, Long menuId) {
        try {
            Menu menu = menuMapper.checkMenuPathUnique(path, menuId);
            return menu != null;
        } catch (Exception e) {
            logger.error("检查菜单路由唯一性失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    /**
     * 检查菜单组件是否唯一
     *
     * @param component 菜单组件
     * @param menuId    菜单ID
     * @return true-存在 false-不存在
     */
    @Override
    public boolean checkMenuComponentUnique(String component, Long menuId) {
        try {
            Menu menu = menuMapper.checkMenuComponentUnique(component, menuId);
            return menu != null;
        } catch (Exception e) {
            logger.error("检查菜单组件唯一性失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    /**
     * 菜单验证方法
     */
    private void validateMenu(Menu menu, Menu existingMenu) {
        // 菜单名称验证
        if (existingMenu == null || !existingMenu.getMenuName().equals(menu.getMenuName())) {
            if (checkMenuNameUnique(menu.getMenuName(), menu.getParentId(),
                    existingMenu != null ? existingMenu.getId() : null)) {
                logger.error("菜单名称已存在: {}", menu.getMenuName());
                throw new BusinessException(StatusCodeEnum.DATA_EXISTS);
            }
        }

        // 菜单权限验证
        if (StringUtils.hasText(menu.getPerms())) {
            if (existingMenu == null || !menu.getPerms().equals(existingMenu.getPerms())) {
                if (checkMenuPermsUnique(menu.getPerms(),
                        existingMenu != null ? existingMenu.getId() : null)) {
                    logger.error("菜单权限已存在: {}", menu.getPerms());
                    throw new BusinessException(StatusCodeEnum.DATA_EXISTS);
                }
            }
        }

        // 菜单路由验证
        if (StringUtils.hasText(menu.getPath())) {
            if (existingMenu == null || !menu.getPath().equals(existingMenu.getPath())) {
                if (checkMenuPathUnique(menu.getPath(),
                        existingMenu != null ? existingMenu.getId() : null)) {
                    logger.error("菜单路由已存在: {}", menu.getPath());
                    throw new BusinessException(StatusCodeEnum.DATA_EXISTS);
                }
            }
        }

        // 菜单组件验证
        if (StringUtils.hasText(menu.getComponent())) {
            if (existingMenu == null || !menu.getComponent().equals(existingMenu.getComponent())) {
                if (checkMenuComponentUnique(menu.getComponent(),
                        existingMenu != null ? existingMenu.getId() : null)) {
                    logger.error("菜单组件已存在: {}", menu.getComponent());
                    throw new BusinessException(StatusCodeEnum.DATA_EXISTS);
                }
            }
        }
    }
}
