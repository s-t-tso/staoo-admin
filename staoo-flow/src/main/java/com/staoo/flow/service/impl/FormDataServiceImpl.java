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
        logger.debug("查询表单数据，ID: {}", id);
        return formDataMapper.getById(id);
    }

    @Override
    public FormData getByBusinessKey(String businessKey, String formKey, Long tenantId) {
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
            logger.debug("查询表单数据列表，表单标识: {}, 租户ID: {}", 
                formData.getFormKey(), formData.getTenantId());
        }
        return formDataMapper.getList(formData);
    }

    @Override
    public int save(FormData formData) {
        // 设置创建时间
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
        // 设置更新时间
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
        logger.info("删除表单数据，ID: {}", id);
        
        try {
            return formDataMapper.deleteById(id);
        } catch (Exception e) {
            logger.error("删除表单数据失败: {}", e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public int deleteByIds(Long[] ids) {
        logger.info("删除表单数据，ID数组大小: {}", ids.length);
        
        try {
            return formDataMapper.deleteByIds(Arrays.asList(ids));
        } catch (Exception e) {
            logger.error("批量删除表单数据失败: {}", e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public List<FormData> getListByFormKey(String formKey, Long tenantId) {
        logger.debug("根据表单标识查询数据列表，formKey: {}, tenantId: {}", formKey, tenantId);
        
        try {
            return formDataMapper.getListByFormKey(formKey, tenantId);
        } catch (Exception e) {
            logger.error("查询表单数据列表失败: {}", e.getMessage(), e);
            return List.of();
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

    @Override
    public byte[] exportFormData(String formKey, Long tenantId) {
        logger.info("导出表单数据，formKey: {}, tenantId: {}", formKey, tenantId);
        
        try {
            // 导出表单数据为JSON格式
            // 1. 查询表单数据
            List<FormData> formDataList = formDataMapper.getListByFormKey(formKey, tenantId);
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
    
    @Override
    public boolean validateFormData(FormData formData) {
        // 验证逻辑已在request层处理，此处保持空实现
        return true;
    }
}