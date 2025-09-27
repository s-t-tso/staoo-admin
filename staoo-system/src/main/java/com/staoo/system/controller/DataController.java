package com.staoo.system.controller;

import com.staoo.common.annotation.LogOperation;
import com.staoo.common.domain.AjaxResult;
import com.staoo.common.domain.TableResult;
import com.staoo.system.pojo.request.OrganizationDataRequest;
import com.staoo.system.pojo.request.UserListDataRequest;
import com.staoo.system.pojo.response.OrganizationNode;
import com.staoo.system.pojo.response.UserResponse;
import com.staoo.system.service.DataService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 数据接口控制器
 * 提供组织架构和用户数据的查询接口
 */
@RestController
@RequestMapping("/system/data")
@Tag(name = "数据查询", description = "组织架构和用户数据查询接口")
public class DataController {

    @Autowired
    private DataService dataService;

    /**
     * 获取组织架构数据
     * 根据租户ID获取完整的组织架构信息
     * 支持分页和筛选条件
     * @param request 组织架构数据请求参数
     * @return 组织架构数据
     */
    @PreAuthorize("hasAuthority('data:org:list')")
    @LogOperation(module = "数据查询", operationType = "查询", content = "组织架构数据查询")
    @GetMapping("/organization")
    public AjaxResult<TableResult<OrganizationNode>> getOrganizationData(@Valid OrganizationDataRequest request) {
        try {
            // 参数校验
            if (request.getTenantId() == null || request.getTenantId() <= 0) {
                return AjaxResult.error("租户ID不能为空");
            }

            // 调用服务层获取组织架构数据
            TableResult<OrganizationNode> tableResult = dataService.getOrganizationData(request);

            return AjaxResult.success(tableResult);
        } catch (Exception e) {
            return AjaxResult.error("获取组织架构数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户列表数据
     * 根据租户ID获取用户列表信息
     * 支持分页、排序和多条件筛选
     * @param request 用户列表数据请求参数
     * @return 用户列表数据
     */
    @PreAuthorize("hasAuthority('data:user:list')")
    @LogOperation(module = "数据查询", operationType = "查询", content = "用户列表数据查询")
    @GetMapping("/users")
    public AjaxResult<TableResult<UserResponse>> getUserListData(@Valid UserListDataRequest request) {
        try {
            // 参数校验
            if (request.getTenantId() == null || request.getTenantId() <= 0) {
                return AjaxResult.error("租户ID不能为空");
            }

            // 调用服务层获取用户列表数据
            TableResult<UserResponse> tableResult = dataService.getUserListData(request);

            return AjaxResult.success(tableResult);
        } catch (Exception e) {
            return AjaxResult.error("获取用户列表数据失败: " + e.getMessage());
        }
    }
}
