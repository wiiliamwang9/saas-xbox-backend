package com.saas.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * 产品实体类
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Schema(description = "产品信息")
@TableName(value = "products", autoResultMap = true)
public class Product extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 产品名称
     */
    @Schema(description = "产品名称", example = "美国标准IP包-月")
    @TableField("product_name")
    @NotBlank(message = "产品名称不能为空")
    @Size(max = 100, message = "产品名称长度不能超过100个字符")
    private String productName;

    /**
     * 产品编码
     */
    @Schema(description = "产品编码", example = "US-STD-30")
    @TableField("product_code")
    @NotBlank(message = "产品编码不能为空")
    @Size(max = 50, message = "产品编码长度不能超过50个字符")
    private String productCode;

    /**
     * IP质量
     */
    @Schema(description = "IP质量", example = "标准", allowableValues = {"标准", "优质", "独享"})
    @TableField("ip_quality")
    @NotBlank(message = "IP质量不能为空")
    private String ipQuality;

    /**
     * IP数量
     */
    @Schema(description = "IP数量", example = "100")
    @TableField("ip_count")
    @NotNull(message = "IP数量不能为空")
    @Min(value = 1, message = "IP数量不能小于1")
    private Integer ipCount;

    /**
     * 时长(天)
     */
    @Schema(description = "时长(天)", example = "30")
    @TableField("duration_days")
    @NotNull(message = "时长不能为空")
    @Min(value = 1, message = "时长不能小于1天")
    private Integer durationDays;

    /**
     * 带宽(Mbps)
     */
    @Schema(description = "带宽(Mbps)", example = "100")
    @TableField("bandwidth_mbps")
    @Min(value = 1, message = "带宽不能小于1Mbps")
    private Integer bandwidthMbps;

    /**
     * 原价
     */
    @Schema(description = "原价", example = "199.00")
    @TableField("original_price")
    @NotNull(message = "原价不能为空")
    @DecimalMin(value = "0.01", message = "原价必须大于0")
    @Digits(integer = 8, fraction = 2, message = "原价格式不正确")
    private BigDecimal originalPrice;

    /**
     * 现价
     */
    @Schema(description = "现价", example = "159.00")
    @TableField("current_price")
    @NotNull(message = "现价不能为空")
    @DecimalMin(value = "0.01", message = "现价必须大于0")
    @Digits(integer = 8, fraction = 2, message = "现价格式不正确")
    private BigDecimal currentPrice;

    /**
     * 国家限制
     */
    @Schema(description = "国家限制", example = "[\"美国\", \"加拿大\"]")
    @TableField(value = "country_limit", typeHandler = JacksonTypeHandler.class)
    private List<String> countryLimit;

    /**
     * 产品状态
     */
    @Schema(description = "产品状态", example = "上架", allowableValues = {"上架", "下架", "停售"})
    @TableField("product_status")
    private String productStatus = "上架";

    /**
     * 排序
     */
    @Schema(description = "排序", example = "0")
    @TableField("sort_order")
    private Integer sortOrder = 0;

    /**
     * 产品描述
     */
    @Schema(description = "产品描述", example = "美国地区标准质量IP资源，适合一般业务使用")
    @TableField("description")
    @Size(max = 1000, message = "产品描述长度不能超过1000个字符")
    private String description;

    /**
     * 产品特性
     */
    @Schema(description = "产品特性", example = "[\"高稳定性\", \"24小时技术支持\"]")
    @TableField(value = "features", typeHandler = JacksonTypeHandler.class)
    private List<String> features;

    // Getter and Setter methods
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getIpQuality() {
        return ipQuality;
    }

    public void setIpQuality(String ipQuality) {
        this.ipQuality = ipQuality;
    }

    public Integer getIpCount() {
        return ipCount;
    }

    public void setIpCount(Integer ipCount) {
        this.ipCount = ipCount;
    }

    public Integer getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }

    public Integer getBandwidthMbps() {
        return bandwidthMbps;
    }

    public void setBandwidthMbps(Integer bandwidthMbps) {
        this.bandwidthMbps = bandwidthMbps;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public List<String> getCountryLimit() {
        return countryLimit;
    }

    public void setCountryLimit(List<String> countryLimit) {
        this.countryLimit = countryLimit;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productName='" + productName + '\'' +
                ", productCode='" + productCode + '\'' +
                ", ipQuality='" + ipQuality + '\'' +
                ", ipCount=" + ipCount +
                ", durationDays=" + durationDays +
                ", bandwidthMbps=" + bandwidthMbps +
                ", originalPrice=" + originalPrice +
                ", currentPrice=" + currentPrice +
                ", countryLimit=" + countryLimit +
                ", productStatus='" + productStatus + '\'' +
                ", sortOrder=" + sortOrder +
                ", description='" + description + '\'' +
                ", features=" + features +
                "} " + super.toString();
    }
}