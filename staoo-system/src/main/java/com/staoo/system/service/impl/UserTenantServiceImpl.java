package com.staoo.system.service.impl;

import com.staoo.common.exception.BusinessException;
import com.staoo.common.domain.TableResult;
import com.staoo.common.enums.StatusCodeEnum;
import com.staoo.system.domain.UserTenant;
import com.staoo.system.mapper.UserTenantMapper;
import com.staoo.system.pojo.request.UserTenantQueryRequest;
import com.staoo.system.service.UserTenantService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Collections;
import org.apache.commons.lang3.StringUtils;

/**
 * 用户-租户关联Service实现类
 * 提供用户与租户关联关系的业务操作具体实现
 */
@Service
public class UserTenantServiceImpl implements UserTenantService {

    private static final Logger log = LoggerFactory.getLogger(UserTenantServiceImpl.class);

    @Autowired
    private UserTenantMapper userTenantMapper;

    // 租户内角色类型常量
    public static final Integer ROLE_TYPE_CREATOR = 1; // 创建者
    public static final Integer ROLE_TYPE_MANAGER = 2;  // 管理者
    public static final Integer ROLE_TYPE_NORMAL = 3;   // 普通用户

    // 状态常量
    public static final Integer STATUS_ENABLE = 0;  // 启用
    public static final Integer STATUS_DISABLE = 1; // 禁用

    /**
     * 新增用户-租户关联
     * @param userTenant 用户-租户关联信息
     * @return 结果
     */
    @Override
    @Transactional
    public boolean addUserTenant(UserTenant userTenant) {
        try {
            // 检查用户是否已经属于该租户
            if (checkUserBelongsToTenant(userTenant.getUserId(), userTenant.getTenantId())) {
                log.warn("用户[{}]已属于租户[{}]", userTenant.getUserId(), userTenant.getTenantId());
                return false;
            }

            // 设置默认值
            if (userTenant.getStatus() == null) {
                userTenant.setStatus(STATUS_ENABLE);
            }
            // 注意：createTime和updateTime字段将由MyBatis拦截器自动填充

            int result = userTenantMapper.insert(userTenant);
            log.info("新增用户[{}]与租户[{}]关联成功", userTenant.getUserId(), userTenant.getTenantId());
            return result > 0;
        } catch (Exception e) {
            log.error("新增用户-租户关联失败", e);
            throw new RuntimeException("新增用户-租户关联失败", e);
        }
    }

    /**
     * 批量新增用户-租户关联
     * @param userTenantList 用户-租户关联列表
     * @return 结果
     */
    @Override
    @Transactional
    public boolean batchAddUserTenant(List<UserTenant> userTenantList) {
        try {
            if (userTenantList == null || userTenantList.isEmpty()) {
                log.warn("批量新增用户-租户关联列表为空");
                return true;
            }

            // 设置默认值并检查冲突
            for (UserTenant userTenant : userTenantList) {
                if (checkUserBelongsToTenant(userTenant.getUserId(), userTenant.getTenantId())) {
                    log.warn("用户[{}]已属于租户[{}]，跳过该关联", userTenant.getUserId(), userTenant.getTenantId());
                    continue;
                }

                if (userTenant.getStatus() == null) {
                    userTenant.setStatus(STATUS_ENABLE);
                }
                // 注意：createTime和updateTime字段将由MyBatis拦截器自动填充

                userTenantMapper.insert(userTenant);
            }

            log.info("批量新增用户-租户关联完成，共处理[{}]条记录", userTenantList.size());
            return true;
        } catch (Exception e) {
            log.error("批量新增用户-租户关联失败", e);
            throw new RuntimeException("批量新增用户-租户关联失败", e);
        }
    }

    /**
     * 删除用户-租户关联
     * @param id 主键ID
     * @return 结果
     */
    @Override
    @Transactional
    public boolean deleteUserTenantById(Long id) {
        try {
            UserTenant userTenant = getUserTenantById(id);
            if (userTenant == null) {
                log.warn("用户-租户关联ID[{}]不存在", id);
                return false;
            }

            int result = userTenantMapper.deleteById(id);
            log.info("删除用户-租户关联ID[{}]成功", id);
            return result > 0;
        } catch (Exception e) {
            log.error("删除用户-租户关联失败", e);
            throw new RuntimeException("删除用户-租户关联失败", e);
        }
    }

    /**
     * 根据用户ID和租户ID删除关联
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 结果
     */
    @Override
    @Transactional
    public boolean deleteUserTenantByUserAndTenant(Long userId, Long tenantId) {
        try {
            if (!checkUserBelongsToTenant(userId, tenantId)) {
                log.warn("用户[{}]不属于租户[{}]", userId, tenantId);
                return false;
            }

            int result = userTenantMapper.deleteByUserAndTenant(userId, tenantId);
            log.info("删除用户[{}]与租户[{}]关联成功", userId, tenantId);
            return result > 0;
        } catch (Exception e) {
            log.error("删除用户-租户关联失败", e);
            throw new RuntimeException("删除用户-租户关联失败", e);
        }
    }

    /**
     * 批量删除用户-租户关联
     * @param ids 主键ID列表
     * @return 结果
     */
    @Override
    @Transactional
    public boolean batchDeleteUserTenantByIds(Long[] ids) {
        try {
            if (ids == null || ids.length == 0) {
                log.warn("批量删除用户-租户关联ID列表为空");
                return true;
            }

            for (Long id : ids) {
                userTenantMapper.deleteById(id);
            }

            log.info("批量删除用户-租户关联完成，共删除[{}]条记录", ids.length);
            return true;
        } catch (Exception e) {
            log.error("批量删除用户-租户关联失败", e);
            throw new RuntimeException("批量删除用户-租户关联失败", e);
        }
    }

    /**
     * 批量删除用户的所有租户关联
     * @param userIds 用户ID列表
     * @return 结果
     */
    @Override
    @Transactional
    public boolean batchDeleteUserTenantByUserIds(Long[] userIds) {
        try {
            if (userIds == null || userIds.length == 0) {
                log.warn("批量删除用户的所有租户关联用户ID列表为空");
                return true;
            }

            for (Long userId : userIds) {
                List<UserTenant> userTenants = getUserTenantsByUserId(userId);
                for (UserTenant userTenant : userTenants) {
                    userTenantMapper.deleteById(userTenant.getId());
                }
            }

            log.info("批量删除用户的所有租户关联完成，共处理[{}]个用户", userIds.length);
            return true;
        } catch (Exception e) {
            log.error("批量删除用户的所有租户关联失败", e);
            throw new RuntimeException("批量删除用户的所有租户关联失败", e);
        }
    }

    /**
     * 批量删除租户的所有用户关联
     * @param tenantIds 租户ID列表
     * @return 结果
     */
    @Override
    @Transactional
    public boolean batchDeleteUserTenantByTenantIds(Long[] tenantIds) {
        try {
            if (tenantIds == null || tenantIds.length == 0) {
                log.warn("批量删除租户的所有用户关联租户ID列表为空");
                return true;
            }

            for (Long tenantId : tenantIds) {
                List<UserTenant> userTenants = getUserTenantsByTenantId(tenantId);
                for (UserTenant userTenant : userTenants) {
                    userTenantMapper.deleteById(userTenant.getId());
                }
            }

            log.info("批量删除租户的所有用户关联完成，共处理[{}]个租户", tenantIds.length);
            return true;
        } catch (Exception e) {
            log.error("批量删除租户的所有用户关联失败", e);
            throw new RuntimeException("批量删除租户的所有用户关联失败", e);
        }
    }

    /**
     * 更新用户-租户关联信息
     * @param userTenant 用户-租户关联信息
     * @return 结果
     */
    @Override
    @Transactional
    public boolean updateUserTenant(UserTenant userTenant) {
        try {
            if (userTenant.getId() == null) {
                log.warn("更新用户-租户关联ID不能为空");
                return false;
            }

            UserTenant existing = getUserTenantById(userTenant.getId());
            if (existing == null) {
                log.warn("用户-租户关联ID[{}]不存在", userTenant.getId());
                return false;
            }

            // 注意：updateTime字段将由MyBatis拦截器自动填充

            int result = userTenantMapper.update(userTenant);
            log.info("更新用户-租户关联ID[{}]成功", userTenant.getId());
            return result > 0;
        } catch (Exception e) {
            log.error("更新用户-租户关联失败", e);
            throw new RuntimeException("更新用户-租户关联失败", e);
        }
    }

    /**
     * 更新用户在租户中的角色类型
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @param roleType 角色类型
     * @return 结果
     */
    @Override
    @Transactional
    public boolean updateUserTenantRoleType(Long userId, Long tenantId, Integer roleType) {
        try {
            if (!checkUserBelongsToTenant(userId, tenantId)) {
                log.warn("用户[{}]不属于租户[{}]", userId, tenantId);
                return false;
            }

            // 验证角色类型是否合法
            if (!isValidRoleType(roleType)) {
                log.warn("角色类型[{}]不合法", roleType);
                return false;
            }

            int result = userTenantMapper.updateRoleType(userId, tenantId, roleType);
            log.info("更新用户[{}]在租户[{}]中的角色类型为[{}]成功", userId, tenantId, roleType);
            return result > 0;
        } catch (Exception e) {
            log.error("更新用户在租户中的角色类型失败", e);
            throw new RuntimeException("更新用户在租户中的角色类型失败", e);
        }
    }

    /**
     * 更新用户在租户中的状态
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @param status 状态
     * @return 结果
     */
    @Override
    @Transactional
    public boolean updateUserTenantStatus(Long userId, Long tenantId, Integer status) {
        try {
            if (!checkUserBelongsToTenant(userId, tenantId)) {
                log.warn("用户[{}]不属于租户[{}]", userId, tenantId);
                return false;
            }

            // 验证状态是否合法
            if (!isValidStatus(status)) {
                log.warn("状态[{}]不合法", status);
                return false;
            }

            int result = userTenantMapper.updateStatus(userId, tenantId, status);
            log.info("更新用户[{}]在租户[{}]中的状态为[{}]成功", userId, tenantId, status);
            return result > 0;
        } catch (Exception e) {
            log.error("更新用户在租户中的状态失败", e);
            throw new RuntimeException("更新用户在租户中的状态失败", e);
        }
    }

    /**
     * 根据ID查询用户-租户关联信息
     * @param id 主键ID
     * @return 用户-租户关联信息
     */
    @Override
    public UserTenant getUserTenantById(Long id) {
        try {
            return userTenantMapper.selectById(id);
        } catch (Exception e) {
            log.error("根据ID查询用户-租户关联信息失败", e);
            throw new RuntimeException("根据ID查询用户-租户关联信息失败", e);
        }
    }

    /**
     * 根据用户ID和租户ID查询关联信息
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 用户-租户关联信息
     */
    @Override
    public UserTenant getUserTenantByUserAndTenant(Long userId, Long tenantId) {
        try {
            return userTenantMapper.selectByUserAndTenant(userId, tenantId);
        } catch (Exception e) {
            log.error("根据用户ID和租户ID查询关联信息失败", e);
            throw new RuntimeException("根据用户ID和租户ID查询关联信息失败", e);
        }
    }

    /**
     * 根据用户ID查询其所有租户关联
     * @param userId 用户ID
     * @return 用户-租户关联列表
     */
    @Override
    public List<UserTenant> getUserTenantsByUserId(Long userId) {
        try {
            return userTenantMapper.selectByUserId(userId);
        } catch (Exception e) {
            log.error("根据用户ID查询其所有租户关联失败", e);
            throw new RuntimeException("根据用户ID查询其所有租户关联失败", e);
        }
    }

    /**
     * 根据租户ID查询其所有用户关联
     * @param tenantId 租户ID
     * @return 用户-租户关联列表
     */
    @Override
    public List<UserTenant> getUserTenantsByTenantId(Long tenantId) {
        try {
            return userTenantMapper.selectByTenantId(tenantId);
        } catch (Exception e) {
            log.error("根据租户ID查询其所有用户关联失败", e);
            throw new RuntimeException("根据租户ID查询其所有用户关联失败", e);
        }
    }

    /**
     * 查询用户在指定租户中的角色类型
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 角色类型
     */
    @Override
    public Integer getUserTenantRoleType(Long userId, Long tenantId) {
        try {
            return userTenantMapper.selectRoleTypeByUserAndTenant(userId, tenantId);
        } catch (Exception e) {
            log.error("查询用户在指定租户中的角色类型失败", e);
            throw new RuntimeException("查询用户在指定租户中的角色类型失败", e);
        }
    }

    /**
     * 检查用户是否属于指定租户
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 是否属于
     */
    @Override
    public boolean checkUserBelongsToTenant(Long userId, Long tenantId) {
        try {
            return userTenantMapper.existsByUserAndTenant(userId, tenantId);
        } catch (Exception e) {
            log.error("检查用户是否属于指定租户失败", e);
            throw new RuntimeException("检查用户是否属于指定租户失败", e);
        }
    }

    /**
     * 检查用户在租户中是否具有指定角色类型
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @param roleType 角色类型
     * @return 是否具有
     */
    @Override
    public boolean checkUserHasRoleTypeInTenant(Long userId, Long tenantId, Integer roleType) {
        try {
            if (!checkUserBelongsToTenant(userId, tenantId)) {
                return false;
            }

            Integer actualRoleType = getUserTenantRoleType(userId, tenantId);
            return Objects.equals(actualRoleType, roleType);
        } catch (Exception e) {
            log.error("检查用户在租户中是否具有指定角色类型失败", e);
            throw new RuntimeException("检查用户在租户中是否具有指定角色类型失败", e);
        }
    }

    /**
     * 检查用户在租户中是否具有创建者权限
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 是否具有
     */
    @Override
    public boolean checkUserIsCreatorInTenant(Long userId, Long tenantId) {
        return checkUserHasRoleTypeInTenant(userId, tenantId, ROLE_TYPE_CREATOR);
    }

    /**
     * 检查用户在租户中是否具有管理者权限
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 是否具有
     */
    @Override
    public boolean checkUserIsManagerInTenant(Long userId, Long tenantId) {
        return checkUserHasRoleTypeInTenant(userId, tenantId, ROLE_TYPE_MANAGER);
    }

    /**
     * 检查用户在租户中是否具有普通用户权限
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 是否具有
     */
    @Override
    public boolean checkUserIsNormalInTenant(Long userId, Long tenantId) {
        return checkUserHasRoleTypeInTenant(userId, tenantId, ROLE_TYPE_NORMAL);
    }

    /**
     * 验证角色类型是否合法
     * @param roleType 角色类型
     * @return 是否合法
     */
    private boolean isValidRoleType(Integer roleType) {
        return roleType != null && 
               (roleType.equals(ROLE_TYPE_CREATOR) || 
                roleType.equals(ROLE_TYPE_MANAGER) || 
                roleType.equals(ROLE_TYPE_NORMAL));
    }

    /**
     * 验证状态是否合法
     * @param status 状态
     * @return 是否合法
     */
    private boolean isValidStatus(Integer status) {
        return status != null && 
               (status.equals(STATUS_ENABLE) || 
                status.equals(STATUS_DISABLE));
    }

    /**
     * 查询用户-租户关联列表
     * @param request 查询请求
     * @return 用户-租户关联列表
     */
    @Override
    public List<UserTenant> getList(UserTenantQueryRequest request) {
        try {
            if (request == null) {
                return Collections.emptyList();
            }
            
            // 使用统一的mapper方法根据查询请求条件获取列表
            return userTenantMapper.getListByRequest(request);
        } catch (Exception e) {
            log.error("查询用户-租户关联列表失败", e);
            throw new RuntimeException("查询用户-租户关联列表失败", e);
        }
    }

    /**
     * 分页查询用户-租户关联列表
     * @param request 查询请求
     * @return 分页结果
     */
    @Override
    public TableResult<UserTenant> getPage(UserTenantQueryRequest request) {
        try {
            if (request == null) {
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "分页查询参数不能为空");
            }

            // 设置分页参数
            int pageNum = request.getPageNum() != null ? request.getPageNum() : 1;
            int pageSize = request.getPageSize() != null ? request.getPageSize() : 10;
            PageHelper.startPage(pageNum, pageSize);

            // 直接使用getList方法获取数据，复用列表查询逻辑
            List<UserTenant> list = getList(request);

            // 处理分页结果
            Page<UserTenant> pageList = (Page<UserTenant>) list;
            
            // 确保参数类型正确匹配TableResult.build方法的要求
            return TableResult.build(
                pageList.getTotal(),  // 保持long类型
                pageNum,             // int自动装箱为Integer
                pageSize,            // int自动装箱为Integer
                pageList.getResult()
            );
        } catch (BusinessException e) {
            // 直接抛出业务异常
            throw e;
        } catch (Exception e) {
            log.error("分页查询用户-租户关联列表失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }
}