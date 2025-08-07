package com.saas.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Proxy模板实体类 - 基于sing-box Inbound配置设计
 * 参考3xui面板的Inbound设计，支持完整的sing-box协议配置
 * 
 * @author SaaS Xbox Team
 * @since 2024-08-04
 */
@Schema(description = "代理产品模板信息")
@TableName(value = "proxy_templates", autoResultMap = true)
public class ProxyTemplate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 产品名称
     */
    @Schema(description = "产品名称", example = "美国静态住宅IP")
    @TableField("product_name")
    @NotBlank(message = "产品名称不能为空")
    @Size(max = 100, message = "产品名称长度不能超过100个字符")
    private String productName;

    /**
     * 默认价格(美元)
     */
    @Schema(description = "默认价格(美元)", example = "29.99")
    @TableField("default_price")
    @NotNull(message = "默认价格不能为空")
    @DecimalMin(value = "0.01", message = "价格必须大于0")
    @Digits(integer = 8, fraction = 2, message = "价格格式不正确")
    private BigDecimal defaultPrice;

    /**
     * 国家
     */
    @Schema(description = "国家", example = "美国")
    @TableField("country")
    @NotBlank(message = "国家不能为空")
    private String country;

    /**
     * 指定节点数量
     */
    @Schema(description = "指定节点数量", example = "5")
    @TableField("specified_nodes")
    @Min(value = 0, message = "指定节点数量不能为负数")
    private Integer specifiedNodes = 0;

    /**
     * 模式: 不受限/黑名单/白名单
     */
    @Schema(description = "模式", example = "不受限", allowableValues = {"不受限", "黑名单", "白名单"})
    @TableField("proxy_mode")
    @NotBlank(message = "模式不能为空")
    private String proxyMode;

    /**
     * 库存数量
     */
    @Schema(description = "库存数量", example = "1000")
    @TableField("stock")
    @Min(value = -1, message = "库存不能小于-1")
    private Integer stock = -1;

    /**
     * 状态: 上架/下架
     */
    @Schema(description = "产品状态", example = "上架", allowableValues = {"上架", "下架"})
    @TableField("status")
    private String status = "下架";

    /**
     * 大陆直连
     */
    @Schema(description = "大陆直连", example = "true")
    @TableField("mainland_direct")
    private Boolean mainlandDirect = false;

    /**
     * Inbound标识
     */
    @Schema(description = "Inbound标识", example = "inbound-vless-reality")
    @TableField("tag")
    @NotBlank(message = "Inbound标识不能为空")
    private String tag;

    /**
     * 协议类型
     */
    @Schema(description = "协议类型", example = "vless", allowableValues = {"vless", "vmess", "trojan", "shadowsocks", "shadowsocks-2022", "hysteria", "hysteria2", "tuic", "naive"})
    @TableField("protocol_type")
    @NotBlank(message = "协议类型不能为空")
    private String protocolType;

    /**
     * 监听地址
     */
    @Schema(description = "监听地址", example = "::")
    @TableField("listen_address")
    private String listenAddress = "::";

    /**
     * 监听端口
     */
    @Schema(description = "监听端口", example = "443")
    @TableField("listen_port")
    @NotNull(message = "监听端口不能为空")
    @Min(value = 1, message = "端口必须大于0")
    @Max(value = 65535, message = "端口不能超过65535")
    private Integer listenPort;

    /**
     * 用户配置 (JSON格式)
     */
    @Schema(description = "用户配置")
    @TableField(value = "users_config", typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> usersConfig;

    /**
     * 传输层配置 (JSON格式)
     */
    @Schema(description = "传输层配置")
    @TableField(value = "transport_config", typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> transportConfig;

    /**
     * TLS配置 (JSON格式)
     */
    @Schema(description = "TLS配置")
    @TableField(value = "tls_config", typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> tlsConfig;

    /**
     * Reality配置 (JSON格式)
     */
    @Schema(description = "Reality配置")
    @TableField(value = "reality_config", typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> realityConfig;

    /**
     * 嗅探配置 (JSON格式)
     */
    @Schema(description = "嗅探配置")
    @TableField(value = "sniff_config", typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> sniffConfig;

    /**
     * 多路复用配置 (JSON格式)
     */
    @Schema(description = "多路复用配置")
    @TableField(value = "mux_config", typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> muxConfig;

    /**
     * 路由标签
     */
    @Schema(description = "路由标签", example = "direct")
    @TableField("route_tag")
    private String routeTag;

    /**
     * sing-box完整配置 (JSON格式) - 用于保存完整的Inbound配置
     */
    @Schema(description = "sing-box完整配置")
    @TableField(value = "singbox_config", typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> singboxConfig;

    /**
     * 可选协议(多选,逗号分隔) - 保留向后兼容
     */
    @Schema(description = "可选协议", example = "vless,vmess,trojan")
    @TableField("protocols")
    private String protocols;

    /**
     * 模式: 顺序/随机/权重
     */
    @Schema(description = "分配模式", example = "随机", allowableValues = {"顺序", "随机", "权重"})
    @TableField("allocation_mode")
    private String allocationMode = "随机";

    /**
     * 绑定域名
     */
    @Schema(description = "绑定域名", example = "proxy.example.com")
    @TableField("domain")
    private String domain;

    /**
     * 负载均衡算法
     */
    @Schema(description = "负载均衡算法", example = "轮询")
    @TableField("load_balance_algorithm")
    private String loadBalanceAlgorithm;

    /**
     * 最大连接数
     */
    @Schema(description = "最大连接数", example = "100")
    @TableField("max_connections")
    @Min(value = -1, message = "最大连接数不能小于-1")
    private Integer maxConnections = -1;

    /**
     * 使用场景
     */
    @Schema(description = "使用场景", example = "社交媒体")
    @TableField("usage_scenario")
    private String usageScenario;

    /**
     * 产品描述
     */
    @Schema(description = "产品描述")
    @TableField("description")
    @Size(max = 500, message = "描述长度不能超过500个字符")
    private String description;

    /**
     * 产品介绍
     */
    @Schema(description = "产品介绍")
    @TableField("product_description")
    @Size(max = 200, message = "产品介绍长度不能超过200个字符")
    private String productDescription;

    /**
     * 宽带计费模式: 流量计费/宽带计费
     */
    @Schema(description = "宽带计费模式", example = "流量计费", allowableValues = {"流量计费", "宽带计费"})
    @TableField("bandwidth_billing_mode")
    private String bandwidthBillingMode;

    /**
     * 扩展配置 (JSON格式)
     */
    @Schema(description = "扩展配置")
    @TableField(value = "extra_config", typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> extraConfig;

    /**
     * 协议配置 (JSON格式) - 存储前端协议详细配置
     */
    @Schema(description = "协议配置")
    @TableField(value = "protocol_configs", typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> protocolConfigs;

    /**
     * 模板图片URL
     */
    @Schema(description = "模板图片URL", example = "https://example.com/images/template.jpg")
    @TableField("image_url")
    @Size(max = 500, message = "图片URL长度不能超过500个字符")
    private String imageUrl;

    // Getters and Setters
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(BigDecimal defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getSpecifiedNodes() {
        return specifiedNodes;
    }

    public void setSpecifiedNodes(Integer specifiedNodes) {
        this.specifiedNodes = specifiedNodes;
    }

    public String getProxyMode() {
        return proxyMode;
    }

    public void setProxyMode(String proxyMode) {
        this.proxyMode = proxyMode;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getMainlandDirect() {
        return mainlandDirect;
    }

    public void setMainlandDirect(Boolean mainlandDirect) {
        this.mainlandDirect = mainlandDirect;
    }

    public String getProtocols() {
        return protocols;
    }

    public void setProtocols(String protocols) {
        this.protocols = protocols;
    }

    public String getAllocationMode() {
        return allocationMode;
    }

    public void setAllocationMode(String allocationMode) {
        this.allocationMode = allocationMode;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getLoadBalanceAlgorithm() {
        return loadBalanceAlgorithm;
    }

    public void setLoadBalanceAlgorithm(String loadBalanceAlgorithm) {
        this.loadBalanceAlgorithm = loadBalanceAlgorithm;
    }

    public Integer getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(Integer maxConnections) {
        this.maxConnections = maxConnections;
    }

    public String getUsageScenario() {
        return usageScenario;
    }

    public void setUsageScenario(String usageScenario) {
        this.usageScenario = usageScenario;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> getExtraConfig() {
        return extraConfig;
    }

    public void setExtraConfig(Map<String, Object> extraConfig) {
        this.extraConfig = extraConfig;
    }

    // sing-box相关字段的Getters and Setters
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    public String getListenAddress() {
        return listenAddress;
    }

    public void setListenAddress(String listenAddress) {
        this.listenAddress = listenAddress;
    }

    public Integer getListenPort() {
        return listenPort;
    }

    public void setListenPort(Integer listenPort) {
        this.listenPort = listenPort;
    }

    public Map<String, Object> getUsersConfig() {
        return usersConfig;
    }

    public void setUsersConfig(Map<String, Object> usersConfig) {
        this.usersConfig = usersConfig;
    }

    public Map<String, Object> getTransportConfig() {
        return transportConfig;
    }

    public void setTransportConfig(Map<String, Object> transportConfig) {
        this.transportConfig = transportConfig;
    }

    public Map<String, Object> getTlsConfig() {
        return tlsConfig;
    }

    public void setTlsConfig(Map<String, Object> tlsConfig) {
        this.tlsConfig = tlsConfig;
    }

    public Map<String, Object> getRealityConfig() {
        return realityConfig;
    }

    public void setRealityConfig(Map<String, Object> realityConfig) {
        this.realityConfig = realityConfig;
    }

    public Map<String, Object> getSniffConfig() {
        return sniffConfig;
    }

    public void setSniffConfig(Map<String, Object> sniffConfig) {
        this.sniffConfig = sniffConfig;
    }

    public Map<String, Object> getMuxConfig() {
        return muxConfig;
    }

    public void setMuxConfig(Map<String, Object> muxConfig) {
        this.muxConfig = muxConfig;
    }

    public String getRouteTag() {
        return routeTag;
    }

    public void setRouteTag(String routeTag) {
        this.routeTag = routeTag;
    }

    public Map<String, Object> getSingboxConfig() {
        return singboxConfig;
    }

    public void setSingboxConfig(Map<String, Object> singboxConfig) {
        this.singboxConfig = singboxConfig;
    }

    public Map<String, Object> getProtocolConfigs() {
        return protocolConfigs;
    }

    public void setProtocolConfigs(Map<String, Object> protocolConfigs) {
        this.protocolConfigs = protocolConfigs;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getBandwidthBillingMode() {
        return bandwidthBillingMode;
    }

    public void setBandwidthBillingMode(String bandwidthBillingMode) {
        this.bandwidthBillingMode = bandwidthBillingMode;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}