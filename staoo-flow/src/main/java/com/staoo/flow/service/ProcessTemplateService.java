package com.staoo.flow.service;

import com.staoo.flow.domain.ProcessTemplate;

import java.util.List;

/**
 * 流程模板服务接口
 * 提供流程模板相关的业务逻辑处理方法
 */
public interface ProcessTemplateService {
    /**
     * 根据ID查询流程模板
     * @param id 模板ID
     * @return 流程模板信息
     */
    ProcessTemplate getById(Long id);

    /**
     * 根据流程标识查询流程模板
     * @param processKey 流程唯一标识
     * @param tenantId 租户ID
     * @return 流程模板信息
     */
    ProcessTemplate getByProcessKey(String processKey, Long tenantId);

    /**
     * 查询流程模板列表
     * @param processTemplate 查询条件
     * @return 流程模板列表
     */
    List<ProcessTemplate> getList(ProcessTemplate processTemplate);

    /**
     * 新增流程模板
     * @param processTemplate 流程模板信息
     * @return 影响行数
     */
    int save(ProcessTemplate processTemplate);

    /**
     * 更新流程模板
     * @param processTemplate 流程模板信息
     * @return 影响行数
     */
    int update(ProcessTemplate processTemplate);

    /**
     * 删除流程模板
     * @param id 模板ID
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 批量删除流程模板
     * @param ids 模板ID列表
     * @return 影响行数
     */
    int deleteByIds(Long[] ids);

    /**
     * 根据租户ID查询所有流程模板
     * @param tenantId 租户ID
     * @return 流程模板列表
     */
    List<ProcessTemplate> getListByTenantId(Long tenantId);

    /**
     * 发布流程模板
     * @param id 模板ID
     * @return 影响行数
     */
    int publishProcessTemplate(Long id);

    /**
     * 复制流程模板
     * @param id 模板ID
     * @param newName 新模板名称
     * @return 新模板ID
     */
    Long copyProcessTemplate(Long id, String newName);

    /**
     * 验证流程标识唯一性
     * @param processKey 流程标识
     * @param tenantId 租户ID
     * @param id 模板ID（更新时使用，排除自身）
     * @return 是否唯一
     */
    boolean checkProcessKeyUnique(String processKey, Long tenantId, Long id);

    /**
     * 根据分类查询流程模板
     * @param category 流程分类
     * @param tenantId 租户ID
     * @return 流程模板列表
     */
    List<ProcessTemplate> getListByCategory(String category, Long tenantId);

    /**
     * 根据表单标识查询关联的流程模板
     * @param formKey 表单标识
     * @param tenantId 租户ID
     * @return 流程模板列表
     */
    List<ProcessTemplate> getListByFormKey(String formKey, Long tenantId);
}