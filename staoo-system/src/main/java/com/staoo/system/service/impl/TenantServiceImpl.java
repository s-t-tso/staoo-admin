package com.staoo.system.service.impl;

import com.staoo.common.domain.TableResult;
import com.staoo.common.domain.PageQuery;
import com.staoo.common.enums.StatusCodeEnum;
import com.staoo.common.exception.BusinessException;
import com.staoo.system.domain.Tenant;
import com.staoo.system.mapper.TenantMapper;
import com.staoo.system.service.TenantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 租户服务实现类
 */
@Service
public class TenantServiceImpl implements TenantService {

    private static final Logger logger = LoggerFactory.getLogger(TenantServiceImpl.class);

    @Autowired
    private TenantMapper tenantMapper;

    @Override
    public Tenant getById(Long id) {
        if (id == null) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "租户ID不能为空");
        }
        return tenantMapper.getById(id);
    }

    @Override
    public Tenant getByTenantName(String tenantName) {
        if (tenantName == null || tenantName.isEmpty()) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "租户名称不能为空");
        }
        return tenantMapper.getByTenantName(tenantName);
    }

    @Override
    public List<Tenant> getList(Tenant tenant) {
        if (tenant == null) {
            tenant = new Tenant();
        }
        return tenantMapper.getList(tenant);
    }

    @Override
    public TableResult<Tenant> getPage(PageQuery pageQuery) {
        if (pageQuery == null) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "分页查询参数不能为空");
        }
        List<Tenant> list = tenantMapper.getPage(pageQuery);
        int total = tenantMapper.count(new Tenant());
        return TableResult.build((long) total, pageQuery.getPageNum(), pageQuery.getPageSize(), list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(Tenant tenant) {
        if (tenant == null) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "租户信息不能为空");
        }
        
        // 校验租户名称是否已存在
        Tenant existingTenant = tenantMapper.getByTenantName(tenant.getTenantName());
        if (existingTenant != null) {
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR, "租户名称已存在");
        }
        
        // 设置创建和更新时间
        LocalDateTime now = LocalDateTime.now();
        tenant.setCreateTime(now);
        tenant.setUpdateTime(now);
        
        return tenantMapper.insert(tenant);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(Tenant tenant) {
        if (tenant == null || tenant.getId() == null) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "租户ID不能为空");
        }
        
        // 校验租户是否存在
        Tenant existingTenant = tenantMapper.getById(tenant.getId());
        if (existingTenant == null) {
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR, "租户不存在");
        }
        
        // 校验租户名称是否已存在（排除当前租户）
        if (tenant.getTenantName() != null && !tenant.getTenantName().equals(existingTenant.getTenantName())) {
            Tenant nameTenant = tenantMapper.getByTenantName(tenant.getTenantName());
            if (nameTenant != null && !nameTenant.getId().equals(tenant.getId())) {
                throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR, "租户名称已存在");
            }
        }
        
        // 设置更新时间
        tenant.setUpdateTime(LocalDateTime.now());
        
        return tenantMapper.update(tenant);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long id) {
        if (id == null) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "租户ID不能为空");
        }
        
        // 校验租户是否存在
        Tenant existingTenant = tenantMapper.getById(id);
        if (existingTenant == null) {
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR, "租户不存在");
        }
        
        // 租户删除逻辑可以根据实际需求添加更多校验，例如租户下是否有用户等
        
        return tenantMapper.delete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteBatch(Long[] ids) {
        if (ids == null || ids.length == 0) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "租户ID集合不能为空");
        }
        
        // 校验租户是否存在
        for (Long id : ids) {
            Tenant existingTenant = tenantMapper.getById(id);
            if (existingTenant == null) {
                throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR, "租户ID为" + id + "的租户不存在");
            }
        }
        
        return tenantMapper.deleteBatch(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int changeStatus(Long id, Integer status) {
        if (id == null) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "租户ID不能为空");
        }
        if (status == null || (status != 0 && status != 1)) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "状态值必须为0或1");
        }
        
        // 校验租户是否存在
        Tenant existingTenant = tenantMapper.getById(id);
        if (existingTenant == null) {
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR, "租户不存在");
        }
        
        Tenant tenant = new Tenant();
        tenant.setId(id);
        tenant.setStatus(status);
        tenant.setUpdateTime(LocalDateTime.now());
        
        return tenantMapper.update(tenant);
    }
}