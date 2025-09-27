package com.staoo.system.service.impl;

import com.staoo.system.domain.UserDept;
import com.staoo.system.mapper.UserDeptMapper;
import com.staoo.system.service.IUserDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户部门关联服务实现类
 * 实现用户部门关联相关的业务逻辑
 */
@Service
public class UserDeptServiceImpl implements IUserDeptService {

    @Autowired
    private UserDeptMapper userDeptMapper;

    @Override
    public List<Long> getDeptIdsByUserId(Long userId) {
        return userDeptMapper.getDeptIdsByUserId(userId);
    }

    @Override
    public List<Long> getUserIdsByDeptId(Long deptId) {
        return userDeptMapper.getUserIdsByDeptId(deptId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUserDept(Long userId, Long deptId) {
        // 检查关联是否已存在
        if (userDeptMapper.existsByUserIdAndDeptId(userId, deptId) != null && 
            userDeptMapper.existsByUserIdAndDeptId(userId, deptId)) {
            return true; // 已存在关联，直接返回成功
        }

        // 创建新关联
        UserDept userDept = new UserDept();
        userDept.setUserId(userId);
        userDept.setDeptId(deptId);
        // createTime和updateTime字段由MyBatis拦截器自动填充

        return userDeptMapper.insert(userDept) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSaveUserDept(Long userId, List<Long> deptIds) {
        if (deptIds == null || deptIds.isEmpty()) {
            return true;
        }

        List<UserDept> userDepts = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (Long deptId : deptIds) {
            // 检查关联是否已存在
            if (userDeptMapper.existsByUserIdAndDeptId(userId, deptId) == null || 
                !userDeptMapper.existsByUserIdAndDeptId(userId, deptId)) {
                UserDept userDept = new UserDept();
                userDept.setUserId(userId);
                userDept.setDeptId(deptId);
                // createTime和updateTime字段由MyBatis拦截器自动填充
                userDepts.add(userDept);
            }
        }

        if (userDepts.isEmpty()) {
            return true; // 所有关联都已存在
        }

        return userDeptMapper.batchInsert(userDepts) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserDept(Long userId, List<Long> deptIds) {
        // 先删除旧关联
        userDeptMapper.deleteByUserId(userId);
        
        // 如果没有新部门，直接返回成功
        if (deptIds == null || deptIds.isEmpty()) {
            return true;
        }
        
        // 添加新关联
        return batchSaveUserDept(userId, deptIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUserDept(Long userId, Long deptId) {
        return userDeptMapper.deleteByUserIdAndDeptId(userId, deptId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByUserId(Long userId) {
        return userDeptMapper.deleteByUserId(userId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByDeptId(Long deptId) {
        return userDeptMapper.deleteByDeptId(deptId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteByUserIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return true;
        }
        return userDeptMapper.batchDeleteByUserIds(userIds) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteByDeptIds(List<Long> deptIds) {
        if (deptIds == null || deptIds.isEmpty()) {
            return true;
        }
        return userDeptMapper.batchDeleteByDeptIds(deptIds) > 0;
    }

    @Override
    public boolean checkUserBelongsToDept(Long userId, Long deptId) {
        return userDeptMapper.existsByUserIdAndDeptId(userId, deptId) != null && 
               userDeptMapper.existsByUserIdAndDeptId(userId, deptId);
    }

    @Override
    public Long getPrimaryDeptId(Long userId) {
        List<Long> deptIds = getDeptIdsByUserId(userId);
        // 默认返回第一个部门ID作为主要部门
        return deptIds != null && !deptIds.isEmpty() ? deptIds.get(0) : null;
    }
}