package com.staoo.system.controller;

import com.staoo.common.domain.OperationLogBase;
import com.staoo.common.domain.TableResult;
import com.staoo.system.pojo.request.OperationLogQueryRequest;
import com.staoo.common.domain.AjaxResult;
import com.staoo.system.service.SystemOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志控制器
 * 提供操作日志相关的RESTful API接口
 */
@RestController
@RequestMapping("/system/operation-log")
@Tag(name = "操作日志", description = "操作日志相关的RESTful API接口")
public class OperationLogController {

    @Autowired
    private SystemOperationLogService operationLogService;

    /**
     * 根据ID查询操作日志
     * @param id 日志ID
     * @return 操作日志信息
     */
    @GetMapping("/{id}")
    public AjaxResult<OperationLogBase> getById(@PathVariable Long id) {
        OperationLogBase operationLogBase = operationLogService.getById(id);
        return AjaxResult.success(operationLogBase);
    }

    /**
     * 查询操作日志列表
     * @param operationLogBase 查询条件
     * @return 操作日志列表
     */
    @GetMapping("/list")
    public AjaxResult<List<OperationLogBase>> getList(OperationLogBase operationLogBase) {
        List<OperationLogBase> list = operationLogService.getList(operationLogBase);
        return AjaxResult.success(list);
    }

    /**
     * 分页查询操作日志
     * @param request 操作日志查询请求
     * @return 分页结果
     */
    @GetMapping("/page")
    public AjaxResult<TableResult<OperationLogBase>> getPage(OperationLogQueryRequest request) {
        TableResult<OperationLogBase> tableResult = operationLogService.getPage(request);
        return AjaxResult.success(tableResult);
    }

    /**
     * 删除操作日志
     * @param id 日志ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> deleteById(@PathVariable Long id) {
        boolean result = operationLogService.deleteById(id);
        return AjaxResult.success(result);
    }

    /**
     * 批量删除操作日志
     * @param ids 日志ID列表
     * @return 操作结果
     */
    @DeleteMapping("/batch")
    public AjaxResult<Boolean> deleteByIds(@RequestBody List<Long> ids) {
        boolean result = operationLogService.deleteByIds(ids);
        return AjaxResult.success(result);
    }

    /**
     * 根据时间范围删除操作日志
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 操作结果
     */
    @DeleteMapping("/time-range")
    public AjaxResult<Boolean> deleteByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        boolean result = operationLogService.deleteByTimeRange(startTime, endTime);
        return AjaxResult.success(result);
    }

    /**
     * 清空操作日志
     * @return 操作结果
     */
    @DeleteMapping("/clear")
    public AjaxResult<Boolean> clearAll() {
        boolean result = operationLogService.clearAll();
        return AjaxResult.success(result);
    }

    /**
     * 根据操作人ID查询操作日志
     * @param userId 操作人ID
     * @return 操作日志列表
     */
    @GetMapping("/user/{userId}")
    public AjaxResult<List<OperationLogBase>> getByUserId(@PathVariable Long userId) {
        List<OperationLogBase> list = operationLogService.getByUserId(userId);
        return AjaxResult.success(list);
    }

    /**
     * 根据模块查询操作日志
     * @param module 模块名称
     * @return 操作日志列表
     */
    @GetMapping("/module/{module}")
    public AjaxResult<List<OperationLogBase>> getByModule(@PathVariable String module) {
        List<OperationLogBase> list = operationLogService.getByModule(module);
        return AjaxResult.success(list);
    }

    /**
     * 根据操作类型查询操作日志
     * @param operationType 操作类型
     * @return 操作日志列表
     */
    @GetMapping("/type/{operationType}")
    public AjaxResult<List<OperationLogBase>> getByOperationType(@PathVariable String operationType) {
        List<OperationLogBase> list = operationLogService.getByOperationType(operationType);
        return AjaxResult.success(list);
    }

    /**
     * 根据IP查询操作日志
     * @param ip IP地址
     * @return 操作日志列表
     */
    @GetMapping("/ip/{ip}")
    public AjaxResult<List<OperationLogBase>> getByIp(@PathVariable String ip) {
        List<OperationLogBase> list = operationLogService.getByIp(ip);
        return AjaxResult.success(list);
    }

    /**
     * 导出操作日志
     * @param operationLogBase 查询条件
     * @return 文件下载链接
     */
    @GetMapping("/export")
    public AjaxResult<String> export(OperationLogBase operationLogBase) {
        String filePath = operationLogService.export(operationLogBase);
        // 在实际项目中，这里应该返回一个可下载的文件链接
        return AjaxResult.success(filePath);
    }

    /**
     * 统计指定时间范围内各模块的操作次数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    @GetMapping("/statistics/module")
    public AjaxResult<List<OperationLogBase>> countByModule(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<OperationLogBase> statistics = operationLogService.countByModule(startTime, endTime);
        return AjaxResult.success(statistics);
    }

    /**
     * 统计指定时间范围内各操作类型的操作次数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    @GetMapping("/statistics/type")
    public AjaxResult<List<OperationLogBase>> countByOperationType(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<OperationLogBase> statistics = operationLogService.countByOperationType(startTime, endTime);
        return AjaxResult.success(statistics);
    }

    /**
     * 统计指定时间范围内的操作总次数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 操作总次数
     */
    @GetMapping("/statistics/total")
    public AjaxResult<Integer> countTotalOperations(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        Integer total = operationLogService.countTotalOperations(startTime, endTime);
        return AjaxResult.success(total);
    }

    /**
     * 查询最近N天的操作趋势
     * @param days 天数
     * @return 操作趋势数据
     */
    @GetMapping("/statistics/trend")
    public ResponseEntity<List<OperationLogBase>> getOperationTrend(@RequestParam Integer days) {
        List<OperationLogBase> trend = operationLogService.getOperationTrend(days);
        return ResponseEntity.ok(trend);
    }
}
