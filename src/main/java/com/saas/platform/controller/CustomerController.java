package com.saas.platform.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.saas.platform.dto.Result;
import com.saas.platform.entity.Customer;
import com.saas.platform.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 客户管理控制器
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Tag(name = "客户管理", description = "客户管理相关接口")
@RestController
@RequestMapping("/customers")
@Validated
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * 分页查询客户列表
     */
    @Operation(summary = "分页查询客户列表", description = "支持按客户名称、类型、VIP等级、状态等筛选")
    @GetMapping("/page")
    public Result<IPage<Customer>> getCustomerPage(
            @Parameter(description = "当前页", example = "1") @RequestParam(defaultValue = "1") Long current,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") Long size,
            @Parameter(description = "客户名称") @RequestParam(required = false) String customerName,
            @Parameter(description = "客户类型") @RequestParam(required = false) String customerType,
            @Parameter(description = "VIP等级") @RequestParam(required = false) String vipLevel,
            @Parameter(description = "客户状态") @RequestParam(required = false) String customerStatus,
            @Parameter(description = "客户经理ID") @RequestParam(required = false) Long managerId) {
        IPage<Customer> page = customerService.getCustomerPage(current, size, customerName, 
                customerType, vipLevel, customerStatus, managerId);
        return Result.success(page);
    }

    /**
     * 根据ID查询客户详情
     */
    @Operation(summary = "查询客户详情", description = "根据客户ID获取详细信息")
    @GetMapping("/{id}")
    public Result<Customer> getCustomerById(
            @Parameter(description = "客户ID", example = "1") @PathVariable @NotNull Long id) {
        Customer customer = customerService.getById(id);
        if (customer == null) {
            return Result.error("客户不存在");
        }
        return Result.success(customer);
    }

    /**
     * 根据账号查询客户
     */
    @Operation(summary = "根据账号查询客户", description = "根据客户账号获取客户信息")
    @GetMapping("/account/{account}")
    public Result<Customer> getByAccount(
            @Parameter(description = "客户账号", example = "user001") @PathVariable String account) {
        Customer customer = customerService.getByAccount(account);
        if (customer == null) {
            return Result.error("客户不存在");
        }
        return Result.success(customer);
    }

    /**
     * 查询某客户经理的客户列表
     */
    @Operation(summary = "查询客户经理的客户列表", description = "获取指定客户经理的所有客户")
    @GetMapping("/manager/{managerId}")
    public Result<List<Customer>> getByManagerId(
            @Parameter(description = "客户经理ID", example = "3") @PathVariable @NotNull Long managerId) {
        List<Customer> customers = customerService.getByManagerId(managerId);
        return Result.success(customers);
    }

    /**
     * 查询子账户列表
     */
    @Operation(summary = "查询子账户列表", description = "根据父账户ID查询子账户")
    @GetMapping("/{parentId}/children")
    public Result<List<Customer>> getByParentId(
            @Parameter(description = "父账户ID", example = "1") @PathVariable @NotNull Long parentId) {
        List<Customer> children = customerService.getByParentId(parentId);
        return Result.success(children);
    }

    /**
     * 创建客户
     */
    @Operation(summary = "创建客户", description = "添加新客户")
    @PostMapping
    public Result<String> createCustomer(@Valid @RequestBody Customer customer) {
        boolean success = customerService.createCustomer(customer);
        return success ? Result.success("客户创建成功") : Result.error("客户创建失败");
    }

    /**
     * 更新客户信息
     */
    @Operation(summary = "更新客户信息", description = "修改客户信息")
    @PutMapping("/{id}")
    public Result<String> updateCustomer(
            @Parameter(description = "客户ID", example = "1") @PathVariable @NotNull Long id,
            @Valid @RequestBody Customer customer) {
        customer.setId(id);
        boolean success = customerService.updateCustomer(customer);
        return success ? Result.success("客户更新成功") : Result.error("客户更新失败");
    }

    /**
     * 删除客户
     */
    @Operation(summary = "删除客户", description = "根据ID删除客户")
    @DeleteMapping("/{id}")
    public Result<String> deleteCustomer(
            @Parameter(description = "客户ID", example = "1") @PathVariable @NotNull Long id) {
        boolean success = customerService.deleteCustomer(id);
        return success ? Result.success("客户删除成功") : Result.error("客户删除失败");
    }

    /**
     * 批量删除客户
     */
    @Operation(summary = "批量删除客户", description = "根据ID列表批量删除客户")
    @DeleteMapping("/batch")
    public Result<String> batchDeleteCustomers(@RequestBody @NotEmpty List<Long> ids) {
        boolean success = customerService.batchDeleteCustomers(ids);
        return success ? Result.success("客户批量删除成功") : Result.error("客户批量删除失败");
    }

    /**
     * 批量更新客户状态
     */
    @Operation(summary = "批量更新客户状态", description = "批量修改客户状态")
    @PutMapping("/batch/status")
    public Result<String> batchUpdateStatus(
            @RequestBody @NotEmpty List<Long> ids,
            @Parameter(description = "新状态", example = "正常") @RequestParam String status) {
        boolean success = customerService.batchUpdateStatus(ids, status);
        return success ? Result.success("客户状态批量更新成功") : Result.error("客户状态批量更新失败");
    }

    /**
     * 客户充值
     */
    @Operation(summary = "客户充值", description = "为客户账户充值")
    @PostMapping("/{id}/recharge")
    public Result<String> recharge(
            @Parameter(description = "客户ID", example = "1") @PathVariable @NotNull Long id,
            @Parameter(description = "充值金额", example = "100.00") @RequestParam @DecimalMin("0.01") BigDecimal amount,
            @Parameter(description = "操作人ID") @RequestParam(required = false) Long operatorId,
            @Parameter(description = "操作人姓名") @RequestParam(required = false) String operatorName,
            @Parameter(description = "备注") @RequestParam(required = false) String remark) {
        boolean success = customerService.recharge(id, amount, operatorId, operatorName, remark);
        return success ? Result.success("充值成功") : Result.error("充值失败");
    }

    /**
     * 余额调整
     */
    @Operation(summary = "余额调整", description = "调整客户账户余额")
    @PostMapping("/{id}/adjust-balance")
    public Result<String> adjustBalance(
            @Parameter(description = "客户ID", example = "1") @PathVariable @NotNull Long id,
            @Parameter(description = "调整金额", example = "50.00") @RequestParam BigDecimal amount,
            @Parameter(description = "操作人ID") @RequestParam Long operatorId,
            @Parameter(description = "操作人姓名") @RequestParam String operatorName,
            @Parameter(description = "备注") @RequestParam(required = false) String remark) {
        boolean success = customerService.adjustBalance(id, amount, operatorId, operatorName, remark);
        return success ? Result.success("余额调整成功") : Result.error("余额调整失败");
    }

    /**
     * 冻结客户
     */
    @Operation(summary = "冻结客户", description = "冻结客户账户")
    @PutMapping("/{id}/freeze")
    public Result<String> freezeCustomer(
            @Parameter(description = "客户ID", example = "1") @PathVariable @NotNull Long id,
            @Parameter(description = "冻结原因") @RequestParam String reason) {
        boolean success = customerService.freezeCustomer(id, reason);
        return success ? Result.success("客户冻结成功") : Result.error("客户冻结失败");
    }

    /**
     * 解冻客户
     */
    @Operation(summary = "解冻客户", description = "解冻客户账户")
    @PutMapping("/{id}/unfreeze")
    public Result<String> unfreezeCustomer(
            @Parameter(description = "客户ID", example = "1") @PathVariable @NotNull Long id) {
        boolean success = customerService.unfreezeCustomer(id);
        return success ? Result.success("客户解冻成功") : Result.error("客户解冻失败");
    }

    /**
     * 升级VIP等级
     */
    @Operation(summary = "升级VIP等级", description = "升级客户VIP等级")
    @PutMapping("/{id}/upgrade-vip")
    public Result<String> upgradeVipLevel(
            @Parameter(description = "客户ID", example = "1") @PathVariable @NotNull Long id,
            @Parameter(description = "新VIP等级", example = "金牌") @RequestParam String vipLevel) {
        boolean success = customerService.upgradeVipLevel(id, vipLevel);
        return success ? Result.success("VIP等级升级成功") : Result.error("VIP等级升级失败");
    }

    /**
     * 分配客户经理
     */
    @Operation(summary = "分配客户经理", description = "为客户分配客户经理")
    @PutMapping("/{id}/assign-manager")
    public Result<String> assignManager(
            @Parameter(description = "客户ID", example = "1") @PathVariable @NotNull Long id,
            @Parameter(description = "客户经理ID", example = "3") @RequestParam @NotNull Long managerId) {
        boolean success = customerService.assignManager(id, managerId);
        return success ? Result.success("客户经理分配成功") : Result.error("客户经理分配失败");
    }

    /**
     * 查询余额不足的客户
     */
    @Operation(summary = "查询余额不足的客户", description = "获取余额低于指定阈值的客户列表")
    @GetMapping("/low-balance")
    public Result<List<Customer>> getLowBalanceCustomers(
            @Parameter(description = "最小余额阈值", example = "100.00") @RequestParam(defaultValue = "100.00") BigDecimal minBalance) {
        List<Customer> customers = customerService.getLowBalanceCustomers(minBalance);
        return Result.success(customers);
    }

    /**
     * 获取客户统计信息
     */
    @Operation(summary = "获取客户统计信息", description = "获取客户的各种统计数据")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getCustomerStatistics() {
        Map<String, Object> statistics = customerService.getCustomerStatistics();
        return Result.success(statistics);
    }

    /**
     * 验证账号是否唯一
     */
    @Operation(summary = "验证账号唯一性", description = "检查客户账号是否已存在")
    @GetMapping("/validate/account")
    public Result<Boolean> validateAccount(
            @Parameter(description = "客户账号", example = "user001") @RequestParam String account,
            @Parameter(description = "排除的客户ID（用于更新时验证）") @RequestParam(required = false) Long excludeId) {
        boolean isUnique = customerService.isAccountUnique(account, excludeId);
        return Result.success(isUnique);
    }

    /**
     * 验证手机号是否唯一
     */
    @Operation(summary = "验证手机号唯一性", description = "检查手机号是否已存在")
    @GetMapping("/validate/phone")
    public Result<Boolean> validatePhone(
            @Parameter(description = "手机号", example = "13800138001") @RequestParam String phone,
            @Parameter(description = "排除的客户ID（用于更新时验证）") @RequestParam(required = false) Long excludeId) {
        boolean isUnique = customerService.isPhoneUnique(phone, excludeId);
        return Result.success(isUnique);
    }

    /**
     * 验证邮箱是否唯一
     */
    @Operation(summary = "验证邮箱唯一性", description = "检查邮箱是否已存在")
    @GetMapping("/validate/email")
    public Result<Boolean> validateEmail(
            @Parameter(description = "邮箱", example = "user@example.com") @RequestParam String email,
            @Parameter(description = "排除的客户ID（用于更新时验证）") @RequestParam(required = false) Long excludeId) {
        boolean isUnique = customerService.isEmailUnique(email, excludeId);
        return Result.success(isUnique);
    }
}