package com.saas.platform.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.saas.platform.dto.Result;
import com.saas.platform.entity.Employee;
import com.saas.platform.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 员工管理控制器
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Tag(name = "员工管理", description = "员工管理相关接口")
@RestController
@RequestMapping("/employees")
@Validated
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 分页查询员工列表
     */
    @Operation(summary = "分页查询员工列表", description = "支持按员工号、用户名、员工姓名、手机号、邮箱、角色、状态、部门筛选")
    @GetMapping("/page")
    public Result<IPage<Employee>> getEmployeePage(
            @Parameter(description = "当前页", example = "1") @RequestParam(defaultValue = "1") Long current,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") Long size,
            @Parameter(description = "员工号") @RequestParam(required = false) String employeeNo,
            @Parameter(description = "用户名") @RequestParam(required = false) String username,
            @Parameter(description = "员工姓名") @RequestParam(required = false) String realName,
            @Parameter(description = "手机号") @RequestParam(required = false) String phone,
            @Parameter(description = "邮箱") @RequestParam(required = false) String email,
            @Parameter(description = "角色") @RequestParam(required = false) String role,
            @Parameter(description = "员工状态") @RequestParam(required = false) String employeeStatus,
            @Parameter(description = "部门ID") @RequestParam(required = false) Long departmentId) {
        IPage<Employee> page = employeeService.getEmployeePage(current, size, employeeNo, username, realName, phone, email, role, employeeStatus, departmentId);
        return Result.success(page);
    }

    /**
     * 创建员工
     */
    @Operation(summary = "创建员工", description = "添加新员工")
    @PostMapping
    public Result<String> createEmployee(@Valid @RequestBody Employee employee) {
        boolean success = employeeService.createEmployee(employee);
        return success ? Result.success("员工创建成功") : Result.error("员工创建失败");
    }

    /**
     * 更新员工信息
     */
    @Operation(summary = "更新员工信息", description = "修改员工信息")
    @PutMapping("/{id}")
    public Result<String> updateEmployee(
            @Parameter(description = "员工ID", example = "1") @PathVariable @NotNull Long id,
            @Valid @RequestBody Employee employee) {
        employee.setId(id);
        boolean success = employeeService.updateEmployee(employee);
        return success ? Result.success("员工更新成功") : Result.error("员工更新失败");
    }

    /**
     * 删除员工
     */
    @Operation(summary = "删除员工", description = "根据ID删除员工")
    @DeleteMapping("/{id}")
    public Result<String> deleteEmployee(
            @Parameter(description = "员工ID", example = "1") @PathVariable @NotNull Long id) {
        boolean success = employeeService.deleteEmployee(id);
        return success ? Result.success("员工删除成功") : Result.error("员工删除失败");
    }

    /**
     * 批量删除员工
     */
    @Operation(summary = "批量删除员工", description = "根据ID列表批量删除员工")
    @DeleteMapping("/batch")
    public Result<String> batchDeleteEmployees(@RequestBody @NotEmpty List<Long> ids) {
        boolean success = employeeService.batchDeleteEmployees(ids);
        return success ? Result.success("员工批量删除成功") : Result.error("员工批量删除失败");
    }

    /**
     * 批量更新员工状态
     */
    @Operation(summary = "批量更新员工状态", description = "批量修改员工状态")
    @PutMapping("/batch/status")
    public Result<String> batchUpdateStatus(
            @RequestBody @NotEmpty List<Long> ids,
            @Parameter(description = "新状态", example = "正常") @RequestParam String status) {
        boolean success = employeeService.batchUpdateStatus(ids, status);
        return success ? Result.success("员工状态批量更新成功") : Result.error("员工状态批量更新失败");
    }

    /**
     * 重置员工密码
     */
    @Operation(summary = "重置员工密码", description = "重置员工密码为默认密码")
    @PutMapping("/{id}/reset-password")
    public Result<String> resetPassword(
            @Parameter(description = "员工ID", example = "1") @PathVariable @NotNull Long id) {
        boolean success = employeeService.resetPassword(id);
        return success ? Result.success("密码重置成功") : Result.error("密码重置失败");
    }

    /**
     * 获取员工统计信息
     */
    @Operation(summary = "获取员工统计信息", description = "获取员工的各种统计数据")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getEmployeeStatistics() {
        Map<String, Object> statistics = employeeService.getEmployeeStatistics();
        return Result.success(statistics);
    }

    /**
     * 获取客户经理选项列表
     */
    @Operation(summary = "获取客户经理选项", description = "获取客户经理下拉选择列表")
    @GetMapping("/options")
    public Result<List<Map<String, Object>>> getManagerOptions(
            @Parameter(description = "角色过滤", required = false) @RequestParam(defaultValue = "客户经理") String role) {
        List<Employee> employees = employeeService.getByRole(role);
        
        List<Map<String, Object>> options = employees.stream()
                .map(employee -> {
                    Map<String, Object> option = new java.util.HashMap<>();
                    option.put("value", employee.getId());
                    option.put("label", employee.getRealName());
                    option.put("employeeNo", employee.getEmployeeNo());
                    option.put("phone", employee.getPhone() != null ? employee.getPhone() : "");
                    option.put("email", employee.getEmail() != null ? employee.getEmail() : "");
                    return option;
                })
                .collect(Collectors.toList());
        
        return Result.success(options);
    }

    /**
     * 根据角色获取员工列表
     */
    @Operation(summary = "根据角色查询员工", description = "根据角色查询员工列表")
    @GetMapping("/role/{role}")
    public Result<List<Employee>> getByRole(
            @Parameter(description = "员工角色", required = true) @PathVariable String role) {
        List<Employee> employees = employeeService.getByRole(role);
        return Result.success(employees);
    }

    /**
     * 根据部门ID获取员工列表
     */
    @Operation(summary = "根据部门查询员工", description = "根据部门ID查询员工列表")
    @GetMapping("/department/{departmentId}")
    public Result<List<Employee>> getByDepartmentId(
            @Parameter(description = "部门ID", required = true) @PathVariable Long departmentId) {
        List<Employee> employees = employeeService.getByDepartmentId(departmentId);
        return Result.success(employees);
    }

    /**
     * 根据ID查询员工详情
     */
    @Operation(summary = "查询员工详情", description = "根据ID查询员工详细信息")
    @GetMapping("/{id}")
    public Result<Employee> getById(
            @Parameter(description = "员工ID", required = true) @PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            // 移除密码字段
            employee.setPassword(null);
        }
        return Result.success(employee);
    }

    /**
     * 验证用户名是否已存在
     */
    @Operation(summary = "验证用户名", description = "验证用户名是否已存在")
    @GetMapping("/validate/username")
    public Result<Boolean> validateUsername(
            @Parameter(description = "用户名", required = true) @RequestParam String username,
            @Parameter(description = "排除的员工ID", required = false) @RequestParam(required = false) Long excludeId) {
        boolean exists = employeeService.isUsernameExists(username, excludeId);
        return Result.success(!exists); // 返回是否可用（不存在为可用）
    }

    /**
     * 验证员工号是否已存在
     */
    @Operation(summary = "验证员工号", description = "验证员工号是否已存在")
    @GetMapping("/validate/employee-no")
    public Result<Boolean> validateEmployeeNo(
            @Parameter(description = "员工号", required = true) @RequestParam String employeeNo,
            @Parameter(description = "排除的员工ID", required = false) @RequestParam(required = false) Long excludeId) {
        boolean exists = employeeService.isEmployeeNoExists(employeeNo, excludeId);
        return Result.success(!exists); // 返回是否可用（不存在为可用）
    }
}