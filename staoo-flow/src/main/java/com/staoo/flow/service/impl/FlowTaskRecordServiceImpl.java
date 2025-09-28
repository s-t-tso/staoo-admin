package com.staoo.flow.service.impl;

import com.staoo.common.util.UserUtils;
import com.staoo.flow.domain.FlowTaskRecord;
import com.staoo.flow.mapper.FlowTaskRecordMapper;
import com.staoo.flow.service.FlowTaskRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 流程任务记录Service实现类
 * 提供流程任务记录的业务逻辑处理实现
 */
@Service
public class FlowTaskRecordServiceImpl implements FlowTaskRecordService {
    private static final Logger logger = LoggerFactory.getLogger(FlowTaskRecordServiceImpl.class);

    @Autowired
    private FlowTaskRecordMapper flowTaskRecordMapper;

    /**
     * 根据ID查询流程任务记录
     * @param id 流程任务记录ID
     * @return 流程任务记录信息
     */
    @Override
    public FlowTaskRecord getById(Long id) {
        return flowTaskRecordMapper.getById(id);
    }

    /**
     * 根据流程实例ID查询流程任务记录
     * @param processInstanceId 流程实例ID
     * @return 流程任务记录列表
     */
    @Override
    public List<FlowTaskRecord> getListByProcessInstanceId(String processInstanceId) {
        if (processInstanceId == null || processInstanceId.isEmpty()) {
            logger.warn("查询流程实例的任务记录时流程实例ID为空");
            return java.util.Collections.emptyList();
        }
        // 从当前登录用户获取租户ID
        Long tenantId = UserUtils.getCurrentTenantId();
        if (tenantId == null) {
            logger.warn("获取当前租户ID失败，租户ID为空");
            return java.util.Collections.emptyList();
        }
        return flowTaskRecordMapper.getListByProcessInstanceId(processInstanceId, tenantId);
    }



    /**
     * 根据任务ID查询流程任务记录
     * @param taskId 任务ID
     * @return 流程任务记录信息
     */
    @Override
    public FlowTaskRecord getByTaskId(String taskId) {
        if (taskId == null || taskId.isEmpty()) {
            logger.warn("查询任务记录时任务ID为空");
            return null;
        }
        // 从当前登录用户获取租户ID
        Long tenantId = UserUtils.getCurrentTenantId();
        if (tenantId == null) {
            logger.warn("获取当前租户ID失败，租户ID为空");
            return null;
        }
        return flowTaskRecordMapper.getByTaskId(taskId, tenantId);
    }

    /**
     * 查询流程任务记录列表
     * @param flowTaskRecord 查询条件
     * @return 流程任务记录列表
     */
    @Override
    public List<FlowTaskRecord> getList(FlowTaskRecord flowTaskRecord) {
        if (flowTaskRecord == null) {
            flowTaskRecord = new FlowTaskRecord();
        }
        // 从当前登录用户获取租户ID
        Long tenantId = UserUtils.getCurrentTenantId();
        if (tenantId == null) {
            logger.warn("获取当前租户ID失败，租户ID为空");
            return java.util.Collections.emptyList();
        }
        flowTaskRecord.setTenantId(tenantId);
        return flowTaskRecordMapper.getList(flowTaskRecord);
    }

    /**
     * 查询流程任务记录总数
     * @param flowTaskRecord 查询条件
     * @return 流程任务记录总数
     */
    @Override
    public int getCount(FlowTaskRecord flowTaskRecord) {
        if (flowTaskRecord == null) {
            flowTaskRecord = new FlowTaskRecord();
        }
        // 从当前登录用户获取租户ID
        Long tenantId = UserUtils.getCurrentTenantId();
        if (tenantId == null) {
            logger.warn("获取当前租户ID失败，租户ID为空");
            return 0;
        }
        flowTaskRecord.setTenantId(tenantId);
        return flowTaskRecordMapper.getCount(flowTaskRecord);
    }

    /**
     * 新增流程任务记录
     * @param flowTaskRecord 流程任务记录信息
     * @return 是否新增成功
     */
    @Override
    public boolean save(FlowTaskRecord flowTaskRecord) {
        if (flowTaskRecord == null) {
            logger.warn("保存流程任务记录时参数为空");
            return false;
        }
        // 从当前登录用户获取租户ID
        Long tenantId = UserUtils.getCurrentTenantId();
        if (tenantId == null) {
            logger.warn("获取当前租户ID失败，租户ID为空");
            return false;
        }
        flowTaskRecord.setTenantId(tenantId);
        flowTaskRecord.setCreateTime(new Date());
        try {
            int rows = flowTaskRecordMapper.insert(flowTaskRecord);
            logger.info("保存流程任务记录成功，ID: {}", flowTaskRecord.getId());
            return rows > 0;
        } catch (Exception e) {
            logger.error("保存流程任务记录失败: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 更新流程任务记录
     * @param flowTaskRecord 流程任务记录信息
     * @return 是否更新成功
     */
    @Override
    public boolean update(FlowTaskRecord flowTaskRecord) {
        if (flowTaskRecord == null || flowTaskRecord.getId() == null) {
            logger.warn("更新流程任务记录时参数为空或ID为空");
            return false;
        }
        try {
            int rows = flowTaskRecordMapper.update(flowTaskRecord);
            logger.info("更新流程任务记录成功，ID: {}", flowTaskRecord.getId());
            return rows > 0;
        } catch (Exception e) {
            logger.error("更新流程任务记录失败，ID: {}, 错误信息: {}", flowTaskRecord.getId(), e.getMessage(), e);
            return false;
        }
    }

    /**
     * 删除流程任务记录
     * @param id 流程任务记录ID
     * @return 是否删除成功
     */
    @Override
    public boolean deleteById(Long id) {
        if (id == null || id <= 0) {
            logger.warn("删除流程任务记录时ID为空或无效: {}", id);
            return false;
        }
        try {
            int rows = flowTaskRecordMapper.deleteById(id);
            logger.info("删除流程任务记录成功，ID: {}", id);
            return rows > 0;
        } catch (Exception e) {
            logger.error("删除流程任务记录失败，ID: {}, 错误信息: {}", id, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 批量删除流程任务记录
     * @param ids 流程任务记录ID列表
     * @return 是否删除成功
     */
    @Override
    public boolean deleteByIds(Long[] ids) {
        if (ids == null || ids.length == 0) {
            logger.warn("批量删除流程任务记录时ID列表为空");
            return false;
        }
        try {
            int rows = flowTaskRecordMapper.deleteByIds(Arrays.asList(ids));
            logger.info("批量删除流程任务记录成功，删除数量: {}", rows);
            return rows > 0;
        } catch (Exception e) {
            logger.error("批量删除流程任务记录失败，错误信息: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 根据处理人ID查询流程任务记录
     * @param assigneeId 处理人ID
     * @return 流程任务记录列表
     */
    @Override
    public List<FlowTaskRecord> getListByAssigneeId(Long assigneeId) {
        if (assigneeId == null || assigneeId <= 0) {
            logger.warn("查询处理人的任务记录时处理人ID为空或无效: {}", assigneeId);
            return null;
        }
        // 从当前登录用户获取租户ID
        Long tenantId = UserUtils.getCurrentTenantId();
        if (tenantId == null) {
            logger.warn("获取当前租户ID失败，租户ID为空");
            return null;
        }
        return flowTaskRecordMapper.getListByAssigneeId(assigneeId, tenantId);
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
     * @return 是否记录成功
     */
    @Override
    public boolean recordTaskAction(String processInstanceId, String taskId, String taskName, Long assigneeId, String assigneeName, String action, String comment) {
        if (processInstanceId == null || taskId == null || action == null) {
            logger.warn("记录任务处理信息时必要参数为空");
            return false;
        }
        try {
            FlowTaskRecord record = new FlowTaskRecord();
            record.setProcessInstanceId(processInstanceId);
            record.setTaskId(taskId);
            record.setTaskName(taskName);
            record.setAssigneeId(assigneeId);
            record.setAssigneeName(assigneeName);
            record.setAction(action);
            record.setComment(comment);
            // 从当前登录用户获取租户ID
            Long tenantId = UserUtils.getCurrentTenantId();
            if (tenantId == null) {
                logger.warn("获取当前租户ID失败，租户ID为空");
                return false;
            }
            record.setTenantId(tenantId);
            record.setCreateTime(new Date());
            record.setCompleteTime(new Date());
            
            int rows = flowTaskRecordMapper.insert(record);
            logger.info("记录任务处理信息成功，流程实例ID: {}, 任务ID: {}, 动作: {}", processInstanceId, taskId, action);
            return rows > 0;
        } catch (Exception e) {
            logger.error("记录任务处理信息失败，错误信息: {}", e.getMessage(), e);
            return false;
        }
    }
}