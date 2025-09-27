package com.staoo.system.service;

import com.staoo.common.domain.TableResult;
import com.staoo.common.domain.PageQuery;
import com.staoo.system.domain.SystemNotice;

import java.util.List;

/**
 * 系统通知Service接口
 * 定义系统通知的业务操作，支持批量删除功能
 */
public interface SystemNoticeService {
    /**
     * 根据ID查询系统通知
     * @param id 通知ID
     * @return 系统通知对象
     */
    SystemNotice getById(Long id);

    /**
     * 查询系统通知列表
     * @param systemNotice 查询条件
     * @return 系统通知列表
     */
    List<SystemNotice> getList(SystemNotice systemNotice);

    /**
     * 分页查询系统通知
     * @param query 分页查询参数
     * @return 分页结果
     */
    TableResult<SystemNotice> getPage(PageQuery query);

    /**
     * 新增系统通知
     * @param systemNotice 系统通知对象
     * @return 是否新增成功
     */
    boolean save(SystemNotice systemNotice);

    /**
     * 更新系统通知
     * @param systemNotice 系统通知对象
     * @return 是否更新成功
     */
    boolean update(SystemNotice systemNotice);

    /**
     * 根据ID删除系统通知
     * @param id 通知ID
     * @return 是否删除成功
     */
    boolean deleteById(Long id);

    /**
     * 批量删除系统通知
     * 支持批量删除功能，根据通知ID列表删除多条通知记录
     * @param ids 通知ID列表
     * @return 是否删除成功
     */
    boolean deleteByIds(List<Long> ids);

    /**
     * 批量更新系统通知状态
     * @param ids 通知ID列表
     * @param status 状态
     * @return 是否更新成功
     */
    boolean updateStatusByIds(List<Long> ids, Integer status);

    /**
     * 发布通知
     * @param id 通知ID
     * @return 是否发布成功
     */
    boolean publish(Long id);

    /**
     * 撤回通知
     * @param id 通知ID
     * @return 是否撤回成功
     */
    boolean recall(Long id);

    /**
     * 标记通知为已读
     * @param id 通知ID
     * @return 是否标记成功
     */
    boolean markAsRead(Long id);

    /**
     * 批量标记通知为已读
     * @param ids 通知ID列表
     * @return 是否标记成功
     */
    boolean batchMarkAsRead(List<Long> ids);
}