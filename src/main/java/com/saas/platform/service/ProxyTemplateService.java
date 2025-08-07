package com.saas.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.saas.platform.entity.ProxyTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 代理产品模板服务接口 - 支持sing-box配置
 * 基于3xui面板的Inbound设计，支持完整的sing-box协议配置
 * 
 * @author SaaS Xbox Team
 * @since 2024-08-04
 */
public interface ProxyTemplateService extends IService<ProxyTemplate> {

    /**
     * 分页查询产品模板列表
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param productName 产品名称
     * @param usageScenario 使用场景
     * @param country 国家
     * @param status 状态
     * @return 分页结果
     */
    IPage<ProxyTemplate> getTemplatePage(Long current, Long size, String productName, 
                                       String usageScenario, String country, String status);

    /**
     * 根据国家查询产品模板列表
     * 
     * @param country 国家
     * @return 产品模板列表
     */
    List<ProxyTemplate> getByCountry(String country);

    /**
     * 根据使用场景查询产品模板列表
     * 
     * @param usageScenario 使用场景
     * @return 产品模板列表
     */
    List<ProxyTemplate> getByUsageScenario(String usageScenario);

    /**
     * 查询上架状态的产品模板列表
     * 
     * @return 产品模板列表
     */
    List<ProxyTemplate> getEnabledTemplates();

    /**
     * 创建产品模板
     * 
     * @param template 产品模板信息
     * @return 是否成功
     */
    boolean createTemplate(ProxyTemplate template);

    /**
     * 更新产品模板信息
     * 
     * @param template 产品模板信息
     * @return 是否成功
     */
    boolean updateTemplate(ProxyTemplate template);

    /**
     * 删除产品模板
     * 
     * @param id 模板ID
     * @return 是否成功
     */
    boolean deleteTemplate(Long id);

    /**
     * 批量删除产品模板
     * 
     * @param ids ID列表
     * @return 是否成功
     */
    boolean batchDeleteTemplates(List<Long> ids);

    /**
     * 批量更新产品模板状态
     * 
     * @param ids ID列表
     * @param status 新状态
     * @return 是否成功
     */
    boolean batchUpdateStatus(List<Long> ids, String status);

    /**
     * 验证产品名称是否唯一
     * 
     * @param productName 产品名称
     * @param excludeId 排除的ID（用于更新时验证）
     * @return 是否唯一
     */
    boolean isProductNameUnique(String productName, Long excludeId);

    /**
     * 获取产品模板统计信息
     * 
     * @return 统计结果
     */
    Map<String, Object> getTemplateStatistics();

    /**
     * 复制产品模板
     * 
     * @param id 源模板ID
     * @param newProductName 新产品名称
     * @return 是否成功
     */
    boolean copyTemplate(Long id, String newProductName);
    
    /**
     * 验证Inbound标识是否唯一
     * 
     * @param tag Inbound标识
     * @param excludeId 排除的ID（用于更新时验证）
     * @return 是否唯一
     */
    boolean isTagUnique(String tag, Long excludeId);
    
    /**
     * 验证端口是否可用
     * 
     * @param port 端口号
     * @param excludeId 排除的ID（用于更新时验证）
     * @return 是否可用
     */
    boolean isPortAvailable(Integer port, Long excludeId);
    
    /**
     * 根据协议类型查询模板
     * 
     * @param protocolType 协议类型
     * @return 产品模板列表
     */
    List<ProxyTemplate> getByProtocolType(String protocolType);
    
    /**
     * 构建sing-box配置
     * 
     * @param template 产品模板
     * @return sing-box配置
     */
    Map<String, Object> buildSingboxConfig(ProxyTemplate template);
    
    /**
     * 验证sing-box配置有效性
     * 
     * @param config sing-box配置
     * @return 验证结果
     */
    Map<String, Object> validateSingboxConfig(Map<String, Object> config);

    /**
     * 上传模板图片
     * 
     * @param file 图片文件
     * @return 图片URL
     */
    String uploadTemplateImage(MultipartFile file);
}