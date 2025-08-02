package com.saas.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.saas.platform.entity.NodeGroup;
import com.saas.platform.entity.Node;

import java.util.List;

/**
 * 节点组服务接口
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
public interface NodeGroupService extends IService<NodeGroup> {

    /**
     * 分页查询节点组列表
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param groupName 节点组名称
     * @param groupType 节点组类型
     * @param country 国家
     * @param region 地区
     * @return 分页结果
     */
    IPage<NodeGroup> getNodeGroupPage(Long current, Long size, String groupName, String groupType,
                                     String country, String region);

    /**
     * 创建节点组
     * 
     * @param nodeGroup 节点组信息
     * @return 是否成功
     */
    boolean createNodeGroup(NodeGroup nodeGroup);

    /**
     * 更新节点组信息
     * 
     * @param nodeGroup 节点组信息
     * @return 是否成功
     */
    boolean updateNodeGroup(NodeGroup nodeGroup);

    /**
     * 删除节点组
     * 
     * @param id 节点组ID
     * @return 是否成功
     */
    boolean deleteNodeGroup(Long id);

    /**
     * 验证节点组名称是否唯一
     * 
     * @param groupName 节点组名称
     * @param excludeId 排除的ID（用于更新时验证）
     * @return 是否唯一
     */
    boolean isGroupNameUnique(String groupName, Long excludeId);

    /**
     * 获取节点组成员列表
     * 
     * @param groupId 节点组ID
     * @return 成员节点列表
     */
    List<Node> getNodeGroupMembers(Long groupId);

    /**
     * 添加节点组成员
     * 
     * @param groupId 节点组ID
     * @param nodeIds 节点ID列表
     * @return 是否成功
     */
    boolean addNodeGroupMembers(Long groupId, List<Long> nodeIds);

    /**
     * 移除节点组成员
     * 
     * @param groupId 节点组ID
     * @param nodeIds 节点ID列表
     * @return 是否成功
     */
    boolean removeNodeGroupMembers(Long groupId, List<Long> nodeIds);

    /**
     * 获取可选择的节点列表
     * 
     * @param groupType 节点组类型
     * @param groupId 节点组ID（编辑时排除已选节点）
     * @param country 国家
     * @param region 地区
     * @param nodeName 节点名称
     * @return 可选择的节点列表
     */
    List<Node> getAvailableNodes(String groupType, Long groupId, String country, String region, String nodeName);
}