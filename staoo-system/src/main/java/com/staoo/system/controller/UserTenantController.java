package com.staoo.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.staoo.common.domain.AjaxResult;
import com.staoo.common.domain.TableResult;
import com.staoo.system.domain.UserTenant;
import com.staoo.system.mapstruct.IUserTenantMapper;
import com.staoo.system.pojo.request.UserTenantQueryRequest;
import com.staoo.system.pojo.request.UserTenantRequest;
import com.staoo.system.pojo.response.UserTenantResponse;
import com.staoo.system.service.UserTenantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * 用户-租户关联Controller
 * 实现用户与租户关联关系管理的REST API接口
 */
@RestController
@RequestMapping("/system/user-tenant")
@Tag(name = "用户-租户关联管理", description = "用户与租户关联关系相关接口")
public class UserTenantController {
    @Autowired
    private UserTenantService userTenantService;

    @Autowired
    private IUserTenantMapper userTenantMapper;

    /**
     * 新增用户-租户关联
     * @param userTenantRequest 用户-租户关联请求参数
     * @return 统一响应
     */
    @PostMapping
    @Operation(summary = "新增用户-租户关联")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    public AjaxResult<Boolean> addUserTenant(@Valid @RequestBody UserTenantRequest userTenantRequest) {
        UserTenant userTenant = userTenantMapper.toEntity(userTenantRequest);
        boolean result = userTenantService.addUserTenant(userTenant);
        return AjaxResult.success(result);
    }

    /**
     * 批量新增用户-租户关联
     * @param userTenantRequestList 用户-租户关联请求列表
     * @return 统一响应
     */
    @PostMapping("/batch")
    @Operation(summary = "批量新增用户-租户关联")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    public AjaxResult<Boolean> batchAddUserTenant(@Valid @RequestBody List<UserTenantRequest> userTenantRequestList) {
        List<UserTenant> userTenantList = userTenantRequestList.stream()
                .map(userTenantMapper::toEntity)
                .toList();
        boolean result = userTenantService.batchAddUserTenant(userTenantList);
        return AjaxResult.success(result);
    }

    /**
     * 更新用户-租户关联
     * @param userTenantRequest 用户-租户关联请求参数
     * @return 统一响应
     */
    @PutMapping
    @Operation(summary = "更新用户-租户关联")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    public AjaxResult<Boolean> updateUserTenant(@Valid @RequestBody UserTenantRequest userTenantRequest) {
        UserTenant userTenant = userTenantMapper.toEntity(userTenantRequest);
        boolean result = userTenantService.updateUserTenant(userTenant);
        return AjaxResult.success(result);
    }

    /**
     * 更新用户在租户中的角色类型
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @param roleType 角色类型
     * @return 统一响应
     */
    @PutMapping("/{userId}/{tenantId}/role-type")
    @Operation(summary = "更新用户在租户中的角色类型")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    public AjaxResult<Boolean> updateUserTenantRoleType(@PathVariable Long userId,
                                                    @PathVariable Long tenantId,
                                                    @RequestBody Integer roleType) {
        boolean result = userTenantService.updateUserTenantRoleType(userId, tenantId, roleType);
        return AjaxResult.success(result);
    }

    /**
     * 更新用户在租户中的状态
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @param status 状态
     * @return 统一响应
     */
    @PutMapping("/{userId}/{tenantId}/status")
    @Operation(summary = "更新用户在租户中的状态")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    public AjaxResult<Boolean> updateUserTenantStatus(@PathVariable Long userId,
                                                  @PathVariable Long tenantId,
                                                  @RequestBody Integer status) {
        boolean result = userTenantService.updateUserTenantStatus(userId, tenantId, status);
        return AjaxResult.success(result);
    }

    /**
     * 根据ID删除用户-租户关联
     * @param id 主键ID
     * @return 统一响应
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "根据ID删除用户-租户关联")
    @PreAuthorize("@ss.hasPermi('system:user:delete')")
    public AjaxResult<Boolean> deleteUserTenantById(@PathVariable Long id) {
        boolean result = userTenantService.deleteUserTenantById(id);
        return AjaxResult.success(result);
    }

    /**
     * 批量删除用户-租户关联
     * @param ids ID列表
     * @return 统一响应
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除用户-租户关联")
    @PreAuthorize("@ss.hasPermi('system:user:delete')")
    public AjaxResult<Boolean> batchDeleteUserTenant(@RequestBody List<Long> ids) {
        Long[] idArray = ids.toArray(new Long[0]);
        boolean result = userTenantService.batchDeleteUserTenantByIds(idArray);
        return AjaxResult.success(result);
    }

    /**
     * 根据用户ID和租户ID删除关联
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 统一响应
     */
    @DeleteMapping("/{userId}/{tenantId}")
    @Operation(summary = "根据用户ID和租户ID删除关联")
    @PreAuthorize("@ss.hasPermi('system:user:delete')")
    public AjaxResult<Boolean> deleteUserTenantByUserAndTenant(@PathVariable Long userId,
                                                           @PathVariable Long tenantId) {
        boolean result = userTenantService.deleteUserTenantByUserAndTenant(userId, tenantId);
        return AjaxResult.success(result);
    }

    /**
     * 根据ID查询用户-租户关联信息
     * @param id 主键ID
     * @return 统一响应
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询用户-租户关联信息")
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    public AjaxResult<UserTenantResponse> getUserTenantById(@PathVariable Long id) {
        UserTenant userTenant = userTenantService.getUserTenantById(id);
        UserTenantResponse response = userTenantMapper.toResponse(userTenant);
        return AjaxResult.success(response);
    }

    /**
     * 根据用户ID和租户ID查询关联信息
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 统一响应
     */
    @GetMapping("/{userId}/{tenantId}")
    @Operation(summary = "根据用户ID和租户ID查询关联信息")
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    public AjaxResult<UserTenantResponse> getUserTenantByUserAndTenant(@PathVariable Long userId,
                                                          @PathVariable Long tenantId) {
        UserTenant userTenant = userTenantService.getUserTenantByUserAndTenant(userId, tenantId);
        UserTenantResponse response = userTenantMapper.toResponse(userTenant);
        return AjaxResult.success(response);
    }

    /**
     * 根据用户ID查询其所有租户关联
     * @param userId 用户ID
     * @return 统一响应
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "根据用户ID查询其所有租户关联")
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    public AjaxResult<List<UserTenantResponse>> getUserTenantsByUserId(@PathVariable Long userId) {
        List<UserTenant> list = userTenantService.getUserTenantsByUserId(userId);
        List<UserTenantResponse> responseList = userTenantMapper.toResponseList(list);
        return AjaxResult.success(responseList);
    }

    /**
     * 根据租户ID查询其所有用户关联
     * @param tenantId 租户ID
     * @return 统一响应
     */
    @GetMapping("/tenant/{tenantId}")
    @Operation(summary = "根据租户ID查询其所有用户关联")
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    public AjaxResult<List<UserTenantResponse>> getUserTenantsByTenantId(@PathVariable Long tenantId) {
        List<UserTenant> list = userTenantService.getUserTenantsByTenantId(tenantId);
        List<UserTenantResponse> responseList = userTenantMapper.toResponseList(list);
        return AjaxResult.success(responseList);
    }

    /**
     * 检查用户是否属于指定租户
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 统一响应
     */
    @GetMapping("/check/{userId}/{tenantId}")
    @Operation(summary = "检查用户是否属于指定租户")
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    public AjaxResult<Boolean> checkUserBelongsToTenant(@PathVariable Long userId,
                                                    @PathVariable Long tenantId) {
        boolean result = userTenantService.checkUserBelongsToTenant(userId, tenantId);
        return AjaxResult.success(result);
    }

    /**
     * 检查用户在租户中是否具有创建者权限
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 统一响应
     */
    @GetMapping("/check-creator/{userId}/{tenantId}")
    @Operation(summary = "检查用户在租户中是否具有创建者权限")
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    public AjaxResult<Boolean> checkUserIsCreatorInTenant(@PathVariable Long userId,
                                                      @PathVariable Long tenantId) {
        boolean result = userTenantService.checkUserIsCreatorInTenant(userId, tenantId);
        return AjaxResult.success(result);
    }

    /**
     * 检查用户在租户中是否具有管理者权限
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 统一响应
     */
    @GetMapping("/check-manager/{userId}/{tenantId}")
    @Operation(summary = "检查用户在租户中是否具有管理者权限")
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    public AjaxResult<Boolean> checkUserIsManagerInTenant(@PathVariable Long userId,
                                                      @PathVariable Long tenantId) {
        boolean result = userTenantService.checkUserIsManagerInTenant(userId, tenantId);
        return AjaxResult.success(result);
    }

    /**
     * 检查用户在租户中是否具有普通用户权限
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 统一响应
     */
    @GetMapping("/check-normal/{userId}/{tenantId}")
    @Operation(summary = "检查用户在租户中是否具有普通用户权限")
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    public AjaxResult<Boolean> checkUserIsNormalInTenant(@PathVariable Long userId,
                                                     @PathVariable Long tenantId) {
        boolean result = userTenantService.checkUserIsNormalInTenant(userId, tenantId);
        return AjaxResult.success(result);
    }

    /**
     * 查询用户-租户关联列表
     * @param request 查询条件
     * @return 统一响应
     */
    @GetMapping("/list")
    @Operation(summary = "查询用户-租户关联列表")
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    public AjaxResult<List<UserTenantResponse>> getList(UserTenantQueryRequest request) {
        // 使用新的Service方法，基于UserTenantQueryRequest参数查询列表
        List<UserTenant> list = userTenantService.getList(request);
        List<UserTenantResponse> responseList = userTenantMapper.toResponseList(list);
        return AjaxResult.success(responseList);
    }

    /**
     * 分页查询用户-租户关联列表
     * @param request 分页查询请求
     * @return 统一响应
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询用户-租户关联列表")
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    public AjaxResult<TableResult<UserTenantResponse>> getPage(UserTenantQueryRequest request) {
        TableResult<UserTenant> tableResult = userTenantService.getPage(request);
        List<UserTenantResponse> responseList = userTenantMapper.toResponseList(tableResult.getList());
        TableResult<UserTenantResponse> finalResult = TableResult.build(
            tableResult.getTotal(),
            tableResult.getPage(),
            tableResult.getPagesize(),
            responseList
        );
        return AjaxResult.success(finalResult);
    }
}
