package com.saas.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saas.platform.entity.Order;
import com.saas.platform.exception.BusinessException;
import com.saas.platform.mapper.OrderMapper;
import com.saas.platform.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 订单服务实现类
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    // 订单号计数器
    private static final AtomicLong ORDER_COUNTER = new AtomicLong(1);

    @Override
    public IPage<Order> getOrderPage(Long current, Long size, String orderNo, String customerAccount, String customerName,
                                   String orderStatus, String paymentStatus, String country, String city, String ipQuality, 
                                   String ipAddress, String productName, Long managerId, String startTime, String endTime) {
        Page<Order> page = new Page<>(current, size);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        
        // 订单号查询
        if (StringUtils.hasText(orderNo)) {
            wrapper.like(Order::getOrderNo, orderNo);
        }
        
        // 客户账号查询
        if (StringUtils.hasText(customerAccount)) {
            wrapper.like(Order::getCustomerAccount, customerAccount);
        }
        
        // 客户名称查询
        if (StringUtils.hasText(customerName)) {
            wrapper.like(Order::getCustomerName, customerName);
        }
        
        // 订单状态查询
        if (StringUtils.hasText(orderStatus)) {
            wrapper.eq(Order::getOrderStatus, orderStatus);
        }
        
        // 支付状态查询
        if (StringUtils.hasText(paymentStatus)) {
            wrapper.eq(Order::getPaymentStatus, paymentStatus);
        }
        
        // 国家/地区查询
        if (StringUtils.hasText(country)) {
            wrapper.eq(Order::getCountry, country);
        }
        
        // 城市查询
        if (StringUtils.hasText(city)) {
            wrapper.eq(Order::getCity, city);
        }
        
        // IP质量查询
        if (StringUtils.hasText(ipQuality)) {
            wrapper.eq(Order::getIpQuality, ipQuality);
        }
        
        // IP地址查询 - 这里使用JSON数组查询，需要用JSON_CONTAINS函数
        if (StringUtils.hasText(ipAddress)) {
            wrapper.apply("JSON_CONTAINS(ip_addresses, '\"" + ipAddress + "\"')");
        }
        
        // 产品名称查询
        if (StringUtils.hasText(productName)) {
            wrapper.like(Order::getProductName, productName);
        }
        
        // 客户经理查询
        if (managerId != null) {
            wrapper.eq(Order::getManagerId, managerId);
        }
        
        // 时间范围查询
        if (StringUtils.hasText(startTime)) {
            try {
                LocalDate startDate = LocalDate.parse(startTime);
                wrapper.ge(Order::getCreatedAt, startDate.atStartOfDay());
            } catch (Exception e) {
                // 日期格式错误，忽略此条件
            }
        }
        
        if (StringUtils.hasText(endTime)) {
            try {
                LocalDate endDate = LocalDate.parse(endTime);
                wrapper.le(Order::getCreatedAt, endDate.atTime(23, 59, 59));
            } catch (Exception e) {
                // 日期格式错误，忽略此条件
            }
        }
        
        // 按创建时间倒序排列
        wrapper.orderByDesc(Order::getCreatedAt);
        
        return this.page(page, wrapper);
    }

    @Override
    public Order getByOrderNo(String orderNo) {
        if (!StringUtils.hasText(orderNo)) {
            return null;
        }
        return baseMapper.selectByOrderNo(orderNo);
    }

    @Override
    public List<Order> getByCustomerId(Long customerId) {
        if (customerId == null) {
            return null;
        }
        return baseMapper.selectByCustomerId(customerId);
    }

    @Override
    public List<Order> getByManagerId(Long managerId) {
        if (managerId == null) {
            return null;
        }
        return baseMapper.selectByManagerId(managerId);
    }

    @Override
    public List<Order> getActiveOrdersByCustomerId(Long customerId) {
        if (customerId == null) {
            return null;
        }
        return baseMapper.selectActiveOrdersByCustomerId(customerId);
    }

    @Override
    public List<Order> getExpiringOrders(Integer days) {
        if (days == null || days <= 0) {
            days = 7; // 默认7天
        }
        return baseMapper.selectExpiringOrders(days);
    }

    @Override
    public List<Order> getExpiredOrders() {
        return baseMapper.selectExpiredOrders();
    }

    @Override
    @Transactional
    public boolean createOrder(Order order) {
        // 生成订单号
        if (!StringUtils.hasText(order.getOrderNo())) {
            order.setOrderNo(generateOrderNo());
        }
        
        // 设置默认值
        if (!StringUtils.hasText(order.getOrderStatus())) {
            order.setOrderStatus("待支付");
        }
        if (!StringUtils.hasText(order.getPaymentStatus())) {
            order.setPaymentStatus("未支付");
        }
        
        // TODO: 验证客户是否存在
        // TODO: 验证产品是否存在
        // TODO: 计算订单金额
        
        return save(order);
    }

    @Override
    @Transactional
    public boolean updateOrder(Order order) {
        // 验证订单是否存在
        Order existOrder = getById(order.getId());
        if (existOrder == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 检查订单状态是否允许修改
        if ("已完成".equals(existOrder.getOrderStatus()) || "已取消".equals(existOrder.getOrderStatus())) {
            throw new BusinessException("订单状态不允许修改");
        }
        
        return updateById(order);
    }

    @Override
    @Transactional
    public boolean deleteOrder(Long id) {
        Order order = getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 只有待支付或已取消的订单才能删除
        if (!"待支付".equals(order.getOrderStatus()) && !"已取消".equals(order.getOrderStatus())) {
            throw new BusinessException("只有待支付或已取消的订单才能删除");
        }
        
        return removeById(id);
    }

    @Override
    @Transactional
    public boolean batchDeleteOrders(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        
        // TODO: 批量检查订单状态
        
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
    public boolean payOrder(String orderNo) {
        Order order = getByOrderNo(orderNo);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        if (!"待支付".equals(order.getOrderStatus())) {
            throw new BusinessException("订单状态不正确");
        }
        
        if (!"未支付".equals(order.getPaymentStatus())) {
            throw new BusinessException("订单支付状态不正确");
        }
        
        // TODO: 检查客户余额
        // TODO: 扣减客户余额
        
        // 更新订单状态
        order.setOrderStatus("已支付");
        order.setPaymentStatus("已支付");
        order.setPaymentTime(LocalDateTime.now());
        
        return updateById(order);
    }

    @Override
    @Transactional
    public boolean refundOrder(String orderNo, String reason) {
        Order order = getByOrderNo(orderNo);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        if (!"已支付".equals(order.getPaymentStatus())) {
            throw new BusinessException("订单未支付，无法退款");
        }
        
        // TODO: 退还客户余额
        
        // 更新订单状态
        order.setOrderStatus("已退款");
        order.setPaymentStatus("已退款");
        
        return updateById(order);
    }

    @Override
    @Transactional
    public boolean pauseOrder(Long id, String reason) {
        Order order = getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        if (!"运行中".equals(order.getOrderStatus())) {
            throw new BusinessException("只有运行中的订单才能暂停");
        }
        
        order.setOrderStatus("已暂停");
        return updateById(order);
    }

    @Override
    @Transactional
    public boolean resumeOrder(Long id) {
        Order order = getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        if (!"已暂停".equals(order.getOrderStatus())) {
            throw new BusinessException("只有暂停的订单才能恢复");
        }
        
        order.setOrderStatus("运行中");
        return updateById(order);
    }

    @Override
    @Transactional
    public boolean renewOrder(Long orderId, Integer days) {
        Order order = getById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        if (days == null || days <= 0) {
            throw new BusinessException("续费天数必须大于0");
        }
        
        // TODO: 计算续费金额
        // TODO: 检查客户余额
        // TODO: 扣减客户余额
        
        // 延长订单到期时间
        LocalDateTime newExpireTime = order.getExpireTime().plusDays(days);
        order.setExpireTime(newExpireTime);
        
        return updateById(order);
    }

    @Override
    @Transactional
    public boolean assignIpToOrder(Long orderId, Integer ipCount) {
        Order order = getById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        if (ipCount == null || ipCount <= 0) {
            throw new BusinessException("IP数量必须大于0");
        }
        
        // TODO: 从IP池中分配IP给订单
        
        return true;
    }

    @Override
    @Transactional
    public String replaceOrderIp(Long orderId, String oldIp, String reason) {
        Order order = getById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        if (!StringUtils.hasText(oldIp)) {
            throw new BusinessException("原IP地址不能为空");
        }
        
        // TODO: 释放原IP
        // TODO: 分配新IP
        
        // 返回新IP地址
        return "192.168.1.1"; // 模拟返回新IP
    }

    @Override
    @Transactional
    public int processExpiredOrders() {
        List<Order> expiredOrders = getExpiredOrders();
        int processedCount = 0;
        
        for (Order order : expiredOrders) {
            if ("运行中".equals(order.getOrderStatus())) {
                order.setOrderStatus("已过期");
                updateById(order);
                processedCount++;
                
                // TODO: 释放订单占用的IP
                // TODO: 发送过期通知
            }
        }
        
        return processedCount;
    }

    @Override
    public String generateOrderNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long counter = ORDER_COUNTER.getAndIncrement();
        return "ORD" + dateStr + String.format("%04d", counter);
    }

    @Override
    public Map<String, Object> getOrderStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, Object> result = new HashMap<>();
        
        // 总订单数
        long totalCount = count();
        result.put("totalCount", totalCount);
        
        // 各状态订单数量
        List<Map<String, Object>> statusCount = baseMapper.countByStatus();
        result.put("statusCount", statusCount);
        
        // 各支付状态订单数量
        List<Map<String, Object>> paymentStatusCount = baseMapper.countByPaymentStatus();
        result.put("paymentStatusCount", paymentStatusCount);
        
        // 指定时间范围内的订单金额汇总
        if (startTime != null && endTime != null) {
            Map<String, Object> amountSum = baseMapper.sumOrderAmount(startTime, endTime);
            result.put("amountSum", amountSum);
        }
        
        return result;
    }

    @Override
    public List<Map<String, Object>> getOrderStatisticsByDate(LocalDateTime startTime, LocalDateTime endTime) {
        return baseMapper.countByDate(startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> getOrderStatisticsByProduct(LocalDateTime startTime, LocalDateTime endTime) {
        return baseMapper.countByProduct(startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> getOrderStatisticsByCountry(LocalDateTime startTime, LocalDateTime endTime) {
        return baseMapper.countByCountry(startTime, endTime);
    }
}