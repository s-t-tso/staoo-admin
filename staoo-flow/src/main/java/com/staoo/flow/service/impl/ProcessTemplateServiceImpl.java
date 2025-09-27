package com.staoo.flow.service.impl;

import com.staoo.flow.domain.ProcessTemplate;
import com.staoo.flow.mapper.ProcessTemplateMapper;
import com.staoo.flow.service.ProcessTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 流程模板服务实现类
 * 实现流程模板相关的业务逻辑处理
 */
@Service
public class ProcessTemplateServiceImpl implements ProcessTemplateService {
    @Autowired
    private ProcessTemplateMapper processTemplateMapper;

    @Override
    public ProcessTemplate getById(Long id) {
        return processTemplateMapper.getById(id);
    }

    @Override
    public ProcessTemplate getByProcessKey(String processKey, Long tenantId) {
        return processTemplateMapper.getByProcessKey(processKey, tenantId);
    }

    @Override
    public List<ProcessTemplate> getList(ProcessTemplate processTemplate) {
        return processTemplateMapper.getList(processTemplate);
    }

    @Override
    public int save(ProcessTemplate processTemplate) {
        processTemplate.setCreateTime(new Date());
        processTemplate.setUpdateTime(new Date());
        processTemplate.setVersion(1);
        if (processTemplate.getStatus() == null) {
            processTemplate.setStatus("DRAFT");
        }
        return processTemplateMapper.insert(processTemplate);
    }

    @Override
    public int update(ProcessTemplate processTemplate) {
        processTemplate.setUpdateTime(new Date());
        return processTemplateMapper.update(processTemplate);
    }

    @Override
    public int deleteById(Long id) {
        return processTemplateMapper.deleteById(id);
    }

    @Override
    public int deleteByIds(Long[] ids) {
        return processTemplateMapper.deleteByIds(Arrays.asList(ids));
    }

    @Override
    public List<ProcessTemplate> getListByTenantId(Long tenantId) {
        return processTemplateMapper.getListByTenantId(tenantId);
    }

    @Override
    public int publishProcessTemplate(Long id) {
        // 发布流程模板，将状态设置为已发布
        return processTemplateMapper.updateStatus(id, "PUBLISHED");
    }

    @Override
    public Long copyProcessTemplate(Long id, String newName) {
        // 获取要复制的流程模板
        ProcessTemplate original = processTemplateMapper.getById(id);
        if (original == null) {
            return null;
        }

        // 创建新的流程模板
        ProcessTemplate newTemplate = new ProcessTemplate();
        newTemplate.setProcessKey(original.getProcessKey() + "_" + UUID.randomUUID().toString().substring(0, 8));
        newTemplate.setProcessName(newName);
        newTemplate.setDescription(original.getDescription());
        newTemplate.setBpmnXml(original.getBpmnXml());
        newTemplate.setStatus("DRAFT");
        newTemplate.setVersion(1);
        newTemplate.setTenantId(original.getTenantId());
        newTemplate.setCreateTime(new Date());
        newTemplate.setUpdateTime(new Date());
        newTemplate.setCreateBy(original.getCreateBy());
        newTemplate.setCategory(original.getCategory());
        newTemplate.setFormKey(original.getFormKey());

        // 保存新模板
        processTemplateMapper.insert(newTemplate);
        return newTemplate.getId();
    }

    @Override
    public boolean checkProcessKeyUnique(String processKey, Long tenantId, Long id) {
        ProcessTemplate processTemplate = processTemplateMapper.getByProcessKey(processKey, tenantId);
        if (processTemplate == null) {
            return true;
        }
        // 如果是更新操作，且找到的模板ID与当前要更新的ID相同，则表示唯一
        return id != null && processTemplate.getId().equals(id);
    }

    @Override
    public List<ProcessTemplate> getListByCategory(String category, Long tenantId) {
        return processTemplateMapper.getListByCategory(category, tenantId);
    }

    @Override
    public List<ProcessTemplate> getListByFormKey(String formKey, Long tenantId) {
        return processTemplateMapper.getListByFormKey(formKey, tenantId);
    }
}