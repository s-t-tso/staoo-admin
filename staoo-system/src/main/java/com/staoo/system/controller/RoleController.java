package com.staoo.system.controller;

import com.staoo.common.domain.TableResult;
import com.staoo.common.domain.AjaxResult;
import com.staoo.common.domain.PageQuery;
import com.staoo.system.domain.Role;
import com.staoo.system.pojo.request.RoleRequest;
import com.staoo.system.pojo.response.RoleResponse;
import com.staoo.system.service.RoleService;
import com.staoo.system.mapstruct.IRoleMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Set;

/**
 * 角色控制器
 * 提供角色管理相关的REST API接口
 */
@RestController
@RequestMapping("/system/role")
@Tag(name = "角色管理", description = "角色管理相关接口")
public class RoleController {

    @Autowired
    private RoleService roleService;
    
    @Autowired
    private IRoleMapper roleMapper;

    /**
     * 根据ID查询角色
     * @param id 角色ID
     * @return 角色信息
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询角色", description = "根据角色ID查询角色详细信息")
    @PreAuthorize("hasAnyAuthority('system:role:query')")
    public AjaxResult<RoleResponse> getById(@PathVariable("id") @Parameter(description = "角色ID") Long id) {
        Role role = roleService.getById(id);
        RoleResponse response = roleMapper.toResponse(role);
        return AjaxResult.success(response);
    }

    /**
     * 查询角色列表
     * @param roleRequest 查询条件
     * @return 角色列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询角色列表", description = "查询角色列表信息")
    @PreAuthorize("hasAnyAuthority('system:role:query')")
    public AjaxResult<List<RoleResponse>> getList(RoleRequest roleRequest) {
        Role role = roleMapper.toEntity(roleRequest);
        List<Role> list = roleService.getList(role);
        List<RoleResponse> responseList = roleMapper.toResponseList(list);
        return AjaxResult.success(responseList);
    }

    /**
     * 分页查询角色
     * @param query 分页查询条件
     * @return 角色分页结果
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询角色", description = "分页查询角色列表信息")
    @PreAuthorize("hasAnyAuthority('system:role:query')")
    public AjaxResult<TableResult<RoleResponse>> getPage(PageQuery query) {
        TableResult<Role> page = roleService.getPage(query);
        List<RoleResponse> responseList = roleMapper.toResponseList(page.getRow());
        TableResult<RoleResponse> responsePage = TableResult.build(page.getTotal(), page.getPage(), page.getPagesize(), responseList);
        return AjaxResult.success(responsePage);
    }

    /**
     * 新增角色
     * @param roleRequest 角色信息
     * @return 操作结果
     */
    @PostMapping
    @Operation(summary = "新增角色", description = "新增角色信息")
    @PreAuthorize("hasAnyAuthority('system:role:add')")
    public AjaxResult<Boolean> save(@RequestBody RoleRequest roleRequest) {
        Role role = roleMapper.toEntity(roleRequest);
        boolean result = roleService.save(role);
        return AjaxResult.success(result);
    }

    /**
     * 更新角色
     * @param roleRequest 角色信息
     * @return 操作结果
     */
    @PutMapping
    @Operation(summary = "更新角色", description = "更新角色信息")
    @PreAuthorize("hasAnyAuthority('system:role:edit')")
    public AjaxResult<Boolean> update(@RequestBody RoleRequest roleRequest) {
        Role role = roleService.getById(roleRequest.getId());
        roleMapper.updateEntity(roleRequest, role);
        boolean result = roleService.update(role);
        return AjaxResult.success(result);
    }

    /**
     * 根据ID删除角色
     * @param id 角色ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "根据ID删除角色", description = "根据角色ID删除角色信息")
    @PreAuthorize("hasAnyAuthority('system:role:delete')")
    public AjaxResult<Boolean> deleteById(@PathVariable("id") @Parameter(description = "角色ID") Long id) {
        boolean result = roleService.deleteById(id);
        return AjaxResult.success(result);
    }

    /**
     * 批量删除角色
     * @param ids 角色ID列表
     * @return 操作结果
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除角色", description = "批量删除角色信息")
    @PreAuthorize("hasAnyAuthority('system:role:delete')")
    public AjaxResult<Boolean> deleteByIds(@RequestBody List<Long> ids) {
        boolean result = roleService.deleteByIds(ids);
        return AjaxResult.success(result);
    }

    /**
     * 批量更新角色状态
     * @param ids 角色ID列表
     * @param status 状态
     * @return 操作结果
     */
    @PutMapping("/status")
    @Operation(summary = "批量更新角色状态", description = "批量更新角色状态")
    @PreAuthorize("hasAnyAuthority('system:role:edit')")
    public AjaxResult<Boolean> updateStatusByIds(@RequestParam("ids") List<Long> ids, 
                                           @RequestParam("status") Integer status) {
        boolean result = roleService.updateStatusByIds(ids, status);
        return AjaxResult.success(result);
    }

    /**
     * 根据用户ID查询角色列表
     * @param userId 用户ID
     * @return 角色列表
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "根据用户ID查询角色列表", description = "根据用户ID查询用户拥有的角色列表")
    @PreAuthorize("hasAnyAuthority('system:user:query')")
    public AjaxResult<List<RoleResponse>> getRolesByUserId(@PathVariable("userId") @Parameter(description = "用户ID") Long userId) {
        List<Role> roles = roleService.getRolesByUserId(userId);
        List<RoleResponse> responseList = roleMapper.toResponseList(roles);
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
        Set<String> permissions = roleService.getPermissionsByUserId(userId);
        return AjaxResult.success(permissions);
    }

    /**
     * 查询角色菜单ID列表
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    @GetMapping("/menu/{roleId}")
    @Operation(summary = "查询角色菜单ID列表", description = "查询角色拥有的菜单ID列表")
    @PreAuthorize("hasAnyAuthority('system:role:query')")
    public AjaxResult<List<Long>> getMenuIdsByRoleId(@PathVariable("roleId") @Parameter(description = "角色ID") Long roleId) {
        List<Long> menuIds = roleService.getMenuIdsByRoleId(roleId);
        return AjaxResult.success(menuIds);
    }

    /**
     * 保存角色菜单关系
     * @param roleId 角色ID
     * @param menuIds 菜单ID列表
     * @return 操作结果
     */
    @PostMapping("/menu")
    @Operation(summary = "保存角色菜单关系", description = "保存角色与菜单的关联关系")
    @PreAuthorize("hasAnyAuthority('system:role:edit')")
    public AjaxResult<Boolean> saveRoleMenus(@RequestParam("roleId") Long roleId, 
                                        @RequestBody List<Long> menuIds) {
        boolean result = roleService.saveRoleMenus(roleId, menuIds);
        return AjaxResult.success(result);
    }

    /**
     * 检查角色名称唯一性
     * @param roleRequest 角色信息
     * @return 检查结果
     */
    @GetMapping("/checkRoleNameUnique")
    @Operation(summary = "检查角色名称唯一性", description = "检查角色名称是否唯一")
    @PreAuthorize("hasAnyAuthority('system:role:add', 'system:role:edit')")
    public AjaxResult<Boolean> checkRoleNameUnique(RoleRequest roleRequest) {
        Role role = roleMapper.toEntity(roleRequest);
        boolean isUnique = !roleService.checkRoleNameUnique(role);
        return AjaxResult.success(isUnique);
    }

    /**
     * 检查角色权限唯一性
     * @param roleRequest 角色信息
     * @return 检查结果
     */
    @GetMapping("/checkRoleKeyUnique")
    @Operation(summary = "检查角色权限唯一性", description = "检查角色权限是否唯一")
    @PreAuthorize("hasAnyAuthority('system:role:add', 'system:role:edit')")
    public AjaxResult<Boolean> checkRoleKeyUnique(RoleRequest roleRequest) {
        Role role = roleMapper.toEntity(roleRequest);
        boolean isUnique = !roleService.checkRoleKeyUnique(role);
        return AjaxResult.success(isUnique);
    }
}
