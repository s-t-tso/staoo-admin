package com.staoo.system.service;

import com.staoo.common.domain.TableResult;
import com.staoo.system.domain.LoginLog;
import com.staoo.system.pojo.request.LoginLogQueryRequest;

import java.util.List;

/**
 * 登录日志Service接口
 */
public interface LoginLogService {
    /**
     * 新增登录日志
     * @param loginLog 登录日志信息
     */
    void addLoginLog(LoginLog loginLog);

    /**
     * 查询登录日志列表
     * @param loginLog 查询条件
     * @return 登录日志列表
     */
    List<LoginLog> selectLoginLogList(LoginLog loginLog);

    /**
     * 批量删除登录日志
     * @param ids 需要删除的数据ID
     */
    void deleteLoginLogByIds(Long[] ids);

    /**
     * 清空登录日志
     */
    void cleanLoginLog();

    /**
     * 查询用户最近的登录日志
     * @param username 用户名
     * @param limit 查询数量
     * @return 登录日志列表
     */
    List<LoginLog> getRecentLoginLogsByUsername(String username, int limit);
    
    /**
     * 分页查询登录日志列表
     * @param request 查询条件和分页参数
     * @return 分页结果
     */
    TableResult<LoginLog> getPage(LoginLogQueryRequest request);
}