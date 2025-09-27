package com.staoo.common.service;

import com.staoo.common.domain.OperationLogBase;

/**
 * 操作日志服务接口
 * 定义在common模块中，避免循环依赖
 */
public interface OperationLogService {

    /**
     * 保存操作日志
     * @param operationLogBase 操作日志
     * @return 是否保存成功
     */
    boolean save(OperationLogBase operationLogBase);
}
