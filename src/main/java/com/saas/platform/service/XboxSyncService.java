package com.saas.platform.service;

import com.saas.platform.dto.Result;
import com.saas.platform.entity.Node;
import java.util.List;

/**
 * Xbox系统同步服务接口
 * 负责从Xbox Controller同步节点信息到SaaS后台
 *
 * @author SaaS Xbox Team
 */
public interface XboxSyncService {

    /**
     * 同步所有节点信息
     * @return 同步结果
     */
    Result<String> syncAllNodes();

    /**
     * 同步指定节点信息
     * @param agentId Agent ID
     * @return 同步结果
     */
    Result<String> syncNodeById(String agentId);

    /**
     * 获取Xbox Controller中的节点列表
     * @return 节点列表
     */
    Result<List<Node>> getXboxNodes();

    /**
     * 同步节点状态信息
     * @return 同步结果
     */
    Result<String> syncNodeStatus();

    /**
     * 同步协议支持信息
     * @return 同步结果
     */
    Result<String> syncProtocolInfo();

    /**
     * 检查Xbox Controller连接状态
     * @return 连接状态
     */
    Result<Boolean> checkXboxConnection();

    /**
     * 部署Agent到远程节点
     * @param nodeIp 节点IP地址
     * @param sshPort SSH端口
     * @param sshUser SSH用户名
     * @param sshPassword SSH密码
     * @return 部署结果
     */
    String deployAgentToNode(String nodeIp, Integer sshPort, String sshUser, String sshPassword);

    /**
     * 更新Agent配置
     * @param agentId Agent ID
     * @param configType 配置类型
     * @param configContent 配置内容
     * @return 更新结果
     */
    String updateAgentConfig(String agentId, String configType, String configContent);

    /**
     * 获取Agent监控信息
     * @param agentId Agent ID
     * @return 监控数据
     */
    Object getAgentMonitoring(String agentId);

    /**
     * 测试Agent连接和功能
     * @param agentId Agent ID
     * @param testType 测试类型
     * @return 测试结果
     */
    String testAgent(String agentId, String testType);
}