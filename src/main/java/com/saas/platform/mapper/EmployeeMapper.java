package com.saas.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.saas.platform.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 员工 Mapper 接口
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

    /**
     * 根据用户名查询员工
     * 
     * @param username 用户名
     * @return 员工信息
     */
    @Select("SELECT * FROM employees WHERE username = #{username} AND deleted_at IS NULL LIMIT 1")
    Employee selectByUsername(@Param("username") String username);

    /**
     * 根据员工号查询员工
     * 
     * @param employeeNo 员工号
     * @return 员工信息
     */
    @Select("SELECT * FROM employees WHERE employee_no = #{employeeNo} AND deleted_at IS NULL LIMIT 1")
    Employee selectByEmployeeNo(@Param("employeeNo") String employeeNo);

    /**
     * 根据角色查询员工列表
     * 
     * @param role 角色
     * @return 员工列表
     */
    @Select("SELECT * FROM employees WHERE role = #{role} AND deleted_at IS NULL ORDER BY created_at DESC")
    List<Employee> selectByRole(@Param("role") String role);

    /**
     * 根据部门ID查询员工列表
     * 
     * @param departmentId 部门ID
     * @return 员工列表
     */
    @Select("SELECT * FROM employees WHERE department_id = #{departmentId} AND deleted_at IS NULL ORDER BY created_at DESC")
    List<Employee> selectByDepartmentId(@Param("departmentId") Long departmentId);

    /**
     * 获取客户经理选项列表
     * 只返回角色为客户经理且状态正常的员工
     * 
     * @return 客户经理列表
     */
    @Select("SELECT * FROM employees WHERE role = '客户经理' AND employee_status = '正常' AND deleted_at IS NULL ORDER BY real_name")
    List<Employee> selectManagerOptions();

    /**
     * 按角色统计员工数量
     * 
     * @return 角色统计列表
     */
    @Select("SELECT role as label, COUNT(*) as value FROM employees WHERE deleted_at IS NULL GROUP BY role")
    List<Map<String, Object>> getEmployeeCountByRole();

    /**
     * 按部门统计员工数量
     * 
     * @return 部门统计列表
     */
    @Select("SELECT COALESCE(department_id, '未分配') as label, COUNT(*) as value FROM employees WHERE deleted_at IS NULL GROUP BY department_id")
    List<Map<String, Object>> getEmployeeCountByDepartment();
}