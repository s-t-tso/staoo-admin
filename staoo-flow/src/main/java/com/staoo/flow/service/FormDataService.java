package com.staoo.flow.service;

import com.staoo.flow.domain.FormData;

import java.util.List;

/**
 * 表单数据服务接口
 * 提供表单数据相关的业务逻辑处理方法
 */
public interface FormDataService {
    /**
     * 根据ID查询表单数据
     * @param id 数据ID
     * @return 表单数据信息
     */
    FormData getById(Long id);

    /**
     * 根据业务键查询表单数据
     * @param businessKey 业务键
     * @param formKey 表单标识
     * @param tenantId 租户ID
     * @return 表单数据信息
     */
    FormData getByBusinessKey(String businessKey, String formKey, Long tenantId);

    /**
     * 查询表单数据列表
     * @param formData 查询条件
     * @return 表单数据列表
     */
    List<FormData> getList(FormData formData);

    /**
     * 新增表单数据
     * @param formData 表单数据信息
     * @return 影响行数
     */
    int save(FormData formData);

    /**
     * 更新表单数据
     * @param formData 表单数据信息
     * @return 影响行数
     */
    int update(FormData formData);

    /**
     * 删除表单数据
     * @param id 数据ID
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 批量删除表单数据
     * @param ids 数据ID列表
     * @return 影响行数
     */
    int deleteByIds(Long[] ids);

    /**
     * 根据表单标识查询表单数据
     * @param formKey 表单标识
     * @param tenantId 租户ID
     * @return 表单数据列表
     */
    List<FormData> getListByFormKey(String formKey, Long tenantId);

    /**
     * 根据创建人ID查询表单数据
     * @param createBy 创建人ID
     * @param tenantId 租户ID
     * @return 表单数据列表
     */
    List<FormData> getListByCreateBy(Long createBy, Long tenantId);

    /**
     * 更新表单数据状态
     * @param id 数据ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(Long id, String status);

    /**
     * 根据流程实例ID查询表单数据
     * @param processInstanceId 流程实例ID
     * @param tenantId 租户ID
     * @return 表单数据信息
     */
    FormData getByProcessInstanceId(String processInstanceId, Long tenantId);

    /**
     * 验证表单数据
     * @param formData 表单数据信息
     * @return 验证结果
     */
    boolean validateFormData(FormData formData);

    /**
     * 导出表单数据
     * @param formKey 表单标识
     * @param tenantId 租户ID
     * @return 导出数据的字节数组
     */
    byte[] exportFormData(String formKey, Long tenantId);
}