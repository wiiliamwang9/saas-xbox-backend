package com.saas.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saas.platform.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品数据访问层接口
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 分页查询产品列表
     * 
     * @param page 分页对象
     * @param productName 产品名称
     * @param ipQuality IP质量
     * @param productStatus 产品状态
     * @return 分页结果
     */
    IPage<Product> selectProductPage(Page<Product> page, 
                                   @Param("productName") String productName,
                                   @Param("ipQuality") String ipQuality,
                                   @Param("productStatus") String productStatus);

    /**
     * 根据产品编码查询产品
     * 
     * @param productCode 产品编码
     * @return 产品信息
     */
    Product selectByProductCode(@Param("productCode") String productCode);

    /**
     * 查询上架的产品列表
     * 
     * @return 产品列表
     */
    List<Product> selectOnlineProducts();

    /**
     * 根据IP质量查询产品列表
     * 
     * @param ipQuality IP质量
     * @return 产品列表
     */
    List<Product> selectByIpQuality(@Param("ipQuality") String ipQuality);

    /**
     * 批量更新产品状态
     * 
     * @param ids ID列表
     * @param status 新状态
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") String status);

    /**
     * 统计各状态产品数量
     * 
     * @return 统计结果
     */
    List<java.util.Map<String, Object>> countByStatus();
}