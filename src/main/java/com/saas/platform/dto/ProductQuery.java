package com.saas.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 产品查询对象
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Schema(description = "产品查询条件")
public class ProductQuery extends PageQuery {

    @Schema(description = "产品名称（模糊查询）", example = "美国")
    private String productName;

    @Schema(description = "产品编码（精确匹配）", example = "US-STD-30")
    private String productCode;

    @Schema(description = "IP质量", example = "标准")
    private String ipQuality;

    @Schema(description = "产品状态", example = "上架")
    private String productStatus;

    @Schema(description = "最小IP数量", example = "50")
    private Integer minIpCount;

    @Schema(description = "最大IP数量", example = "200")
    private Integer maxIpCount;

    @Schema(description = "最小时长(天)", example = "30")
    private Integer minDurationDays;

    @Schema(description = "最大时长(天)", example = "365")
    private Integer maxDurationDays;

    @Schema(description = "最小价格", example = "100.00")
    private java.math.BigDecimal minPrice;

    @Schema(description = "最大价格", example = "500.00")
    private java.math.BigDecimal maxPrice;

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

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public Integer getMinIpCount() {
        return minIpCount;
    }

    public void setMinIpCount(Integer minIpCount) {
        this.minIpCount = minIpCount;
    }

    public Integer getMaxIpCount() {
        return maxIpCount;
    }

    public void setMaxIpCount(Integer maxIpCount) {
        this.maxIpCount = maxIpCount;
    }

    public Integer getMinDurationDays() {
        return minDurationDays;
    }

    public void setMinDurationDays(Integer minDurationDays) {
        this.minDurationDays = minDurationDays;
    }

    public Integer getMaxDurationDays() {
        return maxDurationDays;
    }

    public void setMaxDurationDays(Integer maxDurationDays) {
        this.maxDurationDays = maxDurationDays;
    }

    public java.math.BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(java.math.BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public java.math.BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(java.math.BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }
}