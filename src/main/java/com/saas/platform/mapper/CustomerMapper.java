package com.saas.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saas.platform.entity.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

/**
 * 客户数据访问层接口
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {

    /**
     * 分页查询客户列表
     * 
     * @param page 分页对象
     * @param customerName 客户名称
     * @param customerType 客户类型
     * @param vipLevel VIP等级
     * @param customerStatus 客户状态
     * @param managerId 客户经理ID
     * @return 分页结果
     */
    IPage<Customer> selectCustomerPage(Page<Customer> page,
                                     @Param("customerName") String customerName,
                                     @Param("customerType") String customerType,
                                     @Param("vipLevel") String vipLevel,
                                     @Param("customerStatus") String customerStatus,
                                     @Param("managerId") Long managerId);

    /**
     * 根据账号查询客户
     * 
     * @param account 客户账号
     * @return 客户信息
     */
    @Select("SELECT * FROM customers WHERE account = #{account} AND deleted_at IS NULL LIMIT 1")
    Customer selectByAccount(@Param("account") String account);

    /**
     * 根据手机号查询客户
     * 
     * @param phone 手机号
     * @return 客户信息
     */
    @Select("SELECT * FROM customers WHERE phone = #{phone} AND deleted_at IS NULL LIMIT 1")
    Customer selectByPhone(@Param("phone") String phone);

    /**
     * 根据邮箱查询客户
     * 
     * @param email 邮箱
     * @return 客户信息
     */
    @Select("SELECT * FROM customers WHERE email = #{email} AND deleted_at IS NULL LIMIT 1")
    Customer selectByEmail(@Param("email") String email);

    /**
     * 查询某客户经理的客户列表
     * 
     * @param managerId 客户经理ID
     * @return 客户列表
     */
    @Select("SELECT * FROM customers WHERE manager_id = #{managerId} AND deleted_at IS NULL ORDER BY created_at DESC")
    List<Customer> selectByManagerId(@Param("managerId") Long managerId);

    /**
     * 根据父账户ID查询子账户列表
     * 
     * @param parentId 父账户ID
     * @return 子账户列表
     */
    @Select("SELECT * FROM customers WHERE parent_id = #{parentId} AND deleted_at IS NULL ORDER BY created_at DESC")
    List<Customer> selectByParentId(@Param("parentId") Long parentId);

    /**
     * 更新客户余额
     * 
     * @param customerId 客户ID
     * @param amount 变动金额（正数增加，负数减少）
     * @return 影响行数
     */
    @Update("UPDATE customers SET balance = balance + #{amount}, updated_at = NOW() WHERE id = #{customerId} AND deleted_at IS NULL")
    int updateBalance(@Param("customerId") Long customerId, @Param("amount") BigDecimal amount);

    /**
     * 批量更新客户状态
     * 
     * @param ids ID列表
     * @param status 新状态
     * @return 影响行数
     */
    @Update("<script>UPDATE customers SET customer_status = #{status}, updated_at = NOW() WHERE id IN <foreach collection='ids' item='id' open='(' separator=',' close=')'>#{id}</foreach> AND deleted_at IS NULL</script>")
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") String status);

    /**
     * 统计各VIP等级客户数量
     * 
     * @return 统计结果
     */
    @Select("SELECT vip_level as label, COUNT(*) as value FROM customers WHERE deleted_at IS NULL GROUP BY vip_level")
    List<java.util.Map<String, Object>> countByVipLevel();

    /**
     * 统计各状态客户数量
     * 
     * @return 统计结果
     */
    @Select("SELECT customer_status as label, COUNT(*) as value FROM customers WHERE deleted_at IS NULL GROUP BY customer_status")
    List<java.util.Map<String, Object>> countByStatus();

    /**
     * 统计各注册来源客户数量
     * 
     * @return 统计结果
     */
    @Select("SELECT register_source as label, COUNT(*) as value FROM customers WHERE deleted_at IS NULL GROUP BY register_source")
    List<java.util.Map<String, Object>> countByRegisterSource();

    /**
     * 查询余额不足的客户
     * 
     * @param minBalance 最小余额阈值
     * @return 客户列表
     */
    @Select("SELECT * FROM customers WHERE balance < #{minBalance} AND deleted_at IS NULL ORDER BY balance ASC")
    List<Customer> selectLowBalanceCustomers(@Param("minBalance") BigDecimal minBalance);
}