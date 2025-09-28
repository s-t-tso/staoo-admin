package com.staoo.system.pojo.response;

import com.staoo.common.util.TreeUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 组织节点响应类
 * 用于封装组织架构中的单个节点数据
 */
public class OrganizationNode implements Serializable, TreeUtils.TreeNode<OrganizationNode> {

    /**
     * 子节点列表
     */
    private List<OrganizationNode> children = new ArrayList<>();
    private static final long serialVersionUID = 1L;

    /**
     * 组织ID
     */
    private Long id;

    /**
     * 组织名称
     */
    private String name;

    /**
     * 父组织ID
     */
    private Long parentId;

    /**
     * 组织层级
     */
    private Integer level;

    /**
     * 负责人
     */
    private String manager;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    // 无参构造方法
    public OrganizationNode() {
    }

    // 带参构造方法
    public OrganizationNode(Long id, String name, Long parentId, Integer level, String manager, String phone, String email) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.level = level;
        this.manager = manager;
        this.phone = phone;
        this.email = email;
    }

    // getter and setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<OrganizationNode> getChildren() {
        return children;
    }

    public void setChildren(List<OrganizationNode> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "OrganizationNode{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", level=" + level +
                ", manager='" + manager + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}