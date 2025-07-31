package com.saas.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saas.platform.entity.Node;
import com.saas.platform.exception.BusinessException;
import com.saas.platform.mapper.NodeMapper;
import com.saas.platform.service.NodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 节点服务实现类
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Service
public class NodeServiceImpl extends ServiceImpl<NodeMapper, Node> implements NodeService {

    @Override
    public IPage<Node> getNodePage(Long current, Long size, String nodeName, String country,
                                 String city, String nodeType, String nodeStatus) {
        Page<Node> page = new Page<>(current, size);
        return baseMapper.selectNodePage(page, nodeName, country, city, nodeType, nodeStatus);
    }

    @Override
    public Node getByNodeCode(String nodeCode) {
        if (!StringUtils.hasText(nodeCode)) {
            return null;
        }
        return baseMapper.selectByNodeCode(nodeCode);
    }

    @Override
    public Node getByServerIp(String serverIp) {
        if (!StringUtils.hasText(serverIp)) {
            return null;
        }
        return baseMapper.selectByServerIp(serverIp);
    }

    @Override
    public List<Node> getRunningNodes() {
        return baseMapper.selectRunningNodes();
    }

    @Override
    public List<Node> getByCountry(String country) {
        if (!StringUtils.hasText(country)) {
            return null;
        }
        return baseMapper.selectByCountry(country);
    }

    @Override
    public List<Node> getNeedCheckNodes(Integer hours) {
        if (hours == null || hours <= 0) {
            hours = 1; // 默认1小时
        }
        return baseMapper.selectNeedCheckNodes(hours);
    }

    @Override
    @Transactional
    public boolean createNode(Node node) {
        // 验证节点编码唯一性
        if (!isNodeCodeUnique(node.getNodeCode(), null)) {
            throw new BusinessException("节点编码已存在");
        }
        
        // 验证服务器IP唯一性
        if (!isServerIpUnique(node.getServerIp(), null)) {
            throw new BusinessException("服务器IP已存在");
        }
        
        // 设置默认值
        if (!StringUtils.hasText(node.getNodeStatus())) {
            node.setNodeStatus("停用");
        }
        if (!StringUtils.hasText(node.getNodeType())) {
            node.setNodeType("标准");
        }
        if (node.getCurrentConnections() == null) {
            node.setCurrentConnections(0);
        }
        if (node.getCpuUsage() == null) {
            node.setCpuUsage(BigDecimal.ZERO);
        }
        if (node.getMemoryUsage() == null) {
            node.setMemoryUsage(BigDecimal.ZERO);
        }
        if (node.getDiskUsage() == null) {
            node.setDiskUsage(BigDecimal.ZERO);
        }
        
        return save(node);
    }

    @Override
    @Transactional
    public boolean updateNode(Node node) {
        // 验证节点是否存在
        Node existNode = getById(node.getId());
        if (existNode == null) {
            throw new BusinessException("节点不存在");
        }
        
        // 验证节点编码唯一性
        if (!isNodeCodeUnique(node.getNodeCode(), node.getId())) {
            throw new BusinessException("节点编码已存在");
        }
        
        // 验证服务器IP唯一性
        if (!isServerIpUnique(node.getServerIp(), node.getId())) {
            throw new BusinessException("服务器IP已存在");
        }
        
        return updateById(node);
    }

    @Override
    @Transactional
    public boolean deleteNode(Long id) {
        Node node = getById(id);
        if (node == null) {
            throw new BusinessException("节点不存在");
        }
        
        // 检查节点是否在运行
        if ("运行中".equals(node.getNodeStatus())) {
            throw new BusinessException("节点正在运行中，无法删除");
        }
        
        // TODO: 检查是否有关联的IP或订单
        
        return removeById(id);
    }

    @Override
    @Transactional
    public boolean batchDeleteNodes(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        
        // TODO: 批量检查节点状态和关联数据
        
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
    public boolean startNode(Long id) {
        Node node = getById(id);
        if (node == null) {
            throw new BusinessException("节点不存在");
        }
        
        if ("运行中".equals(node.getNodeStatus())) {
            throw new BusinessException("节点已经是运行状态");
        }
        
        if ("维护中".equals(node.getNodeStatus())) {
            throw new BusinessException("节点正在维护中，无法启动");
        }
        
        // TODO: 执行实际的节点启动操作
        
        node.setNodeStatus("运行中");
        // 记录启动时间可以通过更新updatedAt字段来记录
        // node.setStartTime(LocalDateTime.now());
        return updateById(node);
    }

    @Override
    @Transactional
    public boolean stopNode(Long id, String reason) {
        Node node = getById(id);
        if (node == null) {
            throw new BusinessException("节点不存在");
        }
        
        if ("停用".equals(node.getNodeStatus())) {
            throw new BusinessException("节点已经是停用状态");
        }
        
        // TODO: 执行实际的节点停止操作
        // TODO: 处理节点上的连接和IP分配
        
        node.setNodeStatus("停用");
        return updateById(node);
    }

    @Override
    @Transactional
    public boolean restartNode(Long id) {
        Node node = getById(id);
        if (node == null) {
            throw new BusinessException("节点不存在");
        }
        
        if (!"运行中".equals(node.getNodeStatus())) {
            throw new BusinessException("只有运行中的节点才能重启");
        }
        
        // TODO: 执行实际的节点重启操作
        
        // 记录启动时间可以通过更新updatedAt字段来记录
        // node.setStartTime(LocalDateTime.now());
        return updateById(node);
    }

    @Override
    @Transactional
    public boolean maintainNode(Long id, String reason) {
        Node node = getById(id);
        if (node == null) {
            throw new BusinessException("节点不存在");
        }
        
        if ("维护中".equals(node.getNodeStatus())) {
            throw new BusinessException("节点已经是维护状态");
        }
        
        // TODO: 迁移节点上的连接到其他节点
        
        node.setNodeStatus("维护中");
        return updateById(node);
    }

    @Override
    @Transactional
    public boolean exitMaintainNode(Long id) {
        Node node = getById(id);
        if (node == null) {
            throw new BusinessException("节点不存在");
        }
        
        if (!"维护中".equals(node.getNodeStatus())) {
            throw new BusinessException("只有维护中的节点才能退出维护");
        }
        
        node.setNodeStatus("停用");
        return updateById(node);
    }

    @Override
    @Transactional
    public boolean updateMonitorData(Long nodeId, Integer currentConnections, BigDecimal cpuUsage,
                                   BigDecimal memoryUsage, BigDecimal diskUsage, Integer networkLatency) {
        Node node = getById(nodeId);
        if (node == null) {
            throw new BusinessException("节点不存在");
        }
        
        // 使用Mapper直接更新监控数据，避免覆盖其他字段
        int result = baseMapper.updateMonitorData(nodeId, currentConnections, cpuUsage, 
                                                memoryUsage, diskUsage, networkLatency);
        
        return result > 0;
    }

    @Override
    public Map<String, Object> checkNodeHealth(Long nodeId) {
        Node node = getById(nodeId);
        if (node == null) {
            throw new BusinessException("节点不存在");
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("nodeId", nodeId);
        result.put("nodeName", node.getNodeName());
        result.put("nodeCode", node.getNodeCode());
        result.put("serverIp", node.getServerIp());
        result.put("checkTime", LocalDateTime.now());
        
        try {
            // 检查网络连通性
            long startTime = System.currentTimeMillis();
            InetAddress address = InetAddress.getByName(node.getServerIp());
            boolean reachable = address.isReachable(5000); // 5秒超时
            long latency = System.currentTimeMillis() - startTime;
            
            result.put("networkReachable", reachable);
            result.put("networkLatency", latency);
            
            // 检查资源使用率
            boolean cpuHealthy = node.getCpuUsage().compareTo(new BigDecimal("80")) <= 0;
            boolean memoryHealthy = node.getMemoryUsage().compareTo(new BigDecimal("80")) <= 0;
            boolean diskHealthy = node.getDiskUsage().compareTo(new BigDecimal("90")) <= 0;
            
            result.put("cpuHealthy", cpuHealthy);
            result.put("memoryHealthy", memoryHealthy);
            result.put("diskHealthy", diskHealthy);
            
            // 检查连接数
            boolean connectionHealthy = node.getCurrentConnections() < node.getMaxConnections() * 0.9;
            result.put("connectionHealthy", connectionHealthy);
            
            // 综合健康状态
            boolean overallHealthy = reachable && cpuHealthy && memoryHealthy && diskHealthy && connectionHealthy;
            result.put("overallHealthy", overallHealthy);
            result.put("healthStatus", overallHealthy ? "健康" : "异常");
            
            // 生成健康建议
            List<String> suggestions = new ArrayList<>();
            if (!reachable) suggestions.add("网络不通，请检查网络连接");
            if (!cpuHealthy) suggestions.add("CPU使用率过高，建议优化或扩容");
            if (!memoryHealthy) suggestions.add("内存使用率过高，建议优化或扩容");
            if (!diskHealthy) suggestions.add("磁盘空间不足，请及时清理");
            if (!connectionHealthy) suggestions.add("连接数接近上限，建议分流或扩容");
            
            result.put("suggestions", suggestions);
            
        } catch (IOException e) {
            result.put("networkReachable", false);
            result.put("networkLatency", -1);
            result.put("overallHealthy", false);
            result.put("healthStatus", "网络异常");
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    @Override
    public Map<String, Object> batchCheckNodeHealth(List<Long> nodeIds) {
        Map<String, Object> result = new HashMap<>();
        
        if (nodeIds == null || nodeIds.isEmpty()) {
            result.put("totalCount", 0);
            result.put("healthyCount", 0);
            result.put("unhealthyCount", 0);
            return result;
        }
        
        int totalCount = nodeIds.size();
        int healthyCount = 0;
        int unhealthyCount = 0;
        List<Map<String, Object>> details = new ArrayList<>();
        
        for (Long nodeId : nodeIds) {
            try {
                Map<String, Object> nodeHealth = checkNodeHealth(nodeId);
                details.add(nodeHealth);
                
                if ((Boolean) nodeHealth.get("overallHealthy")) {
                    healthyCount++;
                } else {
                    unhealthyCount++;
                }
            } catch (Exception e) {
                unhealthyCount++;
                Map<String, Object> errorResult = new HashMap<>();
                errorResult.put("nodeId", nodeId);
                errorResult.put("overallHealthy", false);
                errorResult.put("error", e.getMessage());
                details.add(errorResult);
            }
        }
        
        result.put("totalCount", totalCount);
        result.put("healthyCount", healthyCount);
        result.put("unhealthyCount", unhealthyCount);
        result.put("healthyRate", totalCount > 0 ? (double) healthyCount / totalCount * 100 : 0);
        result.put("details", details);
        
        return result;
    }

    @Override
    public List<Node> getHighLoadNodes(BigDecimal cpuThreshold, BigDecimal memoryThreshold) {
        return baseMapper.selectHighLoadNodes(cpuThreshold, memoryThreshold);
    }

    @Override
    public List<Node> getHighConnectionNodes(Integer threshold) {
        return baseMapper.selectHighConnectionNodes(threshold);
    }

    @Override
    public Node getRecommendedNode(String country) {
        List<Node> countryNodes = getByCountry(country);
        
        if (countryNodes.isEmpty()) {
            // 如果指定国家没有节点，返回全局最优节点
            countryNodes = getRunningNodes();
        }
        
        if (countryNodes.isEmpty()) {
            return null;
        }
        
        // 根据负载、连接数等因素选择最优节点
        return countryNodes.stream()
                .filter(node -> "运行中".equals(node.getNodeStatus()))
                .filter(node -> node.getCurrentConnections() < node.getMaxConnections() * 0.8)
                .filter(node -> node.getCpuUsage().compareTo(new BigDecimal("70")) <= 0)
                .filter(node -> node.getMemoryUsage().compareTo(new BigDecimal("70")) <= 0)
                .min(Comparator.comparing(node -> 
                    node.getCurrentConnections().doubleValue() / node.getMaxConnections() +
                    node.getCpuUsage().doubleValue() / 100 +
                    node.getMemoryUsage().doubleValue() / 100))
                .orElse(countryNodes.get(0)); // 如果没有满足条件的，返回第一个
    }

    @Override
    public Map<String, Object> scheduleNodeCheck() {
        Map<String, Object> result = new HashMap<>();
        
        // 获取需要检查的节点（1小时未检查的）
        List<Node> needCheckNodes = getNeedCheckNodes(1);
        
        if (needCheckNodes.isEmpty()) {
            result.put("message", "没有需要检查的节点");
            result.put("checkedCount", 0);
            return result;
        }
        
        List<Long> nodeIds = needCheckNodes.stream().map(Node::getId).collect(Collectors.toList());
        Map<String, Object> checkResult = batchCheckNodeHealth(nodeIds);
        
        result.put("message", "节点检查完成");
        result.put("checkedCount", checkResult.get("totalCount"));
        result.put("healthyCount", checkResult.get("healthyCount"));
        result.put("unhealthyCount", checkResult.get("unhealthyCount"));
        result.put("healthyRate", checkResult.get("healthyRate"));
        
        return result;
    }

    @Override
    public Map<String, Object> getNodeStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        // 总节点数
        long totalCount = count();
        result.put("totalCount", totalCount);
        
        // 各状态节点数量
        List<Map<String, Object>> statusCount = baseMapper.countByStatus();
        result.put("statusCount", statusCount);
        
        // 按国家统计节点数量
        List<Map<String, Object>> countryCount = baseMapper.countByCountry();
        result.put("countryCount", countryCount);
        
        // 按节点类型统计数量
        List<Map<String, Object>> typeCount = baseMapper.countByType();
        result.put("typeCount", typeCount);
        
        // 运行中节点数
        long runningCount = count(new LambdaQueryWrapper<Node>()
                .eq(Node::getNodeStatus, "运行中"));
        result.put("runningCount", runningCount);
        
        // 停用节点数
        long stoppedCount = count(new LambdaQueryWrapper<Node>()
                .eq(Node::getNodeStatus, "停用"));
        result.put("stoppedCount", stoppedCount);
        
        // 维护中节点数
        long maintainingCount = count(new LambdaQueryWrapper<Node>()
                .eq(Node::getNodeStatus, "维护中"));
        result.put("maintainingCount", maintainingCount);
        
        return result;
    }

    @Override
    public Map<String, Object> getNodeMonitorData(Long nodeId, Integer hours) {
        // TODO: 从监控数据库或缓存中获取历史监控数据
        Map<String, Object> result = new HashMap<>();
        result.put("nodeId", nodeId);
        result.put("hours", hours);
        result.put("message", "监控数据功能待实现");
        return result;
    }

    @Override
    public boolean isNodeCodeUnique(String nodeCode, Long excludeId) {
        if (!StringUtils.hasText(nodeCode)) {
            return false;
        }
        
        LambdaQueryWrapper<Node> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Node::getNodeCode, nodeCode);
        
        if (excludeId != null) {
            wrapper.ne(Node::getId, excludeId);
        }
        
        return count(wrapper) == 0;
    }

    @Override
    public boolean isServerIpUnique(String serverIp, Long excludeId) {
        if (!StringUtils.hasText(serverIp)) {
            return false;
        }
        
        LambdaQueryWrapper<Node> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Node::getServerIp, serverIp);
        
        if (excludeId != null) {
            wrapper.ne(Node::getId, excludeId);
        }
        
        return count(wrapper) == 0;
    }

    @Override
    public BigDecimal calculateTotalCost() {
        return baseMapper.sumMonthlyCost();
    }

    @Override
    public List<String> getOptimizationSuggestions(Long nodeId) {
        Node node = getById(nodeId);
        if (node == null) {
            return List.of("节点不存在");
        }
        
        List<String> suggestions = new ArrayList<>();
        
        // CPU优化建议
        if (node.getCpuUsage().compareTo(new BigDecimal("80")) > 0) {
            suggestions.add("CPU使用率过高，建议：1. 检查进程占用；2. 考虑升级CPU；3. 分流部分连接到其他节点");
        } else if (node.getCpuUsage().compareTo(new BigDecimal("20")) < 0) {
            suggestions.add("CPU使用率较低，可以考虑承担更多连接或降级节点配置以节省成本");
        }
        
        // 内存优化建议
        if (node.getMemoryUsage().compareTo(new BigDecimal("80")) > 0) {
            suggestions.add("内存使用率过高，建议：1. 检查内存泄漏；2. 增加内存；3. 优化程序内存使用");
        }
        
        // 磁盘优化建议
        if (node.getDiskUsage().compareTo(new BigDecimal("90")) > 0) {
            suggestions.add("磁盘空间不足，建议：1. 清理日志文件；2. 扩容磁盘；3. 启用日志轮转");
        }
        
        // 连接数优化建议
        double connectionRate = (double) node.getCurrentConnections() / node.getMaxConnections();
        if (connectionRate > 0.9) {
            suggestions.add("连接数接近上限，建议：1. 增加最大连接数；2. 分流到其他节点；3. 优化连接处理效率");
        } else if (connectionRate < 0.1) {
            suggestions.add("连接数较低，可以考虑从其他高负载节点分流连接过来");
        }
        
        // 网络优化建议
        if (node.getNetworkLatency() != null && node.getNetworkLatency() > 200) {
            suggestions.add("网络延迟较高，建议：1. 检查网络配置；2. 考虑更换网络提供商；3. 启用网络优化");
        }
        
        if (suggestions.isEmpty()) {
            suggestions.add("节点运行状态良好，无需特殊优化");
        }
        
        return suggestions;
    }
}