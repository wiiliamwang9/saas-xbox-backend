package com.saas.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.saas.platform.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    Employee selectByUsername(@Param("username") String username);

    /**
     * 根据员工号查询员工
     * 
     * @param employeeNo 员工号
     * @return 员工信息
     */
    Employee selectByEmployeeNo(@Param("employeeNo") String employeeNo);

    /**
     * 根据角色查询员工列表
     * 
     * @param role 角色
     * @return 员工列表
     */
    List<Employee> selectByRole(@Param("role") String role);

    /**
     * 根据部门ID查询员工列表
     * 
     * @param departmentId 部门ID
     * @return 员工列表
     */
    List<Employee> selectByDepartmentId(@Param("departmentId") Long departmentId);

    /**
     * 获取客户经理选项列表
     * 只返回角色为客户经理且状态正常的员工
     * 
     * @return 客户经理列表
     */
    List<Employee> selectManagerOptions();

    /**
     * 按角色统计员工数量
     * 
     * @return 角色统计列表
     */
    List<Map<String, Object>> getEmployeeCountByRole();

    /**
     * 按部门统计员工数量
     * 
     * @return 部门统计列表
     */
    List<Map<String, Object>> getEmployeeCountByDepartment();
}