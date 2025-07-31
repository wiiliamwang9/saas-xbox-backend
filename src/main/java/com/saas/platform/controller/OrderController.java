package com.saas.platform.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.saas.platform.dto.Result;
import com.saas.platform.entity.Order;
import com.saas.platform.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 订单管理控制器
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Tag(name = "订单管理", description = "订单管理相关接口")
@RestController
@RequestMapping("/api/orders")
@Validated
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 分页查询订单列表
     */
    @Operation(summary = "分页查询订单列表", description = "支持按订单号、客户名称、状态等筛选")
    @GetMapping("/page")
    public Result<IPage<Order>> getOrderPage(
            @Parameter(description = "当前页", example = "1") @RequestParam(defaultValue = "1") Long current,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") Long size,
            @Parameter(description = "订单号") @RequestParam(required = false) String orderNo,
            @Parameter(description = "客户名称") @RequestParam(required = false) String customerName,
            @Parameter(description = "订单状态") @RequestParam(required = false) String orderStatus,
            @Parameter(description = "支付状态") @RequestParam(required = false) String paymentStatus,
            @Parameter(description = "客户经理ID") @RequestParam(required = false) Long managerId,
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        IPage<Order> page = orderService.getOrderPage(current, size, orderNo, customerName, 
                orderStatus, paymentStatus, managerId, startTime, endTime);
        return Result.success(page);
    }

    /**
     * 根据ID查询订单详情
     */
    @Operation(summary = "查询订单详情", description = "根据订单ID获取详细信息")
    @GetMapping("/{id}")
    public Result<Order> getOrderById(
            @Parameter(description = "订单ID", example = "1") @PathVariable @NotNull Long id) {
        Order order = orderService.getById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }
        return Result.success(order);
    }

    /**
     * 根据订单号查询订单
     */
    @Operation(summary = "根据订单号查询订单", description = "根据订单号获取订单信息")
    @GetMapping("/no/{orderNo}")
    public Result<Order> getByOrderNo(
            @Parameter(description = "订单号", example = "ORD202407310001") @PathVariable String orderNo) {
        Order order = orderService.getByOrderNo(orderNo);
        if (order == null) {
            return Result.error("订单不存在");
        }
        return Result.success(order);
    }

    /**
     * 查询客户的订单列表
     */
    @Operation(summary = "查询客户的订单列表", description = "获取指定客户的所有订单")
    @GetMapping("/customer/{customerId}")
    public Result<List<Order>> getByCustomerId(
            @Parameter(description = "客户ID", example = "1") @PathVariable @NotNull Long customerId) {
        List<Order> orders = orderService.getByCustomerId(customerId);
        return Result.success(orders);
    }

    /**
     * 查询客户的有效订单
     */
    @Operation(summary = "查询客户的有效订单", description = "获取指定客户的有效订单")
    @GetMapping("/customer/{customerId}/active")
    public Result<List<Order>> getActiveOrdersByCustomerId(
            @Parameter(description = "客户ID", example = "1") @PathVariable @NotNull Long customerId) {
        List<Order> orders = orderService.getActiveOrdersByCustomerId(customerId);
        return Result.success(orders);
    }

    /**
     * 查询某客户经理的订单列表
     */
    @Operation(summary = "查询客户经理的订单列表", description = "获取指定客户经理的所有订单")
    @GetMapping("/manager/{managerId}")
    public Result<List<Order>> getByManagerId(
            @Parameter(description = "客户经理ID", example = "3") @PathVariable @NotNull Long managerId) {
        List<Order> orders = orderService.getByManagerId(managerId);
        return Result.success(orders);
    }

    /**
     * 查询即将过期的订单
     */
    @Operation(summary = "查询即将过期的订单", description = "获取指定天数内即将过期的订单")
    @GetMapping("/expiring")
    public Result<List<Order>> getExpiringOrders(
            @Parameter(description = "天数", example = "7") @RequestParam(defaultValue = "7") Integer days) {
        List<Order> orders = orderService.getExpiringOrders(days);
        return Result.success(orders);
    }

    /**
     * 查询已过期的订单
     */
    @Operation(summary = "查询已过期的订单", description = "获取所有已过期的订单")
    @GetMapping("/expired")
    public Result<List<Order>> getExpiredOrders() {
        List<Order> orders = orderService.getExpiredOrders();
        return Result.success(orders);
    }

    /**
     * 创建订单
     */
    @Operation(summary = "创建订单", description = "添加新订单")
    @PostMapping
    public Result<String> createOrder(@Valid @RequestBody Order order) {
        boolean success = orderService.createOrder(order);
        return success ? Result.success("订单创建成功") : Result.error("订单创建失败");
    }

    /**
     * 更新订单信息
     */
    @Operation(summary = "更新订单信息", description = "修改订单信息")
    @PutMapping("/{id}")
    public Result<String> updateOrder(
            @Parameter(description = "订单ID", example = "1") @PathVariable @NotNull Long id,
            @Valid @RequestBody Order order) {
        order.setId(id);
        boolean success = orderService.updateOrder(order);
        return success ? Result.success("订单更新成功") : Result.error("订单更新失败");
    }

    /**
     * 删除订单
     */
    @Operation(summary = "删除订单", description = "根据ID删除订单")
    @DeleteMapping("/{id}")
    public Result<String> deleteOrder(
            @Parameter(description = "订单ID", example = "1") @PathVariable @NotNull Long id) {
        boolean success = orderService.deleteOrder(id);
        return success ? Result.success("订单删除成功") : Result.error("订单删除失败");
    }

    /**
     * 批量删除订单
     */
    @Operation(summary = "批量删除订单", description = "根据ID列表批量删除订单")
    @DeleteMapping("/batch")
    public Result<String> batchDeleteOrders(@RequestBody @NotEmpty List<Long> ids) {
        boolean success = orderService.batchDeleteOrders(ids);
        return success ? Result.success("订单批量删除成功") : Result.error("订单批量删除失败");
    }

    /**
     * 批量更新订单状态
     */
    @Operation(summary = "批量更新订单状态", description = "批量修改订单状态")
    @PutMapping("/batch/status")
    public Result<String> batchUpdateStatus(
            @RequestBody @NotEmpty List<Long> ids,
            @Parameter(description = "新状态", example = "正常") @RequestParam String status) {
        boolean success = orderService.batchUpdateStatus(ids, status);
        return success ? Result.success("订单状态批量更新成功") : Result.error("订单状态批量更新失败");
    }

    /**
     * 订单支付
     */
    @Operation(summary = "订单支付", description = "处理订单支付")
    @PostMapping("/pay/{orderNo}")
    public Result<String> payOrder(
            @Parameter(description = "订单号", example = "ORD202407310001") @PathVariable String orderNo) {
        boolean success = orderService.payOrder(orderNo);
        return success ? Result.success("订单支付成功") : Result.error("订单支付失败");
    }

    /**
     * 订单退款
     */
    @Operation(summary = "订单退款", description = "处理订单退款")
    @PostMapping("/refund/{orderNo}")
    public Result<String> refundOrder(
            @Parameter(description = "订单号", example = "ORD202407310001") @PathVariable String orderNo,
            @Parameter(description = "退款原因") @RequestParam String reason) {
        boolean success = orderService.refundOrder(orderNo, reason);
        return success ? Result.success("订单退款成功") : Result.error("订单退款失败");
    }

    /**
     * 暂停订单
     */
    @Operation(summary = "暂停订单", description = "暂停订单服务")
    @PutMapping("/{id}/pause")
    public Result<String> pauseOrder(
            @Parameter(description = "订单ID", example = "1") @PathVariable @NotNull Long id,
            @Parameter(description = "暂停原因") @RequestParam String reason) {
        boolean success = orderService.pauseOrder(id, reason);
        return success ? Result.success("订单暂停成功") : Result.error("订单暂停失败");
    }

    /**
     * 恢复订单
     */
    @Operation(summary = "恢复订单", description = "恢复订单服务")
    @PutMapping("/{id}/resume")
    public Result<String> resumeOrder(
            @Parameter(description = "订单ID", example = "1") @PathVariable @NotNull Long id) {
        boolean success = orderService.resumeOrder(id);
        return success ? Result.success("订单恢复成功") : Result.error("订单恢复失败");
    }

    /**
     * 续费订单
     */
    @Operation(summary = "续费订单", description = "为订单续费")
    @PostMapping("/{id}/renew")
    public Result<String> renewOrder(
            @Parameter(description = "订单ID", example = "1") @PathVariable @NotNull Long id,
            @Parameter(description = "续费天数", example = "30") @RequestParam Integer days) {
        boolean success = orderService.renewOrder(id, days);
        return success ? Result.success("订单续费成功") : Result.error("订单续费失败");
    }

    /**
     * 分配IP给订单
     */
    @Operation(summary = "分配IP给订单", description = "为订单分配IP资源")
    @PostMapping("/{id}/assign-ip")
    public Result<String> assignIpToOrder(
            @Parameter(description = "订单ID", example = "1") @PathVariable @NotNull Long id,
            @Parameter(description = "IP数量", example = "10") @RequestParam Integer ipCount) {
        boolean success = orderService.assignIpToOrder(id, ipCount);
        return success ? Result.success("IP分配成功") : Result.error("IP分配失败");
    }

    /**
     * 更换订单IP
     */
    @Operation(summary = "更换订单IP", description = "为订单更换IP地址")
    @PostMapping("/{id}/replace-ip")
    public Result<String> replaceOrderIp(
            @Parameter(description = "订单ID", example = "1") @PathVariable @NotNull Long id,
            @Parameter(description = "原IP地址", example = "192.168.1.1") @RequestParam String oldIp,
            @Parameter(description = "更换原因") @RequestParam String reason) {
        String newIp = orderService.replaceOrderIp(id, oldIp, reason);
        if (newIp != null) {
            return Result.success("IP更换成功，新IP：" + newIp);
        } else {
            return Result.error("IP更换失败");
        }
    }

    /**
     * 处理过期订单
     */
    @Operation(summary = "处理过期订单", description = "批量处理过期订单")
    @PostMapping("/process-expired")
    public Result<String> processExpiredOrders() {
        int count = orderService.processExpiredOrders();
        return Result.success("处理完成，共处理 " + count + " 个过期订单");
    }

    /**
     * 生成订单号
     */
    @Operation(summary = "生成订单号", description = "生成新的订单号")
    @GetMapping("/generate-no")
    public Result<String> generateOrderNo() {
        String orderNo = orderService.generateOrderNo();
        return Result.success(orderNo);
    }

    /**
     * 获取订单统计信息
     */
    @Operation(summary = "获取订单统计信息", description = "获取订单的各种统计数据")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getOrderStatistics(
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        Map<String, Object> statistics = orderService.getOrderStatistics(startTime, endTime);
        return Result.success(statistics);
    }

    /**
     * 按日期统计订单
     */
    @Operation(summary = "按日期统计订单", description = "按日期统计订单数量和金额")
    @GetMapping("/statistics/by-date")
    public Result<List<Map<String, Object>>> getOrderStatisticsByDate(
            @Parameter(description = "开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        List<Map<String, Object>> statistics = orderService.getOrderStatisticsByDate(startTime, endTime);
        return Result.success(statistics);
    }

    /**
     * 按产品统计订单
     */
    @Operation(summary = "按产品统计订单", description = "按产品统计订单数量")
    @GetMapping("/statistics/by-product")
    public Result<List<Map<String, Object>>> getOrderStatisticsByProduct(
            @Parameter(description = "开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        List<Map<String, Object>> statistics = orderService.getOrderStatisticsByProduct(startTime, endTime);
        return Result.success(statistics);
    }

    /**
     * 按国家/地区统计订单
     */
    @Operation(summary = "按国家/地区统计订单", description = "按国家/地区统计订单数量")
    @GetMapping("/statistics/by-country")
    public Result<List<Map<String, Object>>> getOrderStatisticsByCountry(
            @Parameter(description = "开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        List<Map<String, Object>> statistics = orderService.getOrderStatisticsByCountry(startTime, endTime);
        return Result.success(statistics);
    }
}