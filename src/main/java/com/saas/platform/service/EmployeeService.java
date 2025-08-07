package com.saas.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.saas.platform.entity.Employee;

import java.util.List;
import java.util.Map;

/**
 * 员工服务接口
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
public interface EmployeeService extends IService<Employee> {

    /**
     * 分页查询员工列表
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param employeeNo 员工号
     * @param username 用户名
     * @param realName 员工姓名
     * @param phone 手机号
     * @param email 邮箱
     * @param role 角色
     * @param employeeStatus 员工状态
     * @param departmentId 部门ID
     * @return 员工分页数据
     */
    IPage<Employee> getEmployeePage(Long current, Long size, String employeeNo, String username, String realName, String phone, String email, String role, String employeeStatus, Long departmentId);

    /**
     * 创建员工
     * 
     * @param employee 员工信息
     * @return 是否成功
     */
    boolean createEmployee(Employee employee);

    /**
     * 更新员工信息
     * 
     * @param employee 员工信息
     * @return 是否成功
     */
    boolean updateEmployee(Employee employee);

    /**
     * 删除员工
     * 
     * @param id 员工ID
     * @return 是否成功
     */
    boolean deleteEmployee(Long id);

    /**
     * 批量删除员工
     * 
     * @param ids 员工ID列表
     * @return 是否成功
     */
    boolean batchDeleteEmployees(List<Long> ids);

    /**
     * 批量更新员工状态
     * 
     * @param ids 员工ID列表
     * @param status 新状态
     * @return 是否成功
     */
    boolean batchUpdateStatus(List<Long> ids, String status);

    /**
     * 重置员工密码
     * 
     * @param id 员工ID
     * @return 是否成功
     */
    boolean resetPassword(Long id);

    /**
     * 获取员工统计信息
     * 
     * @return 统计数据
     */
    Map<String, Object> getEmployeeStatistics();

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