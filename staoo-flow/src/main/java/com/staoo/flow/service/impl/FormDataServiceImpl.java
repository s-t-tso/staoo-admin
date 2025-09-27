package com.staoo.flow.service.impl;

import com.staoo.flow.domain.FormData;
import com.staoo.flow.mapper.FormDataMapper;
import com.staoo.flow.service.FormDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 表单数据服务实现类
 * 实现表单数据相关的业务逻辑处理
 */
@Service
public class FormDataServiceImpl implements FormDataService {
    @Autowired
    private FormDataMapper formDataMapper;

    @Override
    public FormData getById(Long id) {
        return formDataMapper.getById(id);
    }

    @Override
    public FormData getByBusinessKey(String businessKey, String formKey, Long tenantId) {
        return formDataMapper.getByBusinessKey(businessKey, formKey, tenantId);
    }

    @Override
    public List<FormData> getList(FormData formData) {
        return formDataMapper.getList(formData);
    }

    @Override
    public int save(FormData formData) {
        formData.setCreateTime(new Date());
        formData.setUpdateTime(new Date());
        if (formData.getStatus() == null) {
            formData.setStatus("DRAFT");
        }
        if (formData.getBusinessKey() == null || formData.getBusinessKey().isEmpty()) {
            formData.setBusinessKey(UUID.randomUUID().toString().substring(0, 16));
        }
        return formDataMapper.insert(formData);
    }

    @Override
    public int update(FormData formData) {
        formData.setUpdateTime(new Date());
        return formDataMapper.update(formData);
    }

    @Override
    public int deleteById(Long id) {
        return formDataMapper.deleteById(id);
    }

    @Override
    public int deleteByIds(Long[] ids) {
        return formDataMapper.deleteByIds(Arrays.asList(ids));
    }

    @Override
    public List<FormData> getListByFormKey(String formKey, Long tenantId) {
        return formDataMapper.getListByFormKey(formKey, tenantId);
    }

    @Override
    public List<FormData> getListByCreateBy(Long createBy, Long tenantId) {
        return formDataMapper.getListByCreateBy(createBy, tenantId);
    }

    @Override
    public int updateStatus(Long id, String status) {
        return formDataMapper.updateStatus(id, status);
    }

    @Override
    public FormData getByProcessInstanceId(String processInstanceId, Long tenantId) {
        return formDataMapper.getByProcessInstanceId(processInstanceId, tenantId);
    }

    @Override
    public boolean validateFormData(FormData formData) {
        // 简单的表单数据验证逻辑
        // 1. 检查必要字段
        if (formData == null) {
            return false;
        }
        
        if (formData.getFormKey() == null || formData.getFormKey().isEmpty()) {
            return false;
        }
        
        if (formData.getTemplateId() == null) {
            return false;
        }
        
        if (formData.getFormData() == null || formData.getFormData().isEmpty()) {
            return false;
        }
        
        if (formData.getTenantId() == null) {
            return false;
        }
        
        // 2. 可以根据表单模板的配置进行更复杂的验证
        // 这里简化处理，返回true表示验证通过
        return true;
    }

    @Override
    public byte[] exportFormData(String formKey, Long tenantId) {
        // 导出表单数据为JSON格式
        // 1. 查询表单数据
        List<FormData> formDataList = formDataMapper.getListByFormKey(formKey, tenantId);
        
        // 2. 构建JSON字符串
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");
        for (int i = 0; i < formDataList.size(); i++) {
            FormData formData = formDataList.get(i);
            jsonBuilder.append(formData.getFormData());
            if (i < formDataList.size() - 1) {
                jsonBuilder.append(",");
            }
        }
        jsonBuilder.append("]");
        
        // 3. 转换为字节数组返回
        return jsonBuilder.toString().getBytes();
    }
}