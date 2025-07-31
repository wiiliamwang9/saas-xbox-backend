package com.saas.platform.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.saas.platform.dto.IntentionCustomerDTO;
import com.saas.platform.dto.IntentionCustomerQuery;
import com.saas.platform.dto.Result;
import com.saas.platform.entity.IntentionCustomer;
import com.saas.platform.service.IntentionCustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 意向客户管理控制器
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Tag(name = "意向客户管理", description = "意向客户管理相关接口")
@RestController
@RequestMapping("/admin/intention-customer")
@Validated
public class IntentionCustomerController {

    @Autowired
    private IntentionCustomerService intentionCustomerService;

    /**
     * 分页查询意向客户列表
     */
    @Operation(summary = "分页查询意向客户列表", description = "根据条件分页查询意向客户信息")
    @GetMapping("/page")
    public Result<IPage<IntentionCustomer>> page(@Valid IntentionCustomerQuery query) {
        IPage<IntentionCustomer> page = intentionCustomerService.pageQuery(query);
        return Result.success(page);
    }

    /**
     * 根据ID查询意向客户详情
     */
    @Operation(summary = "查询意向客户详情", description = "根据ID查询意向客户详细信息")
    @GetMapping("/{id}")
    public Result<IntentionCustomer> getById(
            @Parameter(description = "客户ID", required = true) @PathVariable @NotNull Long id) {
        IntentionCustomer customer = intentionCustomerService.getById(id);
        return Result.success(customer);
    }

    /**
     * 新增意向客户
     */
    @Operation(summary = "新增意向客户", description = "添加新的意向客户信息")
    @PostMapping("/save")
    public Result<Void> save(@Valid @RequestBody IntentionCustomerDTO dto) {
        boolean success = intentionCustomerService.saveIntentionCustomer(dto);
        return success ? Result.success("新增成功") : Result.error("新增失败");
    }

    /**
     * 更新意向客户
     */
    @Operation(summary = "更新意向客户", description = "更新意向客户信息")
    @PutMapping("/update")
    public Result<Void> update(@Valid @RequestBody IntentionCustomerDTO dto) {
        boolean success = intentionCustomerService.updateIntentionCustomer(dto);
        return success ? Result.success("更新成功") : Result.error("更新失败");
    }

    /**
     * 删除意向客户
     */
    @Operation(summary = "删除意向客户", description = "根据ID删除意向客户（软删除）")
    @DeleteMapping("/{id}")
    public Result<Void> delete(
            @Parameter(description = "客户ID", required = true) @PathVariable @NotNull Long id) {
        boolean success = intentionCustomerService.deleteIntentionCustomer(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 批量删除意向客户
     */
    @Operation(summary = "批量删除意向客户", description = "根据ID列表批量删除意向客户（软删除）")
    @DeleteMapping("/batch")
    public Result<Void> batchDelete(
            @Parameter(description = "客户ID列表", required = true) @RequestBody @NotEmpty List<Long> ids) {
        boolean success = intentionCustomerService.batchDeleteIntentionCustomer(ids);
        return success ? Result.success("批量删除成功") : Result.error("批量删除失败");
    }

    /**
     * 根据手机号查询意向客户
     */
    @Operation(summary = "根据手机号查询客户", description = "根据手机号查询意向客户信息")
    @GetMapping("/phone/{phone}")
    public Result<IntentionCustomer> getByPhone(
            @Parameter(description = "手机号", required = true) @PathVariable String phone) {
        IntentionCustomer customer = intentionCustomerService.getByPhone(phone);
        return Result.success(customer);
    }

    /**
     * 根据客户经理ID查询意向客户列表
     */
    @Operation(summary = "查询客户经理的客户列表", description = "根据客户经理ID查询其负责的意向客户列表")
    @GetMapping("/manager/{managerId}")
    public Result<List<IntentionCustomer>> getByManagerId(
            @Parameter(description = "客户经理ID", required = true) @PathVariable @NotNull Long managerId) {
        List<IntentionCustomer> customers = intentionCustomerService.getByManagerId(managerId);
        return Result.success(customers);
    }

    /**
     * 统计各意向级别的客户数量
     */
    @Operation(summary = "统计客户意向级别分布", description = "统计各意向级别的客户数量")
    @GetMapping("/statistics/intention-level")
    public Result<List<Map<String, Object>>> countByIntentionLevel(
            @Parameter(description = "客户经理ID", required = false) @RequestParam(required = false) Long managerId) {
        List<Map<String, Object>> statistics = intentionCustomerService.countByIntentionLevel(managerId);
        return Result.success(statistics);
    }

    /**
     * 查询需要跟进的客户列表
     */
    @Operation(summary = "查询需要跟进的客户", description = "查询需要跟进的客户列表")
    @GetMapping("/follow-up")
    public Result<List<IntentionCustomer>> getFollowUpList(
            @Parameter(description = "客户经理ID", required = false) @RequestParam(required = false) Long managerId) {
        List<IntentionCustomer> customers = intentionCustomerService.getFollowUpList(managerId);
        return Result.success(customers);
    }

    /**
     * 导出意向客户数据
     */
    @Operation(summary = "导出客户数据", description = "根据条件导出意向客户数据")
    @GetMapping("/export")
    public Result<List<IntentionCustomer>> export(@Valid IntentionCustomerQuery query) {
        List<IntentionCustomer> customers = intentionCustomerService.exportData(query);
        return Result.success(customers);
    }

    /**
     * 验证手机号是否已存在
     */
    @Operation(summary = "验证手机号", description = "验证手机号是否已存在")
    @GetMapping("/validate/phone")
    public Result<Boolean> validatePhone(
            @Parameter(description = "手机号", required = true) @RequestParam String phone,
            @Parameter(description = "排除的客户ID", required = false) @RequestParam(required = false) Long excludeId) {
        boolean exists = intentionCustomerService.isPhoneExists(phone, excludeId);
        return Result.success(!exists); // 返回是否可用（不存在为可用）
    }
}