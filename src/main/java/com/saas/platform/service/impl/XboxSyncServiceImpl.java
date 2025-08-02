package com.saas.platform.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.saas.platform.dto.Result;
import com.saas.platform.entity.Node;
import com.saas.platform.exception.BusinessException;
import com.saas.platform.mapper.NodeMapper;
import com.saas.platform.service.XboxSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Xbox系统同步服务实现
 *
 * @author SaaS Xbox Team
 */
@Service
public class XboxSyncServiceImpl implements XboxSyncService {

    private static final Logger log = LoggerFactory.getLogger(XboxSyncServiceImpl.class);

    @Autowired
    private NodeMapper nodeMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${xbox.controller.url:http://localhost:8080}")
    private String xboxControllerUrl;

    @Override
    public Result<Boolean> checkXboxConnection() {
        try {
            String healthUrl = xboxControllerUrl + "/health";
            String response = restTemplate.getForObject(healthUrl, String.class);
            
            if (response != null && response.contains("ok")) {
                log.info("Xbox Controller连接正常: {}", response);
                return Result.success(true);
            } else {
                log.warn("Xbox Controller连接异常: {}", response);
                return Result.success(false);
            }
        } catch (Exception e) {
            log.error("检查Xbox Controller连接失败", e);
            return Result.error("连接Xbox Controller失败: " + e.getMessage());
        }
    }

    @Override
    public Result<List<Node>> getXboxNodes() {
        try {
            String agentsUrl = xboxControllerUrl + "/api/v1/agents";
            String response = restTemplate.getForObject(agentsUrl, String.class);
            
            log.info("从Xbox Controller获取节点信息: {}", response);
            
            JSONObject jsonResponse = JSON.parseObject(response);
            if (jsonResponse.getInteger("code") != 200) {
                throw new BusinessException("获取节点信息失败: " + jsonResponse.getString("message"));
            }
            
            JSONObject data = jsonResponse.getJSONObject("data");
            JSONArray items = data.getJSONArray("items");
            
            List<Node> nodes = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                JSONObject item = items.getJSONObject(i);
                Node node = convertJsonToNode(item);
                nodes.add(node);
            }
            
            return Result.success(nodes);
        } catch (Exception e) {
            log.error("获取Xbox节点信息失败", e);
            return Result.error("获取节点信息失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<String> syncAllNodes() {
        try {
            log.info("开始同步所有Xbox节点信息");
            
            // 首先检查连接
            Result<Boolean> connectionResult = checkXboxConnection();
            if (!connectionResult.isSuccess() || !connectionResult.getData()) {
                return Result.error("Xbox Controller连接失败，无法同步");
            }
            
            // 获取Xbox节点列表
            Result<List<Node>> nodesResult = getXboxNodes();
            if (!nodesResult.isSuccess()) {
                return Result.error("获取Xbox节点列表失败: " + nodesResult.getMessage());
            }
            
            List<Node> xboxNodes = nodesResult.getData();
            int syncCount = 0;
            int updateCount = 0;
            int insertCount = 0;
            
            for (Node xboxNode : xboxNodes) {
                // 检查节点是否已存在
                QueryWrapper<Node> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("node_code", xboxNode.getNodeCode());
                Node existingNode = nodeMapper.selectOne(queryWrapper);
                
                if (existingNode != null) {
                    // 更新现有节点
                    updateNodeFromXbox(existingNode, xboxNode);
                    nodeMapper.updateById(existingNode);
                    updateCount++;
                    log.debug("更新节点: {}", existingNode.getNodeName());
                } else {
                    // 插入新节点
                    xboxNode.setCreatedAt(LocalDateTime.now());
                    xboxNode.setUpdatedAt(LocalDateTime.now());
                    nodeMapper.insert(xboxNode);
                    insertCount++;
                    log.debug("插入新节点: {}", xboxNode.getNodeName());
                }
                syncCount++;
            }
            
            String message = String.format("节点同步完成。总计: %d, 新增: %d, 更新: %d", 
                syncCount, insertCount, updateCount);
            log.info(message);
            
            return Result.success(message);
            
        } catch (Exception e) {
            log.error("同步Xbox节点信息失败", e);
            return Result.error("同步失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<String> syncNodeById(String agentId) {
        try {
            log.info("开始同步指定节点: {}", agentId);
            
            String agentUrl = xboxControllerUrl + "/api/v1/agents/" + agentId;
            String response = restTemplate.getForObject(agentUrl, String.class);
            
            JSONObject jsonResponse = JSON.parseObject(response);
            if (jsonResponse.getInteger("code") != 200) {
                return Result.error("获取节点信息失败: " + jsonResponse.getString("message"));
            }
            
            JSONObject data = jsonResponse.getJSONObject("data");
            Node xboxNode = convertJsonToNode(data);
            
            // 检查节点是否已存在
            QueryWrapper<Node> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("node_code", xboxNode.getNodeCode());
            Node existingNode = nodeMapper.selectOne(queryWrapper);
            
            if (existingNode != null) {
                updateNodeFromXbox(existingNode, xboxNode);
                nodeMapper.updateById(existingNode);
                log.info("更新节点成功: {}", existingNode.getNodeName());
                return Result.success("节点更新成功");
            } else {
                xboxNode.setCreatedAt(LocalDateTime.now());
                xboxNode.setUpdatedAt(LocalDateTime.now());
                nodeMapper.insert(xboxNode);
                log.info("插入新节点成功: {}", xboxNode.getNodeName());
                return Result.success("节点插入成功");
            }
            
        } catch (Exception e) {
            log.error("同步节点信息失败: {}", agentId, e);
            return Result.error("同步失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<String> syncNodeStatus() {
        try {
            log.info("开始同步节点状态信息");
            
            Result<List<Node>> nodesResult = getXboxNodes();
            if (!nodesResult.isSuccess()) {
                return Result.error("获取节点状态失败: " + nodesResult.getMessage());
            }
            
            List<Node> xboxNodes = nodesResult.getData();
            int updateCount = 0;
            
            for (Node xboxNode : xboxNodes) {
                QueryWrapper<Node> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("node_code", xboxNode.getNodeCode());
                Node existingNode = nodeMapper.selectOne(queryWrapper);
                
                if (existingNode != null) {
                    // 更新状态信息
                    existingNode.setNodeStatus(xboxNode.getNodeStatus());
                    existingNode.setCurrentConnections(xboxNode.getCurrentConnections());
                    existingNode.setCpuUsage(xboxNode.getCpuUsage());
                    existingNode.setMemoryUsage(xboxNode.getMemoryUsage());
                    existingNode.setNetworkLatency(xboxNode.getNetworkLatency());
                    existingNode.setLastCheckTime(LocalDateTime.now());
                    existingNode.setUpdatedAt(LocalDateTime.now());
                    
                    nodeMapper.updateById(existingNode);
                    updateCount++;
                }
            }
            
            String message = String.format("节点状态同步完成，更新 %d 个节点", updateCount);
            log.info(message);
            return Result.success(message);
            
        } catch (Exception e) {
            log.error("同步节点状态失败", e);
            return Result.error("同步状态失败: " + e.getMessage());
        }
    }

    @Override
    public Result<String> syncProtocolInfo() {
        try {
            log.info("开始同步协议支持信息");
            
            // 获取协议信息的API端点（假设存在）
            String protocolUrl = xboxControllerUrl + "/api/v1/protocols";
            String response = restTemplate.getForObject(protocolUrl, String.class);
            
            // 这里可以根据实际的协议信息API来实现
            log.info("协议信息: {}", response);
            
            return Result.success("协议信息同步完成");
            
        } catch (Exception e) {
            log.warn("同步协议信息失败（可能该API不存在）", e);
            return Result.success("协议信息同步跳过");
        }
    }

    /**
     * 将JSON对象转换为Node实体
     */
    private Node convertJsonToNode(JSONObject json) {
        Node node = new Node();
        
        // 基本信息
        node.setNodeCode(json.getString("id"));
        node.setNodeName(json.getString("hostname"));
        node.setServerIp(json.getString("ip_address"));
        
        // 状态信息
        String status = json.getString("status");
        switch (status.toLowerCase()) {
            case "online":
                node.setNodeStatus("运行中");
                break;
            case "offline":
                node.setNodeStatus("停用");
                break;
            default:
                node.setNodeStatus("故障");
        }
        
        // 位置信息（从metadata中提取，如果有的话）
        JSONObject metadata = json.getJSONObject("metadata");
        if (metadata != null) {
            node.setCountry("未知"); // 默认值，可以根据实际情况修改
            node.setRegion("未知");
            
            // 从metadata中提取更多信息 - city字段已删除
            if (metadata.containsKey("location")) {
                // 如果有位置信息，可以设置到region字段
                String location = metadata.getString("location");
                node.setRegion(location);
            }
        }
        
        // 默认配置
        node.setNodeType("虚拟节点");
        node.setMaxConnections(1000);
        node.setCurrentConnections(0);
        node.setBandwidthMbps(100);
        
        // 性能指标（从实际监控数据中获取，这里设置默认值）
        node.setCpuUsage(BigDecimal.valueOf(0.0));
        node.setMemoryUsage(BigDecimal.valueOf(0.0));
        node.setDiskUsage(BigDecimal.valueOf(0.0));
        node.setNetworkLatency(0);
        
        // 时间信息
        node.setLastCheckTime(LocalDateTime.now());
        
        return node;
    }

    /**
     * 从Xbox节点信息更新现有节点
     */
    private void updateNodeFromXbox(Node existingNode, Node xboxNode) {
        existingNode.setNodeName(xboxNode.getNodeName());
        existingNode.setServerIp(xboxNode.getServerIp());
        existingNode.setNodeStatus(xboxNode.getNodeStatus());
        existingNode.setCurrentConnections(xboxNode.getCurrentConnections());
        existingNode.setCpuUsage(xboxNode.getCpuUsage());
        existingNode.setMemoryUsage(xboxNode.getMemoryUsage());
        existingNode.setDiskUsage(xboxNode.getDiskUsage());
        existingNode.setNetworkLatency(xboxNode.getNetworkLatency());
        existingNode.setLastCheckTime(LocalDateTime.now());
        existingNode.setUpdatedAt(LocalDateTime.now());
    }
}