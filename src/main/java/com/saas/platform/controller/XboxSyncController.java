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

    @Operation(summary = "部署Agent到远程节点", description = "通过Xbox Controller部署Agent到指定远程节点")
    @PostMapping("/deploy-agent")
    public Result<String> deployAgent(
            @Parameter(description = "节点IP地址", required = true)
            @RequestParam String nodeIp,
            @Parameter(description = "SSH端口", required = false)
            @RequestParam(defaultValue = "22") Integer sshPort,
            @Parameter(description = "SSH用户名", required = false)
            @RequestParam(defaultValue = "root") String sshUser,
            @Parameter(description = "SSH密码", required = true)
            @RequestParam String sshPassword) {
        
        log.info("开始部署Agent到节点: {}:{}", nodeIp, sshPort);
        
        try {
            // 这里调用Controller的部署API或执行部署脚本
            String deployResult = xboxSyncService.deployAgentToNode(nodeIp, sshPort, sshUser, sshPassword);
            return Result.success(deployResult);
        } catch (Exception e) {
            log.error("部署Agent失败", e);
            return Result.error("部署失败: " + e.getMessage());
        }
    }

    @Operation(summary = "更新Agent配置", description = "更新指定Agent的sing-box配置，包括黑白名单规则")
    @PostMapping("/update-agent-config")
    public Result<String> updateAgentConfig(
            @Parameter(description = "Agent ID", required = true)
            @RequestParam String agentId,
            @Parameter(description = "配置类型 (blacklist/whitelist/protocols)", required = true)
            @RequestParam String configType,
            @Parameter(description = "配置内容(JSON格式)", required = true)
            @RequestBody String configContent) {
        
        log.info("更新Agent配置: {} - {}", agentId, configType);
        
        try {
            String updateResult = xboxSyncService.updateAgentConfig(agentId, configType, configContent);
            return Result.success(updateResult);
        } catch (Exception e) {
            log.error("更新Agent配置失败", e);
            return Result.error("配置更新失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取Agent系统监控信息", description = "获取指定Agent的CPU、内存、网络等监控信息")
    @GetMapping("/agent-monitoring/{agentId}")
    public Result<Object> getAgentMonitoring(
            @Parameter(description = "Agent ID", required = true)
            @PathVariable String agentId) {
        
        log.info("获取Agent监控信息: {}", agentId);
        
        try {
            Object monitoringData = xboxSyncService.getAgentMonitoring(agentId);
            return Result.success(monitoringData);
        } catch (Exception e) {
            log.error("获取Agent监控信息失败", e);
            return Result.error("获取监控信息失败: " + e.getMessage());
        }
    }

    @Operation(summary = "测试Agent连接", description = "测试指定Agent的连接状态和代理功能")
    @PostMapping("/test-agent/{agentId}")
    public Result<String> testAgent(
            @Parameter(description = "Agent ID", required = true)
            @PathVariable String agentId,
            @Parameter(description = "测试类型 (connection/proxy/all)", required = false)
            @RequestParam(defaultValue = "all") String testType) {
        
        log.info("测试Agent: {} - {}", agentId, testType);
        
        try {
            String testResult = xboxSyncService.testAgent(agentId, testType);
            return Result.success(testResult);
        } catch (Exception e) {
            log.error("测试Agent失败", e);
            return Result.error("测试失败: " + e.getMessage());
        }
    }
}