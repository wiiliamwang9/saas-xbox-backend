package com.saas.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工实体类
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Schema(description = "员工信息")
@TableName("employees")
public class Employee extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 员工号
     */
    @Schema(description = "员工号")
    @TableField("employee_no")
    @NotBlank(message = "员工号不能为空")
    private String employeeNo;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    @TableField("username")
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @Schema(description = "密码")
    @TableField("password")
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 真实姓名
     */
    @Schema(description = "真实姓名")
    @TableField("real_name")
    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    @TableField("phone")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    @TableField("email")
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 部门ID
     */
    @Schema(description = "部门ID")
    @TableField("department_id")
    private Long departmentId;

    /**
     * 角色
     */
    @Schema(description = "角色", allowableValues = {"超级管理员", "管理员", "客户经理", "普通员工", "财务"})
    @TableField("role")
    @NotBlank(message = "角色不能为空")
    private String role;

    /**
     * 员工状态
     */
    @Schema(description = "员工状态", allowableValues = {"正常", "离职", "停用"})
    @TableField("employee_status")
    private String employeeStatus = "正常";

    /**
     * 入职日期
     */
    @Schema(description = "入职日期")
    @TableField("hire_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;

    /**
     * 头像URL
     */
    @Schema(description = "头像URL")
    @TableField("avatar")
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
    @Schema(description = "最后登录IP")
    @TableField("last_login_ip")
    private String lastLoginIp;

    // Getter and Setter methods
    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(String employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
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
        return "Employee{" +
                "employeeNo='" + employeeNo + '\'' +
                ", username='" + username + '\'' +
                ", realName='" + realName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", departmentId=" + departmentId +
                ", role='" + role + '\'' +
                ", employeeStatus='" + employeeStatus + '\'' +
                ", hireDate=" + hireDate +
                ", avatar='" + avatar + '\'' +
                ", lastLoginTime=" + lastLoginTime +
                ", lastLoginIp='" + lastLoginIp + '\'' +
                "} " + super.toString();
    }
}