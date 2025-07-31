package com.saas.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * 产品数据传输对象
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Schema(description = "产品数据传输对象")
public class ProductDTO {

    @Schema(description = "产品ID", example = "1")
    private Long id;

    @Schema(description = "产品名称", example = "美国标准IP包-月")
    @NotBlank(message = "产品名称不能为空")
    @Size(max = 100, message = "产品名称长度不能超过100个字符")
    private String productName;

    @Schema(description = "产品编码", example = "US-STD-30")
    @NotBlank(message = "产品编码不能为空")
    @Size(max = 50, message = "产品编码长度不能超过50个字符")
    private String productCode;

    @Schema(description = "IP质量", example = "标准")
    @NotBlank(message = "IP质量不能为空")
    private String ipQuality;

    @Schema(description = "IP数量", example = "100")
    @NotNull(message = "IP数量不能为空")
    @Min(value = 1, message = "IP数量不能小于1")
    private Integer ipCount;

    @Schema(description = "时长(天)", example = "30")
    @NotNull(message = "时长不能为空")
    @Min(value = 1, message = "时长不能小于1天")
    private Integer durationDays;

    @Schema(description = "带宽(Mbps)", example = "100")
    @Min(value = 1, message = "带宽不能小于1Mbps")
    private Integer bandwidthMbps;

    @Schema(description = "原价", example = "199.00")
    @NotNull(message = "原价不能为空")
    @DecimalMin(value = "0.01", message = "原价必须大于0")
    private BigDecimal originalPrice;

    @Schema(description = "现价", example = "159.00")
    @NotNull(message = "现价不能为空")
    @DecimalMin(value = "0.01", message = "现价必须大于0")
    private BigDecimal currentPrice;

    @Schema(description = "国家限制", example = "[\"美国\", \"加拿大\"]")
    private List<String> countryLimit;

    @Schema(description = "产品状态", example = "上架")
    private String productStatus;

    @Schema(description = "排序", example = "0")
    private Integer sortOrder;

    @Schema(description = "产品描述", example = "美国地区标准质量IP资源，适合一般业务使用")
    @Size(max = 1000, message = "产品描述长度不能超过1000个字符")
    private String description;

    @Schema(description = "产品特性", example = "[\"高稳定性\", \"24小时技术支持\"]")
    private List<String> features;

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}