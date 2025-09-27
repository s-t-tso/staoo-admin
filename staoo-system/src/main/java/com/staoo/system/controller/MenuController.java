package com.staoo.system.controller;

import com.staoo.common.domain.TableResult;
import com.staoo.common.domain.AjaxResult;
import com.staoo.system.domain.Menu;
import com.staoo.system.service.MenuService;
import com.staoo.system.mapstruct.IMenuMapper;
import com.staoo.system.pojo.request.MenuRequest;
import com.staoo.system.pojo.request.MenuQueryRequest;
import com.staoo.system.pojo.response.MenuResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Set;

/**
 * 菜单控制器
 * 提供菜单管理相关的REST API接口
 */
@RestController
@RequestMapping("/system/menu")
@Tag(name = "菜单管理", description = "菜单管理相关接口")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private IMenuMapper menuMapper;

    /**
     * 根据ID查询菜单
     * @param id 菜单ID
     * @return 菜单信息
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询菜单", description = "根据菜单ID查询菜单详细信息")
    @PreAuthorize("hasAnyAuthority('system:menu:query')")
    public AjaxResult<MenuResponse> getById(@PathVariable("id") @Parameter(description = "菜单ID") Long id) {
        Menu menu = menuService.getById(id);
        MenuResponse response = menuMapper.toResponse(menu);
        return AjaxResult.success(response);
    }

    /**
     * 查询菜单列表
     * @param request 查询条件
     * @return 菜单列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询菜单列表", description = "查询菜单列表信息")
    @PreAuthorize("hasAnyAuthority('system:menu:query')")
    public AjaxResult<List<MenuResponse>> getList(MenuQueryRequest request) {
        List<Menu> list = menuService.getList(request);
        List<MenuResponse> responseList = menuMapper.toResponseList(list);
        return AjaxResult.success(responseList);
    }

    /**
     * 分页查询菜单
     * @param request 分页查询条件
     * @return 菜单分页结果
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询菜单", description = "分页查询菜单列表信息")
    @PreAuthorize("hasAnyAuthority('system:menu:query')")
    public AjaxResult<TableResult<MenuResponse>> getPage(MenuQueryRequest request) {
        TableResult<Menu> page = menuService.getPage(request);
        List<MenuResponse> responseList = menuMapper.toResponseList(page.getRow());
        TableResult<MenuResponse> responsePage = TableResult.build(page.getTotal(), page.getPage(), page.getPagesize(), responseList);
        return AjaxResult.success(responsePage);
    }

    /**
     * 新增菜单
     * @param menuRequest 菜单信息
     * @return 操作结果
     */
    @PostMapping
    @Operation(summary = "新增菜单", description = "新增菜单信息")
    @PreAuthorize("hasAnyAuthority('system:menu:add')")
    public AjaxResult<Boolean> save(@RequestBody MenuRequest menuRequest) {
        Menu menu = menuMapper.toEntity(menuRequest);
        boolean result = menuService.save(menu);
        return AjaxResult.success(result);
    }

    /**
     * 更新菜单
     * @param menuRequest 菜单信息
     * @return 操作结果
     */
    @PutMapping
    @Operation(summary = "更新菜单", description = "更新菜单信息")
    @PreAuthorize("hasAnyAuthority('system:menu:edit')")
    public AjaxResult<Boolean> update(@RequestBody MenuRequest menuRequest) {
        Menu menu = menuService.getById(menuRequest.getId());
        menuMapper.updateEntity(menuRequest, menu);
        boolean result = menuService.update(menu);
        return AjaxResult.success(result);
    }

    /**
     * 根据ID删除菜单
     * @param id 菜单ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "根据ID删除菜单", description = "根据菜单ID删除菜单信息")
    @PreAuthorize("hasAnyAuthority('system:menu:delete')")
    public AjaxResult<Boolean> deleteById(@PathVariable("id") @Parameter(description = "菜单ID") Long id) {
        boolean result = menuService.deleteById(id);
        return AjaxResult.success(result);
    }

    /**
     * 批量删除菜单
     * @param ids 菜单ID列表
     * @return 操作结果
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除菜单", description = "批量删除菜单信息")
    @PreAuthorize("hasAnyAuthority('system:menu:delete')")
    public AjaxResult<Boolean> deleteByIds(@RequestBody List<Long> ids) {
        boolean result = menuService.deleteByIds(ids);
        return AjaxResult.success(result);
    }

    /**
     * 批量更新菜单状态
     * @param ids 菜单ID列表
     * @param status 状态
     * @return 操作结果
     */
    @PutMapping("/status")
    @Operation(summary = "批量更新菜单状态", description = "批量更新菜单状态")
    @PreAuthorize("hasAnyAuthority('system:menu:edit')")
    public AjaxResult<Boolean> updateStatusByIds(@RequestParam("ids") List<Long> ids,
                                           @RequestParam("status") Integer status) {
        boolean result = menuService.updateStatusByIds(ids, status);
        return AjaxResult.success(result);
    }

    /**
     * 根据角色ID查询菜单列表
     * @param roleId 角色ID
     * @return 菜单列表
     */
    @GetMapping("/role/{roleId}")
    @Operation(summary = "根据角色ID查询菜单列表", description = "根据角色ID查询角色拥有的菜单列表")
    @PreAuthorize("hasAnyAuthority('system:role:query')")
    public AjaxResult<List<MenuResponse>> getMenusByRoleId(@PathVariable("roleId") @Parameter(description = "角色ID") Long roleId) {
        List<Menu> menus = menuService.getMenusByRoleId(roleId);
        List<MenuResponse> responseList = menuMapper.toResponseList(menus);
        return AjaxResult.success(responseList);
    }

    /**
     * 根据用户ID查询菜单列表
     * @param userId 用户ID
     * @return 菜单列表
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "根据用户ID查询菜单列表", description = "根据用户ID查询用户拥有的菜单列表")
    @PreAuthorize("hasAnyAuthority('system:user:query')")
    public AjaxResult<List<MenuResponse>> getMenusByUserId(@PathVariable("userId") @Parameter(description = "用户ID") Long userId) {
        List<Menu> menus = menuService.getMenusByUserId(userId);
        List<MenuResponse> responseList = menuMapper.toResponseList(menus);
        return AjaxResult.success(responseList);
    }

    /**
     * 根据用户ID查询权限列表
     * @param userId 用户ID
     * @return 权限列表
     */
    @GetMapping("/permissions/{userId}")
    @Operation(summary = "根据用户ID查询权限列表", description = "根据用户ID查询用户拥有的权限列表")
    @PreAuthorize("hasAnyAuthority('system:user:query')")
    public AjaxResult<Set<String>> getPermissionsByUserId(@PathVariable("userId") @Parameter(description = "用户ID") Long userId) {
        Set<String> permissions = menuService.getPermissionsByUserId(userId);
        return AjaxResult.success(permissions);
    }

    /**
     * 查询所有菜单权限标识
     * @return 权限标识列表
     */
    @GetMapping("/allPermissions")
    @Operation(summary = "查询所有菜单权限标识", description = "查询系统中所有的菜单权限标识")
    @PreAuthorize("hasAnyAuthority('system:menu:query')")
    public AjaxResult<Set<String>> getAllPermissions() {
        Set<String> permissions = menuService.getAllPermissions();
        return AjaxResult.success(permissions);
    }

    /**
     * 查询菜单树结构
     * @param menuRequest 查询条件
     * @return 菜单树结构
     */
    @GetMapping("/tree")
    @Operation(summary = "查询菜单树结构", description = "查询菜单树结构信息")
    @PreAuthorize("hasAnyAuthority('system:menu:query')")
    public AjaxResult<List<MenuResponse>> getMenuTree(MenuRequest menuRequest) {
        Menu menu = menuMapper.toEntity(menuRequest);
        List<Menu> menuTree = menuService.getMenuTree(menu);
        List<MenuResponse> responseList = menuMapper.toResponseList(menuTree);
        return AjaxResult.success(responseList);
    }

    /**
     * 查询角色菜单树结构
     * @param roleId 角色ID
     * @return 菜单树结构
     */
    @GetMapping("/roleTree/{roleId}")
    @Operation(summary = "查询角色菜单树结构", description = "查询角色拥有的菜单树结构信息")
    @PreAuthorize("hasAnyAuthority('system:role:query')")
    public AjaxResult<List<MenuResponse>> getRoleMenuTree(@PathVariable("roleId") @Parameter(description = "角色ID") Long roleId) {
        List<Menu> menuTree = menuService.getRoleMenuTree(roleId);
        List<MenuResponse> responseList = menuMapper.toResponseList(menuTree);
        return AjaxResult.success(responseList);
    }

    /**
     * 查询用户菜单树结构
     * @param userId 用户ID
     * @return 菜单树结构
     */
    @GetMapping("/userTree/{userId}")
    @Operation(summary = "查询用户菜单树结构", description = "查询用户拥有的菜单树结构信息")
    @PreAuthorize("hasAnyAuthority('system:user:query')")
    public AjaxResult<List<MenuResponse>> getUserMenuTree(@PathVariable("userId") @Parameter(description = "用户ID") Long userId) {
        List<Menu> menuTree = menuService.getUserMenuTree(userId);
        List<MenuResponse> responseList = menuMapper.toResponseList(menuTree);
        return AjaxResult.success(responseList);
    }

    /**
     * 检查菜单名称唯一性
     * @param menuName 菜单名称
     * @param parentId 父菜单ID
     * @param id 菜单ID（用于排除自身）
     * @return 检查结果
     */
    @GetMapping("/checkMenuNameUnique")
    @Operation(summary = "检查菜单名称唯一性", description = "检查菜单名称是否唯一")
    @PreAuthorize("hasAnyAuthority('system:menu:add', 'system:menu:edit')")
    public AjaxResult<Boolean> checkMenuNameUnique(@RequestParam("menuName") String menuName,
                                             @RequestParam("parentId") Long parentId,
                                             @RequestParam(value = "id", required = false) Long id) {
        boolean isUnique = !menuService.checkMenuNameUnique(menuName, parentId, id);
        return AjaxResult.success(isUnique);
    }

    /**
     * 检查菜单权限唯一性
     * @param perms 菜单权限
     * @param id 菜单ID（用于排除自身）
     * @return 检查结果
     */
    @GetMapping("/checkMenuPermsUnique")
    @Operation(summary = "检查菜单权限唯一性", description = "检查菜单权限是否唯一")
    @PreAuthorize("hasAnyAuthority('system:menu:add', 'system:menu:edit')")
    public AjaxResult<Boolean> checkMenuPermsUnique(@RequestParam("perms") String perms,
                                              @RequestParam(value = "id", required = false) Long id) {
        boolean isUnique = !menuService.checkMenuPermsUnique(perms, id);
        return AjaxResult.success(isUnique);
    }

    /**
     * 检查菜单路由唯一性
     * @param path 菜单路由
     * @param id 菜单ID（用于排除自身）
     * @return 检查结果
     */
    @GetMapping("/checkMenuPathUnique")
    @Operation(summary = "检查菜单路由唯一性", description = "检查菜单路由是否唯一")
    @PreAuthorize("hasAnyAuthority('system:menu:add', 'system:menu:edit')")
    public AjaxResult<Boolean> checkMenuPathUnique(@RequestParam("path") String path,
                                             @RequestParam(value = "id", required = false) Long id) {
        boolean isUnique = !menuService.checkMenuPathUnique(path, id);
        return AjaxResult.success(isUnique);
    }

    /**
     * 检查菜单组件唯一性
     * @param component 菜单组件
     * @param id 菜单ID（用于排除自身）
     * @return 检查结果
     */
    @GetMapping("/checkMenuComponentUnique")
    @Operation(summary = "检查菜单组件唯一性", description = "检查菜单组件是否唯一")
    @PreAuthorize("hasAnyAuthority('system:menu:add', 'system:menu:edit')")
    public AjaxResult<Boolean> checkMenuComponentUnique(@RequestParam("component") String component,
                                                  @RequestParam(value = "id", required = false) Long id) {
        boolean isUnique = !menuService.checkMenuComponentUnique(component, id);
        return AjaxResult.success(isUnique);
    }
}
