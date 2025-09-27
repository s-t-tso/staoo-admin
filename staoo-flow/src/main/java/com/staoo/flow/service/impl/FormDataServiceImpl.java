package com.staoo.flow.service.impl;

import com.staoo.common.util.UserUtils;
import com.staoo.flow.domain.FormData;
import com.staoo.flow.mapper.FormDataMapper;
import com.staoo.flow.service.FormDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 表单数据服务实现类
 * 实现表单数据相关的业务逻辑处理
 */
@Service
public class FormDataServiceImpl implements FormDataService {
    private static final Logger logger = LoggerFactory.getLogger(FormDataServiceImpl.class);
    
    @Autowired
    private FormDataMapper formDataMapper;

    @Override
    public FormData getById(Long id) {
        if (id == null || id <= 0) {
            logger.warn("查询表单数据时ID为空或无效: {}", id);
            return null;
        }
        logger.debug("查询表单数据，ID: {}", id);
        return formDataMapper.getById(id);
    }

    @Override
    public FormData getByBusinessKey(String businessKey, String formKey, Long tenantId) {
        if (businessKey == null || businessKey.isEmpty()) {
            logger.warn("查询表单数据时业务键为空");
            return null;
        }
        if (formKey == null || formKey.isEmpty()) {
            logger.warn("查询表单数据时表单标识为空");
            return null;
        }
        if (tenantId == null || tenantId <= 0) {
            logger.warn("查询表单数据时租户ID为空或无效: {}", tenantId);
            return null;
        }
        logger.debug("根据业务键查询表单数据，businessKey: {}, formKey: {}, tenantId: {}", 
            businessKey, formKey, tenantId);
        return formDataMapper.getByBusinessKey(businessKey, formKey, tenantId);
    }

    @Override
    public List<FormData> getList(FormData formData) {
        if (formData == null) {
            formData = new FormData();
            logger.debug("查询表单数据列表，参数为空，使用默认条件");
        } else {
            // 从当前登录用户获取租户ID（如果未设置）
            if (formData.getTenantId() == null) {
                Long tenantId = UserUtils.getCurrentTenantId();
                if (tenantId != null) {
                    formData.setTenantId(tenantId);
                }
            }
            logger.debug("查询表单数据列表，表单标识: {}, 租户ID: {}", 
                formData.getFormKey(), formData.getTenantId());
        }
        return formDataMapper.getList(formData);
    }

    @Override
    public int save(FormData formData) {
        if (formData == null) {
            logger.warn("保存表单数据时参数为空");
            return 0;
        }
        
        // 验证表单数据
        if (!validateFormData(formData)) {
            logger.warn("表单数据验证失败");
            return 0;
        }
        
        // 设置租户ID（如果未设置）
        if (formData.getTenantId() == null) {
            Long tenantId = UserUtils.getCurrentTenantId();
            if (tenantId != null) {
                formData.setTenantId(tenantId);
            } else {
                logger.warn("无法获取租户ID");
                return 0;
            }
        }
        
        // 设置创建时间
        // 注意：FormData实体类中没有createBy字段，无法设置创建人
        
        formData.setCreateTime(new Date());
        formData.setUpdateTime(new Date());
        
        // 设置默认状态和业务键
        if (formData.getStatus() == null) {
            formData.setStatus("DRAFT");
        }
        if (formData.getBusinessKey() == null || formData.getBusinessKey().isEmpty()) {
            formData.setBusinessKey(UUID.randomUUID().toString().substring(0, 16));
        }
        
        logger.info("保存表单数据，表单标识: {}, 业务键: {}", 
            formData.getFormKey(), formData.getBusinessKey());
        
        try {
            return formDataMapper.insert(formData);
        } catch (Exception e) {
            logger.error("保存表单数据失败: {}", e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public int update(FormData formData) {
        if (formData == null) {
            logger.warn("更新表单数据时参数为空");
            return 0;
        }
        if (formData.getId() == null || formData.getId() <= 0) {
            logger.warn("更新表单数据时ID为空或无效");
            return 0;
        }
        
        // 验证表单数据
        if (!validateFormData(formData)) {
            logger.warn("表单数据验证失败");
            return 0;
        }
        
        // 设置租户ID（如果未设置）
        if (formData.getTenantId() == null) {
            Long tenantId = UserUtils.getCurrentTenantId();
            if (tenantId != null) {
                formData.setTenantId(tenantId);
            } else {
                logger.warn("无法获取租户ID");
                return 0;
            }
        }
        
        // 设置更新时间
        // 注意：FormData实体类中没有updateBy字段，无法设置更新人
        
        formData.setUpdateTime(new Date());
        
        logger.info("更新表单数据，ID: {}, 表单标识: {}", 
            formData.getId(), formData.getFormKey());
        
        try {
            return formDataMapper.update(formData);
        } catch (Exception e) {
            logger.error("更新表单数据失败: {}", e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public int deleteById(Long id) {
        if (id == null || id <= 0) {
            logger.warn("删除表单数据时ID为空或无效: {}", id);
            return 0;
        }
        
        // 获取租户ID
        Long tenantId = UserUtils.getCurrentTenantId();
        if (tenantId == null) {
            logger.warn("无法获取租户ID，无法删除表单数据");
            return 0;
        }
        
        // 检查数据是否存在且属于当前租户
        FormData formData = formDataMapper.getById(id);
        if (formData == null) {
            logger.warn("要删除的表单数据不存在，ID: {}", id);
            return 0;
        }
        
        if (!tenantId.equals(formData.getTenantId())) {
            logger.warn("无权删除其他租户的表单数据，ID: {}, 当前租户ID: {}, 数据租户ID: {}",
                id, tenantId, formData.getTenantId());
            return 0;
        }
        
        logger.info("删除表单数据，ID: {}, 表单标识: {}", id, formData.getFormKey());
        
        try {
            return formDataMapper.deleteById(id);
        } catch (Exception e) {
            logger.error("删除表单数据失败: {}", e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public int deleteByIds(Long[] ids) {
        if (ids == null || ids.length == 0) {
            logger.warn("删除表单数据时ID数组为空");
            return 0;
        }
        
        // 过滤无效ID并转换为List
        List<Long> validIds = Arrays.stream(ids)
            .filter(id -> id != null && id > 0)
            .collect(java.util.stream.Collectors.toList());
        
        if (validIds.isEmpty()) {
            logger.warn("删除表单数据时ID数组中无有效ID");
            return 0;
        }
        
        // 获取租户ID
        Long tenantId = UserUtils.getCurrentTenantId();
        if (tenantId == null) {
            logger.warn("无法获取租户ID，无法删除表单数据");
            return 0;
        }
        
        logger.info("删除表单数据，ID数组大小: {}", validIds.size());
        
        try {
            return formDataMapper.deleteByIds(validIds);
        } catch (Exception e) {
            logger.error("批量删除表单数据失败: {}", e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public List<FormData> getListByFormKey(String formKey, Long tenantId) {
        if (formKey == null || formKey.isEmpty()) {
            logger.warn("根据表单标识查询数据列表时，表单标识为空");
            return Collections.emptyList();
        }
        
        // 获取租户ID（如果传入的租户ID为空）
        Long effectiveTenantId = tenantId;
        if (effectiveTenantId == null) {
            effectiveTenantId = UserUtils.getCurrentTenantId();
            if (effectiveTenantId == null) {
                logger.warn("无法获取租户ID，无法查询表单数据");
                return Collections.emptyList();
            }
        }
        
        logger.debug("根据表单标识查询数据列表，formKey: {}, tenantId: {}", formKey, effectiveTenantId);
        
        try {
            return formDataMapper.getListByFormKey(formKey, effectiveTenantId);
        } catch (Exception e) {
            logger.error("查询表单数据列表失败: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
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

    /**
     * 验证表单数据是否有效
     * 
     * @param formData 表单数据对象
     * @return 是否有效
     */
    @Override
    public boolean validateFormData(FormData formData) {
        if (formData == null) {
            return false;
        }
        if (formData.getFormKey() == null || formData.getFormKey().isEmpty()) {
            logger.warn("表单标识为空");
            return false;
        }
        if (formData.getTenantId() == null || formData.getTenantId() <= 0) {
            logger.warn("租户ID为空或无效");
            return false;
        }
        if (formData.getFormData() == null || formData.getFormData().isEmpty()) {
            logger.warn("表单内容为空");
            return false;
        }
        return true;
    }

    @Override
    public byte[] exportFormData(String formKey, Long tenantId) {
        if (formKey == null || formKey.isEmpty()) {
            logger.warn("导出表单数据时，表单标识为空");
            return new byte[0];
        }
        
        // 获取租户ID（如果传入的租户ID为空）
        Long effectiveTenantId = tenantId;
        if (effectiveTenantId == null) {
            effectiveTenantId = UserUtils.getCurrentTenantId();
            if (effectiveTenantId == null) {
                logger.warn("无法获取租户ID，无法导出表单数据");
                return new byte[0];
            }
        }
        
        logger.info("导出表单数据，formKey: {}, tenantId: {}", formKey, effectiveTenantId);
        
        try {
            // 导出表单数据为JSON格式
            // 1. 查询表单数据
            List<FormData> formDataList = formDataMapper.getListByFormKey(formKey, effectiveTenantId);
            logger.info("表单数据导出完成，共导出 {} 条记录", formDataList != null ? formDataList.size() : 0);
            
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
        } catch (Exception e) {
            logger.error("导出表单数据失败: {}", e.getMessage(), e);
            return new byte[0];
        }
    }
}