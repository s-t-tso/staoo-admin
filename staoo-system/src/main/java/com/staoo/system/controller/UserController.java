package com.staoo.system.controller;

import com.github.pagehelper.Page;
import com.staoo.common.domain.TableResult;
import com.staoo.common.domain.AjaxResult;
import com.staoo.common.domain.PageQuery;
import com.staoo.system.domain.User;
import com.staoo.system.pojo.request.UserQueryRequest;
import com.staoo.system.service.UserService;
import com.staoo.system.pojo.request.UserRequest;
import com.staoo.system.pojo.request.PasswordResetRequest;
import com.staoo.system.pojo.request.PasswordChangeRequest;
import com.staoo.system.pojo.response.UserResponse;
import com.staoo.system.mapstruct.IUserMapper;
import com.staoo.system.validator.PasswordComplexityValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * 用户Controller
 * 实现用户管理的REST API接口
 */
@RestController
@RequestMapping("/system/user")
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private IUserMapper userMapper;

    @Autowired
    private PasswordComplexityValidator passwordComplexityValidator;

    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 统一响应
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询用户")
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    public AjaxResult<UserResponse> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        UserResponse response = userMapper.toResponse(user);
        return AjaxResult.success(response);
    }

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 统一响应
     */
    @GetMapping("/username/{username}")
    @Operation(summary = "根据用户名查询用户")
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    public AjaxResult<UserResponse> getByUsername(@PathVariable String username) {
        User user = userService.getByUsername(username);
        UserResponse response = userMapper.toResponse(user);
        return AjaxResult.success(response);
    }

    /**
     * 分页查询用户
     * @param query 分页查询参数
     * @return 统一响应
     */
    @GetMapping("/list")
    @Operation(summary = "分页查询用户")
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    public TableResult<UserResponse> getPage(UserQueryRequest query) {
        Page<User> tableResult = userService.getPage(query);
        List<UserResponse> responseList = userMapper.toResponseList(tableResult.getResult());
        return TableResult.build(tableResult.getTotal(), tableResult.getPages(), tableResult.getPageSize(), responseList);
    }

    /**
     * 新增用户
     * @param request 用户请求对象
     * @return 统一响应
     */
    @PostMapping
    @Operation(summary = "新增用户")
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    public AjaxResult<Boolean> save(@Valid @RequestBody UserRequest request) {
        User user = userMapper.toEntity(request);
        boolean result = userService.save(user);
        return AjaxResult.success(result);
    }

    /**
     * 更新用户
     * @param request 用户请求对象
     * @return 统一响应
     */
    @PutMapping
    @Operation(summary = "更新用户")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    public AjaxResult<Boolean> update(@Valid @RequestBody UserRequest request) {
        User user = userMapper.toEntity(request);
        boolean result = userService.update(user);
        return AjaxResult.success(result);
    }

    /**
     * 根据ID删除用户
     * @param id 用户ID
     * @return 统一响应
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "根据ID删除用户")
    @PreAuthorize("@ss.hasPermi('system:user:delete')")
    public AjaxResult<Boolean> deleteById(@PathVariable Long id) {
        boolean result = userService.deleteById(id);
        return AjaxResult.success(result);
    }

    /**
     * 批量删除用户
     * @param ids 用户ID列表
     * @return 统一响应
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除用户")
    @PreAuthorize("@ss.hasPermi('system:user:delete')")
    public AjaxResult<Boolean> deleteByIds(@RequestBody List<Long> ids) {
        boolean result = userService.deleteByIds(ids);
        return AjaxResult.success(result);
    }

    /**
     * 批量更新用户状态
     * @param ids 用户ID列表
     * @param status 状态
     * @return 统一响应
     */
    @PutMapping("/status")
    @Operation(summary = "批量更新用户状态")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    public AjaxResult<Boolean> updateStatus(@RequestParam List<Long> ids, @RequestParam Integer status) {
        boolean result = userService.updateStatusByIds(ids, status);
        return AjaxResult.success(result);
    }

    /**
     * 重置用户密码
     * @param id 用户ID
     * @param request 密码重置请求对象
     * @return 统一响应
     */
    @PutMapping("/{id}/resetPassword")
    @Operation(summary = "重置用户密码")
    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    public AjaxResult<Boolean> resetPassword(@PathVariable Long id, @Valid @RequestBody PasswordResetRequest request) {
        // 使用密码复杂度验证策略进行验证
        if (!passwordComplexityValidator.validate(request.getPassword())) {
            return AjaxResult.error(passwordComplexityValidator.getErrorMessage());
        }

        boolean result = userService.resetPassword(id, request.getPassword());
        return AjaxResult.success(result);
    }

    /**
     * 更新用户头像
     * @param id 用户ID
     * @param avatar 头像URL
     * @return 统一响应
     */
    @PutMapping("/{id}/avatar")
    @Operation(summary = "更新用户头像")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    public AjaxResult<Boolean> updateAvatar(@PathVariable Long id, @RequestBody String avatar) {
        boolean result = userService.updateAvatar(id, avatar);
        return AjaxResult.success(result);
    }

    /**
     * 更新用户个人信息
     * @param user 用户信息
     * @return 统一响应
     */
    @PutMapping("/profile")
    @Operation(summary = "更新用户个人信息")
    public AjaxResult<Boolean> updateProfile(@RequestBody User user) {
        boolean result = userService.updateProfile(user);
        return AjaxResult.success(result);
    }

    /**
     * 修改用户密码
     * @param request 密码修改请求对象
     * @return 统一响应
     */
    @PutMapping("/password")
    @Operation(summary = "修改用户密码")
    public AjaxResult<Boolean> changePassword(@Valid @RequestBody PasswordChangeRequest request) {
        // 使用密码复杂度验证策略进行验证
        if (!passwordComplexityValidator.validate(request.getNewPassword())) {
            return AjaxResult.error(passwordComplexityValidator.getErrorMessage());
        }

        boolean result = userService.changePassword(request.getUserId(), request.getOldPassword(), request.getNewPassword());
        return AjaxResult.success(result);
    }

    /**
     * 根据用户ID查询角色ID列表
     * @param userId 用户ID
     * @return 统一响应
     */
    @GetMapping("/{userId}/roles")
    @Operation(summary = "根据用户ID查询角色ID列表")
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    public AjaxResult<List<Long>> getRoleIdsByUserId(@PathVariable Long userId) {
        List<Long> roleIds = userService.getRoleIdsByUserId(userId);
        return AjaxResult.success(roleIds);
    }

    /**
     * 保存用户角色关系
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 统一响应
     */
    @PutMapping("/{userId}/roles")
    @Operation(summary = "保存用户角色关系")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    public AjaxResult<Boolean> saveUserRoles(@PathVariable Long userId, @RequestBody List<Long> roleIds) {
        boolean result = userService.saveUserRoles(userId, roleIds);
        return AjaxResult.success(result);
    }
}
