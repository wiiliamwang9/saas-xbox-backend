package com.saas.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 节点实体类
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Schema(description = "节点信息")
@TableName("nodes")
public class Node extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 节点名称
     */
    @Schema(description = "节点名称", example = "洛杉矶节点1")
    @TableField("node_name")
    @NotBlank(message = "节点名称不能为空")
    @Size(max = 100, message = "节点名称长度不能超过100个字符")
    private String nodeName;

    /**
     * 节点编码
     */
    @Schema(description = "节点编码", example = "LA-NODE-01")
    @TableField("node_code")
    @NotBlank(message = "节点编码不能为空")
    @Size(max = 50, message = "节点编码长度不能超过50个字符")
    private String nodeCode;

    /**
     * 服务器IP
     */
    @Schema(description = "服务器IP", example = "45.32.128.10")
    @TableField("server_ip")
    @NotBlank(message = "服务器IP不能为空")
    @Size(max = 45, message = "服务器IP长度不能超过45个字符")
    private String serverIp;

    /**
     * 国家
     */
    @Schema(description = "国家", example = "美国")
    @TableField("country")
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
     * SSH端口号
     */
    @Schema(description = "SSH端口号", example = "22")
    @TableField("ssh_port")
    @NotNull(message = "SSH端口号不能为空")
    @Min(value = 1, message = "SSH端口号必须大于0")
    @Max(value = 65535, message = "SSH端口号不能超过65535")
    private Integer sshPort = 22;

    /**
     * 密码
     */
    @Schema(description = "密码")
    @TableField("password")
    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 64, message = "密码长度必须在8-64个字符之间")
    private String password;

    /**
     * 域名
     */
    @Schema(description = "域名", example = "node1.example.com")
    @TableField("domain")
    @Size(max = 255, message = "域名长度不能超过255个字符")
    private String domain;

    /**
     * 节点类型
     */
    @Schema(description = "节点类型", example = "完整节点", allowableValues = {"完整节点", "转发节点", "解密节点"})
    @TableField("node_type")
    @NotBlank(message = "节点类型不能为空")
    private String nodeType;

    /**
     * 组合方式（仅完整节点有效）
     */
    @Schema(description = "组合方式", example = "独立节点", allowableValues = {"独立节点", "组合节点", "落地节点"})
    @TableField("combination_type")
    private String combinationType;

    /**
     * 备注
     */
    @Schema(description = "备注")
    @TableField("remark")
    @Size(max = 200, message = "备注长度不能超过200个字符")
    private String remark;

    /**
     * Agent部署状态
     */
    @Schema(description = "Agent部署状态", example = "未部署", allowableValues = {"已部署", "未部署", "部署中"})
    @TableField("agent_status")
    private String agentStatus = "未部署";

    /**
     * 节点状态
     */
    @Schema(description = "节点状态", example = "运行中", allowableValues = {"运行中", "维护中", "故障", "停用"})
    @TableField("node_status")
    private String nodeStatus = "运行中";

    /**
     * 最大连接数
     */
    @Schema(description = "最大连接数", example = "2000")
    @TableField("max_connections")
    @Min(value = 1, message = "最大连接数不能小于1")
    private Integer maxConnections = 1000;

    /**
     * 当前连接数
     */
    @Schema(description = "当前连接数", example = "1250")
    @TableField("current_connections")
    @Min(value = 0, message = "当前连接数不能为负数")
    private Integer currentConnections = 0;

    /**
     * 带宽(Mbps)
     */
    @Schema(description = "带宽(Mbps)", example = "1000")
    @TableField("bandwidth_mbps")
    @Min(value = 1, message = "带宽不能小于1Mbps")
    private Integer bandwidthMbps;

    /**
     * CPU使用率
     */
    @Schema(description = "CPU使用率", example = "45.20")
    @TableField("cpu_usage")
    @DecimalMin(value = "0.00", message = "CPU使用率不能为负数")
    @DecimalMax(value = "100.00", message = "CPU使用率不能超过100%")
    @Digits(integer = 3, fraction = 2, message = "CPU使用率格式不正确")
    private BigDecimal cpuUsage;

    /**
     * 内存使用率
     */
    @Schema(description = "内存使用率", example = "62.50")
    @TableField("memory_usage")
    @DecimalMin(value = "0.00", message = "内存使用率不能为负数")
    @DecimalMax(value = "100.00", message = "内存使用率不能超过100%")
    @Digits(integer = 3, fraction = 2, message = "内存使用率格式不正确")
    private BigDecimal memoryUsage;

    /**
     * 磁盘使用率
     */
    @Schema(description = "磁盘使用率", example = "35.80")
    @TableField("disk_usage")
    @DecimalMin(value = "0.00", message = "磁盘使用率不能为负数")
    @DecimalMax(value = "100.00", message = "磁盘使用率不能超过100%")
    @Digits(integer = 3, fraction = 2, message = "磁盘使用率格式不正确")
    private BigDecimal diskUsage;

    /**
     * 网络延迟(ms)
     */
    @Schema(description = "网络延迟(ms)", example = "15")
    @TableField("network_latency")
    @Min(value = 0, message = "网络延迟不能为负数")
    private Integer networkLatency;

    /**
     * 最后检查时间
     */
    @Schema(description = "最后检查时间")
    @TableField("last_check_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastCheckTime;

    /**
     * 服务商
     */
    @Schema(description = "服务商", example = "Vultr")
    @TableField("provider")
    @Size(max = 100, message = "服务商长度不能超过100个字符")
    private String provider;

    /**
     * 月费用
     */
    @Schema(description = "月费用", example = "299.00")
    @TableField("monthly_cost")
    @DecimalMin(value = "0.00", message = "月费用不能为负数")
    @Digits(integer = 8, fraction = 2, message = "月费用格式不正确")
    private BigDecimal monthlyCost;

    // Getter and Setter methods
    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
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


    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeStatus() {
        return nodeStatus;
    }

    public void setNodeStatus(String nodeStatus) {
        this.nodeStatus = nodeStatus;
    }

    public Integer getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(Integer maxConnections) {
        this.maxConnections = maxConnections;
    }

    public Integer getCurrentConnections() {
        return currentConnections;
    }

    public void setCurrentConnections(Integer currentConnections) {
        this.currentConnections = currentConnections;
    }

    public Integer getBandwidthMbps() {
        return bandwidthMbps;
    }

    public void setBandwidthMbps(Integer bandwidthMbps) {
        this.bandwidthMbps = bandwidthMbps;
    }

    public BigDecimal getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(BigDecimal cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public BigDecimal getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(BigDecimal memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public BigDecimal getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(BigDecimal diskUsage) {
        this.diskUsage = diskUsage;
    }

    public Integer getNetworkLatency() {
        return networkLatency;
    }

    public void setNetworkLatency(Integer networkLatency) {
        this.networkLatency = networkLatency;
    }

    public LocalDateTime getLastCheckTime() {
        return lastCheckTime;
    }

    public void setLastCheckTime(LocalDateTime lastCheckTime) {
        this.lastCheckTime = lastCheckTime;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public BigDecimal getMonthlyCost() {
        return monthlyCost;
    }

    public void setMonthlyCost(BigDecimal monthlyCost) {
        this.monthlyCost = monthlyCost;
    }

    public Integer getSshPort() {
        return sshPort;
    }

    public void setSshPort(Integer sshPort) {
        this.sshPort = sshPort;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getCombinationType() {
        return combinationType;
    }

    public void setCombinationType(String combinationType) {
        this.combinationType = combinationType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAgentStatus() {
        return agentStatus;
    }

    public void setAgentStatus(String agentStatus) {
        this.agentStatus = agentStatus;
    }

    @Override
    public String toString() {
        return "Node{" +
                "nodeName='" + nodeName + '\'' +
                ", nodeCode='" + nodeCode + '\'' +
                ", serverIp='" + serverIp + '\'' +
                ", sshPort=" + sshPort +
                ", password='" + "[HIDDEN]" + '\'' +
                ", domain='" + domain + '\'' +
                ", country='" + country + '\'' +
                ", region='" + region + '\'' +
                ", nodeType='" + nodeType + '\'' +
                ", combinationType='" + combinationType + '\'' +
                ", remark='" + remark + '\'' +
                ", agentStatus='" + agentStatus + '\'' +
                ", nodeStatus='" + nodeStatus + '\'' +
                ", maxConnections=" + maxConnections +
                ", currentConnections=" + currentConnections +
                ", bandwidthMbps=" + bandwidthMbps +
                ", cpuUsage=" + cpuUsage +
                ", memoryUsage=" + memoryUsage +
                ", diskUsage=" + diskUsage +
                ", networkLatency=" + networkLatency +
                ", lastCheckTime=" + lastCheckTime +
                ", provider='" + provider + '\'' +
                ", monthlyCost=" + monthlyCost +
                "} " + super.toString();
    }
}