package com.saas.platform.controller;

import com.saas.platform.dto.Result;
import com.saas.platform.entity.Node;
import com.saas.platform.service.XboxSyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Xbox系统同步控制器
 * 提供手动同步Xbox节点信息的API接口
 *
 * @author SaaS Xbox Team
 */
@Tag(name = "Xbox同步管理", description = "Xbox系统数据同步相关接口")
@RestController
@RequestMapping("/xbox-sync")
public class XboxSyncController {

    private static final Logger log = LoggerFactory.getLogger(XboxSyncController.class);

    @Autowired
    private XboxSyncService xboxSyncService;

    @Operation(summary = "检查Xbox Controller连接状态", description = "测试与Xbox Controller的连接是否正常")
    @GetMapping("/check-connection")
    public Result<Boolean> checkConnection() {
        log.info("手动检查Xbox Controller连接状态");
        return xboxSyncService.checkXboxConnection();
    }

    @Operation(summary = "获取Xbox节点列表", description = "从Xbox Controller获取当前节点列表")
    @GetMapping("/nodes")
    public Result<List<Node>> getXboxNodes() {
        log.info("手动获取Xbox节点列表");
        return xboxSyncService.getXboxNodes();
    }

    @Operation(summary = "同步所有节点信息", description = "手动触发同步所有Xbox节点信息到数据库")
    @PostMapping("/sync-all")
    public Result<String> syncAllNodes() {
        log.info("手动触发同步所有节点信息");
        return xboxSyncService.syncAllNodes();
    }

    @Operation(summary = "同步指定节点信息", description = "手动触发同步指定Agent节点信息")
    @PostMapping("/sync/{agentId}")
    public Result<String> syncNodeById(
            @Parameter(description = "Agent ID", required = true)
            @PathVariable String agentId) {
        log.info("手动触发同步节点信息: {}", agentId);
        return xboxSyncService.syncNodeById(agentId);
    }

    @Operation(summary = "同步节点状态", description = "手动触发同步所有节点的状态信息")
    @PostMapping("/sync-status")
    public Result<String> syncNodeStatus() {
        log.info("手动触发同步节点状态");
        return xboxSyncService.syncNodeStatus();
    }

    @Operation(summary = "同步协议信息", description = "手动触发同步协议支持信息")
    @PostMapping("/sync-protocols")
    public Result<String> syncProtocolInfo() {
        log.info("手动触发同步协议信息");
        return xboxSyncService.syncProtocolInfo();
    }

    @Operation(summary = "强制重新同步", description = "强制重新同步所有数据，包括节点信息、状态和协议")
    @PostMapping("/force-sync")
    public Result<String> forceSync() {
        log.info("手动触发强制重新同步");
        
        try {
            // 检查连接
            Result<Boolean> connectionResult = xboxSyncService.checkXboxConnection();
            if (!connectionResult.isSuccess() || !connectionResult.getData()) {
                return Result.error("Xbox Controller连接失败，无法执行同步");
            }
            
            // 同步节点信息
            Result<String> nodesResult = xboxSyncService.syncAllNodes();
            if (!nodesResult.isSuccess()) {
                return Result.error("同步节点信息失败: " + nodesResult.getMessage());
            }
            
            // 同步状态信息
            Result<String> statusResult = xboxSyncService.syncNodeStatus();
            if (!statusResult.isSuccess()) {
                log.warn("同步状态信息失败: {}", statusResult.getMessage());
            }
            
            // 同步协议信息
            Result<String> protocolResult = xboxSyncService.syncProtocolInfo();
            if (!protocolResult.isSuccess()) {
                log.warn("同步协议信息失败: {}", protocolResult.getMessage());
            }
            
            String message = String.format("强制同步完成。节点: %s, 状态: %s, 协议: %s", 
                nodesResult.getData(), 
                statusResult.isSuccess() ? statusResult.getData() : "失败",
                protocolResult.isSuccess() ? protocolResult.getData() : "失败");
            
            return Result.success(message);
            
        } catch (Exception e) {
            log.error("强制同步异常", e);
            return Result.error("强制同步失败: " + e.getMessage());
        }
    }
}