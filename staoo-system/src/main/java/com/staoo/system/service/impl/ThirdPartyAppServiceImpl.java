package com.staoo.system.service.impl;

import com.staoo.common.domain.TableResult;
import com.staoo.common.domain.PageQuery;
import com.staoo.system.domain.ThirdPartyApp;
import com.staoo.system.mapper.ThirdPartyAppMapper;
import com.staoo.system.service.ThirdPartyAppService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 第三方应用Service实现类
 */
@Service
public class ThirdPartyAppServiceImpl implements ThirdPartyAppService {

    @Autowired
    private ThirdPartyAppMapper thirdPartyAppMapper;

    @Override
    public ThirdPartyApp getById(Long id) {
        ThirdPartyApp app = thirdPartyAppMapper.getById(id);
        if (app != null) {
            // 加载关联数据
            loadRelatedData(app);
        }
        return app;
    }

    @Override
    public ThirdPartyApp getByAppKey(String appKey) {
        ThirdPartyApp app = thirdPartyAppMapper.getByAppKey(appKey);
        if (app != null) {
            // 加载关联数据
            loadRelatedData(app);
        }
        return app;
    }

    @Override
    public List<ThirdPartyApp> getList(ThirdPartyApp app) {
        List<ThirdPartyApp> apps = thirdPartyAppMapper.getList(app);
        for (ThirdPartyApp thirdPartyApp : apps) {
            loadRelatedData(thirdPartyApp);
        }
        return apps;
    }

    @Override
    public TableResult<ThirdPartyApp> getPage(ThirdPartyApp app, PageQuery pageQuery) {
        Long total = thirdPartyAppMapper.getCount(app);
        if (total == 0) {
            return TableResult.empty();
        }
        List<ThirdPartyApp> list = thirdPartyAppMapper.getPage(app, (long)pageQuery.getStartIndex(), (long)pageQuery.getPageSize());
        for (ThirdPartyApp thirdPartyApp : list) {
            loadRelatedData(thirdPartyApp);
        }
        return TableResult.build(total, pageQuery.getPageNum(), pageQuery.getPageSize(), list);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long save(ThirdPartyApp app) {
        // 生成应用密钥
        if (app.getAppSecret() == null || app.getAppSecret().isEmpty()) {
            app.setAppSecret(generateAppSecret());
        } else {
            // 加密存储密钥
            app.setAppSecret(md5Encode(app.getAppSecret()));
        }
        // 生成应用标识
        if (app.getAppKey() == null || app.getAppKey().isEmpty()) {
            app.setAppKey(generateAppKey());
        }
        app.setCreateTime(new Date());
        app.setUpdateTime(new Date());
        thirdPartyAppMapper.insert(app);
        
        // 保存关联数据
        saveRelatedData(app);
        return app.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int update(ThirdPartyApp app) {
        app.setUpdateTime(new Date());
        int result = thirdPartyAppMapper.update(app);
        
        // 更新关联数据
        saveRelatedData(app);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteById(Long id) {
        // 删除关联数据
        thirdPartyAppMapper.deleteCallbackUrls(id);
        thirdPartyAppMapper.deletePermissions(id);
        thirdPartyAppMapper.deleteTenantRelations(id);
        return thirdPartyAppMapper.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteByIds(List<Long> ids) {
        for (Long id : ids) {
            // 删除关联数据
            thirdPartyAppMapper.deleteCallbackUrls(id);
            thirdPartyAppMapper.deletePermissions(id);
            thirdPartyAppMapper.deleteTenantRelations(id);
        }
        return thirdPartyAppMapper.deleteByIds(ids);
    }

    @Override
    public String generateAppSecret() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    
    /**
     * MD5加密方法
     */
    private String md5Encode(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(str.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5加密失败", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String resetAppSecret(Long id) {
        String newSecret = generateAppSecret();
        ThirdPartyApp app = new ThirdPartyApp();
        app.setId(id);
        app.setAppSecret(md5Encode(newSecret));
        app.setUpdateTime(new Date());
        thirdPartyAppMapper.update(app);
        return newSecret; // 返回未加密的密钥
    }

    @Override
    public int changeStatus(Long id, String status) {
        ThirdPartyApp app = new ThirdPartyApp();
        app.setId(id);
        app.setStatus(status);
        app.setUpdateTime(new Date());
        return thirdPartyAppMapper.update(app);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void configurePermissions(Long appId, List<String> permissions) {
        // 先删除旧的权限
        thirdPartyAppMapper.deletePermissions(appId);
        // 添加新的权限
        if (!CollectionUtils.isEmpty(permissions)) {
            for (String permission : permissions) {
                thirdPartyAppMapper.insertPermission(appId, permission);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void configureCallbackUrls(Long appId, List<String> callbackUrls) {
        // 先删除旧的回调地址
        thirdPartyAppMapper.deleteCallbackUrls(appId);
        // 添加新的回调地址
        if (!CollectionUtils.isEmpty(callbackUrls)) {
            for (String callbackUrl : callbackUrls) {
                thirdPartyAppMapper.insertCallbackUrl(appId, callbackUrl);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void configureTenantAccess(Long appId, List<Long> tenantIds) {
        // 先删除旧的租户关系
        thirdPartyAppMapper.deleteTenantRelations(appId);
        // 添加新的租户关系
        if (!CollectionUtils.isEmpty(tenantIds)) {
            for (Long tenantId : tenantIds) {
                thirdPartyAppMapper.insertTenantRelation(appId, tenantId);
            }
        }
    }

    @Override
    public boolean checkPermission(String appKey, String permission) {
        ThirdPartyApp app = getByAppKey(appKey);
        if (app == null || !"0".equals(app.getStatus())) {
            return false;
        }
        List<String> permissions = app.getPermissions();
        return permissions != null && permissions.contains(permission);
    }

    @Override
    public boolean checkTenantAccess(String appKey, Long tenantId) {
        ThirdPartyApp app = getByAppKey(appKey);
        if (app == null || !"0".equals(app.getStatus())) {
            return false;
        }
        List<Long> tenantIds = app.getTenantIds();
        // 如果租户ID列表为空，则表示可以访问所有租户
        return CollectionUtils.isEmpty(tenantIds) || tenantIds.contains(tenantId);
    }

    /**
     * 加载关联数据
     */
    private void loadRelatedData(ThirdPartyApp app) {
        if (app != null) {
            app.setCallbackUrls(thirdPartyAppMapper.getCallbackUrls(app.getId()));
            app.setPermissions(thirdPartyAppMapper.getPermissions(app.getId()));
            app.setTenantIds(thirdPartyAppMapper.getTenantIds(app.getId()));
        }
    }

    /**
     * 保存关联数据
     */
    private void saveRelatedData(ThirdPartyApp app) {
        if (app != null && app.getId() != null) {
            Long appId = app.getId();
            
            // 保存回调地址
            if (app.getCallbackUrls() != null) {
                configureCallbackUrls(appId, app.getCallbackUrls());
            }
            
            // 保存权限
            if (app.getPermissions() != null) {
                configurePermissions(appId, app.getPermissions());
            }
            
            // 保存租户关系
            if (app.getTenantIds() != null) {
                configureTenantAccess(appId, app.getTenantIds());
            }
        }
    }

    /**
     * 生成应用标识
     */
    private String generateAppKey() {
        return "app_" + UUID.randomUUID().toString().substring(0, 16);
    }
}