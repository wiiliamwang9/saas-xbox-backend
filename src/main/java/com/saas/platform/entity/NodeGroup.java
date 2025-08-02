package com.saas.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.*;

/**
 * 节点组实体类
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Schema(description = "节点组信息")
@TableName("node_groups")
public class NodeGroup extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 节点组名称
     */
    @Schema(description = "节点组名称", example = "美国西部节点组")
    @TableField("group_name")
    @NotBlank(message = "节点组名称不能为空")
    @Size(min = 2, max = 30, message = "节点组名称长度必须在2-30个字符之间")
    private String groupName;

    /**
     * 节点组类型
     */
    @Schema(description = "节点组类型", example = "完整节点组", allowableValues = {"完整节点组", "转发节点组", "解密节点组"})
    @TableField("group_type")
    @NotBlank(message = "节点组类型不能为空")
    private String groupType;

    /**
     * 国家
     */
    @Schema(description = "国家", example = "美国")
    @TableField("country")
    @NotBlank(message = "国家不能为空")
    @Size(max = 50, message = "国家长度不能超过50个字符")
    private String country;

    /**
     * 地区
     */
    @Schema(description = "地区", example = "加利福尼亚")
    @TableField("region")
    @Size(max = 50, message = "地区长度不能超过50个字符")
    private String region;

    /**
     * 城市
     */
    @Schema(description = "城市", example = "洛杉矶")
    @TableField("city")
    @NotBlank(message = "城市不能为空")
    @Size(max = 50, message = "城市长度不能超过50个字符")
    private String city;

    /**
     * 备注
     */
    @Schema(description = "备注")
    @TableField("remark")
    @Size(max = 200, message = "备注长度不能超过200个字符")
    private String remark;

    // Getter and Setter methods
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "NodeGroup{" +
                "groupName='" + groupName + '\'' +
                ", groupType='" + groupType + '\'' +
                ", country='" + country + '\'' +
                ", region='" + region + '\'' +
                ", city='" + city + '\'' +
                ", remark='" + remark + '\'' +
                "} " + super.toString();
    }
}