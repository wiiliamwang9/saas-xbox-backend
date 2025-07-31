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
}