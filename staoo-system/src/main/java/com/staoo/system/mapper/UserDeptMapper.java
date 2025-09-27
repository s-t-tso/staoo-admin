package com.staoo.system.mapper;

import com.staoo.system.domain.UserDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户部门关联Mapper接口
 * 提供用户部门关联相关的数据库操作方法
 */
@Mapper
public interface UserDeptMapper {
    /**
     * 根据ID查询用户部门关联
     * @param id 主键ID
     * @return 用户部门关联信息
     */
    UserDept getById(@Param("id") Long id);

    /**
     * 根据用户ID查询部门ID列表
     * @param userId 用户ID
     * @return 部门ID列表
     */
    List<Long> getDeptIdsByUserId(@Param("userId") Long userId);

    /**
     * 根据部门ID查询用户ID列表
     * @param deptId 部门ID
     * @return 用户ID列表
     */
    List<Long> getUserIdsByDeptId(@Param("deptId") Long deptId);

    /**
     * 保存用户部门关联
     * @param userDept 用户部门关联信息
     * @return 影响行数
     */
    int insert(UserDept userDept);

    /**
     * 批量保存用户部门关联
     * @param userDepts 用户部门关联列表
     * @return 影响行数
     */
    int batchInsert(@Param("userDepts") List<UserDept> userDepts);

    /**
     * 根据用户ID删除用户部门关联
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteByUserId(@Param("userId") Long userId);

    /**
     * 根据部门ID删除用户部门关联
     * @param deptId 部门ID
     * @return 影响行数
     */
    int deleteByDeptId(@Param("deptId") Long deptId);

    /**
     * 根据用户ID和部门ID删除用户部门关联
     * @param userId 用户ID
     * @param deptId 部门ID
     * @return 影响行数
     */
    int deleteByUserIdAndDeptId(@Param("userId") Long userId, @Param("deptId") Long deptId);

    /**
     * 批量根据用户ID删除用户部门关联
     * @param userIds 用户ID列表
     * @return 影响行数
     */
    int batchDeleteByUserIds(@Param("userIds") List<Long> userIds);

    /**
     * 批量根据部门ID删除用户部门关联
     * @param deptIds 部门ID列表
     * @return 影响行数
     */
    int batchDeleteByDeptIds(@Param("deptIds") List<Long> deptIds);

    /**
     * 查询用户是否与指定部门关联
     * @param userId 用户ID
     * @param deptId 部门ID
     * @return 是否关联
     */
    Boolean existsByUserIdAndDeptId(@Param("userId") Long userId, @Param("deptId") Long deptId);
}