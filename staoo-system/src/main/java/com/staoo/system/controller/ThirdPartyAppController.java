package com.staoo.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.staoo.common.domain.TableResult;
import com.staoo.common.domain.AjaxResult;
import com.staoo.system.domain.ThirdPartyApp;
import com.staoo.system.mapstruct.IThirdPartyAppMapper;
import com.staoo.system.pojo.request.ThirdPartyAppRequest;
import com.staoo.system.pojo.request.ThirdPartyAppQueryRequest;
import com.staoo.system.pojo.response.ThirdPartyAppResponse;
import com.staoo.system.service.ThirdPartyAppService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 第三方应用Controller
 */
@RestController
@RequestMapping("/system/third-party-app")
@Tag(name = "第三方应用管理", description = "第三方应用管理接口")
public class ThirdPartyAppController {

    @Autowired
    private ThirdPartyAppService thirdPartyAppService;

    @Autowired
    private IThirdPartyAppMapper thirdPartyAppMapper;

    @Operation(summary = "获取应用详情", description = "根据应用ID获取应用详情")
    @GetMapping("/{id}")
    public AjaxResult<ThirdPartyAppResponse> getById(@PathVariable Long id) {
        ThirdPartyApp app = thirdPartyAppService.getById(id);
        ThirdPartyAppResponse response = thirdPartyAppMapper.toResponse(app);
        return AjaxResult.success(response);
    }

    @Operation(summary = "获取应用列表", description = "根据条件查询应用列表")
    @GetMapping("/list")
    public AjaxResult<List<ThirdPartyAppResponse>> getList(ThirdPartyAppRequest request) {
        ThirdPartyApp app = thirdPartyAppMapper.toEntity(request);
        List<ThirdPartyApp> list = thirdPartyAppService.getList(app);
        List<ThirdPartyAppResponse> responseList = thirdPartyAppMapper.toResponseList(list);
        return AjaxResult.success(responseList);
    }

    @Operation(summary = "分页查询应用", description = "分页查询应用列表")
    @GetMapping("/page")
    public AjaxResult<TableResult<ThirdPartyAppResponse>> getPage(ThirdPartyAppQueryRequest request) {
        TableResult<ThirdPartyApp> tableResult = thirdPartyAppService.getPage(request);

        // 转换TableResult中的实体列表
        List<ThirdPartyAppResponse> responseList = thirdPartyAppMapper.toResponseList(tableResult.getList());
        TableResult<ThirdPartyAppResponse> responseResult = TableResult.build(tableResult.getTotal(), tableResult.getPage(), tableResult.getPagesize(), responseList);

        return AjaxResult.success(responseResult);
    }

    @Operation(summary = "新增应用", description = "创建新的第三方应用")
    @PostMapping
    public AjaxResult<Long> save(@RequestBody @Validated ThirdPartyAppRequest request) {
        ThirdPartyApp app = thirdPartyAppMapper.toEntity(request);
        Long appId = thirdPartyAppService.save(app);
        return AjaxResult.success(appId);
    }

    @Operation(summary = "更新应用", description = "更新第三方应用信息")
    @PutMapping
    public AjaxResult<Integer> update(@RequestBody @Validated ThirdPartyAppRequest request) {
        ThirdPartyApp app = thirdPartyAppService.getById(request.getId());
        thirdPartyAppMapper.updateEntity(request, app);
        int result = thirdPartyAppService.update(app);
        return AjaxResult.success(result);
    }

    @Operation(summary = "删除应用", description = "根据应用ID删除应用")
    @DeleteMapping("/{id}")
    public AjaxResult<Integer> deleteById(@PathVariable Long id) {
        int result = thirdPartyAppService.deleteById(id);
        return AjaxResult.success(result);
    }

    @Operation(summary = "批量删除应用", description = "批量删除第三方应用")
    @DeleteMapping("/batch")
    public AjaxResult<Integer> deleteByIds(@RequestBody List<Long> ids) {
        int result = thirdPartyAppService.deleteByIds(ids);
        return AjaxResult.success(result);
    }

    @Operation(summary = "重置应用密钥", description = "重置第三方应用的密钥")
    @PutMapping("/{id}/reset-secret")
    public AjaxResult<String> resetAppSecret(@PathVariable Long id) {
        String newSecret = thirdPartyAppService.resetAppSecret(id);
        return AjaxResult.success(newSecret);
    }

    @Operation(summary = "启用/禁用应用", description = "启用或禁用第三方应用")
    @PutMapping("/{id}/status")
    public AjaxResult<Integer> changeStatus(@PathVariable Long id, @RequestParam String status) {
        int result = thirdPartyAppService.changeStatus(id, status);
        return AjaxResult.success(result);
    }

    @Operation(summary = "配置应用权限", description = "配置第三方应用的权限列表")
    @PutMapping("/{id}/permissions")
    public AjaxResult<Void> configurePermissions(@PathVariable Long id, @RequestBody List<String> permissions) {
        thirdPartyAppService.configurePermissions(id, permissions);
        return AjaxResult.success();
    }

    @Operation(summary = "配置回调地址", description = "配置第三方应用的回调地址列表")
    @PutMapping("/{id}/callback-urls")
    public AjaxResult<Void> configureCallbackUrls(@PathVariable Long id, @RequestBody List<String> callbackUrls) {
        thirdPartyAppService.configureCallbackUrls(id, callbackUrls);
        return AjaxResult.success();
    }

    @Operation(summary = "配置租户访问权限", description = "配置第三方应用可访问的租户ID列表")
    @PutMapping("/{id}/tenant-access")
    public AjaxResult<Void> configureTenantAccess(@PathVariable Long id, @RequestBody List<Long> tenantIds) {
        thirdPartyAppService.configureTenantAccess(id, tenantIds);
        return AjaxResult.success();
    }

    @Operation(summary = "检查应用权限", description = "检查第三方应用是否有权限访问指定资源")
    @GetMapping("/check-permission")
    public AjaxResult<Boolean> checkPermission(@RequestParam String appKey, @RequestParam String permission) {
        boolean hasPermission = thirdPartyAppService.checkPermission(appKey, permission);
        return AjaxResult.success(hasPermission);
    }

    @Operation(summary = "检查租户访问权限", description = "检查第三方应用是否可以访问指定租户")
    @GetMapping("/check-tenant-access")
    public AjaxResult<Boolean> checkTenantAccess(@RequestParam String appKey, @RequestParam Long tenantId) {
        boolean canAccess = thirdPartyAppService.checkTenantAccess(appKey, tenantId);
        return AjaxResult.success(canAccess);
    }
}
