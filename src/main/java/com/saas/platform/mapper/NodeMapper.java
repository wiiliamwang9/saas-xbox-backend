package com.saas.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saas.platform.entity.Node;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 节点数据访问层接口
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Mapper
public interface NodeMapper extends BaseMapper<Node> {

    /**
     * 分页查询节点列表
     * 
     * @param page 分页对象
     * @param nodeName 节点名称
     * @param country 国家
     * @param nodeType 节点类型
     * @param nodeStatus 节点状态
     * @return 分页结果
     */
    IPage<Node> selectNodePage(Page<Node> page,
                              @Param("nodeName") String nodeName,
                              @Param("country") String country,
                              @Param("nodeType") String nodeType,
                              @Param("nodeStatus") String nodeStatus);

    /**
     * 根据节点编码查询节点
     * 
     * @param nodeCode 节点编码
     * @return 节点信息
     */
    Node selectByNodeCode(@Param("nodeCode") String nodeCode);

    /**
     * 根据服务器IP查询节点
     * 
     * @param serverIp 服务器IP
     * @return 节点信息
     */
    Node selectByServerIp(@Param("serverIp") String serverIp);

    /**
     * 查询运行中的节点列表
     * 
     * @return 节点列表
     */
    List<Node> selectRunningNodes();

    /**
     * 按国家查询节点列表
     * 
     * @param country 国家
     * @return 节点列表
     */
    List<Node> selectByCountry(@Param("country") String country);

    /**
     * 查询需要检查的节点（长时间未检查）
     * 
     * @param hours 小时数
     * @return 节点列表
     */
    List<Node> selectNeedCheckNodes(@Param("hours") Integer hours);

    /**
     * 批量更新节点状态
     * 
     * @param ids ID列表
     * @param status 新状态
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") String status);

    /**
     * 更新节点监控数据
     * 
     * @param nodeId 节点ID
     * @param currentConnections 当前连接数
     * @param cpuUsage CPU使用率
     * @param memoryUsage 内存使用率
     * @param diskUsage 磁盘使用率
     * @param networkLatency 网络延迟
     * @return 影响行数
     */
    int updateMonitorData(@Param("nodeId") Long nodeId,
                         @Param("currentConnections") Integer currentConnections,
                         @Param("cpuUsage") BigDecimal cpuUsage,
                         @Param("memoryUsage") BigDecimal memoryUsage,
                         @Param("diskUsage") BigDecimal diskUsage,
                         @Param("networkLatency") Integer networkLatency);

    /**
     * 统计各状态节点数量
     * 
     * @return 统计结果
     */
    List<java.util.Map<String, Object>> countByStatus();

    /**
     * 按国家统计节点数量
     * 
     * @return 统计结果
     */
    List<java.util.Map<String, Object>> countByCountry();

    /**
     * 按节点类型统计数量
     * 
     * @return 统计结果
     */
    List<java.util.Map<String, Object>> countByType();

    /**
     * 查询高负载节点（CPU或内存使用率高）
     * 
     * @param cpuThreshold CPU阈值
     * @param memoryThreshold 内存阈值
     * @return 节点列表
     */
    List<Node> selectHighLoadNodes(@Param("cpuThreshold") BigDecimal cpuThreshold,
                                  @Param("memoryThreshold") BigDecimal memoryThreshold);

    /**
     * 查询连接数接近上限的节点
     * 
     * @param threshold 阈值百分比（如90表示90%）
     * @return 节点列表
     */
    List<Node> selectHighConnectionNodes(@Param("threshold") Integer threshold);

    /**
     * 统计节点总成本
     * 
     * @return 总成本
     */
    BigDecimal sumMonthlyCost();
}