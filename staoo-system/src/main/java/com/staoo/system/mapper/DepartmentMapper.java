package com.staoo.system.mapper;

import com.staoo.system.domain.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门Mapper接口
 * 提供部门相关的数据库操作方法
 */
@Mapper
public interface DepartmentMapper {
    /**
     * 根据ID查询部门
     * @param deptId 部门ID
     * @return 部门信息
     */
    Department getById(@Param("deptId") Long deptId);

    /**
     * 查询部门列表
     * @param department 部门查询条件
     * @return 部门列表
     */
    List<Department> getList(@Param("department") Department department);

    /**
     * 插入部门
     * @param department 部门信息
     * @return 影响行数
     */
    int insert(@Param("department") Department department);

    /**
     * 更新部门
     * @param department 部门信息
     * @return 影响行数
     */
    int update(@Param("department") Department department);

    /**
     * 删除部门
     * @param deptId 部门ID
     * @return 影响行数
     */
    int deleteById(@Param("deptId") Long deptId);

    /**
     * 批量删除部门
     * @param deptIds 部门ID列表
     * @return 影响行数
     */
    int batchDeleteByIds(@Param("deptIds") List<Long> deptIds);

    /**
     * 查询部门是否存在子部门
     * @param deptId 部门ID
     * @return 子部门数量
     */
    int countChildren(@Param("deptId") Long deptId);

    /**
     * 查询部门名称是否唯一
     * @param deptName 部门名称
     * @param parentId 父部门ID
     * @return 部门数量
     */
    int checkDeptNameUnique(@Param("deptName") String deptName, @Param("parentId") Long parentId);

    /**
     * 获取部门树形结构
     * @return 部门列表
     */
    List<Department> getDepartmentTree();

    /**
     * 获取指定部门的所有子部门
     * @param deptId 部门ID
     * @return 子部门列表
     */
    List<Department> getChildrenDepartments(@Param("deptId") Long deptId);
}