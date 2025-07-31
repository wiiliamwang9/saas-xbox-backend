package com.saas.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saas.platform.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单数据访问层接口
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 分页查询订单列表
     * 
     * @param page 分页对象
     * @param orderNo 订单号
     * @param customerName 客户名称
     * @param orderStatus 订单状态
     * @param paymentStatus 支付状态
     * @param managerId 客户经理ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 分页结果
     */
    IPage<Order> selectOrderPage(Page<Order> page,
                               @Param("orderNo") String orderNo,
                               @Param("customerName") String customerName,
                               @Param("orderStatus") String orderStatus,
                               @Param("paymentStatus") String paymentStatus,
                               @Param("managerId") Long managerId,
                               @Param("startTime") LocalDateTime startTime,
                               @Param("endTime") LocalDateTime endTime);

    /**
     * 根据订单号查询订单
     * 
     * @param orderNo 订单号
     * @return 订单信息
     */
    Order selectByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 查询客户的订单列表
     * 
     * @param customerId 客户ID
     * @return 订单列表
     */
    List<Order> selectByCustomerId(@Param("customerId") Long customerId);

    /**
     * 查询某客户经理的订单列表
     * 
     * @param managerId 客户经理ID
     * @return 订单列表
     */
    List<Order> selectByManagerId(@Param("managerId") Long managerId);

    /**
     * 查询即将过期的订单
     * 
     * @param days 天数
     * @return 订单列表
     */
    List<Order> selectExpiringOrders(@Param("days") Integer days);

    /**
     * 查询已过期的订单
     * 
     * @return 订单列表
     */
    List<Order> selectExpiredOrders();

    /**
     * 批量更新订单状态
     * 
     * @param ids ID列表
     * @param status 新状态
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") String status);

    /**
     * 统计各状态订单数量
     * 
     * @return 统计结果
     */
    List<java.util.Map<String, Object>> countByStatus();

    /**
     * 统计各支付状态订单数量
     * 
     * @return 统计结果
     */
    List<java.util.Map<String, Object>> countByPaymentStatus();

    /**
     * 统计订单金额汇总
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 金额汇总
     */
    java.util.Map<String, Object> sumOrderAmount(@Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime);

    /**
     * 按日期统计订单数量和金额
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    List<java.util.Map<String, Object>> countByDate(@Param("startTime") LocalDateTime startTime,
                                                     @Param("endTime") LocalDateTime endTime);

    /**
     * 按产品统计订单数量
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    List<java.util.Map<String, Object>> countByProduct(@Param("startTime") LocalDateTime startTime,
                                                        @Param("endTime") LocalDateTime endTime);

    /**
     * 按国家/地区统计订单数量
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    List<java.util.Map<String, Object>> countByCountry(@Param("startTime") LocalDateTime startTime,
                                                        @Param("endTime") LocalDateTime endTime);

    /**
     * 查询客户的有效订单
     * 
     * @param customerId 客户ID
     * @return 订单列表
     */
    List<Order> selectActiveOrdersByCustomerId(@Param("customerId") Long customerId);
}