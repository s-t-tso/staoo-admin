package com.staoo.system.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.staoo.common.domain.TableResult;
import com.staoo.common.enums.StatusCodeEnum;
import com.staoo.common.exception.BusinessException;
import com.staoo.system.pojo.request.SystemNoticeQueryRequest;
import com.staoo.common.enums.status.NoticeStatusEnum;
import com.staoo.common.enums.status.ReadStatusEnum;
import com.staoo.system.domain.SystemNotice;
import com.staoo.system.mapper.SystemNoticeMapper;
import com.staoo.system.service.SystemNoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统通知Service实现类
 * 实现系统通知的业务操作，支持批量删除功能
 */
@Service
public class SystemNoticeServiceImpl implements SystemNoticeService {
    private static final Logger logger = LoggerFactory.getLogger(SystemNoticeServiceImpl.class);

    @Autowired
    private SystemNoticeMapper systemNoticeMapper;

    @Override
    public SystemNotice getById(Long id) {
        if (id == null || id <= 0) {
            logger.error("查询系统通知失败，通知ID不能为空");
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "通知ID不能为空");
        }
        SystemNotice systemNotice = systemNoticeMapper.selectById(id);
        if (systemNotice == null) {
            throw new BusinessException(StatusCodeEnum.NOTICE_NOT_FOUND);
        }
        return systemNotice;
    }

    @Override
    public List<SystemNotice> getList(SystemNotice systemNotice) {
        return systemNoticeMapper.selectList(systemNotice);
    }
    
    @Override
    public List<SystemNotice> getList(SystemNoticeQueryRequest request) {
        // 参数校验
        if (request == null) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "查询参数不能为空");
        }
        
        return systemNoticeMapper.selectListByRequest(request);
    }

    @Override
    public TableResult<SystemNotice> getPage(SystemNoticeQueryRequest request) {
        try {
            // 参数校验
            if (request == null) {
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "分页查询参数不能为空");
            }
            
            // 设置分页参数
            PageHelper.startPage(request.getPageNum(), request.getPageSize());
            
            // 直接使用新的getList方法获取数据
            List<SystemNotice> list = getList(request);
            Page<SystemNotice> pageList = (Page<SystemNotice>) list;
            
            // 构建分页结果
            return TableResult.build(pageList.getTotal(), request.getPageNum(), request.getPageSize(), list);
        } catch (Exception e) {
            logger.error("分页查询系统通知失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(SystemNotice systemNotice) {
        if (systemNotice == null) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "通知信息不能为空");
        }
        // 参数校验
        validateSystemNotice(systemNotice);
        
        // 注意：createTime和updateTime字段将由MyBatis拦截器自动填充
        
        int count = systemNoticeMapper.insert(systemNotice);
        return count > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(SystemNotice systemNotice) {
        if (systemNotice == null || systemNotice.getId() == null || systemNotice.getId() <= 0) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "通知ID不能为空");
        }
        
        // 检查通知是否存在
        SystemNotice existingNotice = systemNoticeMapper.selectById(systemNotice.getId());
        if (existingNotice == null) {
            throw new BusinessException(StatusCodeEnum.NOTICE_NOT_FOUND);
        }
        
        // 参数校验
        validateSystemNotice(systemNotice);
        
        // 注意：updateTime字段将由MyBatis拦截器自动填充
        
        int count = systemNoticeMapper.update(systemNotice);
        return count > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "通知ID不能为空");
        }
        
        // 检查通知是否存在
        SystemNotice existingNotice = systemNoticeMapper.selectById(id);
        if (existingNotice == null) {
            throw new BusinessException(StatusCodeEnum.NOTICE_NOT_FOUND);
        }
        
        int count = systemNoticeMapper.deleteById(id);
        return count > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "通知ID列表不能为空");
        }
        
        // 检查通知是否存在
        for (Long id : ids) {
            SystemNotice existingNotice = systemNoticeMapper.selectById(id);
            if (existingNotice == null) {
                throw new BusinessException(StatusCodeEnum.NOTICE_NOT_FOUND, "通知ID: " + id + " 不存在");
            }
        }
        
        // 批量删除系统通知，支持批量删除功能
        int count = systemNoticeMapper.deleteByIds(ids);
        return count == ids.size();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatusByIds(List<Long> ids, Integer status) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "通知ID列表不能为空");
        }
        if (status == null) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "状态不能为空");
        }
        
        // 检查通知是否存在
        for (Long id : ids) {
            SystemNotice existingNotice = systemNoticeMapper.selectById(id);
            if (existingNotice == null) {
                throw new BusinessException(StatusCodeEnum.NOTICE_NOT_FOUND, "通知ID: " + id + " 不存在");
            }
        }
        
        int count = systemNoticeMapper.updateStatusByIds(ids, status);
        return count == ids.size();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publish(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "通知ID不能为空");
        }
        
        // 检查通知是否存在
        SystemNotice existingNotice = systemNoticeMapper.selectById(id);
        if (existingNotice == null) {
            throw new BusinessException(StatusCodeEnum.NOTICE_NOT_FOUND);
        }
        
        // 检查通知是否已经发布
        if (NoticeStatusEnum.PUBLISHED.equals(NoticeStatusEnum.getByCode(existingNotice.getStatus()))) {
            throw new BusinessException(StatusCodeEnum.OPERATION_NOT_ALLOWED, "通知已经发布");
        }
        
        // 更新通知状态为已发布，并设置发布时间
        SystemNotice systemNotice = new SystemNotice();
        systemNotice.setId(id);
        systemNotice.setStatus(NoticeStatusEnum.PUBLISHED.getCode());
        // 注意：publishTime和updateTime字段将由MyBatis拦截器自动填充
        
        int count = systemNoticeMapper.update(systemNotice);
        return count > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recall(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "通知ID不能为空");
        }
        
        // 检查通知是否存在
        SystemNotice existingNotice = systemNoticeMapper.selectById(id);
        if (existingNotice == null) {
            throw new BusinessException(StatusCodeEnum.NOTICE_NOT_FOUND);
        }
        
        // 检查通知是否已经撤回
        if (NoticeStatusEnum.RECALLED.equals(NoticeStatusEnum.getByCode(existingNotice.getStatus()))) {
            throw new BusinessException(StatusCodeEnum.OPERATION_NOT_ALLOWED, "通知已经撤回");
        }
        
        // 更新通知状态为已撤回
        SystemNotice systemNotice = new SystemNotice();
        systemNotice.setId(id);
        systemNotice.setStatus(NoticeStatusEnum.RECALLED.getCode());
        // 注意：updateTime字段将由MyBatis拦截器自动填充
        
        int count = systemNoticeMapper.update(systemNotice);
        return count > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean markAsRead(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "通知ID不能为空");
        }
        
        // 检查通知是否存在
        SystemNotice existingNotice = systemNoticeMapper.selectById(id);
        if (existingNotice == null) {
            throw new BusinessException(StatusCodeEnum.NOTICE_NOT_FOUND);
        }
        
        // 检查通知是否已经阅读
        if (ReadStatusEnum.READ.equals(ReadStatusEnum.getByCode(existingNotice.getReadStatus()))) {
            return true; // 已经阅读，直接返回成功
        }
        
        // 更新通知阅读状态为已读
        SystemNotice systemNotice = new SystemNotice();
        systemNotice.setId(id);
        systemNotice.setReadStatus(ReadStatusEnum.READ.getCode());
        // 注意：updateTime字段将由MyBatis拦截器自动填充
        
        int count = systemNoticeMapper.update(systemNotice);
        return count > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchMarkAsRead(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "通知ID列表不能为空");
        }
        
        // 检查通知是否存在
        for (Long id : ids) {
            SystemNotice existingNotice = systemNoticeMapper.selectById(id);
            if (existingNotice == null) {
                throw new BusinessException(StatusCodeEnum.NOTICE_NOT_FOUND, "通知ID: " + id + " 不存在");
            }
        }
        
        // 批量更新通知阅读状态为已读
        int count = systemNoticeMapper.updateStatusByIds(ids, 1);
        return count == ids.size();
    }

    /**
     * 验证系统通知参数
     * @param systemNotice 系统通知对象
     */
    private void validateSystemNotice(SystemNotice systemNotice) {
        if (systemNotice.getTitle() == null || systemNotice.getTitle().trim().isEmpty()) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "通知标题不能为空");
        }
        if (systemNotice.getContent() == null || systemNotice.getContent().trim().isEmpty()) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "通知内容不能为空");
        }
        if (systemNotice.getType() == null) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "通知类型不能为空");
        }
        if (systemNotice.getLevel() == null) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "通知级别不能为空");
        }
        if (systemNotice.getStatus() == null) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "通知状态不能为空");
        }
    }
}