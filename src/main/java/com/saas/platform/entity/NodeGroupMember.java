package com.saas.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.*;

/**
 * 节点组成员关系实体类
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Schema(description = "节点组成员关系")
@TableName("node_group_members")
public class NodeGroupMember extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 节点组ID
     */
    @Schema(description = "节点组ID")
    @TableField("group_id")
    @NotNull(message = "节点组ID不能为空")
    private Long groupId;

    /**
     * 节点ID
     */
    @Schema(description = "节点ID")
    @TableField("node_id")
    @NotNull(message = "节点ID不能为空")
    private Long nodeId;

    // Getter and Setter methods
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public String toString() {
        return "NodeGroupMember{" +
                "groupId=" + groupId +
                ", nodeId=" + nodeId +
                "} " + super.toString();
    }
}