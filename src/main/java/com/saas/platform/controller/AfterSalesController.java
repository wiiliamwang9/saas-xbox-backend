package com.saas.platform.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saas.platform.dto.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 售后管理控制器
 * 
 * @author SaaS Xbox Team
 * @since 2024-08-06
 */
@Tag(name = "售后管理", description = "售后管理相关接口")
@RestController
@RequestMapping("/after-sales")
@Validated
public class AfterSalesController {

    /**
     * 分页查询售后工单列表
     */
    @Operation(summary = "分页查询售后工单列表", description = "支持按IP地址、客户账号、国家、订单ID、解决状态等筛选")
    @GetMapping("/page")
    public Result<IPage<Map<String, Object>>> getAfterSalesPage(
            @Parameter(description = "当前页", example = "1") @RequestParam(defaultValue = "1") Long current,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") Long size,
            @Parameter(description = "IP地址") @RequestParam(required = false) String ipAddress,
            @Parameter(description = "客户账号") @RequestParam(required = false) String customerAccount,
            @Parameter(description = "国家/地区") @RequestParam(required = false) String country,
            @Parameter(description = "订单ID") @RequestParam(required = false) String orderId,
            @Parameter(description = "解决状态") @RequestParam(required = false) String resolveStatus,
            @Parameter(description = "业务范围") @RequestParam(required = false) String businessScope,
            @Parameter(description = "开始时间") @RequestParam(required = false) String startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) String endTime) {
        
        // 模拟数据，实际开发中这里会调用服务层查询数据库
        Page<Map<String, Object>> page = new Page<>(current, size);
        
        List<Map<String, Object>> mockData = new ArrayList<>();
        for (int i = 1; i <= Math.min(size.intValue(), 10); i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", (current - 1) * size + i);
            item.put("supportOrderNo", String.format("SUP%06d", (current - 1) * size + i));
            item.put("orderId", String.format("ORD%06d", (current - 1) * size + i));
            item.put("ipAddress", String.format("192.168.1.%d", i));
            item.put("customerAccount", String.format("user%d@example.com", i));
            item.put("country", new String[]{"美国", "香港", "日本", "新加坡"}[i % 4]);
            item.put("ipQuality", new String[]{"高", "中", "低"}[i % 3]);
            item.put("businessScope", new String[]{"代理服务", "VPN服务", "数据采集"}[i % 3]);
            item.put("problemType", new String[]{"连接超时", "IP被封", "速度慢", "其他问题"}[i % 4]);
            item.put("feedbackTime", "2024-08-06 10:00:00");
            item.put("resolveStatus", new String[]{"未解决", "解决中", "已解决"}[i % 3]);
            mockData.add(item);
        }
        
        page.setRecords(mockData);
        page.setTotal(800L); // 模拟总数
        
        return Result.success(page);
    }

    /**
     * 根据ID查询售后工单详情
     */
    @Operation(summary = "查询售后工单详情", description = "根据工单ID获取详细信息")
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getAfterSalesById(
            @Parameter(description = "工单ID", example = "1") @PathVariable Long id) {
        
        Map<String, Object> mockData = new HashMap<>();
        mockData.put("id", id);
        mockData.put("supportOrderNo", String.format("SUP%06d", id));
        mockData.put("orderId", String.format("ORD%06d", id));
        mockData.put("ipAddress", "192.168.1.100");
        mockData.put("customerAccount", "user@example.com");
        mockData.put("country", "美国");
        mockData.put("ipQuality", "高");
        mockData.put("businessScope", "代理服务");
        mockData.put("problemType", "连接超时");
        mockData.put("feedbackTime", "2024-08-06 10:00:00");
        mockData.put("resolveStatus", "未解决");
        mockData.put("description", "IP连接超时，无法正常使用代理服务");
        
        return Result.success(mockData);
    }

    /**
     * 批量测试IP连通性
     */
    @Operation(summary = "批量测试IP连通性", description = "批量测试选中工单的IP连通性")
    @PostMapping("/batch-test")
    public Result<String> batchTest(@RequestBody Map<String, List<Long>> request) {
        List<Long> ids = request.get("ids");
        if (ids == null || ids.isEmpty()) {
            return Result.error("请选择要测试的工单");
        }
        
        // 模拟批量测试逻辑
        String message = String.format("已开始测试 %d 个工单的IP连通性，请稍后查看测试结果", ids.size());
        return Result.success(message);
    }

    /**
     * 更新工单状态
     */
    @Operation(summary = "更新工单状态", description = "更新售后工单的解决状态")
    @PutMapping("/{id}/status")
    public Result<String> updateStatus(
            @Parameter(description = "工单ID", example = "1") @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        
        String status = request.get("status");
        if (status == null || status.trim().isEmpty()) {
            return Result.error("状态不能为空");
        }
        
        // 模拟状态更新逻辑
        return Result.success("工单状态更新成功");
    }
}