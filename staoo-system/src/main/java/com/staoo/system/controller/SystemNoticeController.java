package com.staoo.system.controller;

import com.staoo.common.domain.TableResult;
import com.staoo.common.domain.AjaxResult;
import com.staoo.common.domain.PageQuery;
import com.staoo.system.domain.SystemNotice;
import com.staoo.system.service.SystemNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * 系统通知Controller
 * 实现系统通知的REST API接口，支持批量删除功能
 */
@RestController
@RequestMapping("/system/notice")
@Tag(name = "系统通知管理", description = "系统通知相关接口")
public class SystemNoticeController {
    @Autowired
    private SystemNoticeService systemNoticeService;

    /**
     * 根据ID查询系统通知
     * @param id 通知ID
     * @return 统一响应
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询系统通知")
    @PreAuthorize("@ss.hasPermi('system:notice:query')")
    public AjaxResult<SystemNotice> getById(@PathVariable Long id) {
        SystemNotice systemNotice = systemNoticeService.getById(id);
        return AjaxResult.success(systemNotice);
    }

    /**
     * 查询系统通知列表
     * @param systemNotice 查询条件
     * @return 统一响应
     */
    @GetMapping("/list")
    @Operation(summary = "查询系统通知列表")
    @PreAuthorize("@ss.hasPermi('system:notice:query')")
    public AjaxResult<List<SystemNotice>> getList(SystemNotice systemNotice) {
        List<SystemNotice> list = systemNoticeService.getList(systemNotice);
        return AjaxResult.success(list);
    }

    /**
     * 分页查询系统通知
     * @param query 分页查询参数
     * @return 统一响应
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询系统通知")
    @PreAuthorize("@ss.hasPermi('system:notice:query')")
    public AjaxResult<TableResult<SystemNotice>> getPage(PageQuery query) {
        TableResult<SystemNotice> tableResult = systemNoticeService.getPage(query);
        return AjaxResult.success(tableResult);
    }

    /**
     * 新增系统通知
     * @param systemNotice 系统通知对象
     * @return 统一响应
     */
    @PostMapping
    @Operation(summary = "新增系统通知")
    @PreAuthorize("@ss.hasPermi('system:notice:add')")
    public AjaxResult<Boolean> save(@RequestBody SystemNotice systemNotice) {
        boolean result = systemNoticeService.save(systemNotice);
        return AjaxResult.success(result);
    }

    /**
     * 更新系统通知
     * @param systemNotice 系统通知对象
     * @return 统一响应
     */
    @PutMapping
    @Operation(summary = "更新系统通知")
    @PreAuthorize("@ss.hasPermi('system:notice:edit')")
    public AjaxResult<Boolean> update(@RequestBody SystemNotice systemNotice) {
        boolean result = systemNoticeService.update(systemNotice);
        return AjaxResult.success(result);
    }

    /**
     * 根据ID删除系统通知
     * @param id 通知ID
     * @return 统一响应
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "根据ID删除系统通知")
    @PreAuthorize("@ss.hasPermi('system:notice:delete')")
    public AjaxResult<Boolean> deleteById(@PathVariable Long id) {
        boolean result = systemNoticeService.deleteById(id);
        return AjaxResult.success(result);
    }

    /**
     * 批量删除系统通知
     * 支持批量删除功能，根据通知ID列表删除多条通知记录
     * @param ids 通知ID列表
     * @return 统一响应
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除系统通知")
    @PreAuthorize("@ss.hasPermi('system:notice:delete')")
    public AjaxResult<Boolean> deleteByIds(@RequestBody List<Long> ids) {
        boolean result = systemNoticeService.deleteByIds(ids);
        return AjaxResult.success(result);
    }

    /**
     * 批量更新系统通知状态
     * @param ids 通知ID列表
     * @param status 状态
     * @return 统一响应
     */
    @PutMapping("/status")
    @Operation(summary = "批量更新系统通知状态")
    @PreAuthorize("@ss.hasPermi('system:notice:edit')")
    public AjaxResult<Boolean> updateStatus(@RequestParam List<Long> ids, @RequestParam Integer status) {
        boolean result = systemNoticeService.updateStatusByIds(ids, status);
        return AjaxResult.success(result);
    }

    /**
     * 发布通知
     * @param id 通知ID
     * @return 统一响应
     */
    @PutMapping("/{id}/publish")
    @Operation(summary = "发布通知")
    @PreAuthorize("@ss.hasPermi('system:notice:publish')")
    public AjaxResult<Boolean> publish(@PathVariable Long id) {
        boolean result = systemNoticeService.publish(id);
        return AjaxResult.success(result);
    }

    /**
     * 撤回通知
     * @param id 通知ID
     * @return 统一响应
     */
    @PutMapping("/{id}/recall")
    @Operation(summary = "撤回通知")
    @PreAuthorize("@ss.hasPermi('system:notice:recall')")
    public AjaxResult<Boolean> recall(@PathVariable Long id) {
        boolean result = systemNoticeService.recall(id);
        return AjaxResult.success(result);
    }

    /**
     * 标记通知为已读
     * @param id 通知ID
     * @return 统一响应
     */
    @PutMapping("/{id}/read")
    @Operation(summary = "标记通知为已读")
    public AjaxResult<Boolean> markAsRead(@PathVariable Long id) {
        boolean result = systemNoticeService.markAsRead(id);
        return AjaxResult.success(result);
    }

    /**
     * 批量标记通知为已读
     * @param ids 通知ID列表
     * @return 统一响应
     */
    @PutMapping("/read/batch")
    @Operation(summary = "批量标记通知为已读")
    public AjaxResult<Boolean> batchMarkAsRead(@RequestBody List<Long> ids) {
        boolean result = systemNoticeService.batchMarkAsRead(ids);
        return AjaxResult.success(result);
    }
}
