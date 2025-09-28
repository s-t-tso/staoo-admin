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
import java.util.ArrayList;
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
        return systemNoticeMapper.selectListByRequest(request);
    }

    @Override
    public TableResult<SystemNotice> getPage(SystemNoticeQueryRequest request) {
        try {
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
        // 注意：createTime和updateTime字段将由MyBatis拦截器自动填充

        int count = systemNoticeMapper.insert(systemNotice);
        return count > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(SystemNotice systemNotice) {
        // 检查通知是否存在
        SystemNotice existingNotice = systemNoticeMapper.selectById(systemNotice.getId());
        if (existingNotice == null) {
            throw new BusinessException(StatusCodeEnum.NOTICE_NOT_FOUND);
        }

        // 注意：updateTime字段将由MyBatis拦截器自动填充

        int count = systemNoticeMapper.update(systemNotice);
        return count > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Long id) {
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
        try {
            // 创建批量验证器链
            BatchNoticeValidationChain validationChain = new BatchNoticeValidationChain();
            validationChain.addValidator(new BatchNoticeExistsValidator());

            // 执行验证
            validationChain.validate(ids);

            // 批量删除系统通知，支持批量删除功能
            int count = systemNoticeMapper.deleteByIds(ids);
            return count == ids.size();
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.error("批量删除通知失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatusByIds(List<Long> ids, Integer status) {
        try {
            // 创建批量验证器链
            BatchNoticeValidationChain validationChain = new BatchNoticeValidationChain();
            validationChain.addValidator(new BatchNoticeExistsValidator());

            // 执行验证
            validationChain.validate(ids);

            int count = systemNoticeMapper.updateStatusByIds(ids, status);
            return count == ids.size();
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.error("批量更新通知状态失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publish(Long id) {
        try {
            // 创建验证器链
            NoticeValidationChain validationChain = new NoticeValidationChain();
            validationChain.addValidator(new NoticeExistsValidator())
                          .addValidator(new NoticeNotPublishedValidator());

            // 执行验证
            validationChain.validate(id);

            // 更新通知状态为已发布，并设置发布时间
            // 注意：publishTime和updateTime字段将由MyBatis拦截器自动填充
            SystemNotice systemNotice = new SystemNotice();
            systemNotice.setId(id);
            systemNotice.setStatus(NoticeStatusEnum.PUBLISHED.getCode());

            int count = systemNoticeMapper.update(systemNotice);
            return count > 0;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.error("发布通知失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recall(Long id) {
        try {
            // 创建验证器链
            NoticeValidationChain validationChain = new NoticeValidationChain();
            validationChain.addValidator(new NoticeExistsValidator())
                          .addValidator(new NoticeNotRecalledValidator());

            // 执行验证
            validationChain.validate(id);

            // 更新通知状态为已撤回
            // 注意：updateTime字段将由MyBatis拦截器自动填充
            SystemNotice systemNotice = new SystemNotice();
            systemNotice.setId(id);
            systemNotice.setStatus(NoticeStatusEnum.RECALLED.getCode());

            int count = systemNoticeMapper.update(systemNotice);
            return count > 0;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.error("撤回通知失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean markAsRead(Long id) {
        try {
            // 检查通知是否已经阅读（这是一个特殊的验证，不是抛出异常而是直接返回true）
            SystemNotice existingNotice = systemNoticeMapper.selectById(id);
            if (existingNotice == null) {
                throw new BusinessException(StatusCodeEnum.NOTICE_NOT_FOUND);
            }

            if (ReadStatusEnum.READ.equals(ReadStatusEnum.getByCode(existingNotice.getReadStatus()))) {
                return true; // 已经阅读，直接返回成功
            }

            // 更新通知阅读状态为已读
            // 注意：updateTime字段将由MyBatis拦截器自动填充
            SystemNotice systemNotice = new SystemNotice();
            systemNotice.setId(id);
            systemNotice.setReadStatus(ReadStatusEnum.READ.getCode());

            int count = systemNoticeMapper.update(systemNotice);
            return count > 0;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.error("标记通知为已读失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchMarkAsRead(List<Long> ids) {
        try {
            // 创建批量验证器链
            BatchNoticeValidationChain validationChain = new BatchNoticeValidationChain();
            validationChain.addValidator(new BatchNoticeExistsValidator());

            // 执行验证
            validationChain.validate(ids);

            // 批量更新通知阅读状态为已读
            int count = systemNoticeMapper.updateStatusByIds(ids, 1);
            return count == ids.size();
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.error("批量标记通知为已读失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    /**
     * 批量通知验证器接口
     */
    private interface BatchNoticeValidator {
        void validate(List<Long> noticeIds);
    }

    /**
     * 批量验证器链，用于组合多个批量验证器
     */
    private class BatchNoticeValidationChain {
        private List<BatchNoticeValidator> validators = new ArrayList<>();

        public BatchNoticeValidationChain addValidator(BatchNoticeValidator validator) {
            validators.add(validator);
            return this;
        }

        public void validate(List<Long> noticeIds) {
            for (BatchNoticeValidator validator : validators) {
                validator.validate(noticeIds);
            }
        }
    }

    /**
     * 批量通知存在验证器
     */
    private class BatchNoticeExistsValidator implements BatchNoticeValidator {
        @Override
        public void validate(List<Long> noticeIds) {
            for (Long id : noticeIds) {
                SystemNotice existingNotice = systemNoticeMapper.selectById(id);
                if (existingNotice == null) {
                    throw new BusinessException(StatusCodeEnum.NOTICE_NOT_FOUND, "通知ID: " + id + " 不存在");
                }
            }
        }
    }

    /**
     * 通知验证器接口
     */
    private interface NoticeValidator {
        void validate(Long noticeId);
    }

    /**
     * 验证器链，用于组合多个验证器
     */
    private class NoticeValidationChain {
        private List<NoticeValidator> validators = new ArrayList<>();

        public NoticeValidationChain addValidator(NoticeValidator validator) {
            validators.add(validator);
            return this;
        }

        public void validate(Long noticeId) {
            for (NoticeValidator validator : validators) {
                validator.validate(noticeId);
            }
        }
    }

    /**
     * 通知存在验证器
     */
    private class NoticeExistsValidator implements NoticeValidator {
        @Override
        public void validate(Long noticeId) {
            SystemNotice existingNotice = systemNoticeMapper.selectById(noticeId);
            if (existingNotice == null) {
                throw new BusinessException(StatusCodeEnum.NOTICE_NOT_FOUND);
            }
        }
    }

    /**
     * 通知未发布验证器
     */
    private class NoticeNotPublishedValidator implements NoticeValidator {
        @Override
        public void validate(Long noticeId) {
            SystemNotice existingNotice = systemNoticeMapper.selectById(noticeId);
            if (NoticeStatusEnum.PUBLISHED.equals(NoticeStatusEnum.getByCode(existingNotice.getStatus()))) {
                throw new BusinessException(StatusCodeEnum.OPERATION_NOT_ALLOWED, "通知已经发布");
            }
        }
    }

    /**
     * 通知未撤回验证器
     */
    private class NoticeNotRecalledValidator implements NoticeValidator {
        @Override
        public void validate(Long noticeId) {
            SystemNotice existingNotice = systemNoticeMapper.selectById(noticeId);
            if (NoticeStatusEnum.RECALLED.equals(NoticeStatusEnum.getByCode(existingNotice.getStatus()))) {
                throw new BusinessException(StatusCodeEnum.OPERATION_NOT_ALLOWED, "通知已经撤回");
            }
        }
    }
}
