package com.staoo.system.service.impl;

import com.staoo.system.domain.Department;
import com.staoo.system.mapper.DepartmentMapper;
import com.staoo.system.service.IDepartmentService;
import com.staoo.system.service.IUserDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 部门服务实现类
 * 实现部门管理相关的业务逻辑
 */
@Service
public class DepartmentServiceImpl implements IDepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private IUserDeptService userDeptService;

    @Override
    public List<Department> getDepartmentList(Department department) {
        // 调用departmentMapper查询数据库获取部门列表
        List<Department> departments = departmentMapper.getList(department);
        return departments != null ? departments : new ArrayList<>();
    }

    @Override
    public Department getDepartmentById(Long deptId) {
        // 调用departmentMapper查询数据库获取部门信息
        Department department = departmentMapper.getById(deptId);
        
        // 查询不到部门时返回null
        return department;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createDepartment(Department department) {
        // 设置创建时间和更新时间
        department.setCreateTime(LocalDateTime.now());
        department.setUpdateTime(LocalDateTime.now());
        
        // 调用departmentMapper插入数据
        int rows = departmentMapper.insert(department);
        return rows > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDepartment(Department department) {
        // 设置更新时间
        department.setUpdateTime(LocalDateTime.now());
        
        // 调用departmentMapper更新数据
        int rows = departmentMapper.update(department);
        return rows > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDepartment(Long deptId) {
        // 调用departmentMapper删除数据
        int rows = departmentMapper.deleteById(deptId);
        
        // 删除部门的同时，删除用户部门关联
        if (rows > 0) {
            userDeptService.deleteByDeptId(deptId);
        }
        return rows > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteDepartments(List<Long> deptIds) {
        if (deptIds == null || deptIds.isEmpty()) {
            return true;
        }
        
        // 调用departmentMapper批量删除数据
        int rows = departmentMapper.batchDeleteByIds(deptIds);
        
        // 批量删除部门的同时，批量删除用户部门关联
        if (rows > 0) {
            userDeptService.batchDeleteByDeptIds(deptIds);
        }
        return rows > 0;
    }

    @Override
    public List<Department> getDepartmentTree() {
        // 调用departmentMapper获取部门树形结构
        return departmentMapper.getDepartmentTree();
    }

    @Override
    public List<Department> getChildrenDepartments(Long deptId) {
        // 调用departmentMapper查询子部门
        List<Department> departments = departmentMapper.getChildrenDepartments(deptId);
        return departments != null ? departments : new ArrayList<>();
    }

    @Override
    public List<Long> getDepartmentUserIds(Long deptId) {
        // 使用用户部门关联服务获取部门下的用户ID列表
        return userDeptService.getUserIdsByDeptId(deptId);
    }

    @Override
    public boolean hasChildrenDepartments(Long deptId) {
        // 调用departmentMapper查询是否存在子部门
        int count = departmentMapper.countChildren(deptId);
        return count > 0;
    }

    @Override
    public boolean checkDepartmentNameUnique(Department department) {
        // 调用departmentMapper检查部门名称是否唯一
        int count = departmentMapper.checkDeptNameUnique(department.getDeptName(), department.getParentId());
        return count == 0;
    }
}