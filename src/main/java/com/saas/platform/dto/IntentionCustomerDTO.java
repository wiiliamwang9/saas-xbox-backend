package com.saas.platform.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 意向客户数据传输对象
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Schema(description = "意向客户信息")
public class IntentionCustomerDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 客户名称
     */
    @Schema(description = "客户名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "客户名称不能为空")
    @Size(max = 50, message = "客户名称长度不能超过50个字符")
    private String customerName;

    /**
     * 客户来源
     */
    @Schema(description = "客户来源", allowableValues = {"线上注册", "线下拜访", "活动导入", "其他"}, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "客户来源不能为空")
    private String customerSource;

    /**
     * 客户类型
     */
    @Schema(description = "客户类型", allowableValues = {"个人", "企业", "工作室"})
    private String customerType = "个人";

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 微信号
     */
    @Schema(description = "微信号")
    @Size(max = 30, message = "微信号长度不能超过30个字符")
    private String wechat;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 公司名称
     */
    @Schema(description = "公司名称")
    @Size(max = 100, message = "公司名称长度不能超过100个字符")
    private String companyName;

    /**
     * 客户经理ID
     */
    @Schema(description = "客户经理ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "客户经理不能为空")
    private Long managerId;

    /**
     * 意向级别
     */
    @Schema(description = "意向级别", allowableValues = {"潜在客户", "意向客户", "准客户", "签约客户"}, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "意向级别不能为空")
    private String intentionLevel;

    /**
     * 关联账号
     */
    @Schema(description = "关联账号列表")
    private List<String> relatedAccounts;

    /**
     * 是否成交
     */
    @Schema(description = "是否成交")
    private Boolean isDeal = false;

    /**
     * 折扣
     */
    @Schema(description = "折扣(0-100)")
    @DecimalMin(value = "0.00", message = "折扣不能小于0")
    @DecimalMax(value = "100.00", message = "折扣不能大于100")
    private BigDecimal discount = BigDecimal.ZERO;

    /**
     * 备注
     */
    @Schema(description = "备注")
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;

    /**
     * 下次跟进时间
     */
    @Schema(description = "下次跟进时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime followUpTime;

    /**
     * 最后联系时间
     */
    @Schema(description = "最后联系时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastContactTime;

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
        return "IntentionCustomerDTO{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
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
                '}';
    }
}