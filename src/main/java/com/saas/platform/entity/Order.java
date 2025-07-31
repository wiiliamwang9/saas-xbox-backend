package com.saas.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单实体类
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Schema(description = "订单信息")
@TableName(value = "orders", autoResultMap = true)
public class Order extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    @Schema(description = "订单号", example = "ORD202407310001")
    @TableField("order_no")
    @NotBlank(message = "订单号不能为空")
    @Size(max = 50, message = "订单号长度不能超过50个字符")
    private String orderNo;

    /**
     * 客户ID
     */
    @Schema(description = "客户ID", example = "1")
    @TableField("customer_id")
    @NotNull(message = "客户ID不能为空")
    private Long customerId;

    /**
     * 客户账号
     */
    @Schema(description = "客户账号", example = "user001")
    @TableField("customer_account")
    @NotBlank(message = "客户账号不能为空")
    @Size(max = 100, message = "客户账号长度不能超过100个字符")
    private String customerAccount;

    /**
     * 客户名称
     */
    @Schema(description = "客户名称", example = "张三")
    @TableField("customer_name")
    @NotBlank(message = "客户名称不能为空")
    @Size(max = 100, message = "客户名称长度不能超过100个字符")
    private String customerName;

    /**
     * 客户经理ID
     */
    @Schema(description = "客户经理ID", example = "3")
    @TableField("manager_id")
    private Long managerId;

    /**
     * 产品ID
     */
    @Schema(description = "产品ID", example = "1")
    @TableField("product_id")
    @NotNull(message = "产品ID不能为空")
    private Long productId;

    /**
     * 产品名称
     */
    @Schema(description = "产品名称", example = "美国标准IP包-月")
    @TableField("product_name")
    @NotBlank(message = "产品名称不能为空")
    @Size(max = 100, message = "产品名称长度不能超过100个字符")
    private String productName;

    /**
     * 订单状态
     */
    @Schema(description = "订单状态", example = "正常", allowableValues = {"正常", "已暂停", "已过期", "人工核验", "待付款", "已退款"})
    @TableField("order_status")
    @NotBlank(message = "订单状态不能为空")
    private String orderStatus;

    /**
     * 支付状态
     */
    @Schema(description = "支付状态", example = "已付款", allowableValues = {"待付款", "已付款", "已退款", "部分退款"})
    @TableField("payment_status")
    private String paymentStatus = "待付款";

    /**
     * 订单总金额
     */
    @Schema(description = "订单总金额", example = "199.00")
    @TableField("total_amount")
    @NotNull(message = "订单总金额不能为空")
    @DecimalMin(value = "0.01", message = "订单总金额必须大于0")
    @Digits(integer = 8, fraction = 2, message = "订单总金额格式不正确")
    private BigDecimal totalAmount;

    /**
     * 实际支付金额
     */
    @Schema(description = "实际支付金额", example = "159.00")
    @TableField("actual_amount")
    @NotNull(message = "实际支付金额不能为空")
    @DecimalMin(value = "0.01", message = "实际支付金额必须大于0")
    @Digits(integer = 8, fraction = 2, message = "实际支付金额格式不正确")
    private BigDecimal actualAmount;

    /**
     * 折扣金额
     */
    @Schema(description = "折扣金额", example = "40.00")
    @TableField("discount_amount")
    @DecimalMin(value = "0.00", message = "折扣金额不能为负数")
    @Digits(integer = 8, fraction = 2, message = "折扣金额格式不正确")
    private BigDecimal discountAmount = BigDecimal.ZERO;

    /**
     * 国家/地区
     */
    @Schema(description = "国家/地区", example = "美国")
    @TableField("country")
    @Size(max = 50, message = "国家/地区长度不能超过50个字符")
    private String country;

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
    private String ipQuality;

    /**
     * IP数量
     */
    @Schema(description = "IP数量", example = "100")
    @TableField("ip_count")
    @Min(value = 1, message = "IP数量不能小于1")
    private Integer ipCount;

    /**
     * 购买时长(天)
     */
    @Schema(description = "购买时长(天)", example = "30")
    @TableField("duration_days")
    @NotNull(message = "购买时长不能为空")
    @Min(value = 1, message = "购买时长不能小于1天")
    private Integer durationDays;

    /**
     * 开始日期
     */
    @Schema(description = "开始日期", example = "2024-07-01")
    @TableField("start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    /**
     * 结束日期
     */
    @Schema(description = "结束日期", example = "2024-07-31")
    @TableField("end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    /**
     * IP地址列表
     */
    @Schema(description = "IP地址列表", example = "[\"192.168.1.1\", \"192.168.1.2\"]")
    @TableField(value = "ip_addresses", typeHandler = JacksonTypeHandler.class)
    private List<String> ipAddresses;

    /**
     * 支付时间
     */
    @Schema(description = "支付时间")
    @TableField("payment_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentTime;

    /**
     * 过期时间
     */
    @Schema(description = "过期时间")
    @TableField("expire_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;

    // Getter and Setter methods
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerAccount() {
        return customerAccount;
    }

    public void setCustomerAccount(String customerAccount) {
        this.customerAccount = customerAccount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<String> getIpAddresses() {
        return ipAddresses;
    }

    public void setIpAddresses(List<String> ipAddresses) {
        this.ipAddresses = ipAddresses;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(LocalDateTime paymentTime) {
        this.paymentTime = paymentTime;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderNo='" + orderNo + '\'' +
                ", customerId=" + customerId +
                ", customerAccount='" + customerAccount + '\'' +
                ", customerName='" + customerName + '\'' +
                ", managerId=" + managerId +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", totalAmount=" + totalAmount +
                ", actualAmount=" + actualAmount +
                ", discountAmount=" + discountAmount +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", ipQuality='" + ipQuality + '\'' +
                ", ipCount=" + ipCount +
                ", durationDays=" + durationDays +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", ipAddresses=" + ipAddresses +
                ", paymentTime=" + paymentTime +
                ", expireTime=" + expireTime +
                "} " + super.toString();
    }
}