package com.staoo.system.controller;

import com.staoo.common.annotation.LogOperation;
import com.staoo.common.domain.AjaxResult;
import com.staoo.common.domain.TableResult;
import com.staoo.system.domain.DataSubscription;
import com.staoo.system.mapstruct.IDataSubscriptionMapper;
import com.staoo.system.pojo.request.DataSubscriptionRequest;
import com.staoo.system.pojo.request.SubscriptionQueryRequest;
import com.staoo.system.pojo.response.DataSubscriptionResponse;
import com.staoo.system.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订阅管理控制器
 * 提供数据订阅相关的API接口
 */
@RestController
@RequestMapping("/system/subscription")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private IDataSubscriptionMapper dataSubscriptionMapper;

    /**
     * 创建数据订阅
     * @param request 数据订阅请求对象
     * @return 订阅结果
     */
    @PreAuthorize("hasAuthority('subscription:add')")
    @LogOperation(module = "数据订阅", operationType = "创建", content = "创建数据订阅")
    @PostMapping
    public AjaxResult<Long> subscribe(@RequestBody @Validated DataSubscriptionRequest request) {
        try {
            DataSubscription subscription = dataSubscriptionMapper.toEntity(request);
            Long subscriptionId = subscriptionService.subscribe(subscription.getAppKey(), subscription.getDataType(), subscription.getCallbackUrl());
            return AjaxResult.success(subscriptionId);
        } catch (Exception e) {
            return AjaxResult.error("订阅失败: " + e.getMessage());
        }
    }

    /**
     * 取消数据订阅
     * @param request 数据订阅请求对象
     * @return 取消订阅结果
     */
    @PreAuthorize("hasAuthority('subscription:delete')")
    @LogOperation(module = "数据订阅", operationType = "删除", content = "取消数据订阅")
    @DeleteMapping
    public AjaxResult<Boolean> unsubscribe(@RequestBody DataSubscriptionRequest request) {
        try {
            boolean success = subscriptionService.unsubscribe(request.getAppKey(), request.getDataType());
            if (success) {
                return AjaxResult.success(success);
            } else {
                return AjaxResult.error("取消订阅失败: 未找到对应订阅");
            }
        } catch (Exception e) {
            return AjaxResult.error("取消订阅失败: " + e.getMessage());
        }
    }

    /**
     * 获取应用的所有订阅
     * @param appKey 应用标识
     * @return 订阅列表
     */
    @PreAuthorize("hasAuthority('subscription:list')")
    @LogOperation(module = "数据订阅", operationType = "查询", content = "获取应用的所有订阅")
    @GetMapping("/app/{appKey}")
    public AjaxResult<List<DataSubscriptionResponse>> getAppSubscriptions(@PathVariable String appKey) {
        try {
            List<DataSubscription> subscriptions = subscriptionService.getAppSubscriptions(appKey);
            List<DataSubscriptionResponse> responses = dataSubscriptionMapper.toResponseList(subscriptions);
            return AjaxResult.success(responses);
        } catch (Exception e) {
            return AjaxResult.error("获取订阅列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取指定数据类型的所有订阅
     * @param dataType 数据类型
     * @return 订阅列表
     */
    @PreAuthorize("hasAuthority('subscription:list')")
    @LogOperation(module = "数据订阅", operationType = "查询", content = "获取指定数据类型的所有订阅")
    @GetMapping("/dataType/{dataType}")
    public AjaxResult<List<DataSubscriptionResponse>> getSubscriptionsByDataType(@PathVariable String dataType) {
        try {
            List<DataSubscription> subscriptions = subscriptionService.getSubscriptionsByDataType(dataType);
            List<DataSubscriptionResponse> responses = dataSubscriptionMapper.toResponseList(subscriptions);
            return AjaxResult.success(responses);
        } catch (Exception e) {
            return AjaxResult.error("获取订阅列表失败: " + e.getMessage());
        }
    }

    /**
     * 修改订阅状态
     * @param request 数据订阅请求对象
     * @return 修改结果
     */
    @PreAuthorize("hasAuthority('subscription:edit')")
    @LogOperation(module = "数据订阅", operationType = "修改", content = "修改订阅状态")
    @PutMapping("/status")
    public AjaxResult<Boolean> changeSubscriptionStatus(@RequestBody DataSubscriptionRequest request) {
        try {
            boolean success = subscriptionService.changeSubscriptionStatus(request.getId(), request.getStatus());
            if (success) {
                return AjaxResult.success(success);
            } else {
                return AjaxResult.error("状态修改失败");
            }
        } catch (Exception e) {
            return AjaxResult.error("状态修改失败: " + e.getMessage());
        }
    }

    /**
     * 分页查询订阅列表
     * @param request 分页查询参数
     * @return 分页结果
     */
    @PreAuthorize("hasAuthority('subscription:list')")
    @LogOperation(module = "数据订阅", operationType = "查询", content = "分页查询订阅列表")
    @GetMapping("/page")
    public AjaxResult<TableResult<DataSubscriptionResponse>> getPage(SubscriptionQueryRequest request) {
        try {
            TableResult<DataSubscription> tableResult = subscriptionService.getPage(request);
            List<DataSubscriptionResponse> responses = dataSubscriptionMapper.toResponseList(tableResult.getRow());
            TableResult<DataSubscriptionResponse> result = new TableResult<>();
            result.setTotal(tableResult.getTotal());
            result.setPage(tableResult.getPage());
            result.setPagesize(tableResult.getPagesize());
            result.setRow(responses);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("分页查询订阅列表失败: " + e.getMessage());
        }
    }
}
