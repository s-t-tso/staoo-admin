package com.staoo.system.controller;

import com.staoo.common.domain.TableResult;
import com.staoo.common.domain.PageQuery;
import com.staoo.common.domain.AjaxResult;
import com.staoo.system.domain.Tenant;
import com.staoo.system.mapstruct.ITenantMapper;
import com.staoo.system.pojo.request.TenantRequest;
import com.staoo.system.pojo.response.TenantResponse;
import com.staoo.system.service.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租户控制器
 */
@RestController
@RequestMapping("/system/tenant")
@Tag(name = "租户管理", description = "租户管理相关接口")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @Autowired
    private ITenantMapper tenantMapper;

    /**
     * 根据ID查询租户
     * @param id 租户ID
     * @return 响应结果
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询租户", description = "根据租户ID获取租户详细信息")
    public AjaxResult<TenantResponse> getById(@PathVariable Long id) {
        Tenant tenant = tenantService.getById(id);
        TenantResponse response = tenantMapper.toResponse(tenant);
        return AjaxResult.success(response);
    }

    /**
     * 根据租户名称查询租户
     * @param tenantName 租户名称
     * @return 响应结果
     */
    @GetMapping("/name/{tenantName}")
    @Operation(summary = "根据租户名称查询租户", description = "根据租户名称获取租户信息")
    public AjaxResult<TenantResponse> getByTenantName(@PathVariable String tenantName) {
        Tenant tenant = tenantService.getByTenantName(tenantName);
        TenantResponse response = tenantMapper.toResponse(tenant);
        return AjaxResult.success(response);
    }

    /**
     * 查询租户列表
     * @param request 租户请求对象
     * @return 响应结果
     */
    @GetMapping("/list")
    @Operation(summary = "查询租户列表", description = "根据条件查询租户列表")
    public AjaxResult<List<TenantResponse>> getList(TenantRequest request) {
        Tenant tenant = tenantMapper.toEntity(request);
        List<Tenant> list = tenantService.getList(tenant);
        List<TenantResponse> responseList = tenantMapper.toResponseList(list);
        return AjaxResult.success(responseList);
    }

    /**
     * 分页查询租户
     * @param pageQuery 分页查询条件
     * @return 响应结果
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询租户", description = "分页查询租户列表")
    public AjaxResult<TableResult<TenantResponse>> getPage(PageQuery pageQuery) {
        TableResult<Tenant> tableResult = tenantService.getPage(pageQuery);
        TableResult<TenantResponse> responseResult = TableResult.build(
            tableResult.getTotal(), 
            tableResult.getPage(), 
            tableResult.getPagesize(), 
            tenantMapper.toResponseList(tableResult.getRow())
        );
        return AjaxResult.success(responseResult);
    }

    /**
     * 新增租户
     * @param request 租户请求对象
     * @return 响应结果
     */
    @PostMapping
    @Operation(summary = "新增租户", description = "创建新的租户")
    public AjaxResult<Integer> insert(@RequestBody TenantRequest request) {
        Tenant tenant = tenantMapper.toEntity(request);
        int result = tenantService.insert(tenant);
        return AjaxResult.success(result);
    }

    /**
     * 修改租户
     * @param request 租户请求对象
     * @return 响应结果
     */
    @PutMapping
    @Operation(summary = "修改租户", description = "更新租户信息")
    public AjaxResult<Integer> update(@RequestBody TenantRequest request) {
        Tenant tenant = tenantService.getById(request.getId());
        if (tenant == null) {
            return AjaxResult.error("租户不存在");
        }
        tenantMapper.updateEntity(request, tenant);
        int result = tenantService.update(tenant);
        return AjaxResult.success(result);
    }

    /**
     * 删除租户
     * @param id 租户ID
     * @return 响应结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除租户", description = "删除指定租户")
    public AjaxResult<Integer> delete(@PathVariable Long id) {
        int result = tenantService.delete(id);
        return AjaxResult.success(result);
    }

    /**
     * 批量删除租户
     * @param ids 租户ID集合
     * @return 响应结果
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除租户", description = "批量删除指定租户")
    public AjaxResult<Integer> deleteBatch(@RequestBody Long[] ids) {
        int result = tenantService.deleteBatch(ids);
        return AjaxResult.success(result);
    }

    /**
     * 启用/禁用租户
     * @param id 租户ID
     * @param status 状态（0:禁用，1:启用）
     * @return 响应结果
     */
    @PutMapping("/status/{id}/{status}")
    @Operation(summary = "启用/禁用租户", description = "修改租户状态")
    public AjaxResult<Integer> changeStatus(@PathVariable Long id, @PathVariable Integer status) {
        int result = tenantService.changeStatus(id, status);
        return AjaxResult.success(result);
    }
}
