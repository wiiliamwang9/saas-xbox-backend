package com.saas.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saas.platform.entity.ProxyTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Proxy模板数据访问层接口
 * 
 * @author SaaS Xbox Team
 * @since 2024-08-04
 */
@Mapper
public interface ProxyTemplateMapper extends BaseMapper<ProxyTemplate> {

    /**
     * 分页查询代理产品模板列表
     * 
     * @param page 分页对象
     * @param productName 产品名称
     * @param usageScenario 使用场景
     * @param country 国家
     * @param status 状态
     * @return 分页结果
     */
    IPage<ProxyTemplate> selectTemplatePage(Page<ProxyTemplate> page,
                                          @Param("productName") String productName,
                                          @Param("usageScenario") String usageScenario,
                                          @Param("country") String country,
                                          @Param("status") String status);

    /**
     * 根据产品名称查询模板
     * 
     * @param productName 产品名称
     * @return 模板信息
     */
    ProxyTemplate selectByProductName(@Param("productName") String productName);

    /**
     * 根据国家查询模板列表
     * 
     * @param country 国家
     * @return 模板列表
     */
    List<ProxyTemplate> selectByCountry(@Param("country") String country);

    /**
     * 根据使用场景查询模板列表
     * 
     * @param usageScenario 使用场景
     * @return 模板列表
     */
    List<ProxyTemplate> selectByUsageScenario(@Param("usageScenario") String usageScenario);

    /**
     * 根据协议类型查询模板列表
     * 
     * @param protocolType 协议类型
     * @return 模板列表
     */
    List<ProxyTemplate> selectByProtocolType(@Param("protocolType") String protocolType);

    /**
     * 查询启用状态的模板列表
     * 
     * @return 模板列表
     */
    List<ProxyTemplate> selectEnabledTemplates();

    /**
     * 批量更新模板状态
     * 
     * @param ids ID列表
     * @param status 新状态
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") String status);

    /**
     * 统计各国家模板数量
     * 
     * @return 统计结果
     */
    List<java.util.Map<String, Object>> countByCountry();

    /**
     * 统计各使用场景模板数量
     * 
     * @return 统计结果
     */
    List<java.util.Map<String, Object>> countByUsageScenario();

    /**
     * 统计各代理模式模板数量
     * 
     * @return 统计结果
     */
    List<java.util.Map<String, Object>> countByProxyMode();

    /**
     * 统计各协议类型模板数量
     * 
     * @return 统计结果
     */
    List<java.util.Map<String, Object>> countByProtocolType();

    /**
     * 统计各状态模板数量
     * 
     * @return 统计结果
     */
    List<java.util.Map<String, Object>> countByStatus();

    /**
     * 统计总库存
     * 
     * @return 总库存数量
     */
    Long sumStock();

    /**
     * 计算平均价格
     * 
     * @return 平均价格
     */
    java.math.BigDecimal avgPrice();
}