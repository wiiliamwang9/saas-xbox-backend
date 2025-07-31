package com.saas.platform.controller;

import com.saas.platform.dto.Result;
import com.saas.platform.entity.Employee;
import com.saas.platform.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/admin/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

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