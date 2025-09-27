package com.staoo.system.mapper;

import com.staoo.system.domain.LoginLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 登录日志Mapper接口
 */
@Mapper
public interface LoginLogMapper {
    /**
     * 新增登录日志
     * @param loginLog 登录日志信息
     * @return 结果
     */
    int insertLoginLog(LoginLog loginLog);

    /**
     * 查询登录日志列表
     * @param loginLog 查询条件
     * @return 登录日志列表
     */
    List<LoginLog> selectLoginLogList(LoginLog loginLog);

    /**
     * 批量删除登录日志
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteLoginLogByIds(Long[] ids);

    /**
     * 清空登录日志
     * @return 结果
     */
    int cleanLoginLog();

    /**
     * 查询用户最近的登录日志
     * @param username 用户名
     * @param limit 查询数量
     * @return 登录日志列表
     */
    List<LoginLog> selectRecentLoginLogsByUsername(@Param("username") String username, @Param("limit") int limit);
}