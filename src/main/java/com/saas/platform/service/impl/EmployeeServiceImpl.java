package com.saas.platform.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saas.platform.entity.Employee;
import com.saas.platform.mapper.EmployeeMapper;
import com.saas.platform.service.EmployeeService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 员工服务实现类
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Override
    public Employee getByUsername(String username) {
        if (StrUtil.isBlank(username)) {
            return null;
        }
        return baseMapper.selectByUsername(username);
    }

    @Override
    public Employee getByEmployeeNo(String employeeNo) {
        if (StrUtil.isBlank(employeeNo)) {
            return null;
        }
        return baseMapper.selectByEmployeeNo(employeeNo);
    }

    @Override
    @Cacheable(value = "employee:role", key = "#role")
    public List<Employee> getByRole(String role) {
        if (StrUtil.isBlank(role)) {
            return List.of();
        }
        return baseMapper.selectByRole(role);
    }

    @Override
    @Cacheable(value = "employee:department", key = "#departmentId")
    public List<Employee> getByDepartmentId(Long departmentId) {
        if (departmentId == null) {
            return List.of();
        }
        return baseMapper.selectByDepartmentId(departmentId);
    }

    @Override
    @Cacheable(value = "employee:manager_options")
    public List<Employee> getManagerOptions() {
        return baseMapper.selectManagerOptions();
    }

    @Override
    public boolean isUsernameExists(String username, Long excludeId) {
        if (StrUtil.isBlank(username)) {
            return false;
        }
        
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername, username);
        
        if (excludeId != null) {
            wrapper.ne(Employee::getId, excludeId);
        }
        
        return this.count(wrapper) > 0;
    }

    @Override
    public boolean isEmployeeNoExists(String employeeNo, Long excludeId) {
        if (StrUtil.isBlank(employeeNo)) {
            return false;
        }
        
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getEmployeeNo, employeeNo);
        
        if (excludeId != null) {
            wrapper.ne(Employee::getId, excludeId);
        }
        
        return this.count(wrapper) > 0;
    }
}