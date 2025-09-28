package com.staoo.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 树形结构工具类
 * 提供通用的树形结构构建方法，支持各种需要树状展示的数据
 * @param <T> 节点类型
 */
public class TreeUtils<T extends TreeUtils.TreeNode<T>> {

    /**
     * 构建树形结构
     * @param nodes 节点列表
     * @param rootId 根节点ID
     * @return 构建好的树形结构列表
     */
    public static <T extends TreeNode<T>> List<T> buildTree(List<T> nodes, Long rootId) {
        if (nodes == null || nodes.isEmpty()) {
            return new ArrayList<>();
        }

        // 将节点列表转换为Map，便于快速查找
        Map<Long, T> nodeMap = nodes.stream()
                .collect(Collectors.toMap(TreeNode::getId, Function.identity()));

        List<T> tree = new ArrayList<>();

        // 遍历所有节点，构建树结构
        for (T node : nodes) {
            Long parentId = node.getParentId();

            // 如果是根节点，添加到树的根列表
            if (rootId.equals(parentId)) {
                tree.add(node);
            } else {
                // 否则，添加到父节点的子节点列表
                T parentNode = nodeMap.get(parentId);
                if (parentNode != null) {
                    List<T> children = parentNode.getChildren();
                    if (children == null) {
                        children = new ArrayList<>();
                        parentNode.setChildren(children);
                    }
                    children.add(node);
                }
            }
        }

        return tree;
    }

    /**
     * 构建树形结构（默认根节点ID为0）
     * @param nodes 节点列表
     * @return 构建好的树形结构列表
     */
    public static <T extends TreeNode<T>> List<T> buildTree(List<T> nodes) {
        return buildTree(nodes, 0L);
    }

    /**
     * 树形节点接口
     * 所有需要构建树形结构的实体类都需要实现此接口
     * @param <T> 节点类型
     */
    public interface TreeNode<T> {
        /**
         * 获取节点ID
         * @return 节点ID
         */
        Long getId();

        /**
         * 获取父节点ID
         * @return 父节点ID
         */
        Long getParentId();

        /**
         * 获取子节点列表
         * @return 子节点列表
         */
        List<T> getChildren();

        /**
         * 设置子节点列表
         * @param children 子节点列表
         */
        void setChildren(List<T> children);
    }
}
