package com.saas.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saas.platform.entity.Product;
import com.saas.platform.exception.BusinessException;
import com.saas.platform.mapper.ProductMapper;
import com.saas.platform.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 产品服务实现类
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Override
    public IPage<Product> getProductPage(Long current, Long size, String productName, String ipQuality, String productStatus) {
        Page<Product> page = new Page<>(current, size);
        return baseMapper.selectProductPage(page, productName, ipQuality, productStatus);
    }

    @Override
    public Product getByProductCode(String productCode) {
        if (!StringUtils.hasText(productCode)) {
            return null;
        }
        return baseMapper.selectByProductCode(productCode);
    }

    @Override
    public List<Product> getOnlineProducts() {
        return baseMapper.selectOnlineProducts();
    }

    @Override
    public List<Product> getByIpQuality(String ipQuality) {
        if (!StringUtils.hasText(ipQuality)) {
            return null;
        }
        return baseMapper.selectByIpQuality(ipQuality);
    }

    @Override
    @Transactional
    public boolean createProduct(Product product) {
        // 验证产品编码唯一性
        if (!isProductCodeUnique(product.getProductCode(), null)) {
            throw new BusinessException("产品编码已存在");
        }
        
        // 验证价格逻辑
        if (product.getCurrentPrice().compareTo(product.getOriginalPrice()) > 0) {
            throw new BusinessException("现价不能高于原价");
        }
        
        return save(product);
    }

    @Override
    @Transactional
    public boolean updateProduct(Product product) {
        // 验证产品是否存在
        Product existProduct = getById(product.getId());
        if (existProduct == null) {
            throw new BusinessException("产品不存在");
        }
        
        // 验证产品编码唯一性
        if (!isProductCodeUnique(product.getProductCode(), product.getId())) {
            throw new BusinessException("产品编码已存在");
        }
        
        // 验证价格逻辑
        if (product.getCurrentPrice().compareTo(product.getOriginalPrice()) > 0) {
            throw new BusinessException("现价不能高于原价");
        }
        
        return updateById(product);
    }

    @Override
    @Transactional
    public boolean deleteProduct(Long id) {
        Product product = getById(id);
        if (product == null) {
            throw new BusinessException("产品不存在");
        }
        
        // 检查是否有关联的订单（这里可以加入业务逻辑检查）
        // TODO: 检查是否有未完成的订单使用此产品
        
        return removeById(id);
    }

    @Override
    @Transactional
    public boolean batchDeleteProducts(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        
        // TODO: 批量检查是否有关联的订单
        
        return removeByIds(ids);
    }

    @Override
    @Transactional
    public boolean batchUpdateStatus(List<Long> ids, String status) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        
        return baseMapper.batchUpdateStatus(ids, status) > 0;
    }

    @Override
    @Transactional
    public boolean onlineProduct(Long id) {
        Product product = getById(id);
        if (product == null) {
            throw new BusinessException("产品不存在");
        }
        
        if ("上架".equals(product.getProductStatus())) {
            throw new BusinessException("产品已经是上架状态");
        }
        
        product.setProductStatus("上架");
        return updateById(product);
    }

    @Override
    @Transactional
    public boolean offlineProduct(Long id) {
        Product product = getById(id);
        if (product == null) {
            throw new BusinessException("产品不存在");
        }
        
        if ("下架".equals(product.getProductStatus())) {
            throw new BusinessException("产品已经是下架状态");
        }
        
        product.setProductStatus("下架");
        return updateById(product);
    }

    @Override
    public Map<String, Object> getProductStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        // 总产品数
        long totalCount = count();
        result.put("totalCount", totalCount);
        
        // 各状态产品数量
        List<Map<String, Object>> statusCount = baseMapper.countByStatus();
        result.put("statusCount", statusCount);
        
        // 上架产品数
        long onlineCount = count(new LambdaQueryWrapper<Product>()
                .eq(Product::getProductStatus, "上架"));
        result.put("onlineCount", onlineCount);
        
        // 下架产品数
        long offlineCount = count(new LambdaQueryWrapper<Product>()
                .eq(Product::getProductStatus, "下架"));
        result.put("offlineCount", offlineCount);
        
        return result;
    }

    @Override
    public boolean isProductCodeUnique(String productCode, Long excludeId) {
        if (!StringUtils.hasText(productCode)) {
            return false;
        }
        
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getProductCode, productCode);
        
        if (excludeId != null) {
            wrapper.ne(Product::getId, excludeId);
        }
        
        return count(wrapper) == 0;
    }
}