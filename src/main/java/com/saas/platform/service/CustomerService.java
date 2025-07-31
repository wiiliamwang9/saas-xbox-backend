package com.saas.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.saas.platform.entity.Customer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 客户服务接口
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
public interface CustomerService extends IService<Customer> {

    /**
     * 分页查询客户列表
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param customerName 客户名称
     * @param customerType 客户类型
     * @param vipLevel VIP等级
     * @param customerStatus 客户状态
     * @param managerId 客户经理ID
     * @return 分页结果
     */
    IPage<Customer> getCustomerPage(Long current, Long size, String customerName, String customerType, 
                                   String vipLevel, String customerStatus, Long managerId);

    /**
     * 根据账号查询客户
     * 
     * @param account 客户账号
     * @return 客户信息
     */
    Customer getByAccount(String account);

    /**
     * 根据手机号查询客户
     * 
     * @param phone 手机号
     * @return 客户信息
     */
    Customer getByPhone(String phone);

    /**
     * 根据邮箱查询客户
     * 
     * @param email 邮箱
     * @return 客户信息
     */
    Customer getByEmail(String email);

    /**
     * 查询某客户经理的客户列表
     * 
     * @param managerId 客户经理ID
     * @return 客户列表
     */
    List<Customer> getByManagerId(Long managerId);

    /**
     * 根据父账户ID查询子账户列表
     * 
     * @param parentId 父账户ID
     * @return 子账户列表
     */
    List<Customer> getByParentId(Long parentId);

    /**
     * 创建客户
     * 
     * @param customer 客户信息
     * @return 是否成功
     */
    boolean createCustomer(Customer customer);

    /**
     * 更新客户信息
     * 
     * @param customer 客户信息
     * @return 是否成功
     */
    boolean updateCustomer(Customer customer);

    /**
     * 删除客户
     * 
     * @param id 客户ID
     * @return 是否成功
     */
    boolean deleteCustomer(Long id);

    /**
     * 批量删除客户
     * 
     * @param ids ID列表
     * @return 是否成功
     */
    boolean batchDeleteCustomers(List<Long> ids);

    /**
     * 批量更新客户状态
     * 
     * @param ids ID列表
     * @param status 新状态
     * @return 是否成功
     */
    boolean batchUpdateStatus(List<Long> ids, String status);

    /**
     * 客户充值
     * 
     * @param customerId 客户ID
     * @param amount 充值金额
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @param remark 备注
     * @return 是否成功
     */
    boolean recharge(Long customerId, BigDecimal amount, Long operatorId, String operatorName, String remark);

    /**
     * 客户消费
     * 
     * @param customerId 客户ID
     * @param amount 消费金额
     * @param orderId 关联订单ID
     * @param remark 备注
     * @return 是否成功
     */
    boolean consume(Long customerId, BigDecimal amount, Long orderId, String remark);

    /**
     * 余额调整
     * 
     * @param customerId 客户ID
     * @param amount 调整金额（正数增加，负数减少）
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @param remark 备注
     * @return 是否成功
     */
    boolean adjustBalance(Long customerId, BigDecimal amount, Long operatorId, String operatorName, String remark);

    /**
     * 冻结客户
     * 
     * @param id 客户ID
     * @param reason 冻结原因
     * @return 是否成功
     */
    boolean freezeCustomer(Long id, String reason);

    /**
     * 解冻客户
     * 
     * @param id 客户ID
     * @return 是否成功
     */
    boolean unfreezeCustomer(Long id);

    /**
     * 升级VIP等级
     * 
     * @param id 客户ID
     * @param vipLevel 新VIP等级
     * @return 是否成功
     */
    boolean upgradeVipLevel(Long id, String vipLevel);

    /**
     * 分配客户经理
     * 
     * @param customerId 客户ID
     * @param managerId 客户经理ID
     * @return 是否成功
     */
    boolean assignManager(Long customerId, Long managerId);

    /**
     * 查询余额不足的客户
     * 
     * @param minBalance 最小余额阈值
     * @return 客户列表
     */
    List<Customer> getLowBalanceCustomers(BigDecimal minBalance);

    /**
     * 获取客户统计信息
     * 
     * @return 统计结果
     */
    Map<String, Object> getCustomerStatistics();

    /**
     * 验证账号是否唯一
     * 
     * @param account 客户账号
     * @param excludeId 排除的ID（用于更新时验证）
     * @return 是否唯一
     */
    boolean isAccountUnique(String account, Long excludeId);

    /**
     * 验证手机号是否唯一
     * 
     * @param phone 手机号
     * @param excludeId 排除的ID（用于更新时验证）
     * @return 是否唯一
     */
    boolean isPhoneUnique(String phone, Long excludeId);

    /**
     * 验证邮箱是否唯一
     * 
     * @param email 邮箱
     * @param excludeId 排除的ID（用于更新时验证）
     * @return 是否唯一
     */
    boolean isEmailUnique(String email, Long excludeId);
}