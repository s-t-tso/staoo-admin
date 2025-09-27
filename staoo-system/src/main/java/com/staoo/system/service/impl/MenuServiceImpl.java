package com.staoo.system.service.impl;

import com.staoo.common.domain.TableResult;
import com.staoo.common.exception.BusinessException;
import com.staoo.common.enums.StatusCodeEnum;
import com.staoo.system.domain.Menu;
import com.staoo.common.domain.PageQuery;
import com.staoo.system.mapper.MenuMapper;
import com.staoo.system.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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
        if (id == null || id <= 0) {
            logger.error("查询菜单ID无效: {}", id);
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
        }
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
    public TableResult<Menu> getPage(PageQuery query) {
        try {
            // 构建查询条件
            Menu menu = new Menu();
            // 如果有搜索关键词，可以设置到查询条件中
            if (StringUtils.hasText(query.getKeyword())) {
                menu.setMenuName(query.getKeyword());
                menu.setPath(query.getKeyword());
                menu.setPerms(query.getKeyword());
            }

            // 查询总数
            int total = menuMapper.getCount(menu);
            if (total == 0) {
                return TableResult.empty();
            }

            // 分页参数已由PageQuery自动处理
            // TODO: 这里需要设置分页参数到mybatis的分页插件或查询条件中

            // 查询列表
            List<Menu> list = menuMapper.getList(menu);
            return TableResult.build((long)total, query.getPageNum(), query.getPageSize(), list);
        } catch (Exception e) {
            logger.error("分页查询菜单失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(Menu menu) {
        try {
            // 参数校验
            validateMenu(menu);

            // 检查菜单名称唯一性
            if (checkMenuNameUnique(menu.getMenuName(), menu.getParentId(), null)) {
                logger.error("菜单名称已存在: {}", menu.getMenuName());
                throw new BusinessException(StatusCodeEnum.DATA_EXISTS);
            }

            // 检查菜单权限唯一性
            if (StringUtils.hasText(menu.getPerms()) && checkMenuPermsUnique(menu.getPerms(), null)) {
                logger.error("菜单权限已存在: {}", menu.getPerms());
                throw new BusinessException(StatusCodeEnum.DATA_EXISTS);
            }

            // 检查菜单路由唯一性
            if (StringUtils.hasText(menu.getPath()) && checkMenuPathUnique(menu.getPath(), null)) {
                logger.error("菜单路由已存在: {}", menu.getPath());
                throw new BusinessException(StatusCodeEnum.DATA_EXISTS);
            }

            // 检查菜单组件唯一性
            if (StringUtils.hasText(menu.getComponent()) && checkMenuComponentUnique(menu.getComponent(), null)) {
                logger.error("菜单组件已存在: {}", menu.getComponent());
                throw new BusinessException(StatusCodeEnum.DATA_EXISTS);
            }

            // 设置创建时间和更新时间
            LocalDateTime now = LocalDateTime.now();
            menu.setCreateTime(now);
            menu.setUpdateTime(now);

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
            if (menu == null || menu.getId() == null || menu.getId() <= 0) {
                logger.error("菜单ID无效");
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }

            // 检查菜单是否存在
            Menu existingMenu = menuMapper.getById(menu.getId());
            if (existingMenu == null) {
                logger.error("菜单不存在: {}", menu.getId());
                throw new BusinessException(StatusCodeEnum.MENU_NOT_FOUND);
            }

            // 检查菜单名称唯一性
            if (!existingMenu.getMenuName().equals(menu.getMenuName()) && 
                checkMenuNameUnique(menu.getMenuName(), menu.getParentId(), menu.getId())) {
                logger.error("菜单名称已存在: {}", menu.getMenuName());
                throw new BusinessException(StatusCodeEnum.DATA_EXISTS);
            }

            // 检查菜单权限唯一性
            if (StringUtils.hasText(menu.getPerms()) && 
                !menu.getPerms().equals(existingMenu.getPerms()) && 
                checkMenuPermsUnique(menu.getPerms(), menu.getId())) {
                logger.error("菜单权限已存在: {}", menu.getPerms());
                throw new BusinessException(StatusCodeEnum.DATA_EXISTS);
            }

            // 检查菜单路由唯一性
            if (StringUtils.hasText(menu.getPath()) && 
                !menu.getPath().equals(existingMenu.getPath()) && 
                checkMenuPathUnique(menu.getPath(), menu.getId())) {
                logger.error("菜单路由已存在: {}", menu.getPath());
                throw new BusinessException(StatusCodeEnum.DATA_EXISTS);
            }

            // 检查菜单组件唯一性
            if (StringUtils.hasText(menu.getComponent()) && 
                !menu.getComponent().equals(existingMenu.getComponent()) && 
                checkMenuComponentUnique(menu.getComponent(), menu.getId())) {
                logger.error("菜单组件已存在: {}", menu.getComponent());
                throw new BusinessException(StatusCodeEnum.DATA_EXISTS);
            }

            // 设置更新时间
            menu.setUpdateTime(LocalDateTime.now());

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
            if (id == null || id <= 0) {
                logger.error("菜单ID无效: {}", id);
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }

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

            // TODO: 这里可以添加检查菜单是否被角色引用的逻辑

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
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByIds(List<Long> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                logger.error("菜单ID列表为空");
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }

            // 检查是否有子菜单
            for (Long id : ids) {
                int childCount = menuMapper.getCountByParentId(id);
                if (childCount > 0) {
                    logger.error("菜单下存在子菜单，不能删除: {}", id);
                    throw new BusinessException(StatusCodeEnum.OPERATION_NOT_ALLOWED);
                }
            }

            // TODO: 这里可以添加检查菜单是否被角色引用的逻辑

            // 批量删除菜单
            int result = menuMapper.deleteByIds(ids);
            return result > 0;
        } catch (BusinessException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            logger.error("批量删除菜单失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatusByIds(List<Long> ids, Integer status) {
        try {
            if (ids == null || ids.isEmpty()) {
                logger.error("菜单ID列表为空");
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }
            if (status == null) {
                logger.error("菜单状态为空");
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }

            // 批量更新菜单状态
            int result = menuMapper.updateStatusByIds(ids, status);
            return result > 0;
        } catch (BusinessException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            logger.error("批量更新菜单状态失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    public List<Menu> getMenusByRoleId(Long roleId) {
        try {
            if (roleId == null || roleId <= 0) {
                logger.error("角色ID无效: {}", roleId);
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }

            return menuMapper.getMenusByRoleId(roleId);
        } catch (BusinessException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            logger.error("查询角色菜单列表失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    public List<Menu> getMenusByUserId(Long userId) {
        try {
            if (userId == null || userId <= 0) {
                logger.error("用户ID无效: {}", userId);
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }

            return menuMapper.getMenusByUserId(userId);
        } catch (BusinessException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            logger.error("查询用户菜单列表失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    public Set<String> getPermissionsByUserId(Long userId) {
        try {
            if (userId == null || userId <= 0) {
                logger.error("用户ID无效: {}", userId);
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }

            List<String> permissions = new ArrayList<>();
            Set<String> permsSet = menuMapper.getPermissionsByUserId(userId);
            if (permsSet != null && !permsSet.isEmpty()) {
                permissions.addAll(permsSet);
            }
            return new HashSet<>(permissions);
        } catch (BusinessException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            logger.error("查询用户权限列表失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    public Set<String> getAllPermissions() {
        try {
            return menuMapper.getAllPermissions();
        } catch (Exception e) {
            logger.error("查询所有菜单权限标识失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    public List<Menu> getMenuTree(Menu menu) {
        try {
            List<Menu> menus = menuMapper.getMenuTree(menu);
            return buildMenuTree(menus);
        } catch (Exception e) {
            logger.error("查询菜单树结构失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    public List<Menu> getRoleMenuTree(Long roleId) {
        try {
            if (roleId == null || roleId <= 0) {
                logger.error("角色ID无效: {}", roleId);
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }

            List<Menu> menus = menuMapper.getRoleMenuTree(roleId);
            return buildMenuTree(menus);
        } catch (BusinessException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            logger.error("查询角色菜单树结构失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    public List<Menu> getUserMenuTree(Long userId) {
        try {
            if (userId == null || userId <= 0) {
                logger.error("用户ID无效: {}", userId);
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }

            List<Menu> menus = menuMapper.getUserMenuTree(userId);
            return buildMenuTree(menus);
        } catch (BusinessException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            logger.error("查询用户菜单树结构失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    public List<Menu> buildMenuTree(List<Menu> menus) {
        if (CollectionUtils.isEmpty(menus)) {
            return Collections.emptyList();
        }

        // 将菜单列表转换为树结构
        List<Menu> menuTree = new ArrayList<>();
        // 查找根节点（parentId为0或null）
        List<Menu> rootMenus = menus.stream()
                .filter(menu -> menu.getParentId() == null || menu.getParentId() == 0)
                .collect(Collectors.toList());

        // 为每个根节点构建子树
        for (Menu rootMenu : rootMenus) {
            buildSubMenuTree(rootMenu, menus);
            menuTree.add(rootMenu);
        }

        return menuTree;
    }

    @Override
    public boolean checkMenuNameUnique(String menuName, Long parentId, Long id) {
        if (!StringUtils.hasText(menuName) || parentId == null) {
            return false;
        }

        Menu menu = menuMapper.checkMenuNameUnique(menuName, parentId, id);
        return menu != null;
    }

    @Override
    public boolean checkMenuPermsUnique(String perms, Long id) {
        if (!StringUtils.hasText(perms)) {
            return false;
        }

        Menu menu = menuMapper.checkMenuPermsUnique(perms, id);
        return menu != null;
    }

    @Override
    public boolean checkMenuPathUnique(String path, Long id) {
        if (!StringUtils.hasText(path)) {
            return false;
        }

        Menu menu = menuMapper.checkMenuPathUnique(path, id);
        return menu != null;
    }

    @Override
    public boolean checkMenuComponentUnique(String component, Long id) {
        if (!StringUtils.hasText(component)) {
            return false;
        }

        Menu menu = menuMapper.checkMenuComponentUnique(component, id);
        return menu != null;
    }

    /**
     * 验证菜单信息
     * @param menu 菜单信息
     */
    private void validateMenu(Menu menu) {
        if (menu == null) {
            logger.error("菜单信息为空");
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
        }
        if (!StringUtils.hasText(menu.getMenuName())) {
            logger.error("菜单名称为空");
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
        }
        if (menu.getMenuType() == null) {
            logger.error("菜单类型为空");
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
        }
        if (menu.getParentId() == null) {
            logger.error("父菜单ID为空");
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
        }
        if (menu.getOrderNum() == null || menu.getOrderNum() <= 0) {
            logger.error("菜单排序为空或无效");
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
        }
        // 其他验证逻辑...
    }

    /**
     * 构建子菜单树
     * @param parentMenu 父菜单
     * @param menus 所有菜单列表
     */
    private void buildSubMenuTree(Menu parentMenu, List<Menu> menus) {
        // 查找当前菜单的子菜单
        List<Menu> children = menus.stream()
                .filter(menu -> menu.getParentId() != null && menu.getParentId().equals(parentMenu.getId()))
                .collect(Collectors.toList());

        if (!children.isEmpty()) {
            parentMenu.setChildren(children);
            // 递归构建子菜单的子树
            for (Menu child : children) {
                buildSubMenuTree(child, menus);
            }
        }
    }
}