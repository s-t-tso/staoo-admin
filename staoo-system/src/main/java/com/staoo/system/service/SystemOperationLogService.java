package com.staoo.system.service;

import com.staoo.common.domain.OperationLogBase;
import com.staoo.common.domain.TableResult;
import com.staoo.common.service.OperationLogService;
import com.staoo.system.pojo.request.OperationLogQueryRequest;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志服务接口
 * 定义操作日志相关的业务操作方法
 */
public interface SystemOperationLogService extends OperationLogService {
    /**
     * 根据ID查询操作日志
     * @param id 日志ID
     * @return 操作日志信息
     */
    OperationLogBase getById(Long id);

    /**
     * 查询操作日志列表
     * @param operationLogBase 查询条件
     * @return 操作日志列表
     */
    List<OperationLogBase> getList(OperationLogBase operationLogBase);

    /**
     * 分页查询操作日志
     * @param request 操作日志查询请求
     * @return 分页结果
     */
    TableResult<OperationLogBase> getPage(OperationLogQueryRequest request);

    /**
     * 新增操作日志
     * @param operationLogBase 操作日志信息
     * @return 是否成功
     */
    boolean save(OperationLogBase operationLogBase);

    /**
     * 批量新增操作日志
     * @param operationLogBases 操作日志列表
     * @return 是否成功
     */
    boolean saveBatch(List<OperationLogBase> operationLogBases);

    /**
     * 删除操作日志
     * @param id 日志ID
     * @return 是否成功
     */
    boolean deleteById(Long id);

    /**
     * 批量删除操作日志
     * @param ids 日志ID列表
     * @return 是否成功
     */
    boolean deleteByIds(List<Long> ids);

    /**
     * 根据时间范围删除操作日志
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 是否成功
     */
    boolean deleteByTimeRange(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 清空操作日志
     * @return 是否成功
     */
    boolean clearAll();

    /**
     * 根据操作人ID查询操作日志
     * @param userId 操作人ID
     * @return 操作日志列表
     */
    List<OperationLogBase> getByUserId(Long userId);

    /**
     * 根据模块查询操作日志
     * @param module 模块名称
     * @return 操作日志列表
     */
    List<OperationLogBase> getByModule(String module);

    /**
     * 根据操作类型查询操作日志
     * @param operationType 操作类型
     * @return 操作日志列表
     */
    List<OperationLogBase> getByOperationType(String operationType);

    /**
     * 根据IP查询操作日志
     * @param ip IP地址
     * @return 操作日志列表
     */
    List<OperationLogBase> getByIp(String ip);

    /**
     * 导出操作日志
     * @param operationLogBase 查询条件
     * @return 导出文件路径
     */
    String export(OperationLogBase operationLogBase);

    /**
     * 统计指定时间范围内各模块的操作次数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果列表
     */
    List<OperationLogBase> countByModule(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 统计指定时间范围内各操作类型的操作次数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果列表
     */
    List<OperationLogBase> countByOperationType(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 统计指定时间范围内的操作总次数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 操作总次数
     */
    int countTotalOperations(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查询最近N天的操作趋势
     * @param days 天数
     * @return 操作趋势数据
     */
    List<OperationLogBase> getOperationTrend(Integer days);
}
