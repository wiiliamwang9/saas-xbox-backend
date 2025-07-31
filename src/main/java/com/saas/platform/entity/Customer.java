package com.saas.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 客户实体类
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Schema(description = "客户信息")
@TableName("customers")
public class Customer extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 客户账号
     */
    @Schema(description = "客户账号", example = "user001")
    @TableField("account")
    @NotBlank(message = "客户账号不能为空")
    @Size(max = 100, message = "客户账号长度不能超过100个字符")
    private String account;

    /**
     * 密码
     */
    @Schema(description = "密码")
    @TableField("password")
    @JsonIgnore
    @NotBlank(message = "密码不能为空")
    @Size(max = 100, message = "密码长度不能超过100个字符")
    private String password;

    /**
     * 客户名称
     */
    @Schema(description = "客户名称", example = "张三")
    @TableField("customer_name")
    @NotBlank(message = "客户名称不能为空")
    @Size(max = 100, message = "客户名称长度不能超过100个字符")
    private String customerName;

    /**
     * 客户类型
     */
    @Schema(description = "客户类型", example = "个人", allowableValues = {"个人", "企业", "工作室"})
    @TableField("customer_type")
    private String customerType = "个人";

    /**
     * 手机号
     */
    @Schema(description = "手机号", example = "13800138001")
    @TableField("phone")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱", example = "user@example.com")
    @TableField("email")
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email;

    /**
     * 父账户ID
     */
    @Schema(description = "父账户ID", example = "1")
    @TableField("parent_id")
    private Long parentId;

    /**
     * VIP等级
     */
    @Schema(description = "VIP等级", example = "普通", allowableValues = {"普通", "银牌", "金牌", "钻石"})
    @TableField("vip_level")
    private String vipLevel = "普通";

    /**
     * 账户余额
     */
    @Schema(description = "账户余额", example = "1580.50")
    @TableField("balance")
    @DecimalMin(value = "0.00", message = "账户余额不能为负数")
    @Digits(integer = 8, fraction = 2, message = "账户余额格式不正确")
    private BigDecimal balance = BigDecimal.ZERO;

    /**
     * 客户状态
     */
    @Schema(description = "客户状态", example = "正常", allowableValues = {"正常", "冻结", "黑名单"})
    @TableField("customer_status")
    private String customerStatus = "正常";

    /**
     * 注册来源
     */
    @Schema(description = "注册来源", example = "官网注册", allowableValues = {"官网注册", "推广注册", "线下开发"})
    @TableField("register_source")
    private String registerSource;

    /**
     * 客户经理ID
     */
    @Schema(description = "客户经理ID", example = "3")
    @TableField("manager_id")
    private Long managerId;

    /**
     * 头像URL
     */
    @Schema(description = "头像URL", example = "/images/avatar/user001.jpg")
    @TableField("avatar")
    @Size(max = 255, message = "头像URL长度不能超过255个字符")
    private String avatar;

    /**
     * 最后登录时间
     */
    @Schema(description = "最后登录时间")
    @TableField("last_login_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginTime;

    /**
     * 最后登录IP
     */
    @Schema(description = "最后登录IP", example = "192.168.1.100")
    @TableField("last_login_ip")
    @Size(max = 45, message = "最后登录IP长度不能超过45个字符")
    private String lastLoginIp;

    // Getter and Setter methods
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(String vipLevel) {
        this.vipLevel = vipLevel;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "account='" + account + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerType='" + customerType + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", parentId=" + parentId +
                ", vipLevel='" + vipLevel + '\'' +
                ", balance=" + balance +
                ", customerStatus='" + customerStatus + '\'' +
                ", registerSource='" + registerSource + '\'' +
                ", managerId=" + managerId +
                ", avatar='" + avatar + '\'' +
                ", lastLoginTime=" + lastLoginTime +
                ", lastLoginIp='" + lastLoginIp + '\'' +
                "} " + super.toString();
    }
}