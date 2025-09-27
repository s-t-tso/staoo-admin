package com.staoo.flow.service.impl;

import com.staoo.common.util.UserUtils;
import com.staoo.flow.domain.ProcessTemplate;
import com.staoo.flow.mapper.ProcessTemplateMapper;
import com.staoo.flow.service.ProcessTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 流程模板服务实现类
 * 实现流程模板相关的业务逻辑处理
 */
@Service
public class ProcessTemplateServiceImpl implements ProcessTemplateService {
    private static final Logger logger = LoggerFactory.getLogger(ProcessTemplateServiceImpl.class);
    
    @Autowired
    private ProcessTemplateMapper processTemplateMapper;

    @Override
    public ProcessTemplate getById(Long id) {
        if (id == null || id <= 0) {
            logger.warn("查询流程模板时ID为空或无效: {}", id);
            return null;
        }
        logger.debug("查询流程模板，ID: {}", id);
        try {
            return processTemplateMapper.getById(id);
        } catch (Exception e) {
            logger.error("查询流程模板失败: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public ProcessTemplate getByProcessKey(String processKey, Long tenantId) {
        if (processKey == null || processKey.isEmpty()) {
            logger.warn("查询流程模板时流程标识为空");
            return null;
        }
        
        // 获取租户ID（如果传入的租户ID为空）
        Long effectiveTenantId = tenantId;
        if (effectiveTenantId == null) {
            effectiveTenantId = UserUtils.getCurrentTenantId();
            if (effectiveTenantId == null) {
                logger.warn("无法获取租户ID，无法查询流程模板");
                return null;
            }
        }
        
        logger.debug("查询流程模板，processKey: {}, tenantId: {}", processKey, effectiveTenantId);
        
        try {
            return processTemplateMapper.getByProcessKey(processKey, effectiveTenantId);
        } catch (Exception e) {
            logger.error("查询流程模板失败: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<ProcessTemplate> getList(ProcessTemplate processTemplate) {
        if (processTemplate == null) {
            processTemplate = new ProcessTemplate();
            logger.debug("查询流程模板列表，参数为空，使用默认条件");
        } else {
            // 从当前登录用户获取租户ID（如果未设置）
            if (processTemplate.getTenantId() == null) {
                Long tenantId = UserUtils.getCurrentTenantId();
                if (tenantId != null) {
                    processTemplate.setTenantId(tenantId);
                }
            }
            logger.debug("查询流程模板列表，流程标识: {}, 状态: {}, 租户ID: {}, 分类: {}", 
                processTemplate.getProcessKey(), processTemplate.getStatus(), 
                processTemplate.getTenantId(), processTemplate.getCategory());
        }
        
        try {
            return processTemplateMapper.getList(processTemplate);
        } catch (Exception e) {
            logger.error("查询流程模板列表失败: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public int save(ProcessTemplate processTemplate) {
        if (processTemplate == null) {
            logger.warn("保存流程模板时参数为空");
            return 0;
        }
        
        // 验证必要字段
        if (processTemplate.getProcessName() == null || processTemplate.getProcessName().isEmpty()) {
            logger.warn("保存流程模板时流程名称为空");
            return 0;
        }
        if (processTemplate.getProcessKey() == null || processTemplate.getProcessKey().isEmpty()) {
            logger.warn("保存流程模板时流程标识为空");
            return 0;
        }
        if (processTemplate.getBpmnXml() == null || processTemplate.getBpmnXml().isEmpty()) {
            logger.warn("保存流程模板时BPMN定义为空");
            return 0;
        }
        
        // 设置租户ID（如果未设置）
        if (processTemplate.getTenantId() == null) {
            Long tenantId = UserUtils.getCurrentTenantId();
            if (tenantId != null) {
                processTemplate.setTenantId(tenantId);
            } else {
                logger.warn("无法获取租户ID");
                return 0;
            }
        }
        
        // 检查流程标识是否唯一
        if (!checkProcessKeyUnique(processTemplate.getProcessKey(), processTemplate.getTenantId(), null)) {
            logger.warn("流程标识已存在: {}", processTemplate.getProcessKey());
            return 0;
        }
        
        // 创建时间和更新时间由MyBatis拦截器自动填充
        processTemplate.setVersion(1);
        
        // 设置默认状态
        if (processTemplate.getStatus() == null) {
            processTemplate.setStatus("DRAFT");
        }
        
        logger.info("保存流程模板，名称: {}, 标识: {}", processTemplate.getProcessName(), processTemplate.getProcessKey());
        
        try {
            return processTemplateMapper.insert(processTemplate);
        } catch (Exception e) {
            logger.error("保存流程模板失败: {}", e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public int update(ProcessTemplate processTemplate) {
        if (processTemplate == null) {
            logger.warn("更新流程模板时参数为空");
            return 0;
        }
        
        // 验证ID
        if (processTemplate.getId() == null || processTemplate.getId() <= 0) {
            logger.warn("更新流程模板时ID为空或无效");
            return 0;
        }
        
        // 验证必要字段
        if (processTemplate.getProcessName() == null || processTemplate.getProcessName().isEmpty()) {
            logger.warn("更新流程模板时流程名称为空");
            return 0;
        }
        if (processTemplate.getProcessKey() == null || processTemplate.getProcessKey().isEmpty()) {
            logger.warn("更新流程模板时流程标识为空");
            return 0;
        }
        if (processTemplate.getBpmnXml() == null || processTemplate.getBpmnXml().isEmpty()) {
            logger.warn("更新流程模板时BPMN定义为空");
            return 0;
        }
        
        // 获取租户ID
        Long tenantId = UserUtils.getCurrentTenantId();
        if (tenantId == null) {
            logger.warn("无法获取租户ID");
            return 0;
        }
        processTemplate.setTenantId(tenantId);
        
        // 检查流程标识是否唯一（排除当前记录）
        if (!checkProcessKeyUnique(processTemplate.getProcessKey(), tenantId, processTemplate.getId())) {
            logger.warn("流程标识已存在: {}", processTemplate.getProcessKey());
            return 0;
        }
        
        // 更新时间由MyBatis拦截器自动填充
        
        // 检查版本号并自增
        if (processTemplate.getVersion() == null) {
            // 如果没有传入版本号，从数据库获取
            ProcessTemplate existingTemplate = processTemplateMapper.getById(processTemplate.getId());
            if (existingTemplate != null) {
                processTemplate.setVersion(existingTemplate.getVersion() + 1);
            } else {
                processTemplate.setVersion(1);
            }
        } else {
            processTemplate.setVersion(processTemplate.getVersion() + 1);
        }
        
        logger.info("更新流程模板，ID: {}, 名称: {}, 版本: {}", 
            processTemplate.getId(), processTemplate.getProcessName(), processTemplate.getVersion());
        
        try {
            return processTemplateMapper.update(processTemplate);
        } catch (Exception e) {
            logger.error("更新流程模板失败: {}", e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public int deleteById(Long id) {
        if (id == null || id <= 0) {
            logger.warn("删除流程模板时ID为空或无效: {}", id);
            return 0;
        }
        
        // 获取租户ID
        Long tenantId = UserUtils.getCurrentTenantId();
        if (tenantId == null) {
            logger.warn("无法获取租户ID，无法删除流程模板");
            return 0;
        }
        
        // 验证记录存在并属于当前租户
        ProcessTemplate processTemplate = processTemplateMapper.getById(id);
        if (processTemplate == null) {
            logger.warn("流程模板不存在，ID: {}", id);
            return 0;
        }
        if (!tenantId.equals(processTemplate.getTenantId())) {
            logger.warn("无权限删除该流程模板，ID: {}, 租户ID: {}", id, processTemplate.getTenantId());
            return 0;
        }
        
        logger.info("删除流程模板，ID: {}, 名称: {}", id, processTemplate.getProcessName());
        
        try {
            return processTemplateMapper.deleteById(id);
        } catch (Exception e) {
            logger.error("删除流程模板失败: {}", e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public int deleteByIds(Long[] ids) {
        if (ids == null || ids.length == 0) {
            logger.warn("删除流程模板时ID数组为空");
            return 0;
        }
        
        // 过滤无效ID
        List<Long> validIds = new ArrayList<>();
        for (Long id : ids) {
            if (id != null && id > 0) {
                validIds.add(id);
            } else {
                logger.warn("删除流程模板时包含无效ID: {}", id);
            }
        }
        
        if (validIds.isEmpty()) {
            logger.warn("删除流程模板时没有有效的ID");
            return 0;
        }
        
        // 获取租户ID
        Long tenantId = UserUtils.getCurrentTenantId();
        if (tenantId == null) {
            logger.warn("无法获取租户ID，无法删除流程模板");
            return 0;
        }
        
        // 验证所有记录都属于当前租户
        List<ProcessTemplate> templates = new ArrayList<>();
        for (Long validId : validIds) {
            ProcessTemplate template = processTemplateMapper.getById(validId);
            if (template != null) {
                templates.add(template);
            }
        }
        List<Long> accessibleIds = new ArrayList<>();
        for (ProcessTemplate template : templates) {
            if (template != null && tenantId.equals(template.getTenantId())) {
                accessibleIds.add(template.getId());
            } else {
                logger.warn("无权限删除流程模板，ID: {}", template != null ? template.getId() : "未知");
            }
        }
        
        if (accessibleIds.isEmpty()) {
            logger.warn("没有权限删除任何流程模板");
            return 0;
        }
        
        logger.info("删除流程模板，ID列表: {}, 数量: {}", accessibleIds, accessibleIds.size());
        
        try {
            return processTemplateMapper.deleteByIds(accessibleIds);
        } catch (Exception e) {
            logger.error("批量删除流程模板失败: {}", e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public List<ProcessTemplate> getListByTenantId(Long tenantId) {
        // 获取租户ID（如果传入的租户ID为空）
        Long effectiveTenantId = tenantId;
        if (effectiveTenantId == null) {
            effectiveTenantId = UserUtils.getCurrentTenantId();
            if (effectiveTenantId == null) {
                logger.warn("无法获取租户ID，无法查询流程模板列表");
                return Collections.emptyList();
            }
        }
        
        logger.debug("查询流程模板列表，租户ID: {}", effectiveTenantId);
        
        try {
            return processTemplateMapper.getListByTenantId(effectiveTenantId);
        } catch (Exception e) {
            logger.error("查询流程模板列表失败: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public int publishProcessTemplate(Long id) {
        if (id == null || id <= 0) {
            logger.warn("发布流程模板时ID为空或无效: {}", id);
            return 0;
        }
        
        // 获取租户ID
        Long tenantId = UserUtils.getCurrentTenantId();
        if (tenantId == null) {
            logger.warn("无法获取租户ID，无法发布流程模板");
            return 0;
        }
        
        // 验证记录存在并属于当前租户
        ProcessTemplate processTemplate = processTemplateMapper.getById(id);
        if (processTemplate == null) {
            logger.warn("流程模板不存在，ID: {}", id);
            return 0;
        }
        if (!tenantId.equals(processTemplate.getTenantId())) {
            logger.warn("无权限发布该流程模板，ID: {}, 租户ID: {}", id, processTemplate.getTenantId());
            return 0;
        }
        
        // 检查是否已经发布
        if ("PUBLISHED".equals(processTemplate.getStatus())) {
            logger.warn("流程模板已经处于发布状态，ID: {}", id);
            return 1; // 已经是发布状态，返回成功
        }
        
        // 更新状态和时间
        ProcessTemplate updateTemplate = new ProcessTemplate();
        updateTemplate.setId(id);
        updateTemplate.setStatus("PUBLISHED");
        // updateTime字段由MyBatis拦截器自动填充
        
        logger.info("发布流程模板，ID: {}, 名称: {}", id, processTemplate.getProcessName());
        
        try {
            return processTemplateMapper.update(updateTemplate);
        } catch (Exception e) {
            logger.error("发布流程模板失败: {}", e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public Long copyProcessTemplate(Long id, String newName) {
        if (id == null || id <= 0) {
            logger.warn("复制流程模板时ID为空或无效: {}", id);
            return null;
        }
        
        if (newName == null || newName.isEmpty()) {
            logger.warn("复制流程模板时新名称为空");
            return null;
        }
        
        // 获取租户ID
        Long tenantId = UserUtils.getCurrentTenantId();
        if (tenantId == null) {
            logger.warn("无法获取租户ID，无法复制流程模板");
            return null;
        }
        
        // 验证原流程模板存在并属于当前租户
        ProcessTemplate original = processTemplateMapper.getById(id);
        if (original == null) {
            logger.warn("流程模板不存在，ID: {}", id);
            return null;
        }
        if (!tenantId.equals(original.getTenantId())) {
            logger.warn("无权限复制该流程模板，ID: {}, 租户ID: {}", id, original.getTenantId());
            return null;
        }
        
        // 创建新流程模板
        ProcessTemplate newTemplate = new ProcessTemplate();
        newTemplate.setProcessKey(generateUniqueProcessKey(original.getProcessKey(), tenantId));
        newTemplate.setProcessName(newName);
        newTemplate.setCategory(original.getCategory());
        newTemplate.setDescription(original.getDescription());
        newTemplate.setBpmnXml(original.getBpmnXml());
        newTemplate.setStatus("DRAFT");
        newTemplate.setVersion(1);
        newTemplate.setTenantId(original.getTenantId());
        // createTime和updateTime字段由MyBatis拦截器自动填充
        newTemplate.setCreateBy(original.getCreateBy());
        newTemplate.setFormKey(original.getFormKey());
        
        logger.info("复制流程模板，原ID: {}, 原名称: {}, 新名称: {}", 
            id, original.getProcessName(), newName);
        
        try {
            // 保存新模板
            processTemplateMapper.insert(newTemplate);
            return newTemplate.getId();
        } catch (Exception e) {
            logger.error("复制流程模板失败: {}", e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * 生成唯一的流程标识
     */
    private String generateUniqueProcessKey(String originalKey, Long tenantId) {
        String newKey = originalKey + "_copy";
        int counter = 1;
        
        // 检查是否存在相同的流程标识
        while (!checkProcessKeyUnique(newKey, tenantId, null)) {
            newKey = originalKey + "_copy" + counter;
            counter++;
        }
        
        return newKey;
    }

    @Override
    public boolean checkProcessKeyUnique(String processKey, Long tenantId, Long id) {
        if (processKey == null || processKey.isEmpty()) {
            logger.warn("检查流程标识唯一性时流程标识为空");
            return false;
        }
        
        if (tenantId == null || tenantId <= 0) {
            logger.warn("检查流程标识唯一性时租户ID为空或无效: {}", tenantId);
            return false;
        }
        
        logger.debug("检查流程标识唯一性，processKey: {}, tenantId: {}, id: {}", 
            processKey, tenantId, id);
        
        try {
            ProcessTemplate processTemplate = processTemplateMapper.getByProcessKey(processKey, tenantId);
            if (processTemplate == null) {
                return true;
            }
            // 如果是更新操作，且找到的模板ID与当前要更新的ID相同，则表示唯一
            return id != null && processTemplate.getId().equals(id);
        } catch (Exception e) {
            logger.error("检查流程标识唯一性失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<ProcessTemplate> getListByCategory(String category, Long tenantId) {
        // 获取租户ID（如果传入的租户ID为空）
        Long effectiveTenantId = tenantId;
        if (effectiveTenantId == null) {
            effectiveTenantId = UserUtils.getCurrentTenantId();
            if (effectiveTenantId == null) {
                logger.warn("无法获取租户ID，无法查询流程模板列表");
                return Collections.emptyList();
            }
        }
        
        logger.debug("查询流程模板列表，分类: {}, 租户ID: {}", category, effectiveTenantId);
        
        try {
            return processTemplateMapper.getListByCategory(category, effectiveTenantId);
        } catch (Exception e) {
            logger.error("查询流程模板列表失败: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<ProcessTemplate> getListByFormKey(String formKey, Long tenantId) {
        if (formKey == null || formKey.isEmpty()) {
            logger.warn("查询流程模板列表时表单标识为空");
            return Collections.emptyList();
        }
        
        // 获取租户ID（如果传入的租户ID为空）
        Long effectiveTenantId = tenantId;
        if (effectiveTenantId == null) {
            effectiveTenantId = UserUtils.getCurrentTenantId();
            if (effectiveTenantId == null) {
                logger.warn("无法获取租户ID，无法查询流程模板列表");
                return Collections.emptyList();
            }
        }
        
        logger.debug("查询流程模板列表，表单标识: {}, 租户ID: {}", formKey, effectiveTenantId);
        
        try {
            return processTemplateMapper.getListByFormKey(formKey, effectiveTenantId);
        } catch (Exception e) {
            logger.error("查询流程模板列表失败: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}