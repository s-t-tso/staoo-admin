package com.staoo.system.service.impl;

import com.staoo.common.domain.TableResult;
import com.staoo.system.domain.Department;
import com.staoo.system.domain.User;
import com.staoo.system.mapstruct.IUserMapper;
import com.staoo.system.pojo.request.OrganizationDataRequest;
import com.staoo.system.pojo.request.UserListDataRequest;
import com.staoo.system.pojo.response.OrganizationNode;
import com.staoo.system.pojo.response.UserResponse;
import com.staoo.system.service.DataService;
import com.staoo.system.service.IDepartmentService;
import com.staoo.system.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据服务实现类
 * 实现DataService接口，提供组织架构和用户数据的查询服务
 */
@Service
public class DataServiceImpl implements DataService {
    
    private static final Logger logger = LoggerFactory.getLogger(DataServiceImpl.class);
    
    @Autowired
    private IDepartmentService departmentService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private IUserMapper userMapper;
    
    @Override
    public TableResult<OrganizationNode> getOrganizationData(OrganizationDataRequest request) {
        try {
            // 构建查询条件
            Department department = new Department();
            department.setTenantId(request.getTenantId());
            
            if (StringUtils.hasText(request.getKeyword())) {
                department.setDeptName(request.getKeyword());
            }
            
            // 查询部门列表
            List<Department> departments = departmentService.getDepartmentList(department);
            
            // 转换为OrganizationNode列表
            List<OrganizationNode> nodes = departments.stream().map(dept -> {
                OrganizationNode node = new OrganizationNode();
                node.setId(dept.getId());
                node.setName(dept.getDeptName());
                node.setParentId(dept.getParentId());
                // 这里可以根据需要设置其他字段
                node.setLevel(calculateLevel(dept.getId(), departments));
                node.setManager(dept.getLeaderName());
                // 可以从其他表或缓存中获取负责人电话和邮箱
                return node;
            }).collect(Collectors.toList());
            
            // 构建树形结构（可选，根据前端需求）
            // nodes = buildTree(nodes);
            
            // 计算总数
            long total = nodes.size();
            
            // 处理分页
            Integer pageNum = request.getPageNum() != null && request.getPageNum() > 0 ? request.getPageNum() : 1;
            Integer pageSize = request.getPageSize() != null && request.getPageSize() > 0 ? request.getPageSize() : 10;
            
            int startIndex = (pageNum - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, nodes.size());
            
            List<OrganizationNode> pageData = startIndex < nodes.size() ? nodes.subList(startIndex, endIndex) : Collections.emptyList();
            
            return TableResult.build(total, pageNum, pageSize, pageData);
        } catch (Exception e) {
            logger.error("获取组织架构数据失败", e);
            throw e;
        }
    }
    
    @Override
    public TableResult<UserResponse> getUserListData(UserListDataRequest request) {
        try {
            // 构建查询条件
            User user = new User();
            user.setTenantId(request.getTenantId());
            
            if (request.getDepartmentId() != null) {
                user.setDeptId(request.getDepartmentId());
            }
            
            if (StringUtils.hasText(request.getKeyword())) {
                user.setUsername(request.getKeyword());
                user.setNickname(request.getKeyword());
                user.setPhone(request.getKeyword());
                user.setEmail(request.getKeyword());
            }
            
            // 查询用户列表
            List<User> users = userService.getList(user);
            
            // 转换为UserResponse列表
            List<UserResponse> userResponses = userMapper.toResponseList(users);
            
            // 排序处理
            if (StringUtils.hasText(request.getSortBy())) {
                String sortBy = request.getSortBy();
                String sortOrder = request.getSortOrder() != null ? request.getSortOrder() : "asc";
                
                userResponses = userResponses.stream()
                        .sorted((u1, u2) -> compareUsers(u1, u2, sortBy, sortOrder))
                        .collect(Collectors.toList());
            }
            
            // 计算总数
            long total = userResponses.size();
            
            // 处理分页
            Integer pageNum = request.getPageNum() != null && request.getPageNum() > 0 ? request.getPageNum() : 1;
            Integer pageSize = request.getPageSize() != null && request.getPageSize() > 0 ? request.getPageSize() : 10;
            
            int startIndex = (pageNum - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, userResponses.size());
            
            List<UserResponse> pageData = startIndex < userResponses.size() ? userResponses.subList(startIndex, endIndex) : Collections.emptyList();
            
            return TableResult.build(total, pageNum, pageSize, pageData);
        } catch (Exception e) {
            logger.error("获取用户列表数据失败", e);
            throw e;
        }
    }
    
    /**
     * 计算部门层级
     */
    private Integer calculateLevel(Long deptId, List<Department> allDepartments) {
        // 构建父ID到部门的映射，以便快速查找
        Map<Long, Department> deptMap = allDepartments.stream()
                .collect(Collectors.toMap(Department::getId, dept -> dept));
        
        int level = 1;
        Long currentId = deptId;
        
        while (true) {
            Department dept = deptMap.get(currentId);
            if (dept == null || dept.getParentId() == null || dept.getParentId() == 0) {
                break;
            }
            level++;
            currentId = dept.getParentId();
        }
        
        return level;
    }
    
    /**
     * 构建部门树形结构
     */
    private List<OrganizationNode> buildTree(List<OrganizationNode> nodes) {
        // 为了简化，这里不实现完整的树形结构构建
        // 实际项目中可能需要递归构建树形结构
        return nodes;
    }
    
    /**
     * 比较两个用户对象，用于排序
     */
    private int compareUsers(UserResponse u1, UserResponse u2, String sortBy, String sortOrder) {
        int result = 0;
        
        switch (sortBy) {
            case "createTime":
                result = u1.getCreateTime().compareTo(u2.getCreateTime());
                break;
            case "username":
                result = u1.getUsername().compareTo(u2.getUsername());
                break;
            case "nickname":
                result = u1.getNickname().compareTo(u2.getNickname());
                break;
            // 可以根据需要添加更多排序字段
            default:
                // 默认按ID排序
                result = u1.getId().compareTo(u2.getId());
        }
        
        return "desc".equalsIgnoreCase(sortOrder) ? -result : result;
    }
}