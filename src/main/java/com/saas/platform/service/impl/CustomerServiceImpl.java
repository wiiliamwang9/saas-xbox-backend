package com.saas.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saas.platform.entity.Customer;
import com.saas.platform.exception.BusinessException;
import com.saas.platform.mapper.CustomerMapper;
import com.saas.platform.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户服务实现类
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    @Override
    public IPage<Customer> getCustomerPage(Long current, Long size, String customerName, String customerType, 
                                         String vipLevel, String customerStatus, Long managerId) {
        Page<Customer> page = new Page<>(current, size);
        return baseMapper.selectCustomerPage(page, customerName, customerType, vipLevel, customerStatus, managerId);
    }

    @Override
    public Customer getByAccount(String account) {
        if (!StringUtils.hasText(account)) {
            return null;
        }
        return baseMapper.selectByAccount(account);
    }

    @Override
    public Customer getByPhone(String phone) {
        if (!StringUtils.hasText(phone)) {
            return null;
        }
        return baseMapper.selectByPhone(phone);
    }

    @Override
    public Customer getByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return null;
        }
        return baseMapper.selectByEmail(email);
    }

    @Override
    public List<Customer> getByManagerId(Long managerId) {
        if (managerId == null) {
            return null;
        }
        return baseMapper.selectByManagerId(managerId);
    }

    @Override
    public List<Customer> getByParentId(Long parentId) {
        if (parentId == null) {
            return null;
        }
        return baseMapper.selectByParentId(parentId);
    }

    @Override
    @Transactional
    public boolean createCustomer(Customer customer) {
        // 验证账号唯一性
        if (!isAccountUnique(customer.getAccount(), null)) {
            throw new BusinessException("客户账号已存在");
        }
        
        // 验证手机号唯一性
        if (StringUtils.hasText(customer.getPhone()) && !isPhoneUnique(customer.getPhone(), null)) {
            throw new BusinessException("手机号已存在");
        }
        
        // 验证邮箱唯一性
        if (StringUtils.hasText(customer.getEmail()) && !isEmailUnique(customer.getEmail(), null)) {
            throw new BusinessException("邮箱已存在");
        }
        
        // 设置默认值
        if (customer.getBalance() == null) {
            customer.setBalance(BigDecimal.ZERO);
        }
        if (!StringUtils.hasText(customer.getCustomerType())) {
            customer.setCustomerType("个人");
        }
        if (!StringUtils.hasText(customer.getVipLevel())) {
            customer.setVipLevel("普通");
        }
        if (!StringUtils.hasText(customer.getCustomerStatus())) {
            customer.setCustomerStatus("正常");
        }
        
        return save(customer);
    }

    @Override
    @Transactional
    public boolean updateCustomer(Customer customer) {
        // 验证客户是否存在
        Customer existCustomer = getById(customer.getId());
        if (existCustomer == null) {
            throw new BusinessException("客户不存在");
        }
        
        // 验证账号唯一性
        if (!isAccountUnique(customer.getAccount(), customer.getId())) {
            throw new BusinessException("客户账号已存在");
        }
        
        // 验证手机号唯一性
        if (StringUtils.hasText(customer.getPhone()) && !isPhoneUnique(customer.getPhone(), customer.getId())) {
            throw new BusinessException("手机号已存在");
        }
        
        // 验证邮箱唯一性
        if (StringUtils.hasText(customer.getEmail()) && !isEmailUnique(customer.getEmail(), customer.getId())) {
            throw new BusinessException("邮箱已存在");
        }
        
        return updateById(customer);
    }

    @Override
    @Transactional
    public boolean deleteCustomer(Long id) {
        Customer customer = getById(id);
        if (customer == null) {
            throw new BusinessException("客户不存在");
        }
        
        // 检查是否有子账户
        List<Customer> subCustomers = getByParentId(id);
        if (subCustomers != null && !subCustomers.isEmpty()) {
            throw new BusinessException("存在子账户，无法删除");
        }
        
        // TODO: 检查是否有未完成的订单
        
        return removeById(id);
    }

    @Override
    @Transactional
    public boolean batchDeleteCustomers(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        
        // TODO: 批量检查是否有子账户和未完成的订单
        
        return removeByIds(ids);
    }

    @Override
    @Transactional
    public boolean batchUpdateStatus(List<Long> ids, String status) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        
        return baseMapper.batchUpdateStatus(ids, status) > 0;
    }

    @Override
    @Transactional
    public boolean recharge(Long customerId, BigDecimal amount, Long operatorId, String operatorName, String remark) {
        if (customerId == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("参数错误");
        }
        
        Customer customer = getById(customerId);
        if (customer == null) {
            throw new BusinessException("客户不存在");
        }
        
        if (!"正常".equals(customer.getCustomerStatus())) {
            throw new BusinessException("客户状态异常，无法充值");
        }
        
        // 更新余额
        int result = baseMapper.updateBalance(customerId, amount);
        if (result <= 0) {
            throw new BusinessException("充值失败");
        }
        
        // TODO: 记录充值日志
        
        return true;
    }

    @Override
    @Transactional
    public boolean consume(Long customerId, BigDecimal amount, Long orderId, String remark) {
        if (customerId == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("参数错误");
        }
        
        Customer customer = getById(customerId);
        if (customer == null) {
            throw new BusinessException("客户不存在");
        }
        
        if (customer.getBalance().compareTo(amount) < 0) {
            throw new BusinessException("余额不足");
        }
        
        // 扣减余额
        int result = baseMapper.updateBalance(customerId, amount.negate());
        if (result <= 0) {
            throw new BusinessException("扣费失败");
        }
        
        // TODO: 记录消费日志
        
        return true;
    }

    @Override
    @Transactional
    public boolean adjustBalance(Long customerId, BigDecimal amount, Long operatorId, String operatorName, String remark) {
        if (customerId == null || amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
            throw new BusinessException("参数错误");
        }
        
        Customer customer = getById(customerId);
        if (customer == null) {
            throw new BusinessException("客户不存在");
        }
        
        // 如果是扣减余额，检查余额是否足够
        if (amount.compareTo(BigDecimal.ZERO) < 0 && customer.getBalance().add(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("调整后余额不能为负数");
        }
        
        // 调整余额
        int result = baseMapper.updateBalance(customerId, amount);
        if (result <= 0) {
            throw new BusinessException("余额调整失败");
        }
        
        // TODO: 记录调整日志
        
        return true;
    }

    @Override
    @Transactional
    public boolean freezeCustomer(Long id, String reason) {
        Customer customer = getById(id);
        if (customer == null) {
            throw new BusinessException("客户不存在");
        }
        
        if ("冻结".equals(customer.getCustomerStatus())) {
            throw new BusinessException("客户已经是冻结状态");
        }
        
        customer.setCustomerStatus("冻结");
        return updateById(customer);
    }

    @Override
    @Transactional
    public boolean unfreezeCustomer(Long id) {
        Customer customer = getById(id);
        if (customer == null) {
            throw new BusinessException("客户不存在");
        }
        
        if (!"冻结".equals(customer.getCustomerStatus())) {
            throw new BusinessException("客户不是冻结状态");
        }
        
        customer.setCustomerStatus("正常");
        return updateById(customer);
    }

    @Override
    @Transactional
    public boolean upgradeVipLevel(Long id, String vipLevel) {
        Customer customer = getById(id);
        if (customer == null) {
            throw new BusinessException("客户不存在");
        }
        
        customer.setVipLevel(vipLevel);
        return updateById(customer);
    }

    @Override
    @Transactional
    public boolean assignManager(Long customerId, Long managerId) {
        Customer customer = getById(customerId);
        if (customer == null) {
            throw new BusinessException("客户不存在");
        }
        
        customer.setManagerId(managerId);
        return updateById(customer);
    }

    @Override
    public List<Customer> getLowBalanceCustomers(BigDecimal minBalance) {
        if (minBalance == null) {
            minBalance = BigDecimal.ZERO;
        }
        return baseMapper.selectLowBalanceCustomers(minBalance);
    }

    @Override
    public Map<String, Object> getCustomerStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        // 总客户数
        long totalCount = count();
        result.put("totalCount", totalCount);
        
        // 各状态客户数量
        List<Map<String, Object>> statusCount = baseMapper.countByStatus();
        result.put("statusCount", statusCount);
        
        // 各VIP等级客户数量
        List<Map<String, Object>> vipLevelCount = baseMapper.countByVipLevel();
        result.put("vipLevelCount", vipLevelCount);
        
        // 各注册来源客户数量
        List<Map<String, Object>> registerSourceCount = baseMapper.countByRegisterSource();
        result.put("registerSourceCount", registerSourceCount);
        
        // 正常客户数
        long normalCount = count(new LambdaQueryWrapper<Customer>()
                .eq(Customer::getCustomerStatus, "正常"));
        result.put("normalCount", normalCount);
        
        // 冻结客户数
        long frozenCount = count(new LambdaQueryWrapper<Customer>()
                .eq(Customer::getCustomerStatus, "冻结"));
        result.put("frozenCount", frozenCount);
        
        return result;
    }

    @Override
    public boolean isAccountUnique(String account, Long excludeId) {
        if (!StringUtils.hasText(account)) {
            return false;
        }
        
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Customer::getAccount, account);
        
        if (excludeId != null) {
            wrapper.ne(Customer::getId, excludeId);
        }
        
        return count(wrapper) == 0;
    }

    @Override
    public boolean isPhoneUnique(String phone, Long excludeId) {
        if (!StringUtils.hasText(phone)) {
            return true; // 手机号为空时认为是唯一的
        }
        
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Customer::getPhone, phone);
        
        if (excludeId != null) {
            wrapper.ne(Customer::getId, excludeId);
        }
        
        return count(wrapper) == 0;
    }

    @Override
    public boolean isEmailUnique(String email, Long excludeId) {
        if (!StringUtils.hasText(email)) {
            return true; // 邮箱为空时认为是唯一的
        }
        
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Customer::getEmail, email);
        
        if (excludeId != null) {
            wrapper.ne(Customer::getId, excludeId);
        }
        
        return count(wrapper) == 0;
    }
}