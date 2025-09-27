package com.staoo.flow.service.impl;

import com.staoo.flow.domain.FormTemplate;
import com.staoo.flow.mapper.FormTemplateMapper;
import com.staoo.flow.service.FormTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 表单模板服务实现类
 * 实现表单模板相关的业务逻辑处理
 */
@Service
public class FormTemplateServiceImpl implements FormTemplateService {
    @Autowired
    private FormTemplateMapper formTemplateMapper;

    @Override
    public FormTemplate getById(Long id) {
        return formTemplateMapper.getById(id);
    }

    @Override
    public FormTemplate getByFormKey(String formKey, Long tenantId) {
        return formTemplateMapper.getByFormKey(formKey, tenantId);
    }

    @Override
    public List<FormTemplate> getList(FormTemplate formTemplate) {
        return formTemplateMapper.getList(formTemplate);
    }

    @Override
    public int save(FormTemplate formTemplate) {
        formTemplate.setCreateTime(new Date());
        formTemplate.setUpdateTime(new Date());
        formTemplate.setVersion(1);
        if (formTemplate.getStatus() == null) {
            formTemplate.setStatus("DRAFT");
        }
        return formTemplateMapper.insert(formTemplate);
    }

    @Override
    public int update(FormTemplate formTemplate) {
        formTemplate.setUpdateTime(new Date());
        return formTemplateMapper.update(formTemplate);
    }

    @Override
    public int deleteById(Long id) {
        return formTemplateMapper.deleteById(id);
    }

    @Override
    public int deleteByIds(Long[] ids) {
        return formTemplateMapper.deleteByIds(Arrays.asList(ids));
    }

    @Override
    public List<FormTemplate> getListByTenantId(Long tenantId) {
        return formTemplateMapper.getListByTenantId(tenantId);
    }

    @Override
    public int publishFormTemplate(Long id) {
        // 发布表单模板，将状态设置为已发布
        return formTemplateMapper.updateStatus(id, "PUBLISHED");
    }

    @Override
    public Long copyFormTemplate(Long id, String newName) {
        // 获取要复制的表单模板
        FormTemplate original = formTemplateMapper.getById(id);
        if (original == null) {
            return null;
        }

        // 创建新的表单模板
        FormTemplate newTemplate = new FormTemplate();
        newTemplate.setFormKey(original.getFormKey() + "_" + UUID.randomUUID().toString().substring(0, 8));
        newTemplate.setFormName(newName);
        newTemplate.setDescription(original.getDescription());
        newTemplate.setFormConfig(original.getFormConfig());
        newTemplate.setStatus("DRAFT");
        newTemplate.setVersion(1);
        newTemplate.setTenantId(original.getTenantId());
        newTemplate.setCreateTime(new Date());
        newTemplate.setUpdateTime(new Date());
        newTemplate.setCreateBy(original.getCreateBy());

        // 保存新模板
        formTemplateMapper.insert(newTemplate);
        return newTemplate.getId();
    }

    @Override
    public boolean checkFormKeyUnique(String formKey, Long tenantId, Long id) {
        FormTemplate formTemplate = formTemplateMapper.getByFormKey(formKey, tenantId);
        if (formTemplate == null) {
            return true;
        }
        // 如果是更新操作，且找到的模板ID与当前要更新的ID相同，则表示唯一
        return id != null && formTemplate.getId().equals(id);
    }
}