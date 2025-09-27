package com.staoo.system.controller;

import com.staoo.common.domain.AjaxResult;
import com.staoo.common.domain.TableResult;
import com.github.pagehelper.PageInfo;
import com.staoo.system.domain.Department;
import com.staoo.system.mapstruct.IDepartmentMapper;
import com.staoo.system.pojo.request.DepartmentQueryRequest;
import com.staoo.system.pojo.request.DepartmentRequest;
import com.staoo.system.pojo.response.DepartmentResponse;
import com.staoo.system.service.IDepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 部门控制器
 * 实现部门管理的REST API接口
 */
@RestController
@RequestMapping("/system/department")
@Tag(name = "部门管理", description = "部门管理相关接口")
public class DepartmentController {

    @Autowired
    private IDepartmentService departmentService;

    @Autowired
    private IDepartmentMapper departmentMapper;

    /**
     * 根据ID获取部门信息
     * @param deptId 部门ID
     * @return 响应结果
     */
    @GetMapping("/{deptId}")
    @Operation(summary = "根据ID获取部门信息", description = "根据部门ID获取部门详细信息")
    public AjaxResult<DepartmentResponse> getDepartmentById(@PathVariable Long deptId) {
        Department department = departmentService.getDepartmentById(deptId);
        DepartmentResponse response = departmentMapper.toResponse(department);
        return AjaxResult.success(response);
    }

    /**
     * 获取部门列表
     * @param request 部门查询请求参数
     * @return 响应结果
     */
    @GetMapping("/list")
    @Operation(summary = "获取部门列表", description = "根据条件获取部门列表")
    public AjaxResult<List<DepartmentResponse>> getDepartmentList(DepartmentQueryRequest request) {
        List<Department> departments = departmentService.getList(request);
        List<DepartmentResponse> responses = departmentMapper.toResponseList(departments);
        return AjaxResult.success(responses);
    }

    /**
     * 分页查询部门
     * @param request 部门分页查询参数
     * @return 部门分页结果
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询部门", description = "分页查询部门列表")
    public AjaxResult<TableResult<DepartmentResponse>> getPage(DepartmentQueryRequest request) {
        PageInfo<Department> pageInfo = new PageInfo<>(departmentService.getPage(request));
        List<DepartmentResponse> responses = departmentMapper.toResponseList(pageInfo.getList());
        TableResult<DepartmentResponse> result = TableResult.build(pageInfo.getTotal(), responses);
        return AjaxResult.success(result);
    }

    /**
     * 获取部门树
     * @return 响应结果
     */
    @GetMapping("/tree")
    @Operation(summary = "获取部门树", description = "获取部门的树形结构数据")
    public AjaxResult<List<DepartmentResponse>> getDepartmentTree() {
        List<Department> departments = departmentService.getDepartmentTree();
        List<DepartmentResponse> responses = departmentMapper.toResponseList(departments);
        return AjaxResult.success(responses);
    }

    /**
     * 获取子部门
     * @param deptId 部门ID
     * @return 响应结果
     */
    @GetMapping("/children/{deptId}")
    @Operation(summary = "获取子部门", description = "获取指定部门的所有子部门")
    public AjaxResult<List<DepartmentResponse>> getChildrenDepartments(@PathVariable Long deptId) {
        List<Department> departments = departmentService.getChildrenDepartments(deptId);
        List<DepartmentResponse> responses = departmentMapper.toResponseList(departments);
        return AjaxResult.success(responses);
    }

    /**
     * 创建部门
     * @param request 部门请求对象
     * @return 响应结果
     */
    @PostMapping
    @Operation(summary = "创建部门", description = "创建新的部门")
    @PreAuthorize("@ss.hasPermi('system:department:add')")
    public AjaxResult<Boolean> createDepartment(@Valid @RequestBody DepartmentRequest request) {
        Department department = departmentMapper.toEntity(request);
        boolean result = departmentService.createDepartment(department);
        return AjaxResult.success(result);
    }

    /**
     * 更新部门
     * @param request 部门请求对象
     * @return 响应结果
     */
    @PutMapping
    @Operation(summary = "更新部门", description = "更新部门信息")
    @PreAuthorize("@ss.hasPermi('system:department:edit')")
    public AjaxResult<Boolean> updateDepartment(@Valid @RequestBody DepartmentRequest request) {
        Department department = departmentService.getDepartmentById(request.getId());
        if (department == null) {
            return AjaxResult.error("部门不存在");
        }
        departmentMapper.updateEntity(request, department);
        boolean result = departmentService.updateDepartment(department);
        return AjaxResult.success(result);
    }

    /**
     * 删除部门
     * @param deptId 部门ID
     * @return 响应结果
     */
    @DeleteMapping("/{deptId}")
    @Operation(summary = "删除部门", description = "删除指定部门")
    @PreAuthorize("@ss.hasPermi('system:department:delete')")
    public AjaxResult<Boolean> deleteDepartment(@PathVariable Long deptId) {
        boolean result = departmentService.deleteDepartment(deptId);
        return AjaxResult.success(result);
    }

    /**
     * 批量删除部门
     * @param deptIds 部门ID列表
     * @return 响应结果
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除部门", description = "批量删除指定的多个部门")
    @PreAuthorize("@ss.hasPermi('system:department:delete')")
    public AjaxResult<Boolean> batchDeleteDepartments(@RequestBody List<Long> deptIds) {
        boolean result = departmentService.batchDeleteDepartments(deptIds);
        return AjaxResult.success(result);
    }
}
