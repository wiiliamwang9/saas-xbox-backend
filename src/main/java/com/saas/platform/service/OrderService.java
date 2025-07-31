package com.saas.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.saas.platform.entity.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 订单服务接口
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
public interface OrderService extends IService<Order> {

    /**
     * 分页查询订单列表
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param orderNo 订单号
     * @param customerName 客户名称
     * @param orderStatus 订单状态
     * @param paymentStatus 支付状态
     * @param managerId 客户经理ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 分页结果
     */
    IPage<Order> getOrderPage(Long current, Long size, String orderNo, String customerName,
                             String orderStatus, String paymentStatus, Long managerId,
                             LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据订单号查询订单
     * 
     * @param orderNo 订单号
     * @return 订单信息
     */
    Order getByOrderNo(String orderNo);

    /**
     * 查询客户的订单列表
     * 
     * @param customerId 客户ID
     * @return 订单列表
     */
    List<Order> getByCustomerId(Long customerId);

    /**
     * 查询某客户经理的订单列表
     * 
     * @param managerId 客户经理ID
     * @return 订单列表
     */
    List<Order> getByManagerId(Long managerId);

    /**
     * 查询客户的有效订单
     * 
     * @param customerId 客户ID
     * @return 订单列表
     */
    List<Order> getActiveOrdersByCustomerId(Long customerId);

    /**
     * 查询即将过期的订单
     * 
     * @param days 天数
     * @return 订单列表
     */
    List<Order> getExpiringOrders(Integer days);

    /**
     * 查询已过期的订单
     * 
     * @return 订单列表
     */
    List<Order> getExpiredOrders();

    /**
     * 创建订单
     * 
     * @param order 订单信息
     * @return 是否成功
     */
    boolean createOrder(Order order);

    /**
     * 更新订单信息
     * 
     * @param order 订单信息
     * @return 是否成功
     */
    boolean updateOrder(Order order);

    /**
     * 删除订单
     * 
     * @param id 订单ID
     * @return 是否成功
     */
    boolean deleteOrder(Long id);

    /**
     * 批量删除订单
     * 
     * @param ids ID列表
     * @return 是否成功
     */
    boolean batchDeleteOrders(List<Long> ids);

    /**
     * 批量更新订单状态
     * 
     * @param ids ID列表
     * @param status 新状态
     * @return 是否成功
     */
    boolean batchUpdateStatus(List<Long> ids, String status);

    /**
     * 订单支付
     * 
     * @param orderNo 订单号
     * @return 是否成功
     */
    boolean payOrder(String orderNo);

    /**
     * 订单退款
     * 
     * @param orderNo 订单号
     * @param reason 退款原因
     * @return 是否成功
     */
    boolean refundOrder(String orderNo, String reason);

    /**
     * 暂停订单
     * 
     * @param id 订单ID
     * @param reason 暂停原因
     * @return 是否成功
     */
    boolean pauseOrder(Long id, String reason);

    /**
     * 恢复订单
     * 
     * @param id 订单ID
     * @return 是否成功
     */
    boolean resumeOrder(Long id);

    /**
     * 续费订单
     * 
     * @param orderId 订单ID
     * @param days 续费天数
     * @return 是否成功
     */
    boolean renewOrder(Long orderId, Integer days);

    /**
     * 分配IP给订单
     * 
     * @param orderId 订单ID
     * @param ipCount 需要的IP数量
     * @return 是否成功
     */
    boolean assignIpToOrder(Long orderId, Integer ipCount);

    /**
     * 更换订单IP
     * 
     * @param orderId 订单ID
     * @param oldIp 原IP
     * @param reason 更换原因
     * @return 新IP地址
     */
    String replaceOrderIp(Long orderId, String oldIp, String reason);

    /**
     * 处理过期订单
     * 
     * @return 处理的订单数量
     */
    int processExpiredOrders();

    /**
     * 生成订单号
     * 
     * @return 订单号
     */
    String generateOrderNo();

    /**
     * 获取订单统计信息
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    Map<String, Object> getOrderStatistics(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 按日期统计订单数量和金额
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    List<Map<String, Object>> getOrderStatisticsByDate(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 按产品统计订单数量
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    List<Map<String, Object>> getOrderStatisticsByProduct(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 按国家/地区统计订单数量
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    List<Map<String, Object>> getOrderStatisticsByCountry(LocalDateTime startTime, LocalDateTime endTime);
}