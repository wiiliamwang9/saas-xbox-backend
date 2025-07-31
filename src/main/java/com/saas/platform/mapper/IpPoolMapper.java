package com.saas.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saas.platform.entity.IpPool;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * IP池数据访问层接口
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Mapper
public interface IpPoolMapper extends BaseMapper<IpPool> {

    /**
     * 分页查询IP池列表
     * 
     * @param page 分页对象
     * @param ipAddress IP地址
     * @param country 国家
     * @param city 城市
     * @param ipQuality IP质量
     * @param ipStatus IP状态
     * @param nodeId 节点ID
     * @return 分页结果
     */
    IPage<IpPool> selectIpPoolPage(Page<IpPool> page,
                                  @Param("ipAddress") String ipAddress,
                                  @Param("country") String country,
                                  @Param("city") String city,
                                  @Param("ipQuality") String ipQuality,
                                  @Param("ipStatus") String ipStatus,
                                  @Param("nodeId") Long nodeId);

    /**
     * 根据IP地址查询
     * 
     * @param ipAddress IP地址
     * @return IP信息
     */
    IpPool selectByIpAddress(@Param("ipAddress") String ipAddress);

    /**
     * 查询可用的IP列表
     * 
     * @param country 国家
     * @param ipQuality IP质量
     * @param limit 限制数量
     * @return IP列表
     */
    List<IpPool> selectAvailableIps(@Param("country") String country,
                                   @Param("ipQuality") String ipQuality,
                                   @Param("limit") Integer limit);

    /**
     * 查询节点下的IP列表
     * 
     * @param nodeId 节点ID
     * @return IP列表
     */
    List<IpPool> selectByNodeId(@Param("nodeId") Long nodeId);

    /**
     * 查询被订单占用的IP列表
     * 
     * @param orderId 订单ID
     * @return IP列表
     */
    List<IpPool> selectByOrderId(@Param("orderId") Long orderId);

    /**
     * 批量分配IP给订单
     * 
     * @param ipIds IP ID列表
     * @param orderId 订单ID
     * @return 影响行数
     */
    int batchAssignToOrder(@Param("ipIds") List<Long> ipIds, @Param("orderId") Long orderId);

    /**
     * 批量释放IP
     * 
     * @param ipIds IP ID列表
     * @return 影响行数
     */
    int batchReleaseIps(@Param("ipIds") List<Long> ipIds);

    /**
     * 批量更新IP状态
     * 
     * @param ids ID列表
     * @param status 新状态
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") String status);

    /**
     * 统计各状态IP数量
     * 
     * @return 统计结果
     */
    List<java.util.Map<String, Object>> countByStatus();

    /**
     * 按国家统计IP数量
     * 
     * @return 统计结果
     */
    List<java.util.Map<String, Object>> countByCountry();

    /**
     * 按IP质量统计数量
     * 
     * @return 统计结果
     */
    List<java.util.Map<String, Object>> countByQuality();

    /**
     * 查询测试失败的IP
     * 
     * @return IP列表
     */
    List<IpPool> selectFailedTestIps();

    /**
     * 查询长时间未测试的IP
     * 
     * @param hours 小时数
     * @return IP列表
     */
    List<IpPool> selectUntestedIps(@Param("hours") Integer hours);

    /**
     * 批量更新IP测试结果
     * 
     * @param ipAddress IP地址
     * @param testResult 测试结果
     * @param testLatency 测试延迟
     * @param testMessage 测试消息
     * @return 影响行数
     */
    int updateTestResult(@Param("ipAddress") String ipAddress,
                        @Param("testResult") String testResult,
                        @Param("testLatency") Integer testLatency,
                        @Param("testMessage") String testMessage);
}