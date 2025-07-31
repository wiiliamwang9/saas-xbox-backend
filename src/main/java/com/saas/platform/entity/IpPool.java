package com.saas.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * IP池实体类
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Schema(description = "IP池信息")
@TableName("ip_pools")
public class IpPool extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * IP地址
     */
    @Schema(description = "IP地址", example = "192.168.1.1")
    @TableField("ip_address")
    @NotBlank(message = "IP地址不能为空")
    @Size(max = 45, message = "IP地址长度不能超过45个字符")
    private String ipAddress;

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
    @Size(max = 50, message = "城市长度不能超过50个字符")
    private String city;

    /**
     * IP质量
     */
    @Schema(description = "IP质量", example = "标准", allowableValues = {"标准", "优质", "独享"})
    @TableField("ip_quality")
    @NotBlank(message = "IP质量不能为空")
    private String ipQuality;

    /**
     * IP状态
     */
    @Schema(description = "IP状态", example = "可用", allowableValues = {"可用", "占用", "故障", "测试中", "维护中"})
    @TableField("ip_status")
    private String ipStatus = "可用";

    /**
     * 端口范围
     */
    @Schema(description = "端口范围", example = "8000-8999")
    @TableField("port_range")
    @Size(max = 100, message = "端口范围长度不能超过100个字符")
    private String portRange;

    /**
     * 带宽(Mbps)
     */
    @Schema(description = "带宽(Mbps)", example = "100")
    @TableField("bandwidth_mbps")
    @Min(value = 1, message = "带宽不能小于1Mbps")
    private Integer bandwidthMbps;

    /**
     * 供应商
     */
    @Schema(description = "供应商", example = "LA-Provider")
    @TableField("provider")
    @Size(max = 100, message = "供应商长度不能超过100个字符")
    private String provider;

    /**
     * 成本价格
     */
    @Schema(description = "成本价格", example = "1.50")
    @TableField("cost_price")
    @DecimalMin(value = "0.00", message = "成本价格不能为负数")
    @Digits(integer = 8, fraction = 2, message = "成本价格格式不正确")
    private BigDecimal costPrice;

    /**
     * 关联节点ID
     */
    @Schema(description = "关联节点ID", example = "1")
    @TableField("node_id")
    private Long nodeId;

    /**
     * 当前占用订单ID
     */
    @Schema(description = "当前占用订单ID", example = "1")
    @TableField("current_order_id")
    private Long currentOrderId;

    /**
     * 最后测试时间
     */
    @Schema(description = "最后测试时间")
    @TableField("last_test_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastTestTime;

    /**
     * 测试结果
     */
    @Schema(description = "测试结果", example = "成功", allowableValues = {"成功", "失败"})
    @TableField("test_result")
    private String testResult;

    /**
     * 测试延迟(ms)
     */
    @Schema(description = "测试延迟(ms)", example = "15")
    @TableField("test_latency")
    @Min(value = 0, message = "测试延迟不能为负数")
    private Integer testLatency;

    /**
     * 测试信息
     */
    @Schema(description = "测试信息", example = "连接正常")
    @TableField("test_message")
    private String testMessage;

    // Getter and Setter methods
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
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

    public String getIpQuality() {
        return ipQuality;
    }

    public void setIpQuality(String ipQuality) {
        this.ipQuality = ipQuality;
    }

    public String getIpStatus() {
        return ipStatus;
    }

    public void setIpStatus(String ipStatus) {
        this.ipStatus = ipStatus;
    }

    public String getPortRange() {
        return portRange;
    }

    public void setPortRange(String portRange) {
        this.portRange = portRange;
    }

    public Integer getBandwidthMbps() {
        return bandwidthMbps;
    }

    public void setBandwidthMbps(Integer bandwidthMbps) {
        this.bandwidthMbps = bandwidthMbps;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Long getCurrentOrderId() {
        return currentOrderId;
    }

    public void setCurrentOrderId(Long currentOrderId) {
        this.currentOrderId = currentOrderId;
    }

    public LocalDateTime getLastTestTime() {
        return lastTestTime;
    }

    public void setLastTestTime(LocalDateTime lastTestTime) {
        this.lastTestTime = lastTestTime;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public Integer getTestLatency() {
        return testLatency;
    }

    public void setTestLatency(Integer testLatency) {
        this.testLatency = testLatency;
    }

    public String getTestMessage() {
        return testMessage;
    }

    public void setTestMessage(String testMessage) {
        this.testMessage = testMessage;
    }

    @Override
    public String toString() {
        return "IpPool{" +
                "ipAddress='" + ipAddress + '\'' +
                ", country='" + country + '\'' +
                ", region='" + region + '\'' +
                ", city='" + city + '\'' +
                ", ipQuality='" + ipQuality + '\'' +
                ", ipStatus='" + ipStatus + '\'' +
                ", portRange='" + portRange + '\'' +
                ", bandwidthMbps=" + bandwidthMbps +
                ", provider='" + provider + '\'' +
                ", costPrice=" + costPrice +
                ", nodeId=" + nodeId +
                ", currentOrderId=" + currentOrderId +
                ", lastTestTime=" + lastTestTime +
                ", testResult='" + testResult + '\'' +
                ", testLatency=" + testLatency +
                ", testMessage='" + testMessage + '\'' +
                "} " + super.toString();
    }
}