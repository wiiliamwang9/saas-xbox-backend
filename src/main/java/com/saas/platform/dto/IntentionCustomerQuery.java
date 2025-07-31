package com.saas.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 意向客户查询DTO
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Schema(description = "意向客户查询参数")
public class IntentionCustomerQuery extends TimeRangeQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 客户名称（模糊查询）
     */
    @Schema(description = "客户名称", example = "张三")
    private String customerName;

    /**
     * 客户来源
     */
    @Schema(description = "客户来源", allowableValues = {"线上注册", "线下拜访", "活动导入", "其他"})
    private String customerSource;

    /**
     * 意向级别
     */
    @Schema(description = "意向级别", allowableValues = {"潜在客户", "意向客户", "准客户", "签约客户"})
    private String intentionLevel;

    /**
     * 客户经理ID
     */
    @Schema(description = "客户经理ID", example = "1")
    private Long managerId;

    /**
     * 是否成交
     */
    @Schema(description = "是否成交", example = "false")
    private Boolean isDeal;

    /**
     * 手机号（精确查询）
     */
    @Schema(description = "手机号", example = "13800138000")
    private String phone;

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

    public String getIntentionLevel() {
        return intentionLevel;
    }

    public void setIntentionLevel(String intentionLevel) {
        this.intentionLevel = intentionLevel;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public Boolean getIsDeal() {
        return isDeal;
    }

    public void setIsDeal(Boolean isDeal) {
        this.isDeal = isDeal;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "IntentionCustomerQuery{" +
                "customerName='" + customerName + '\'' +
                ", customerSource='" + customerSource + '\'' +
                ", intentionLevel='" + intentionLevel + '\'' +
                ", managerId=" + managerId +
                ", isDeal=" + isDeal +
                ", phone='" + phone + '\'' +
                "} " + super.toString();
    }
}