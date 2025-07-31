package com.saas.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.saas.platform.entity.IpPool;

import java.util.List;
import java.util.Map;

/**
 * IP池服务接口
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
public interface IpPoolService extends IService<IpPool> {

    /**
     * 分页查询IP池列表
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param ipAddress IP地址
     * @param country 国家
     * @param city 城市
     * @param ipQuality IP质量
     * @param ipStatus IP状态
     * @param nodeId 节点ID
     * @return 分页结果
     */
    IPage<IpPool> getIpPoolPage(Long current, Long size, String ipAddress, String country,
                               String city, String ipQuality, String ipStatus, Long nodeId);

    /**
     * 根据IP地址查询
     * 
     * @param ipAddress IP地址
     * @return IP信息
     */
    IpPool getByIpAddress(String ipAddress);

    /**
     * 查询可用的IP列表
     * 
     * @param country 国家
     * @param ipQuality IP质量
     * @param limit 限制数量
     * @return IP列表
     */
    List<IpPool> getAvailableIps(String country, String ipQuality, Integer limit);

    /**
     * 查询节点下的IP列表
     * 
     * @param nodeId 节点ID
     * @return IP列表
     */
    List<IpPool> getByNodeId(Long nodeId);

    /**
     * 查询被订单占用的IP列表
     * 
     * @param orderId 订单ID
     * @return IP列表
     */
    List<IpPool> getByOrderId(Long orderId);

    /**
     * 创建IP
     * 
     * @param ipPool IP信息
     * @return 是否成功
     */
    boolean createIpPool(IpPool ipPool);

    /**
     * 更新IP信息
     * 
     * @param ipPool IP信息
     * @return 是否成功
     */
    boolean updateIpPool(IpPool ipPool);

    /**
     * 删除IP
     * 
     * @param id IP ID
     * @return 是否成功
     */
    boolean deleteIpPool(Long id);

    /**
     * 批量删除IP
     * 
     * @param ids ID列表
     * @return 是否成功
     */
    boolean batchDeleteIpPools(List<Long> ids);

    /**
     * 批量导入IP
     * 
     * @param ipPools IP列表
     * @return 成功导入的数量
     */
    int batchImportIps(List<IpPool> ipPools);

    /**
     * 批量分配IP给订单
     * 
     * @param ipIds IP ID列表
     * @param orderId 订单ID
     * @return 是否成功
     */
    boolean batchAssignToOrder(List<Long> ipIds, Long orderId);

    /**
     * 批量释放IP
     * 
     * @param ipIds IP ID列表
     * @return 是否成功
     */
    boolean batchReleaseIps(List<Long> ipIds);

    /**
     * 批量更新IP状态
     * 
     * @param ids ID列表
     * @param status 新状态
     * @return 是否成功
     */
    boolean batchUpdateStatus(List<Long> ids, String status);

    /**
     * 测试IP连通性
     * 
     * @param ipAddress IP地址
     * @return 测试结果
     */
    Map<String, Object> testIpConnectivity(String ipAddress);

    /**
     * 批量测试IP
     * 
     * @param ipIds IP ID列表
     * @return 测试结果统计
     */
    Map<String, Object> batchTestIps(List<Long> ipIds);

    /**
     * 自动分配IP
     * 
     * @param country 国家
     * @param ipQuality IP质量
     * @param count 数量
     * @param orderId 订单ID
     * @return 分配的IP列表
     */
    List<IpPool> autoAssignIps(String country, String ipQuality, Integer count, Long orderId);

    /**
     * 更换IP
     * 
     * @param oldIpId 旧IP ID
     * @param orderId 订单ID
     * @param reason 更换原因
     * @return 新IP信息
     */
    IpPool replaceIp(Long oldIpId, Long orderId, String reason);

    /**
     * 查询测试失败的IP
     * 
     * @return IP列表
     */
    List<IpPool> getFailedTestIps();

    /**
     * 查询长时间未测试的IP
     * 
     * @param hours 小时数
     * @return IP列表
     */
    List<IpPool> getUntestedIps(Integer hours);

    /**
     * 定时检查IP状态
     * 
     * @return 检查结果
     */
    Map<String, Object> scheduleIpCheck();

    /**
     * 获取IP池统计信息
     * 
     * @return 统计结果
     */
    Map<String, Object> getIpPoolStatistics();

    /**
     * 验证IP地址是否唯一
     * 
     * @param ipAddress IP地址
     * @param excludeId 排除的ID（用于更新时验证）
     * @return 是否唯一
     */
    boolean isIpAddressUnique(String ipAddress, Long excludeId);

    /**
     * 根据条件筛选可用IP
     * 
     * @param country 国家
     * @param city 城市
     * @param ipQuality IP质量
     * @param minBandwidth 最小带宽
     * @param count 需要数量
     * @return IP列表
     */
    List<IpPool> filterAvailableIps(String country, String city, String ipQuality, 
                                   Integer minBandwidth, Integer count);
}