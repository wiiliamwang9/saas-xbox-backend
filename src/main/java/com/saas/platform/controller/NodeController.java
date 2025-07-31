package com.saas.platform.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.saas.platform.dto.Result;
import com.saas.platform.entity.Node;
import com.saas.platform.service.NodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 节点管理控制器
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Tag(name = "节点管理", description = "节点管理相关接口")
@RestController
@RequestMapping("/api/nodes")
@Validated
public class NodeController {

    @Autowired
    private NodeService nodeService;

    /**
     * 分页查询节点列表
     */
    @Operation(summary = "分页查询节点列表", description = "支持按节点名称、国家、城市、节点类型、节点状态筛选")
    @GetMapping("/page")
    public Result<IPage<Node>> getNodePage(
            @Parameter(description = "当前页", example = "1") @RequestParam(defaultValue = "1") Long current,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") Long size,
            @Parameter(description = "节点名称") @RequestParam(required = false) String nodeName,
            @Parameter(description = "国家") @RequestParam(required = false) String country,
            @Parameter(description = "城市") @RequestParam(required = false) String city,
            @Parameter(description = "节点类型") @RequestParam(required = false) String nodeType,
            @Parameter(description = "节点状态") @RequestParam(required = false) String nodeStatus) {
        IPage<Node> page = nodeService.getNodePage(current, size, nodeName, country, city, nodeType, nodeStatus);
        return Result.success(page);
    }

    /**
     * 查询运行中的节点列表
     */
    @Operation(summary = "查询运行中的节点列表", description = "获取所有运行中状态的节点")
    @GetMapping("/running")
    public Result<List<Node>> getRunningNodes() {
        List<Node> nodes = nodeService.getRunningNodes();
        return Result.success(nodes);
    }

    /**
     * 按国家查询节点列表
     */
    @Operation(summary = "按国家查询节点列表", description = "根据国家筛选节点")
    @GetMapping("/country/{country}")
    public Result<List<Node>> getByCountry(
            @Parameter(description = "国家", example = "美国") @PathVariable String country) {
        List<Node> nodes = nodeService.getByCountry(country);
        return Result.success(nodes);
    }

    /**
     * 根据ID查询节点详情
     */
    @Operation(summary = "查询节点详情", description = "根据节点ID获取详细信息")
    @GetMapping("/{id}")
    public Result<Node> getNodeById(
            @Parameter(description = "节点ID", example = "1") @PathVariable @NotNull Long id) {
        Node node = nodeService.getById(id);
        if (node == null) {
            return Result.error("节点不存在");
        }
        return Result.success(node);
    }

    /**
     * 根据节点编码查询节点
     */
    @Operation(summary = "根据节点编码查询节点", description = "根据节点编码获取节点信息")
    @GetMapping("/code/{nodeCode}")
    public Result<Node> getByNodeCode(
            @Parameter(description = "节点编码", example = "LA-NODE-01") @PathVariable String nodeCode) {
        Node node = nodeService.getByNodeCode(nodeCode);
        if (node == null) {
            return Result.error("节点不存在");
        }
        return Result.success(node);
    }

    /**
     * 创建节点
     */
    @Operation(summary = "创建节点", description = "新增节点信息")
    @PostMapping
    public Result<Boolean> createNode(@Valid @RequestBody Node node) {
        // 验证节点编码唯一性
        if (!nodeService.isNodeCodeUnique(node.getNodeCode(), null)) {
            return Result.error("节点编码已存在");
        }
        
        // 验证服务器IP唯一性
        if (!nodeService.isServerIpUnique(node.getServerIp(), null)) {
            return Result.error("服务器IP已存在");
        }

        boolean success = nodeService.createNode(node);
        if (success) {
            return Result.success("节点创建成功", true);
        } else {
            return Result.error("节点创建失败");
        }
    }

    /**
     * 更新节点信息
     */
    @Operation(summary = "更新节点信息", description = "修改节点信息")
    @PutMapping("/{id}")
    public Result<Boolean> updateNode(
            @Parameter(description = "节点ID", example = "1") @PathVariable @NotNull Long id,
            @Valid @RequestBody Node node) {
        
        // 验证节点是否存在
        Node existingNode = nodeService.getById(id);
        if (existingNode == null) {
            return Result.error("节点不存在");
        }

        // 验证节点编码唯一性（排除当前节点）
        if (!nodeService.isNodeCodeUnique(node.getNodeCode(), id)) {
            return Result.error("节点编码已存在");
        }
        
        // 验证服务器IP唯一性（排除当前节点）
        if (!nodeService.isServerIpUnique(node.getServerIp(), id)) {
            return Result.error("服务器IP已存在");
        }

        node.setId(id);
        boolean success = nodeService.updateNode(node);
        if (success) {
            return Result.success("节点更新成功", true);
        } else {
            return Result.error("节点更新失败");
        }
    }

    /**
     * 删除节点
     */
    @Operation(summary = "删除节点", description = "根据ID删除节点")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteNode(
            @Parameter(description = "节点ID", example = "1") @PathVariable @NotNull Long id) {
        
        // 验证节点是否存在
        Node existingNode = nodeService.getById(id);
        if (existingNode == null) {
            return Result.error("节点不存在");
        }

        boolean success = nodeService.deleteNode(id);
        if (success) {
            return Result.success("节点删除成功", true);
        } else {
            return Result.error("节点删除失败");
        }
    }

    /**
     * 批量删除节点
     */
    @Operation(summary = "批量删除节点", description = "根据ID列表批量删除节点")
    @DeleteMapping("/batch")
    public Result<Boolean> batchDeleteNodes(
            @Parameter(description = "节点ID列表") @RequestBody @NotEmpty List<Long> ids) {
        boolean success = nodeService.batchDeleteNodes(ids);
        if (success) {
            return Result.success("批量删除成功", true);
        } else {
            return Result.error("批量删除失败");
        }
    }

    /**
     * 批量更新节点状态
     */
    @Operation(summary = "批量更新节点状态", description = "批量修改节点状态")
    @PutMapping("/batch/status")
    public Result<Boolean> batchUpdateStatus(
            @Parameter(description = "节点ID列表") @RequestParam @NotEmpty List<Long> ids,
            @Parameter(description = "新状态", example = "运行中") @RequestParam @NotNull String status) {
        boolean success = nodeService.batchUpdateStatus(ids, status);
        if (success) {
            return Result.success("批量更新状态成功", true);
        } else {
            return Result.error("批量更新状态失败");
        }
    }

    /**
     * 启动节点
     */
    @Operation(summary = "启动节点", description = "启动指定节点")
    @PostMapping("/{id}/start")
    public Result<Boolean> startNode(
            @Parameter(description = "节点ID", example = "1") @PathVariable @NotNull Long id) {
        boolean success = nodeService.startNode(id);
        if (success) {
            return Result.success("节点启动成功", true);
        } else {
            return Result.error("节点启动失败");
        }
    }

    /**
     * 停止节点
     */
    @Operation(summary = "停止节点", description = "停止指定节点")
    @PostMapping("/{id}/stop")
    public Result<Boolean> stopNode(
            @Parameter(description = "节点ID", example = "1") @PathVariable @NotNull Long id,
            @Parameter(description = "停止原因") @RequestParam(required = false) String reason) {
        boolean success = nodeService.stopNode(id, reason);
        if (success) {
            return Result.success("节点停止成功", true);
        } else {
            return Result.error("节点停止失败");
        }
    }

    /**
     * 重启节点
     */
    @Operation(summary = "重启节点", description = "重启指定节点")
    @PostMapping("/{id}/restart")
    public Result<Boolean> restartNode(
            @Parameter(description = "节点ID", example = "1") @PathVariable @NotNull Long id) {
        boolean success = nodeService.restartNode(id);
        if (success) {
            return Result.success("节点重启成功", true);
        } else {
            return Result.error("节点重启失败");
        }
    }

    /**
     * 节点进入维护模式
     */
    @Operation(summary = "节点进入维护模式", description = "设置节点为维护状态")
    @PostMapping("/{id}/maintain")
    public Result<Boolean> maintainNode(
            @Parameter(description = "节点ID", example = "1") @PathVariable @NotNull Long id,
            @Parameter(description = "维护原因") @RequestParam(required = false) String reason) {
        boolean success = nodeService.maintainNode(id, reason);
        if (success) {
            return Result.success("节点进入维护模式成功", true);
        } else {
            return Result.error("节点进入维护模式失败");
        }
    }

    /**
     * 节点退出维护模式
     */
    @Operation(summary = "节点退出维护模式", description = "节点退出维护状态")
    @PostMapping("/{id}/exit-maintain")
    public Result<Boolean> exitMaintainNode(
            @Parameter(description = "节点ID", example = "1") @PathVariable @NotNull Long id) {
        boolean success = nodeService.exitMaintainNode(id);
        if (success) {
            return Result.success("节点退出维护模式成功", true);
        } else {
            return Result.error("节点退出维护模式失败");
        }
    }

    /**
     * 检查节点健康状态
     */
    @Operation(summary = "检查节点健康状态", description = "检查指定节点的健康状态")
    @GetMapping("/{id}/health")
    public Result<Map<String, Object>> checkNodeHealth(
            @Parameter(description = "节点ID", example = "1") @PathVariable @NotNull Long id) {
        Map<String, Object> healthInfo = nodeService.checkNodeHealth(id);
        return Result.success(healthInfo);
    }

    /**
     * 批量检查节点健康状态
     */
    @Operation(summary = "批量检查节点健康状态", description = "批量检查节点健康状态")
    @PostMapping("/batch/health")
    public Result<Map<String, Object>> batchCheckNodeHealth(
            @Parameter(description = "节点ID列表") @RequestBody @NotEmpty List<Long> nodeIds) {
        Map<String, Object> result = nodeService.batchCheckNodeHealth(nodeIds);
        return Result.success(result);
    }

    /**
     * 获取节点统计信息
     */
    @Operation(summary = "获取节点统计信息", description = "获取节点相关的统计数据")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getNodeStatistics() {
        Map<String, Object> statistics = nodeService.getNodeStatistics();
        return Result.success(statistics);
    }

    /**
     * 获取节点监控数据
     */
    @Operation(summary = "获取节点监控数据", description = "获取节点监控数据")
    @GetMapping("/{id}/monitor")
    public Result<Map<String, Object>> getNodeMonitorData(
            @Parameter(description = "节点ID", example = "1") @PathVariable @NotNull Long id,
            @Parameter(description = "时间范围（小时）", example = "24") @RequestParam(defaultValue = "24") Integer hours) {
        Map<String, Object> monitorData = nodeService.getNodeMonitorData(id, hours);
        return Result.success(monitorData);
    }

    /**
     * 查询高负载节点
     */
    @Operation(summary = "查询高负载节点", description = "查询CPU或内存使用率过高的节点")
    @GetMapping("/high-load")
    public Result<List<Node>> getHighLoadNodes(
            @Parameter(description = "CPU阈值", example = "80.0") @RequestParam(defaultValue = "80.0") BigDecimal cpuThreshold,
            @Parameter(description = "内存阈值", example = "80.0") @RequestParam(defaultValue = "80.0") BigDecimal memoryThreshold) {
        List<Node> nodes = nodeService.getHighLoadNodes(cpuThreshold, memoryThreshold);
        return Result.success(nodes);
    }

    /**
     * 查询连接数接近上限的节点
     */
    @Operation(summary = "查询连接数接近上限的节点", description = "查询连接数使用率过高的节点")
    @GetMapping("/high-connection")
    public Result<List<Node>> getHighConnectionNodes(
            @Parameter(description = "阈值百分比", example = "90") @RequestParam(defaultValue = "90") Integer threshold) {
        List<Node> nodes = nodeService.getHighConnectionNodes(threshold);
        return Result.success(nodes);
    }

    /**
     * 获取推荐节点
     */
    @Operation(summary = "获取推荐节点", description = "根据负载均衡算法推荐节点")
    @GetMapping("/recommend")
    public Result<Node> getRecommendedNode(
            @Parameter(description = "国家", example = "美国") @RequestParam(required = false) String country) {
        Node node = nodeService.getRecommendedNode(country);
        if (node == null) {
            return Result.error("暂无可用节点");
        }
        return Result.success(node);
    }

    /**
     * 计算节点总成本
     */
    @Operation(summary = "计算节点总成本", description = "计算所有节点的月费用总和")
    @GetMapping("/total-cost")
    public Result<BigDecimal> calculateTotalCost() {
        BigDecimal totalCost = nodeService.calculateTotalCost();
        return Result.success(totalCost);
    }

    /**
     * 获取节点优化建议
     */
    @Operation(summary = "获取节点优化建议", description = "根据节点性能数据提供优化建议")
    @GetMapping("/{id}/suggestions")
    public Result<List<String>> getOptimizationSuggestions(
            @Parameter(description = "节点ID", example = "1") @PathVariable @NotNull Long id) {
        List<String> suggestions = nodeService.getOptimizationSuggestions(id);
        return Result.success(suggestions);
    }
}