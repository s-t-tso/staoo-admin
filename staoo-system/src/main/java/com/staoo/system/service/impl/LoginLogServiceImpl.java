package com.staoo.system.service.impl;

import com.staoo.system.domain.LoginLog;
import com.staoo.system.mapper.LoginLogMapper;
import com.staoo.system.service.LoginLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 登录日志ServiceImpl
 */
@Service
public class LoginLogServiceImpl implements LoginLogService {
    private static final Logger logger = LoggerFactory.getLogger(LoginLogServiceImpl.class);

    @Autowired
    private LoginLogMapper loginLogMapper;

    /**
     * 新增登录日志
     * @param loginLog 登录日志信息
     */
    @Override
    public void addLoginLog(LoginLog loginLog) {
        try {
            loginLogMapper.insertLoginLog(loginLog);
        } catch (Exception e) {
            logger.error("新增登录日志失败", e);
        }
    }

    /**
     * 查询登录日志列表
     * @param loginLog 查询条件
     * @return 登录日志列表
     */
    @Override
    public List<LoginLog> selectLoginLogList(LoginLog loginLog) {
        try {
            return loginLogMapper.selectLoginLogList(loginLog);
        } catch (Exception e) {
            logger.error("查询登录日志列表失败", e);
            return null;
        }
    }

    /**
     * 批量删除登录日志
     * @param ids 需要删除的数据ID
     */
    @Override
    public void deleteLoginLogByIds(Long[] ids) {
        try {
            loginLogMapper.deleteLoginLogByIds(ids);
        } catch (Exception e) {
            logger.error("批量删除登录日志失败", e);
        }
    }

    /**
     * 清空登录日志
     */
    @Override
    public void cleanLoginLog() {
        try {
            loginLogMapper.cleanLoginLog();
        } catch (Exception e) {
            logger.error("清空登录日志失败", e);
        }
    }

    /**
     * 查询用户最近的登录日志
     * @param username 用户名
     * @param limit 查询数量
     * @return 登录日志列表
     */
    @Override
    public List<LoginLog> getRecentLoginLogsByUsername(String username, int limit) {
        try {
            return loginLogMapper.selectRecentLoginLogsByUsername(username, limit);
        } catch (Exception e) {
            logger.error("查询用户最近登录日志失败", e);
            return null;
        }
    }
}