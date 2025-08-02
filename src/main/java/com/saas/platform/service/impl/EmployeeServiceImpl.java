package com.saas.platform.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saas.platform.entity.Employee;
import com.saas.platform.mapper.EmployeeMapper;
import com.saas.platform.service.EmployeeService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public IPage<Employee> getEmployeePage(Long current, Long size, String realName, String role, String employeeStatus, Long departmentId) {
        Page<Employee> page = new Page<>(current, size);
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        
        if (StrUtil.isNotBlank(realName)) {
            wrapper.like(Employee::getRealName, realName);
        }
        if (StrUtil.isNotBlank(role)) {
            wrapper.eq(Employee::getRole, role);
        }
        if (StrUtil.isNotBlank(employeeStatus)) {
            wrapper.eq(Employee::getEmployeeStatus, employeeStatus);
        }
        if (departmentId != null) {
            wrapper.eq(Employee::getDepartmentId, departmentId);
        }
        
        wrapper.orderByDesc(Employee::getCreatedAt);
        return this.page(page, wrapper);
    }

    @Override
    @CacheEvict(value = {"employee:role", "employee:department", "employee:manager_options"}, allEntries = true)
    public boolean createEmployee(Employee employee) {
        if (employee == null) {
            return false;
        }
        
        // 设置默认值
        if (StrUtil.isBlank(employee.getEmployeeStatus())) {
            employee.setEmployeeStatus("正常");
        }
        if (employee.getHireDate() == null) {
            employee.setHireDate(LocalDate.now());
        }
        
        // 密码加密
        if (StrUtil.isNotBlank(employee.getPassword())) {
            employee.setPassword(BCrypt.hashpw(employee.getPassword(), BCrypt.gensalt()));
        }
        
        return this.save(employee);
    }

    @Override
    @CacheEvict(value = {"employee:role", "employee:department", "employee:manager_options"}, allEntries = true)
    public boolean updateEmployee(Employee employee) {
        if (employee == null || employee.getId() == null) {
            return false;
        }
        
        // 如果有新密码，进行加密
        if (StrUtil.isNotBlank(employee.getPassword())) {
            employee.setPassword(BCrypt.hashpw(employee.getPassword(), BCrypt.gensalt()));
        } else {
            // 如果没有新密码，不更新密码字段
            employee.setPassword(null);
        }
        
        return this.updateById(employee);
    }

    @Override
    @CacheEvict(value = {"employee:role", "employee:department", "employee:manager_options"}, allEntries = true)
    public boolean deleteEmployee(Long id) {
        if (id == null) {
            return false;
        }
        return this.removeById(id);
    }

    @Override
    @CacheEvict(value = {"employee:role", "employee:department", "employee:manager_options"}, allEntries = true)
    public boolean batchDeleteEmployees(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        return this.removeByIds(ids);
    }

    @Override
    @CacheEvict(value = {"employee:role", "employee:department", "employee:manager_options"}, allEntries = true)
    public boolean batchUpdateStatus(List<Long> ids, String status) {
        if (ids == null || ids.isEmpty() || StrUtil.isBlank(status)) {
            return false;
        }
        
        LambdaUpdateWrapper<Employee> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Employee::getId, ids);
        updateWrapper.set(Employee::getEmployeeStatus, status);
        updateWrapper.set(Employee::getUpdatedAt, LocalDateTime.now());
        
        return this.update(updateWrapper);
    }

    @Override
    public boolean resetPassword(Long id) {
        if (id == null) {
            return false;
        }
        
        // 默认密码: 123456
        String defaultPassword = BCrypt.hashpw("123456", BCrypt.gensalt());
        
        LambdaUpdateWrapper<Employee> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Employee::getId, id);
        updateWrapper.set(Employee::getPassword, defaultPassword);
        updateWrapper.set(Employee::getUpdatedAt, LocalDateTime.now());
        
        return this.update(updateWrapper);
    }

    @Override
    public Map<String, Object> getEmployeeStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // 总员工数
        long totalCount = this.count();
        statistics.put("totalCount", totalCount);
        
        // 在职员工数
        long activeCount = this.count(new LambdaQueryWrapper<Employee>()
                .eq(Employee::getEmployeeStatus, "正常"));
        statistics.put("activeCount", activeCount);
        
        // 离职员工数
        long inactiveCount = this.count(new LambdaQueryWrapper<Employee>()
                .eq(Employee::getEmployeeStatus, "离职"));
        statistics.put("inactiveCount", inactiveCount);
        
        // 停用员工数
        long disabledCount = this.count(new LambdaQueryWrapper<Employee>()
                .eq(Employee::getEmployeeStatus, "停用"));
        statistics.put("disabledCount", disabledCount);
        
        // 按角色统计
        List<Map<String, Object>> roleStats = baseMapper.getEmployeeCountByRole();
        statistics.put("roleStats", roleStats);
        
        // 按部门统计
        List<Map<String, Object>> deptStats = baseMapper.getEmployeeCountByDepartment();
        statistics.put("deptStats", deptStats);
        
        return statistics;
    }

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