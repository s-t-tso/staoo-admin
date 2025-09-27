package com.staoo.flow.service;

import com.staoo.flow.domain.FlowTaskRecord;

import java.util.List;

/**
 * 流程任务记录Service接口
 * 提供流程任务记录的业务逻辑处理方法
 */
public interface FlowTaskRecordService {
    /**
     * 根据ID查询流程任务记录
     * @param id 流程任务记录ID
     * @return 流程任务记录信息
     */
    FlowTaskRecord getById(Long id);

    /**
     * 根据流程实例ID查询流程任务记录
     * @param processInstanceId 流程实例ID
     * @return 流程任务记录列表
     */
    List<FlowTaskRecord> getListByProcessInstanceId(String processInstanceId);

    /**
     * 根据任务ID查询流程任务记录
     * @param taskId 任务ID
     * @return 流程任务记录信息
     */
    FlowTaskRecord getByTaskId(String taskId);

    /**
     * 查询流程任务记录列表
     * @param flowTaskRecord 查询条件
     * @return 流程任务记录列表
     */
    List<FlowTaskRecord> getList(FlowTaskRecord flowTaskRecord);

    /**
     * 查询流程任务记录总数
     * @param flowTaskRecord 查询条件
     * @return 流程任务记录总数
     */
    int getCount(FlowTaskRecord flowTaskRecord);

    /**
     * 新增流程任务记录
     * @param flowTaskRecord 流程任务记录信息
     * @return 是否新增成功
     */
    boolean save(FlowTaskRecord flowTaskRecord);

    /**
     * 更新流程任务记录
     * @param flowTaskRecord 流程任务记录信息
     * @return 是否更新成功
     */
    boolean update(FlowTaskRecord flowTaskRecord);

    /**
     * 删除流程任务记录
     * @param id 流程任务记录ID
     * @return 是否删除成功
     */
    boolean deleteById(Long id);

    /**
     * 批量删除流程任务记录
     * @param ids 流程任务记录ID列表
     * @return 是否删除成功
     */
    boolean deleteByIds(Long[] ids);

    /**
     * 根据处理人ID查询流程任务记录
     * @param assigneeId 处理人ID
     * @return 流程任务记录列表
     */
    List<FlowTaskRecord> getListByAssigneeId(Long assigneeId);

    /**
     * 记录任务处理信息
     * @param processInstanceId 流程实例ID
     * @param taskId 任务ID
     * @param taskName 任务名称
     * @param assigneeId 处理人ID
     * @param assigneeName 处理人姓名
     * @param action 处理动作
     * @param comment 处理意见
     * @return 是否记录成功
     */
    boolean recordTaskAction(String processInstanceId, String taskId, String taskName, Long assigneeId, String assigneeName, String action, String comment);
}