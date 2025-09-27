package com.staoo.system.service.impl;

import com.staoo.common.domain.TableResult;
import com.staoo.common.enums.StatusCodeEnum;
import com.staoo.common.exception.BusinessException;
import com.staoo.common.domain.PageQuery;
import com.staoo.system.domain.SystemNotice;
import com.staoo.system.mapper.SystemNoticeMapper;
import com.staoo.system.service.SystemNoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    public TableResult<SystemNotice> getPage(PageQuery query) {
        if (query == null) {
            query = new PageQuery();
        }
        // 构建查询条件
        SystemNotice systemNotice = new SystemNotice();
        // 这里可以根据需要设置查询条件
        
        // 查询总数
        Long total = systemNoticeMapper.selectCount(systemNotice);
        if (total == 0) {
            return TableResult.empty();
        }
        
        // 查询列表
        List<SystemNotice> list = systemNoticeMapper.selectList(systemNotice);
        
        // 构建分页结果
        return TableResult.build(total, query.getPageNum(), query.getPageSize(), list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(SystemNotice systemNotice) {
        if (systemNotice == null) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "通知信息不能为空");
        }
        // 参数校验
        validateSystemNotice(systemNotice);
        
        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        systemNotice.setCreateTime(now);
        systemNotice.setUpdateTime(now);
        
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
        
        // 设置更新时间
        systemNotice.setUpdateTime(LocalDateTime.now());
        
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
        if (existingNotice.getStatus() == 1) {
            throw new BusinessException(StatusCodeEnum.OPERATION_NOT_ALLOWED, "通知已经发布");
        }
        
        // 更新通知状态为已发布，并设置发布时间
        SystemNotice systemNotice = new SystemNotice();
        systemNotice.setId(id);
        systemNotice.setStatus(1);
        systemNotice.setPublishTime(LocalDateTime.now());
        systemNotice.setUpdateTime(LocalDateTime.now());
        
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
        if (existingNotice.getStatus() == 2) {
            throw new BusinessException(StatusCodeEnum.OPERATION_NOT_ALLOWED, "通知已经撤回");
        }
        
        // 更新通知状态为已撤回
        SystemNotice systemNotice = new SystemNotice();
        systemNotice.setId(id);
        systemNotice.setStatus(2);
        systemNotice.setUpdateTime(LocalDateTime.now());
        
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
        if (existingNotice.getReadStatus() == 1) {
            return true; // 已经阅读，直接返回成功
        }
        
        // 更新通知阅读状态为已读
        SystemNotice systemNotice = new SystemNotice();
        systemNotice.setId(id);
        systemNotice.setReadStatus(1);
        systemNotice.setUpdateTime(LocalDateTime.now());
        
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