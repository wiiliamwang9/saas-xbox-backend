package com.saas.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.saas.platform.entity.Product;

import java.util.List;
import java.util.Map;

/**
 * 产品服务接口
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
public interface ProductService extends IService<Product> {

    /**
     * 分页查询产品列表
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param productName 产品名称
     * @param ipQuality IP质量
     * @param productStatus 产品状态
     * @return 分页结果
     */
    IPage<Product> getProductPage(Long current, Long size, String productName, String ipQuality, String productStatus);

    /**
     * 根据产品编码查询产品
     * 
     * @param productCode 产品编码
     * @return 产品信息
     */
    Product getByProductCode(String productCode);

    /**
     * 查询上架的产品列表
     * 
     * @return 产品列表
     */
    List<Product> getOnlineProducts();

    /**
     * 根据IP质量查询产品列表
     * 
     * @param ipQuality IP质量
     * @return 产品列表
     */
    List<Product> getByIpQuality(String ipQuality);

    /**
     * 创建产品
     * 
     * @param product 产品信息
     * @return 是否成功
     */
    boolean createProduct(Product product);

    /**
     * 更新产品信息
     * 
     * @param product 产品信息
     * @return 是否成功
     */
    boolean updateProduct(Product product);

    /**
     * 删除产品
     * 
     * @param id 产品ID
     * @return 是否成功
     */
    boolean deleteProduct(Long id);

    /**
     * 批量删除产品
     * 
     * @param ids ID列表
     * @return 是否成功
     */
    boolean batchDeleteProducts(List<Long> ids);

    /**
     * 批量更新产品状态
     * 
     * @param ids ID列表
     * @param status 新状态
     * @return 是否成功
     */
    boolean batchUpdateStatus(List<Long> ids, String status);

    /**
     * 产品上架
     * 
     * @param id 产品ID
     * @return 是否成功
     */
    boolean onlineProduct(Long id);

    /**
     * 产品下架
     * 
     * @param id 产品ID
     * @return 是否成功
     */
    boolean offlineProduct(Long id);

    /**
     * 获取产品统计信息
     * 
     * @return 统计结果
     */
    Map<String, Object> getProductStatistics();

    /**
     * 验证产品编码是否唯一
     * 
     * @param productCode 产品编码
     * @param excludeId 排除的ID（用于更新时验证）
     * @return 是否唯一
     */
    boolean isProductCodeUnique(String productCode, Long excludeId);
}