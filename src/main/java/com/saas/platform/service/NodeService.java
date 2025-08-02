package com.saas.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.saas.platform.entity.Node;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 节点服务接口
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
public interface NodeService extends IService<Node> {

    /**
     * 分页查询节点列表
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param nodeName 节点名称
     * @param country 国家
     * @param nodeType 节点类型
     * @param nodeStatus 节点状态
     * @return 分页结果
     */
    IPage<Node> getNodePage(Long current, Long size, String nodeName, String country,
                           String nodeType, String nodeStatus);

    /**
     * 根据节点编码查询节点
     * 
     * @param nodeCode 节点编码
     * @return 节点信息
     */
    Node getByNodeCode(String nodeCode);

    /**
     * 根据服务器IP查询节点
     * 
     * @param serverIp 服务器IP
     * @return 节点信息
     */
    Node getByServerIp(String serverIp);

    /**
     * 查询运行中的节点列表
     * 
     * @return 节点列表
     */
    List<Node> getRunningNodes();

    /**
     * 按国家查询节点列表
     * 
     * @param country 国家
     * @return 节点列表
     */
    List<Node> getByCountry(String country);

    /**
     * 查询需要检查的节点（长时间未检查）
     * 
     * @param hours 小时数
     * @return 节点列表
     */
    List<Node> getNeedCheckNodes(Integer hours);

    /**
     * 创建节点
     * 
     * @param node 节点信息
     * @return 是否成功
     */
    boolean createNode(Node node);

    /**
     * 更新节点信息
     * 
     * @param node 节点信息
     * @return 是否成功
     */
    boolean updateNode(Node node);

    /**
     * 删除节点
     * 
     * @param id 节点ID
     * @return 是否成功
     */
    boolean deleteNode(Long id);

    /**
     * 批量删除节点
     * 
     * @param ids ID列表
     * @return 是否成功
     */
    boolean batchDeleteNodes(List<Long> ids);

    /**
     * 批量更新节点状态
     * 
     * @param ids ID列表
     * @param status 新状态
     * @return 是否成功
     */
    boolean batchUpdateStatus(List<Long> ids, String status);

    /**
     * 启动节点
     * 
     * @param id 节点ID
     * @return 是否成功
     */
    boolean startNode(Long id);

    /**
     * 停止节点
     * 
     * @param id 节点ID
     * @param reason 停止原因
     * @return 是否成功
     */
    boolean stopNode(Long id, String reason);

    /**
     * 重启节点
     * 
     * @param id 节点ID
     * @return 是否成功
     */
    boolean restartNode(Long id);

    /**
     * 节点进入维护模式
     * 
     * @param id 节点ID
     * @param reason 维护原因
     * @return 是否成功
     */
    boolean maintainNode(Long id, String reason);

    /**
     * 节点退出维护模式
     * 
     * @param id 节点ID
     * @return 是否成功
     */
    boolean exitMaintainNode(Long id);

    /**
     * 更新节点监控数据
     * 
     * @param nodeId 节点ID
     * @param currentConnections 当前连接数
     * @param cpuUsage CPU使用率
     * @param memoryUsage 内存使用率
     * @param diskUsage 磁盘使用率
     * @param networkLatency 网络延迟
     * @return 是否成功
     */
    boolean updateMonitorData(Long nodeId, Integer currentConnections, BigDecimal cpuUsage,
                             BigDecimal memoryUsage, BigDecimal diskUsage, Integer networkLatency);

    /**
     * 检查节点健康状态
     * 
     * @param nodeId 节点ID
     * @return 健康状态信息
     */
    Map<String, Object> checkNodeHealth(Long nodeId);

    /**
     * 批量检查节点健康状态
     * 
     * @param nodeIds 节点ID列表
     * @return 检查结果
     */
    Map<String, Object> batchCheckNodeHealth(List<Long> nodeIds);

    /**
     * 查询高负载节点
     * 
     * @param cpuThreshold CPU阈值
     * @param memoryThreshold 内存阈值
     * @return 节点列表
     */
    List<Node> getHighLoadNodes(BigDecimal cpuThreshold, BigDecimal memoryThreshold);

    /**
     * 查询连接数接近上限的节点
     * 
     * @param threshold 阈值百分比
     * @return 节点列表
     */
    List<Node> getHighConnectionNodes(Integer threshold);

    /**
     * 节点负载均衡
     * 
     * @param country 国家
     * @return 推荐的节点
     */
    Node getRecommendedNode(String country);

    /**
     * 定时检查节点状态
     * 
     * @return 检查结果
     */
    Map<String, Object> scheduleNodeCheck();

    /**
     * 获取节点统计信息
     * 
     * @return 统计结果
     */
    Map<String, Object> getNodeStatistics();

    /**
     * 获取节点监控数据
     * 
     * @param nodeId 节点ID
     * @param hours 时间范围（小时）
     * @return 监控数据
     */
    Map<String, Object> getNodeMonitorData(Long nodeId, Integer hours);

    /**
     * 验证节点编码是否唯一
     * 
     * @param nodeCode 节点编码
     * @param excludeId 排除的ID（用于更新时验证）
     * @return 是否唯一
     */
    boolean isNodeCodeUnique(String nodeCode, Long excludeId);

    /**
     * 验证服务器IP是否唯一
     * 
     * @param serverIp 服务器IP
     * @param excludeId 排除的ID（用于更新时验证）
     * @return 是否唯一
     */
    boolean isServerIpUnique(String serverIp, Long excludeId);

    /**
     * 计算节点总成本
     * 
     * @return 总成本
     */
    BigDecimal calculateTotalCost();

    /**
     * 节点性能优化建议
     * 
     * @param nodeId 节点ID
     * @return 优化建议
     */
    List<String> getOptimizationSuggestions(Long nodeId);

    /**
     * 部署Agent
     * 
     * @param nodeId 节点ID
     * @return 是否成功
     */
    boolean deployAgent(Long nodeId);

    /**
     * 删除Agent
     * 
     * @param nodeId 节点ID
     * @return 是否成功
     */
    boolean deleteAgent(Long nodeId);
}