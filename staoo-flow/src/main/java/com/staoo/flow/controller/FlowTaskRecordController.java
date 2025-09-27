package com.staoo.flow.controller;

import com.staoo.common.annotation.LogOperation;
import com.staoo.common.domain.AjaxResult;
import com.staoo.flow.domain.FlowTaskRecord;
import com.staoo.flow.mapstruct.FlowTaskRecordConverter;
import com.staoo.flow.pojo.request.FlowTaskRecordRequest;
import com.staoo.flow.pojo.response.FlowTaskRecordResponse;
import com.staoo.flow.service.FlowTaskRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 流程任务记录控制器
 * 处理流程任务记录相关的HTTP请求
 */
@RestController
@RequestMapping("/api/flow/taskRecord")
@Tag(name = "流程任务记录管理")
public class FlowTaskRecordController {

    @Autowired
    private FlowTaskRecordService flowTaskRecordService;
    
    @Autowired
    private FlowTaskRecordConverter flowTaskRecordConverter;

    /**
     * 根据ID查询流程任务记录
     * @param id 流程任务记录ID
     * @return 流程任务记录信息
     */
    @PreAuthorize("@ss.hasPermi('system:flow:taskRecord:query')")
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询流程任务记录")
    @LogOperation(content = "查询流程任务记录")
    public AjaxResult<FlowTaskRecordResponse> getById(@PathVariable Long id) {
        FlowTaskRecord record = flowTaskRecordService.getById(id);
        if (record == null) {
            return AjaxResult.error("流程任务记录不存在");
        }
        return AjaxResult.success(flowTaskRecordConverter.toResponse(record));
    }

    /**
     * 根据流程实例ID查询流程任务记录列表
     * @param processInstanceId 流程实例ID
     * @return 流程任务记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:flow:taskRecord:query')")
    @GetMapping("/listByProcess/{processInstanceId}")
    @Operation(summary = "根据流程实例ID查询流程任务记录列表")
    @LogOperation(content = "查询流程实例的任务记录")
    public AjaxResult<List<FlowTaskRecordResponse>> getListByProcessInstanceId(@PathVariable String processInstanceId) {
        List<FlowTaskRecord> list = flowTaskRecordService.getListByProcessInstanceId(processInstanceId);
        return AjaxResult.success(flowTaskRecordConverter.toResponseList(list));
    }

    /**
     * 根据任务ID查询流程任务记录
     * @param taskId 任务ID
     * @return 流程任务记录信息
     */
    @PreAuthorize("@ss.hasPermi('system:flow:taskRecord:query')")
    @GetMapping("/byTaskId/{taskId}")
    @Operation(summary = "根据任务ID查询流程任务记录")
    @LogOperation(content = "查询任务记录")
    public AjaxResult<FlowTaskRecordResponse> getByTaskId(@PathVariable String taskId) {
        FlowTaskRecord record = flowTaskRecordService.getByTaskId(taskId);
        if (record == null) {
            return AjaxResult.error("任务记录不存在");
        }
        return AjaxResult.success(flowTaskRecordConverter.toResponse(record));
    }

    /**
     * 查询流程任务记录列表
     * @param request 查询条件
     * @return 流程任务记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:flow:taskRecord:query')")
    @GetMapping("/list")
    @Operation(summary = "查询流程任务记录列表")
    @LogOperation(content = "查询流程任务记录列表")
    public AjaxResult<List<FlowTaskRecordResponse>> getList(FlowTaskRecordRequest request) {
        FlowTaskRecord flowTaskRecord = flowTaskRecordConverter.toEntity(request);
        List<FlowTaskRecord> list = flowTaskRecordService.getList(flowTaskRecord);
        return AjaxResult.success(flowTaskRecordConverter.toResponseList(list));
    }

    /**
     * 新增流程任务记录
     * @param request 流程任务记录请求信息
     * @return 操作结果
     */
    @PreAuthorize("@ss.hasPermi('system:flow:taskRecord:add')")
    @PostMapping
    @Operation(summary = "新增流程任务记录")
    @LogOperation(content = "新增流程任务记录")
    public AjaxResult<String> add(@Validated @RequestBody FlowTaskRecordRequest request) {
        FlowTaskRecord flowTaskRecord = flowTaskRecordConverter.toEntity(request);
        boolean success = flowTaskRecordService.save(flowTaskRecord);
        if (success) {
            return AjaxResult.success("新增流程任务记录成功");
        }
        return AjaxResult.error("新增流程任务记录失败");
    }

    /**
     * 更新流程任务记录
     * @param request 流程任务记录请求信息
     * @return 操作结果
     */
    @PreAuthorize("@ss.hasPermi('system:flow:taskRecord:edit')")
    @PutMapping
    @Operation(summary = "更新流程任务记录")
    @LogOperation(content = "更新流程任务记录")
    public AjaxResult<String> update(@Validated @RequestBody FlowTaskRecordRequest request) {
        FlowTaskRecord flowTaskRecord = flowTaskRecordConverter.toEntity(request);
        boolean success = flowTaskRecordService.update(flowTaskRecord);
        if (success) {
            return AjaxResult.success("更新流程任务记录成功");
        }
        return AjaxResult.error("更新流程任务记录失败");
    }

    /**
     * 删除流程任务记录
     * @param id 流程任务记录ID
     * @return 操作结果
     */
    @PreAuthorize("@ss.hasPermi('system:flow:taskRecord:remove')")
    @DeleteMapping("/{id}")
    @Operation(summary = "删除流程任务记录")
    @LogOperation(content = "删除流程任务记录")
    public AjaxResult<String> deleteById(@PathVariable Long id) {
        boolean success = flowTaskRecordService.deleteById(id);
        if (success) {
            return AjaxResult.success("删除流程任务记录成功");
        }
        return AjaxResult.error("删除流程任务记录失败");
    }

    /**
     * 批量删除流程任务记录
     * @param ids 流程任务记录ID列表
     * @return 操作结果
     */
    @PreAuthorize("@ss.hasPermi('system:flow:taskRecord:remove')")
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除流程任务记录")
    @LogOperation(content = "批量删除流程任务记录")
    public AjaxResult<String> deleteByIds(@RequestBody Long[] ids) {
        boolean success = flowTaskRecordService.deleteByIds(ids);
        if (success) {
            return AjaxResult.success("批量删除流程任务记录成功");
        }
        return AjaxResult.error("批量删除流程任务记录失败");
    }

    /**
     * 根据处理人ID查询流程任务记录列表
     * @param assigneeId 处理人ID
     * @return 流程任务记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:flow:taskRecord:query')")
    @GetMapping("/listByAssignee/{assigneeId}")
    @Operation(summary = "根据处理人ID查询流程任务记录列表")
    @LogOperation(content = "查询处理人的任务记录")
    public AjaxResult<List<FlowTaskRecordResponse>> getListByAssigneeId(@PathVariable Long assigneeId) {
        List<FlowTaskRecord> list = flowTaskRecordService.getListByAssigneeId(assigneeId);
        return AjaxResult.success(flowTaskRecordConverter.toResponseList(list));
    }

    /**
     * 记录任务处理信息
     * @param processInstanceId 流程实例ID
     * @param taskId 任务ID
     * @param taskName 任务名称
     * @param assigneeId 处理人ID
     * @param assigneeName 处理人姓名
     * @param action 处理动作
     * @param comment 处理意见
     * @return 操作结果
     */
    @PreAuthorize("@ss.hasPermi('system:flow:taskRecord:add')")
    @PostMapping("/recordAction")
    @Operation(summary = "记录任务处理信息")
    @LogOperation(content = "记录任务处理信息")
    public AjaxResult<String> recordTaskAction(
            @RequestParam String processInstanceId,
            @RequestParam String taskId,
            @RequestParam(required = false) String taskName,
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(required = false) String assigneeName,
            @RequestParam String action,
            @RequestParam(required = false) String comment) {
        boolean success = flowTaskRecordService.recordTaskAction(processInstanceId, taskId, taskName, assigneeId, assigneeName, action, comment);
        if (success) {
            return AjaxResult.success("记录任务处理信息成功");
        }
        return AjaxResult.error("记录任务处理信息失败");
    }
}