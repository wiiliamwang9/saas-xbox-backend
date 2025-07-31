package com.saas.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * 客户查询对象
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Schema(description = "客户查询条件")
public class CustomerQuery extends PageQuery {

    @Schema(description = "客户名称（模糊查询）", example = "张三")
    private String customerName;

    @Schema(description = "客户账号（模糊查询）", example = "user001")
    private String account;

    @Schema(description = "客户类型", example = "个人")
    private String customerType;

    @Schema(description = "手机号（精确匹配）", example = "13800138001")
    private String phone;

    @Schema(description = "邮箱（模糊查询）", example = "user@example.com")
    private String email;

    @Schema(description = "VIP等级", example = "普通")
    private String vipLevel;

    @Schema(description = "客户状态", example = "正常")
    private String customerStatus;

    @Schema(description = "注册来源", example = "官网注册")
    private String registerSource;

    @Schema(description = "客户经理ID", example = "3")
    private Long managerId;

    @Schema(description = "最小余额", example = "100.00")
    private BigDecimal minBalance;

    @Schema(description = "最大余额", example = "10000.00")
    private BigDecimal maxBalance;

    @Schema(description = "是否有父账户", example = "true")
    private Boolean hasParent;

    // Getter and Setter methods
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(String vipLevel) {
        this.vipLevel = vipLevel;
    }

    public String getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(String customerStatus) {
        this.customerStatus = customerStatus;
    }

    public String getRegisterSource() {
        return registerSource;
    }

    public void setRegisterSource(String registerSource) {
        this.registerSource = registerSource;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public BigDecimal getMinBalance() {
        return minBalance;
    }

    public void setMinBalance(BigDecimal minBalance) {
        this.minBalance = minBalance;
    }

    public BigDecimal getMaxBalance() {
        return maxBalance;
    }

    public void setMaxBalance(BigDecimal maxBalance) {
        this.maxBalance = maxBalance;
    }

    public Boolean getHasParent() {
        return hasParent;
    }

    public void setHasParent(Boolean hasParent) {
        this.hasParent = hasParent;
    }
}