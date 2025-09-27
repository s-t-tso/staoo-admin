package com.staoo.flow.mapper;

import com.staoo.flow.domain.FlowTaskRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 流程任务记录Mapper接口
 * 提供流程任务记录的数据库操作方法
 */
@Mapper
public interface FlowTaskRecordMapper {
    /**
     * 根据ID查询流程任务记录
     * @param id 流程任务记录ID
     * @return 流程任务记录信息
     */
    FlowTaskRecord getById(Long id);

    /**
     * 根据流程实例ID查询流程任务记录
     * @param processInstanceId 流程实例ID
     * @param tenantId 租户ID
     * @return 流程任务记录列表
     */
    List<FlowTaskRecord> getListByProcessInstanceId(@Param("processInstanceId") String processInstanceId, @Param("tenantId") Long tenantId);

    /**
     * 根据任务ID查询流程任务记录
     * @param taskId 任务ID
     * @param tenantId 租户ID
     * @return 流程任务记录信息
     */
    FlowTaskRecord getByTaskId(@Param("taskId") String taskId, @Param("tenantId") Long tenantId);

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
     * @return 影响行数
     */
    int insert(FlowTaskRecord flowTaskRecord);

    /**
     * 更新流程任务记录
     * @param flowTaskRecord 流程任务记录信息
     * @return 影响行数
     */
    int update(FlowTaskRecord flowTaskRecord);

    /**
     * 删除流程任务记录
     * @param id 流程任务记录ID
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 批量删除流程任务记录
     * @param ids 流程任务记录ID列表
     * @return 影响行数
     */
    int deleteByIds(@Param("ids") List<Long> ids);

    /**
     * 根据处理人ID查询流程任务记录
     * @param assigneeId 处理人ID
     * @param tenantId 租户ID
     * @return 流程任务记录列表
     */
    List<FlowTaskRecord> getListByAssigneeId(@Param("assigneeId") Long assigneeId, @Param("tenantId") Long tenantId);
}