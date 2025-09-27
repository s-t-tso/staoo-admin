package com.staoo.system.mapper;

import com.staoo.common.domain.OperationLogBase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志Mapper接口
 * 提供操作日志相关的数据库操作方法
 */
@Mapper
public interface OperationLogMapper {
    /**
     * 根据ID查询操作日志
     * @param id 日志ID
     * @return 操作日志信息
     */
    OperationLogBase getById(@Param("id") Long id);

    /**
     * 查询操作日志列表
     * @param operationLogBase 查询条件
     * @return 操作日志列表
     */
    List<OperationLogBase> getList(OperationLogBase operationLogBase);

    /**
     * 分页查询操作日志
     * @param operationLogBase 查询条件
     * @param startIndex 起始位置
     * @param pageSize 每页条数
     * @return 操作日志列表
     */
    List<OperationLogBase> getPageList(@Param("operationLogBase") OperationLogBase operationLogBase,
                                       @Param("startIndex") Integer startIndex,
                                       @Param("pageSize") Integer pageSize);

    /**
     * 查询操作日志总数
     * @param operationLogBase 查询条件
     * @return 操作日志总数
     */
    int getCount(OperationLogBase operationLogBase);

    /**
     * 新增操作日志
     * @param operationLogBase 操作日志信息
     * @return 影响行数
     */
    int insert(OperationLogBase operationLogBase);

    /**
     * 批量新增操作日志
     * @param operationLogBases 操作日志列表
     * @return 影响行数
     */
    int insertBatch(@Param("operationLogBases") List<OperationLogBase> operationLogBases);

    /**
     * 删除操作日志
     * @param id 日志ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 批量删除操作日志
     * @param ids 日志ID列表
     * @return 影响行数
     */
    int deleteByIds(@Param("ids") List<Long> ids);

    /**
     * 根据时间范围删除操作日志
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 影响行数
     */
    int deleteByTimeRange(@Param("startTime") LocalDateTime startTime,
                          @Param("endTime") LocalDateTime endTime);

    /**
     * 根据操作人ID查询操作日志
     * @param userId 操作人ID
     * @return 操作日志列表
     */
    List<OperationLogBase> getByUserId(@Param("userId") Long userId);

    /**
     * 根据模块查询操作日志
     * @param module 模块名称
     * @return 操作日志列表
     */
    List<OperationLogBase> getByModule(@Param("module") String module);

    /**
     * 根据操作类型查询操作日志
     * @param operationType 操作类型
     * @return 操作日志列表
     */
    List<OperationLogBase> getByOperationType(@Param("operationType") String operationType);

    /**
     * 根据IP查询操作日志
     * @param ip IP地址
     * @return 操作日志列表
     */
    List<OperationLogBase> getByIp(@Param("ip") String ip);

    /**
     * 统计指定时间范围内各模块的操作次数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果列表
     */
    List<OperationLogBase> countByModule(@Param("startTime") LocalDateTime startTime,
                                         @Param("endTime") LocalDateTime endTime);

    /**
     * 统计指定时间范围内各操作类型的操作次数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果列表
     */
    List<OperationLogBase> countByOperationType(@Param("startTime") LocalDateTime startTime,
                                                @Param("endTime") LocalDateTime endTime);

    /**
     * 统计指定时间范围内的操作总次数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 操作总次数
     */
    int countTotalOperations(@Param("startTime") LocalDateTime startTime,
                             @Param("endTime") LocalDateTime endTime);

    /**
     * 查询最近N天的操作趋势
     * @param days 天数
     * @return 操作趋势数据
     */
    List<OperationLogBase> getOperationTrend(@Param("days") Integer days);
}
