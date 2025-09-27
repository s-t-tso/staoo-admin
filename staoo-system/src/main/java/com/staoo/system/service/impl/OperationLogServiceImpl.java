package com.staoo.system.service.impl;

import com.staoo.common.domain.OperationLogBase;
import com.staoo.common.domain.TableResult;
import com.staoo.common.domain.PageQuery;
import com.staoo.common.enums.StatusCodeEnum;
import com.staoo.common.exception.BusinessException;
import com.staoo.system.mapper.OperationLogMapper;
import com.staoo.system.service.SystemOperationLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志服务实现类
 * 实现操作日志相关的业务逻辑
 */
@Service
public class OperationLogServiceImpl implements SystemOperationLogService {
    private static final Logger logger = LoggerFactory.getLogger(OperationLogServiceImpl.class);

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Override
    public OperationLogBase getById(Long id) {
        if (id == null || id <= 0) {
            logger.error("查询操作日志ID无效: {}", id);
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
        }
        try {
            return operationLogMapper.getById(id);
        } catch (Exception e) {
            logger.error("查询操作日志失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    public List<OperationLogBase> getList(OperationLogBase operationLogBase) {
        try {
            return operationLogMapper.getList(operationLogBase);
        } catch (Exception e) {
            logger.error("查询操作日志列表失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    public TableResult<OperationLogBase> getPage(PageQuery query) {
        try {
            // 构建查询条件
            OperationLogBase operationLogBase = new OperationLogBase();
            // 如果有搜索关键词，可以设置到查询条件中
            if (StringUtils.hasText(query.getKeyword())) {
                // 这里可以根据业务需求设置不同的搜索字段
                operationLogBase.setModule(query.getKeyword());
                operationLogBase.setContent(query.getKeyword());
                operationLogBase.setUsername(query.getKeyword());
            }

            // 查询总数
            int total = operationLogMapper.getCount(operationLogBase);
            if (total == 0) {
                return TableResult.empty();
            }

            // 计算分页参数
            Integer startIndex = query.getStartIndex();
            Integer pageSize = query.getPageSize();

            // 查询列表
            List<OperationLogBase> list = operationLogMapper.getPageList(operationLogBase, startIndex, pageSize);
            return TableResult.build((long) total, query.getPageNum(), pageSize, list);
        } catch (Exception e) {
            logger.error("分页查询操作日志失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(OperationLogBase operationLogBase) {
        try {
            if (operationLogBase == null) {
                logger.error("操作日志不能为空");
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }

            // 设置操作时间
            LocalDateTime now = LocalDateTime.now();
            if (operationLogBase.getCreateTime() == null) {
                operationLogBase.setCreateTime(now);
            }
            if (operationLogBase.getUpdateTime() == null) {
                operationLogBase.setUpdateTime(now);
            }

            int result = operationLogMapper.insert(operationLogBase);
            return result > 0;
        } catch (BusinessException e) {
            logger.error("保存操作日志失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("保存操作日志失败", e);
            throw new BusinessException(StatusCodeEnum.DATABASE_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatch(List<OperationLogBase> operationLogBases) {
        try {
            if (operationLogBases == null || operationLogBases.isEmpty()) {
                logger.error("操作日志列表不能为空");
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }

            // 设置操作时间
            LocalDateTime now = LocalDateTime.now();
            for (OperationLogBase operationLogBase : operationLogBases) {
                if (operationLogBase.getCreateTime() == null) {
                    operationLogBase.setCreateTime(now);
                }
                if (operationLogBase.getUpdateTime() == null) {
                    operationLogBase.setUpdateTime(now);
                }
            }

            int result = operationLogMapper.insertBatch(operationLogBases);
            return result > 0;
        } catch (BusinessException e) {
            logger.error("批量保存操作日志失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("批量保存操作日志失败", e);
            throw new BusinessException(StatusCodeEnum.DATABASE_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Long id) {
        try {
            if (id == null || id <= 0) {
                logger.error("删除操作日志ID无效: {}", id);
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }
            int result = operationLogMapper.deleteById(id);
            return result > 0;
        } catch (BusinessException e) {
            logger.error("删除操作日志失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("删除操作日志失败", e);
            throw new BusinessException(StatusCodeEnum.DATABASE_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByIds(List<Long> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                logger.error("删除操作日志ID列表不能为空");
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }
            int result = operationLogMapper.deleteByIds(ids);
            return result > 0;
        } catch (BusinessException e) {
            logger.error("批量删除操作日志失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("批量删除操作日志失败", e);
            throw new BusinessException(StatusCodeEnum.DATABASE_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        try {
            if (startTime == null || endTime == null) {
                logger.error("删除操作日志时间范围不能为空");
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }
            if (startTime.isAfter(endTime)) {
                logger.error("开始时间不能晚于结束时间");
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }
            int result = operationLogMapper.deleteByTimeRange(startTime, endTime);
            return result >= 0;
        } catch (BusinessException e) {
            logger.error("根据时间范围删除操作日志失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("根据时间范围删除操作日志失败", e);
            throw new BusinessException(StatusCodeEnum.DATABASE_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean clearAll() {
        try {
            // 这里可以根据业务需求添加清除前的验证逻辑
            int result = operationLogMapper.deleteByTimeRange(LocalDateTime.MIN, LocalDateTime.now());
            return result >= 0;
        } catch (Exception e) {
            logger.error("清空操作日志失败", e);
            throw new BusinessException(StatusCodeEnum.DATABASE_ERROR);
        }
    }

    @Override
    public List<OperationLogBase> getByUserId(Long userId) {
        try {
            if (userId == null || userId <= 0) {
                logger.error("查询操作人ID无效: {}", userId);
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }
            return operationLogMapper.getByUserId(userId);
        } catch (BusinessException e) {
            logger.error("根据操作人ID查询操作日志失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("根据操作人ID查询操作日志失败", e);
            throw new BusinessException(StatusCodeEnum.DATABASE_ERROR);
        }
    }

    @Override
    public List<OperationLogBase> getByModule(String module) {
        try {
            if (!StringUtils.hasText(module)) {
                logger.error("模块名称不能为空");
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }
            return operationLogMapper.getByModule(module);
        } catch (BusinessException e) {
            logger.error("根据模块查询操作日志失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("根据模块查询操作日志失败", e);
            throw new BusinessException(StatusCodeEnum.DATABASE_ERROR);
        }
    }

    @Override
    public List<OperationLogBase> getByOperationType(String operationType) {
        try {
            if (!StringUtils.hasText(operationType)) {
                logger.error("操作类型不能为空");
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }
            return operationLogMapper.getByOperationType(operationType);
        } catch (BusinessException e) {
            logger.error("根据操作类型查询操作日志失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("根据操作类型查询操作日志失败", e);
            throw new BusinessException(StatusCodeEnum.DATABASE_ERROR);
        }
    }

    @Override
    public List<OperationLogBase> getByIp(String ip) {
        try {
            if (!StringUtils.hasText(ip)) {
                logger.error("IP地址不能为空");
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }
            return operationLogMapper.getByIp(ip);
        } catch (BusinessException e) {
            logger.error("根据IP查询操作日志失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("根据IP查询操作日志失败", e);
            throw new BusinessException(StatusCodeEnum.DATABASE_ERROR);
        }
    }

    @Override
    public String export(OperationLogBase operationLogBase) {
        try {
            // 导出功能的实现逻辑
            // 1. 查询符合条件的操作日志
            // 2. 生成Excel或CSV文件
            // 3. 返回文件路径
            logger.info("导出操作日志开始");

            // 这里只是一个示例实现，实际应该根据业务需求实现具体的导出逻辑
            List<OperationLogBase> logs = getList(operationLogBase);
            if (logs.isEmpty()) {
                throw new BusinessException(StatusCodeEnum.DATA_NOT_FOUND);
            }

            // 模拟导出文件路径
            String filePath = "/export/operation_logs_" + System.currentTimeMillis() + ".xlsx";
            logger.info("导出操作日志成功，文件路径: {}", filePath);
            return filePath;
        } catch (BusinessException e) {
            logger.error("导出操作日志失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("导出操作日志失败", e);
            throw new BusinessException(StatusCodeEnum.FILE_DOWNLOAD_ERROR);
        }
    }

    @Override
    public List<OperationLogBase> countByModule(LocalDateTime startTime, LocalDateTime endTime) {
        try {
            if (startTime == null || endTime == null) {
                logger.error("统计时间范围不能为空");
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }
            if (startTime.isAfter(endTime)) {
                logger.error("开始时间不能晚于结束时间");
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }
            return operationLogMapper.countByModule(startTime, endTime);
        } catch (BusinessException e) {
            logger.error("统计各模块操作次数失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("统计各模块操作次数失败", e);
            throw new BusinessException(StatusCodeEnum.DATABASE_ERROR);
        }
    }

    @Override
    public List<OperationLogBase> countByOperationType(LocalDateTime startTime, LocalDateTime endTime) {
        try {
            if (startTime == null || endTime == null) {
                logger.error("统计时间范围不能为空");
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }
            if (startTime.isAfter(endTime)) {
                logger.error("开始时间不能晚于结束时间");
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }
            return operationLogMapper.countByOperationType(startTime, endTime);
        } catch (BusinessException e) {
            logger.error("统计各操作类型操作次数失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("统计各操作类型操作次数失败", e);
            throw new BusinessException(StatusCodeEnum.DATABASE_ERROR);
        }
    }

    @Override
    public int countTotalOperations(LocalDateTime startTime, LocalDateTime endTime) {
        try {
            if (startTime == null || endTime == null) {
                logger.error("统计时间范围不能为空");
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }
            if (startTime.isAfter(endTime)) {
                logger.error("开始时间不能晚于结束时间");
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }
            return operationLogMapper.countTotalOperations(startTime, endTime);
        } catch (BusinessException e) {
            logger.error("统计操作总次数失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("统计操作总次数失败", e);
            throw new BusinessException(StatusCodeEnum.DATABASE_ERROR);
        }
    }

    @Override
    public List<OperationLogBase> getOperationTrend(Integer days) {
        try {
            if (days == null || days <= 0) {
                logger.error("查询天数无效: {}", days);
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR);
            }
            return operationLogMapper.getOperationTrend(days);
        } catch (BusinessException e) {
            logger.error("查询操作趋势失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("查询操作趋势失败", e);
            throw new BusinessException(StatusCodeEnum.DATABASE_ERROR);
        }
    }
}
