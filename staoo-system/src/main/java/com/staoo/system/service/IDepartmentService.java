package com.staoo.system.service;

import com.github.pagehelper.Page;
import com.staoo.system.domain.Department;
import com.staoo.system.pojo.request.DepartmentQueryRequest;
import com.staoo.system.pojo.request.DepartmentRequest;

import java.util.List;

/**
 * 部门服务接口
 * 提供部门管理相关的业务逻辑操作
 */
public interface IDepartmentService {
    /**
     * 获取部门列表
     * @param department 查询条件
     * @return 部门列表
     */
    List<Department> getDepartmentList(Department department);

    /**
     * 根据查询请求获取部门列表
     * @param request 查询请求参数
     * @return 部门列表
     */
    List<Department> getList(DepartmentQueryRequest request);

    /**
     * 分页查询部门
     * @param request 部门分页查询参数
     * @return 部门分页结果
     */
    Page<Department> getPage(DepartmentQueryRequest request);

    /**
     * 根据ID获取部门信息
     * @param deptId 部门ID
     * @return 部门信息
     */
    Department getDepartmentById(Long deptId);

    /**
     * 创建部门
     * @param department 部门信息
     * @return 是否成功
     */
    boolean createDepartment(Department department);

    /**
     * 更新部门
     * @param department 部门信息
     * @return 是否成功
     */
    boolean updateDepartment(Department department);

    /**
     * 删除部门
     * @param deptId 部门ID
     * @return 是否成功
     */
    boolean deleteDepartment(Long deptId);

    /**
     * 批量删除部门
     * @param deptIds 部门ID列表
     * @return 是否成功
     */
    boolean batchDeleteDepartments(List<Long> deptIds);

    /**
     * 获取部门树形结构
     * @return 部门树
     */
    List<Department> getDepartmentTree();

    /**
     * 获取指定部门的所有子部门
     * @param deptId 部门ID
     * @return 子部门列表
     */
    List<Department> getChildrenDepartments(Long deptId);

    /**
     * 获取部门下的用户ID列表
     * @param deptId 部门ID
     * @return 用户ID列表
     */
    List<Long> getDepartmentUserIds(Long deptId);

    /**
     * 检查部门是否存在子部门
     * @param deptId 部门ID
     * @return 是否存在子部门
     */
    boolean hasChildrenDepartments(Long deptId);

    /**
     * 检查部门名称是否唯一
     * @param department 部门信息
     * @return 是否唯一
     */
    boolean checkDepartmentNameUnique(Department department);
}