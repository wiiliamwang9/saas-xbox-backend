package com.saas.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 意向客户实体类
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Schema(description = "意向客户信息")
@TableName("intention_customers")
public class IntentionCustomer extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 客户名称
     */
    @Schema(description = "客户名称")
    @TableField("customer_name")
    @NotBlank(message = "客户名称不能为空")
    private String customerName;

    /**
     * 客户来源
     */
    @Schema(description = "客户来源", allowableValues = {"线上注册", "线下拜访", "活动导入", "其他"})
    @TableField("customer_source")
    @NotBlank(message = "客户来源不能为空")
    private String customerSource;

    /**
     * 客户类型
     */
    @Schema(description = "客户类型", allowableValues = {"个人", "企业", "工作室"})
    @TableField("customer_type")
    private String customerType = "个人";

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    @TableField("phone")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 微信号
     */
    @Schema(description = "微信号")
    @TableField("wechat")
    private String wechat;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    @TableField("email")
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 公司名称
     */
    @Schema(description = "公司名称")
    @TableField("company_name")
    private String companyName;

    /**
     * 客户经理ID
     */
    @Schema(description = "客户经理ID")
    @TableField("manager_id")
    @NotNull(message = "客户经理不能为空")
    private Long managerId;

    /**
     * 意向级别
     */
    @Schema(description = "意向级别", allowableValues = {"潜在客户", "意向客户", "准客户", "签约客户"})
    @TableField("intention_level")
    @NotBlank(message = "意向级别不能为空")
    private String intentionLevel;

    /**
     * 关联账号
     */
    @Schema(description = "关联账号列表")
    @TableField("related_accounts")
    private List<String> relatedAccounts;

    /**
     * 是否成交
     */
    @Schema(description = "是否成交")
    @TableField("is_deal")
    private Boolean isDeal = false;

    /**
     * 折扣
     */
    @Schema(description = "折扣(0-100)")
    @TableField("discount")
    @DecimalMin(value = "0.00", message = "折扣不能小于0")
    @DecimalMax(value = "100.00", message = "折扣不能大于100")
    private BigDecimal discount = BigDecimal.ZERO;

    /**
     * 备注
     */
    @Schema(description = "备注")
    @TableField("remark")
    private String remark;

    /**
     * 下次跟进时间
     */
    @Schema(description = "下次跟进时间")
    @TableField("follow_up_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime followUpTime;

    /**
     * 最后联系时间
     */
    @Schema(description = "最后联系时间")
    @TableField("last_contact_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastContactTime;

    // Getter and Setter methods
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerSource() {
        return customerSource;
    }

    public void setCustomerSource(String customerSource) {
        this.customerSource = customerSource;
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

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public String getIntentionLevel() {
        return intentionLevel;
    }

    public void setIntentionLevel(String intentionLevel) {
        this.intentionLevel = intentionLevel;
    }

    public List<String> getRelatedAccounts() {
        return relatedAccounts;
    }

    public void setRelatedAccounts(List<String> relatedAccounts) {
        this.relatedAccounts = relatedAccounts;
    }

    public Boolean getIsDeal() {
        return isDeal;
    }

    public void setIsDeal(Boolean isDeal) {
        this.isDeal = isDeal;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getFollowUpTime() {
        return followUpTime;
    }

    public void setFollowUpTime(LocalDateTime followUpTime) {
        this.followUpTime = followUpTime;
    }

    public LocalDateTime getLastContactTime() {
        return lastContactTime;
    }

    public void setLastContactTime(LocalDateTime lastContactTime) {
        this.lastContactTime = lastContactTime;
    }

    @Override
    public String toString() {
        return "IntentionCustomer{" +
                "customerName='" + customerName + '\'' +
                ", customerSource='" + customerSource + '\'' +
                ", customerType='" + customerType + '\'' +
                ", phone='" + phone + '\'' +
                ", wechat='" + wechat + '\'' +
                ", email='" + email + '\'' +
                ", companyName='" + companyName + '\'' +
                ", managerId=" + managerId +
                ", intentionLevel='" + intentionLevel + '\'' +
                ", relatedAccounts=" + relatedAccounts +
                ", isDeal=" + isDeal +
                ", discount=" + discount +
                ", remark='" + remark + '\'' +
                ", followUpTime=" + followUpTime +
                ", lastContactTime=" + lastContactTime +
                "} " + super.toString();
    }
}