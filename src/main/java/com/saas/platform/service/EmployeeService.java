package com.saas.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.saas.platform.entity.Employee;

import java.util.List;

/**
 * 员工服务接口
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
public interface EmployeeService extends IService<Employee> {

    /**
     * 根据用户名查询员工
     * 
     * @param username 用户名
     * @return 员工信息
     */
    Employee getByUsername(String username);

    /**
     * 根据员工号查询员工
     * 
     * @param employeeNo 员工号
     * @return 员工信息
     */
    Employee getByEmployeeNo(String employeeNo);

    /**
     * 根据角色查询员工列表
     * 
     * @param role 角色
     * @return 员工列表
     */
    List<Employee> getByRole(String role);

    /**
     * 根据部门ID查询员工列表
     * 
     * @param departmentId 部门ID
     * @return 员工列表
     */
    List<Employee> getByDepartmentId(Long departmentId);

    /**
     * 获取客户经理选项列表
     * 只返回角色为客户经理且状态正常的员工
     * 
     * @return 客户经理列表
     */
    List<Employee> getManagerOptions();

    /**
     * 验证用户名是否已存在
     * 
     * @param username 用户名
     * @param excludeId 排除的员工ID（用于更新时的验证）
     * @return 是否存在
     */
    boolean isUsernameExists(String username, Long excludeId);

    /**
     * 验证员工号是否已存在
     * 
     * @param employeeNo 员工号
     * @param excludeId 排除的员工ID（用于更新时的验证）
     * @return 是否存在
     */
    boolean isEmployeeNoExists(String employeeNo, Long excludeId);
}