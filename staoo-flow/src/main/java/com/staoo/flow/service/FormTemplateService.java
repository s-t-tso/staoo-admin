package com.staoo.flow.service;

import com.staoo.flow.domain.FormTemplate;

import java.util.List;

/**
 * 表单模板服务接口
 * 提供表单模板相关的业务逻辑处理方法
 */
public interface FormTemplateService {
    /**
     * 根据ID查询表单模板
     * @param id 模板ID
     * @return 表单模板信息
     */
    FormTemplate getById(Long id);

    /**
     * 根据表单标识查询表单模板
     * @param formKey 表单唯一标识
     * @param tenantId 租户ID
     * @return 表单模板信息
     */
    FormTemplate getByFormKey(String formKey, Long tenantId);

    /**
     * 查询表单模板列表
     * @param formTemplate 查询条件
     * @return 表单模板列表
     */
    List<FormTemplate> getList(FormTemplate formTemplate);

    /**
     * 新增表单模板
     * @param formTemplate 表单模板信息
     * @return 影响行数
     */
    int save(FormTemplate formTemplate);

    /**
     * 更新表单模板
     * @param formTemplate 表单模板信息
     * @return 影响行数
     */
    int update(FormTemplate formTemplate);

    /**
     * 删除表单模板
     * @param id 模板ID
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 批量删除表单模板
     * @param ids 模板ID列表
     * @return 影响行数
     */
    int deleteByIds(Long[] ids);

    /**
     * 根据租户ID查询所有表单模板
     * @param tenantId 租户ID
     * @return 表单模板列表
     */
    List<FormTemplate> getListByTenantId(Long tenantId);

    /**
     * 发布表单模板
     * @param id 模板ID
     * @return 影响行数
     */
    int publishFormTemplate(Long id);

    /**
     * 复制表单模板
     * @param id 模板ID
     * @param newName 新模板名称
     * @return 新模板ID
     */
    Long copyFormTemplate(Long id, String newName);

    /**
     * 验证表单标识唯一性
     * @param formKey 表单标识
     * @param tenantId 租户ID
     * @param id 模板ID（更新时使用，排除自身）
     * @return 是否唯一
     */
    boolean checkFormKeyUnique(String formKey, Long tenantId, Long id);
}