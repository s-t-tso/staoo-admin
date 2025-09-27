package com.staoo.system.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.staoo.common.domain.TableResult;
import com.staoo.common.enums.StatusCodeEnum;
import com.staoo.common.exception.BusinessException;
import com.staoo.system.domain.LoginLog;
import com.staoo.system.mapper.LoginLogMapper;
import com.staoo.system.pojo.request.LoginLogQueryRequest;
import com.staoo.system.service.LoginLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    @Override
    public TableResult<LoginLog> getPage(LoginLogQueryRequest request) {
        try {
            if (request == null) {
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "分页查询参数不能为空");
            }
            
            // 设置分页参数
            PageHelper.startPage(request.getPageNum(), request.getPageSize());
            
            // 构建查询条件
            LoginLog loginLog = new LoginLog();
            
            // 从请求对象中设置查询条件
            loginLog.setTenantId(request.getTenantId());
            loginLog.setUserId(request.getUserId());
            loginLog.setUsername(request.getUsername());
            loginLog.setIp(request.getIp());
            loginLog.setLocation(request.getLocation());
            loginLog.setBrowser(request.getBrowser());
            loginLog.setOs(request.getOs());
            
            // 处理状态参数
            if (request.getStatus() != null) {
                loginLog.setStatus(Integer.valueOf(request.getStatus()));
            }
            
            // 如果有搜索关键词，设置到查询条件中
            if (StringUtils.hasText(request.getKeyword())) {
                loginLog.setUsername(request.getKeyword());
                loginLog.setIp(request.getKeyword());
            }
            
            // 查询列表
            List<LoginLog> list = loginLogMapper.selectLoginLogList(loginLog);
            Page<LoginLog> pageList = (Page<LoginLog>) list;
            
            // 构建分页结果
            return TableResult.build(pageList.getTotal(), request.getPageNum(), request.getPageSize(), list);
        } catch (Exception e) {
            logger.error("分页查询登录日志失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }
}