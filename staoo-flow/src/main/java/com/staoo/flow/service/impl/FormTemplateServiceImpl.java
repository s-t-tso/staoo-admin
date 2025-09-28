package com.staoo.flow.service.impl;

import com.staoo.common.util.UserUtils;
import com.staoo.flow.domain.FormTemplate;
import com.staoo.flow.mapper.FormTemplateMapper;
import com.staoo.flow.service.FormTemplateService;
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
import java.util.stream.Collectors;

/**
 * 表单模板服务实现类
 * 实现表单模板相关的业务逻辑处理
 */
@Service
public class FormTemplateServiceImpl implements FormTemplateService {
    private static final Logger logger = LoggerFactory.getLogger(FormTemplateServiceImpl.class);
    
    @Autowired
    private FormTemplateMapper formTemplateMapper;

    @Override
    public FormTemplate getById(Long id) {
        logger.debug("查询表单模板，ID: {}", id);
        try {
            return formTemplateMapper.getById(id);
        } catch (Exception e) {
            logger.error("查询表单模板失败: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public FormTemplate getByFormKey(String formKey, Long tenantId) {
        // 获取租户ID（如果传入的租户ID为空）
        Long effectiveTenantId = tenantId;
        if (effectiveTenantId == null) {
            effectiveTenantId = UserUtils.getCurrentTenantId();
            if (effectiveTenantId == null) {
                logger.warn("无法获取租户ID，无法查询表单模板");
                return null;
            }
        }
        
        logger.debug("查询表单模板，formKey: {}, tenantId: {}", formKey, effectiveTenantId);
        
        try {
            return formTemplateMapper.getByFormKey(formKey, effectiveTenantId);
        } catch (Exception e) {
            logger.error("查询表单模板失败: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<FormTemplate> getList(FormTemplate formTemplate) {
        if (formTemplate == null) {
            formTemplate = new FormTemplate();
            logger.debug("查询表单模板列表，参数为空，使用默认条件");
        } else {
            // 从当前登录用户获取租户ID（如果未设置）
            if (formTemplate.getTenantId() == null) {
                Long tenantId = UserUtils.getCurrentTenantId();
                if (tenantId != null) {
                    formTemplate.setTenantId(tenantId);
                }
            }
            logger.debug("查询表单模板列表，表单标识: {}, 状态: {}, 租户ID: {}", 
                formTemplate.getFormKey(), formTemplate.getStatus(), 
                formTemplate.getTenantId());
        }
        
        try {
            return formTemplateMapper.getList(formTemplate);
        } catch (Exception e) {
            logger.error("查询表单模板列表失败: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public int save(FormTemplate formTemplate) {
        // 设置租户ID（如果未设置）
        if (formTemplate.getTenantId() == null) {
            Long tenantId = UserUtils.getCurrentTenantId();
            if (tenantId != null) {
                formTemplate.setTenantId(tenantId);
            } else {
                logger.warn("无法获取租户ID");
                return 0;
            }
        }
        
        // 设置默认状态
        if (formTemplate.getStatus() == null) {
            formTemplate.setStatus("DRAFT");
        }
        
        logger.info("保存表单模板，名称: {}, 标识: {}", formTemplate.getFormName(), formTemplate.getFormKey());
        
        try {
            return formTemplateMapper.insert(formTemplate);
        } catch (Exception e) {
            logger.error("保存表单模板失败: {}", e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public int update(FormTemplate formTemplate) {
        // 获取租户ID
        Long tenantId = UserUtils.getCurrentTenantId();
        if (tenantId != null) {
            formTemplate.setTenantId(tenantId);
        }
        
        logger.info("更新表单模板，ID: {}, 名称: {}, 标识: {}", 
            formTemplate.getId(), formTemplate.getFormName(), formTemplate.getFormKey());
        
        try {
            return formTemplateMapper.update(formTemplate);
        } catch (Exception e) {
            logger.error("更新表单模板失败: {}", e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public int deleteById(Long id) {
        if (id == null || id <= 0) {
            logger.warn("删除表单模板时ID为空或无效: {}", id);
            return 0;
        }
        
        // 获取当前租户ID
        Long tenantId = UserUtils.getCurrentTenantId();
        if (tenantId == null) {
            logger.warn("无法获取租户ID，无法删除表单模板");
            return 0;
        }
        
        // 检查记录是否存在并属于当前租户
        FormTemplate formTemplate = formTemplateMapper.getById(id);
        if (formTemplate == null) {
            logger.warn("表单模板不存在，ID: {}", id);
            return 0;
        }
        if (!tenantId.equals(formTemplate.getTenantId())) {
            logger.warn("无权限删除其他租户的表单模板，ID: {}", id);
            return 0;
        }
        
        logger.info("删除表单模板，ID: {}, 名称: {}", id, formTemplate.getFormName());
        
        try {
            return formTemplateMapper.deleteById(id);
        } catch (Exception e) {
            logger.error("删除表单模板失败: {}", e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public int deleteByIds(Long[] ids) {
        if (ids == null || ids.length == 0) {
            logger.warn("删除表单模板时ID列表为空");
            return 0;
        }
        
        // 验证ID列表中的每个ID是否有效
        for (Long id : ids) {
            if (id == null || id <= 0) {
                logger.warn("删除表单模板时ID列表中包含无效ID");
                return 0;
            }
        }
        
        // 获取当前租户ID
        Long tenantId = UserUtils.getCurrentTenantId();
        if (tenantId == null) {
            logger.warn("无法获取租户ID，无法删除表单模板");
            return 0;
        }
        
        logger.info("批量删除表单模板，ID列表: {}", Arrays.toString(ids));
        
        try {
            return formTemplateMapper.deleteByIds(Arrays.asList(ids));
        } catch (Exception e) {
            logger.error("批量删除表单模板失败: {}", e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public List<FormTemplate> getListByTenantId(Long tenantId) {
        // 获取租户ID（如果传入的租户ID为空）
        Long effectiveTenantId = tenantId;
        if (effectiveTenantId == null) {
            effectiveTenantId = UserUtils.getCurrentTenantId();
            if (effectiveTenantId == null) {
                logger.warn("无法获取租户ID，无法查询表单模板列表");
                return Collections.emptyList();
            }
        }
        
        logger.debug("按租户ID查询表单模板列表，租户ID: {}", effectiveTenantId);
        
        try {
            return formTemplateMapper.getListByTenantId(effectiveTenantId);
        } catch (Exception e) {
            logger.error("按租户ID查询表单模板列表失败: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public int publishFormTemplate(Long id) {
        // 使用验证策略链进行参数验证
        ValidationChain validationChain = new ValidationChain();
        validationChain.addValidator(new IdValidator())
                      .addValidator(new TenantValidator())
                      .addValidator(new TemplateExistsValidator());
                       
        ValidationContext context = new ValidationContext(id, null);
        if (!validationChain.validate(context)) {
            return 0;
        }
        
        logger.info("发布表单模板，ID: {}, 名称: {}", id, context.getOriginalTemplate().getFormName());
        
        try {
            return formTemplateMapper.updateStatus(id, "PUBLISHED");
        } catch (Exception e) {
            logger.error("发布表单模板失败: {}", e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public Long copyFormTemplate(Long id, String newName) {
        // 使用验证策略链进行参数验证
        ValidationChain validationChain = new ValidationChain();
        validationChain.addValidator(new IdValidator())
                      .addValidator(new NameValidator())
                      .addValidator(new TenantValidator())
                      .addValidator(new TemplateExistsValidator());
                       
        ValidationContext context = new ValidationContext(id, newName);
        if (!validationChain.validate(context)) {
            return null;
        }
        
        logger.info("复制表单模板，ID: {}, 名称: {}, 新名称: {}", id, context.getOriginalTemplate().getFormName(), newName);
        
        try {
            // 创建新的表单模板
            FormTemplate newTemplate = new FormTemplate();
            
            // 生成唯一的表单标识
            String newFormKey = generateUniqueFormKey(context.getOriginalTemplate().getFormKey(), context.getTenantId());
            newTemplate.setFormKey(newFormKey);
            
            newTemplate.setFormName(newName);
            newTemplate.setDescription(context.getOriginalTemplate().getDescription());
            newTemplate.setFormConfig(context.getOriginalTemplate().getFormConfig());
            newTemplate.setStatus("DRAFT");
            newTemplate.setVersion(1);
            newTemplate.setTenantId(context.getOriginalTemplate().getTenantId());
            // createTime和updateTime字段由MyBatis拦截器自动填充
            newTemplate.setCreateBy(context.getOriginalTemplate().getCreateBy());
            
            logger.info("已创建表单模板副本，新名称: {}, 新标识: {}", 
                newTemplate.getFormName(), newTemplate.getFormKey());
            
            // 保存新模板
            formTemplateMapper.insert(newTemplate);
            return newTemplate.getId();
        } catch (Exception e) {
            logger.error("复制表单模板失败: {}", e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * 生成唯一的表单标识
     * @param originalFormKey 原始表单标识
     * @param tenantId 租户ID
     * @return 唯一的表单标识
     */
    private String generateUniqueFormKey(String originalFormKey, Long tenantId) {
        String newFormKey = originalFormKey + "_copy";
        int counter = 1;
        
        // 检查是否已存在相同的表单标识，如果存在则添加数字后缀
        while (!checkFormKeyUnique(newFormKey, tenantId, null)) {
            newFormKey = originalFormKey + "_copy" + counter;
            counter++;
        }
        
        return newFormKey;
    }

    @Override
    public boolean checkFormKeyUnique(String formKey, Long tenantId, Long id) {
        if (formKey == null || formKey.isEmpty()) {
            logger.warn("检查表单标识唯一性时表单标识为空");
            return false;
        }
        if (tenantId == null || tenantId <= 0) {
            logger.warn("检查表单标识唯一性时租户ID为空或无效");
            return false;
        }
        
        logger.debug("检查表单标识唯一性，表单标识: {}, 租户ID: {}, 当前ID: {}", 
            formKey, tenantId, id);
        
        try {
            FormTemplate formTemplate = formTemplateMapper.getByFormKey(formKey, tenantId);
            if (formTemplate == null) {
                logger.debug("表单标识可用: {}", formKey);
                return true;
            }
            
            boolean isUnique = id != null && formTemplate.getId().equals(id);
            if (!isUnique) {
                logger.warn("表单标识已存在: {}", formKey);
            }
            
            return isUnique;
        } catch (Exception e) {
            logger.error("检查表单标识唯一性失败: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 验证上下文类，用于在验证链中传递数据
     */
    private class ValidationContext {
        private Long id;
        private String newName;
        private Long tenantId;
        private FormTemplate originalTemplate;
        
        public ValidationContext(Long id, String newName) {
            this.id = id;
            this.newName = newName;
        }
        
        public Long getId() { return id; }
        public String getNewName() { return newName; }
        public Long getTenantId() { return tenantId; }
        public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
        public FormTemplate getOriginalTemplate() { return originalTemplate; }
        public void setOriginalTemplate(FormTemplate originalTemplate) { this.originalTemplate = originalTemplate; }
    }
    
    /**
     * 验证器接口
     */
    private interface Validator {
        boolean validate(ValidationContext context);
    }
    
    /**
     * ID验证器
     */
    private class IdValidator implements Validator {
        @Override
        public boolean validate(ValidationContext context) {
            if (context.getId() == null || context.getId() <= 0) {
                logger.warn("复制表单模板时ID为空或无效: {}", context.getId());
                return false;
            }
            return true;
        }
    }
    
    /**
     * 名称验证器
     */
    private class NameValidator implements Validator {
        @Override
        public boolean validate(ValidationContext context) {
            if (context.getNewName() == null || context.getNewName().isEmpty()) {
                logger.warn("复制表单模板时新名称为空");
                return false;
            }
            return true;
        }
    }
    
    /**
     * 租户验证器
     */
    private class TenantValidator implements Validator {
        @Override
        public boolean validate(ValidationContext context) {
            Long tenantId = UserUtils.getCurrentTenantId();
            if (tenantId == null) {
                logger.warn("无法获取租户ID，无法复制表单模板");
                return false;
            }
            context.setTenantId(tenantId);
            return true;
        }
    }
    
    /**
     * 模板存在验证器
     */
    private class TemplateExistsValidator implements Validator {
        @Override
        public boolean validate(ValidationContext context) {
            FormTemplate original = formTemplateMapper.getById(context.getId());
            if (original == null) {
                logger.warn("表单模板不存在，ID: {}", context.getId());
                return false;
            }
            if (!context.getTenantId().equals(original.getTenantId())) {
                logger.warn("无权限复制其他租户的表单模板，ID: {}", context.getId());
                return false;
            }
            context.setOriginalTemplate(original);
            return true;
        }
    }
    
    /**
     * 验证链类，用于组合多个验证器
     */
    private class ValidationChain {
        private List<Validator> validators = new ArrayList<>();
        
        public ValidationChain addValidator(Validator validator) {
            validators.add(validator);
            return this;
        }
        
        public boolean validate(ValidationContext context) {
            for (Validator validator : validators) {
                if (!validator.validate(context)) {
                    return false;
                }
            }
            return true;
        }
    }
}