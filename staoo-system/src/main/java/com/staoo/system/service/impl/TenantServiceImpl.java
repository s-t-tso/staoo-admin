package com.staoo.system.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.staoo.common.domain.TableResult;
import com.staoo.common.domain.PageQuery;
import com.staoo.common.enums.StatusCodeEnum;
import com.staoo.common.exception.BusinessException;
import com.staoo.system.domain.Tenant;
import com.staoo.system.mapper.TenantMapper;
import com.staoo.system.pojo.request.TenantQueryRequest;
import com.staoo.system.service.TenantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
        return tenantMapper.getById(id);
    }

    @Override
    public Tenant getByTenantName(String tenantName) {
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
    public List<Tenant> getList(TenantQueryRequest request) {
        return tenantMapper.getListByRequest(request);
    }

    @Override
    public TableResult<Tenant> getPage(TenantQueryRequest request) {
        try {
            // 设置分页参数
            PageHelper.startPage(request.getPageNum(), request.getPageSize());
            
            // 直接调用getList方法获取数据
            List<Tenant> list = getList(request);
            Page<Tenant> pageList = (Page<Tenant>) list;
            
            // 构建返回结果
            return TableResult.build(pageList.getTotal(), request.getPageNum(), request.getPageSize(), list);
        } catch (Exception e) {
            logger.error("分页查询租户失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(Tenant tenant) {
        // 校验租户名称是否已存在
        Tenant existingTenant = tenantMapper.getByTenantName(tenant.getTenantName());
        if (existingTenant != null) {
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR, "租户名称已存在");
        }
        
        // 注意：createTime和updateTime字段将由MyBatis拦截器自动填充
        
        return tenantMapper.insert(tenant);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(Tenant tenant) {
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
        
        // 注意：updateTime字段将由MyBatis拦截器自动填充
        
        return tenantMapper.update(tenant);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long id) {
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
        // 校验租户是否存在
        Tenant existingTenant = tenantMapper.getById(id);
        if (existingTenant == null) {
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR, "租户不存在");
        }
        
        Tenant tenant = new Tenant();
        tenant.setId(id);
        tenant.setStatus(status);
        // 注意：updateTime字段将由MyBatis拦截器自动填充
        
        return tenantMapper.update(tenant);
    }
}