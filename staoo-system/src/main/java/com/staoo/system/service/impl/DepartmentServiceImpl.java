package com.staoo.system.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.staoo.common.enums.StatusCodeEnum;
import com.staoo.common.exception.BusinessException;
import com.staoo.common.util.TreeUtils;
import com.staoo.system.domain.Department;
import com.staoo.system.mapper.DepartmentMapper;
import com.staoo.system.pojo.request.DepartmentQueryRequest;
import com.staoo.system.service.IDepartmentService;
import com.staoo.system.service.IUserDeptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 部门服务实现类
 * 实现部门管理相关的业务逻辑
 */
@Service
public class DepartmentServiceImpl implements IDepartmentService {
    private static final Logger logger = LoggerFactory.getLogger(DepartmentServiceImpl.class);

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
    public List<Department> getList(DepartmentQueryRequest request) {
        // 参数校验
        if (request == null) {
            return new ArrayList<>();
        }
        
        // 调用departmentMapper查询数据库获取部门列表
        List<Department> departments = departmentMapper.getListByRequest(request);
        return departments != null ? departments : new ArrayList<>();
    }

    @Override
    public Page<Department> getPage(DepartmentQueryRequest request) {
        try {
            // 设置分页参数
            PageHelper.startPage(request.getPageNum(), request.getPageSize());
            
            // 复用getList方法进行查询
            List<Department> departmentList = getList(request);
            Page<Department> pageList = (Page<Department>) departmentList;
            
            return pageList;
        } catch (Exception e) {
            logger.error("分页查询部门失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
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
        // 注意：createTime和updateTime字段将由MyBatis拦截器自动填充
        
        // 调用departmentMapper插入数据
        int rows = departmentMapper.insert(department);
        return rows > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDepartment(Department department) {
        // 注意：updateTime字段将由MyBatis拦截器自动填充
        
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

    /**
     * 获取部门树形结构
     */
    @Override
    public List<Department> getDepartmentTree() {
        // 调用Mapper层获取所有部门数据
        List<Department> departments = departmentMapper.getDepartmentTree();
        // 使用通用的TreeUtils构建部门树形结构并返回
        return TreeUtils.buildTree(departments, 0L);
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