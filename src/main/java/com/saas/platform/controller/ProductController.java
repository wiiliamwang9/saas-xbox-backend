package com.saas.platform.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.saas.platform.dto.Result;
import com.saas.platform.entity.Product;
import com.saas.platform.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 产品管理控制器
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Tag(name = "产品管理", description = "产品管理相关接口")
@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 分页查询产品列表
     */
    @Operation(summary = "分页查询产品列表", description = "支持按产品名称、IP质量、产品状态筛选")
    @GetMapping("/page")
    public Result<IPage<Product>> getProductPage(
            @Parameter(description = "当前页", example = "1") @RequestParam(defaultValue = "1") Long current,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") Long size,
            @Parameter(description = "产品名称") @RequestParam(required = false) String productName,
            @Parameter(description = "IP质量") @RequestParam(required = false) String ipQuality,
            @Parameter(description = "产品状态") @RequestParam(required = false) String productStatus) {
        IPage<Product> page = productService.getProductPage(current, size, productName, ipQuality, productStatus);
        return Result.success(page);
    }

    /**
     * 查询上架产品列表
     */
    @Operation(summary = "查询上架产品列表", description = "获取所有上架状态的产品")
    @GetMapping("/online")
    public Result<List<Product>> getOnlineProducts() {
        List<Product> products = productService.getOnlineProducts();
        return Result.success(products);
    }

    /**
     * 根据IP质量查询产品
     */
    @Operation(summary = "根据IP质量查询产品", description = "按IP质量筛选产品")
    @GetMapping("/quality/{ipQuality}")
    public Result<List<Product>> getByIpQuality(
            @Parameter(description = "IP质量", example = "标准") @PathVariable String ipQuality) {
        List<Product> products = productService.getByIpQuality(ipQuality);
        return Result.success(products);
    }

    /**
     * 根据ID查询产品详情
     */
    @Operation(summary = "查询产品详情", description = "根据产品ID获取详细信息")
    @GetMapping("/{id}")
    public Result<Product> getProductById(
            @Parameter(description = "产品ID", example = "1") @PathVariable @NotNull Long id) {
        Product product = productService.getById(id);
        if (product == null) {
            return Result.error("产品不存在");
        }
        return Result.success(product);
    }

    /**
     * 根据产品编码查询产品
     */
    @Operation(summary = "根据产品编码查询产品", description = "根据产品编码获取产品信息")
    @GetMapping("/code/{productCode}")
    public Result<Product> getByProductCode(
            @Parameter(description = "产品编码", example = "US-STD-30") @PathVariable String productCode) {
        Product product = productService.getByProductCode(productCode);
        if (product == null) {
            return Result.error("产品不存在");
        }
        return Result.success(product);
    }

    /**
     * 创建产品
     */
    @Operation(summary = "创建产品", description = "添加新产品")
    @PostMapping
    public Result<String> createProduct(@Valid @RequestBody Product product) {
        boolean success = productService.createProduct(product);
        return success ? Result.success("产品创建成功") : Result.error("产品创建失败");
    }

    /**
     * 更新产品信息
     */
    @Operation(summary = "更新产品信息", description = "修改产品信息")
    @PutMapping("/{id}")
    public Result<String> updateProduct(
            @Parameter(description = "产品ID", example = "1") @PathVariable @NotNull Long id,
            @Valid @RequestBody Product product) {
        product.setId(id);
        boolean success = productService.updateProduct(product);
        return success ? Result.success("产品更新成功") : Result.error("产品更新失败");
    }

    /**
     * 删除产品
     */
    @Operation(summary = "删除产品", description = "根据ID删除产品")
    @DeleteMapping("/{id}")
    public Result<String> deleteProduct(
            @Parameter(description = "产品ID", example = "1") @PathVariable @NotNull Long id) {
        boolean success = productService.deleteProduct(id);
        return success ? Result.success("产品删除成功") : Result.error("产品删除失败");
    }

    /**
     * 批量删除产品
     */
    @Operation(summary = "批量删除产品", description = "根据ID列表批量删除产品")
    @DeleteMapping("/batch")
    public Result<String> batchDeleteProducts(@RequestBody @NotEmpty List<Long> ids) {
        boolean success = productService.batchDeleteProducts(ids);
        return success ? Result.success("产品批量删除成功") : Result.error("产品批量删除失败");
    }

    /**
     * 批量更新产品状态
     */
    @Operation(summary = "批量更新产品状态", description = "批量修改产品状态")
    @PutMapping("/batch/status")
    public Result<String> batchUpdateStatus(
            @RequestBody @NotEmpty List<Long> ids,
            @Parameter(description = "新状态", example = "上架") @RequestParam String status) {
        boolean success = productService.batchUpdateStatus(ids, status);
        return success ? Result.success("产品状态批量更新成功") : Result.error("产品状态批量更新失败");
    }

    /**
     * 产品上架
     */
    @Operation(summary = "产品上架", description = "将产品状态设置为上架")
    @PutMapping("/{id}/online")
    public Result<String> onlineProduct(
            @Parameter(description = "产品ID", example = "1") @PathVariable @NotNull Long id) {
        boolean success = productService.onlineProduct(id);
        return success ? Result.success("产品上架成功") : Result.error("产品上架失败");
    }

    /**
     * 产品下架
     */
    @Operation(summary = "产品下架", description = "将产品状态设置为下架")
    @PutMapping("/{id}/offline")
    public Result<String> offlineProduct(
            @Parameter(description = "产品ID", example = "1") @PathVariable @NotNull Long id) {
        boolean success = productService.offlineProduct(id);
        return success ? Result.success("产品下架成功") : Result.error("产品下架失败");
    }

    /**
     * 获取产品统计信息
     */
    @Operation(summary = "获取产品统计信息", description = "获取产品的各种统计数据")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getProductStatistics() {
        Map<String, Object> statistics = productService.getProductStatistics();
        return Result.success(statistics);
    }

    /**
     * 验证产品编码是否唯一
     */
    @Operation(summary = "验证产品编码唯一性", description = "检查产品编码是否已存在")
    @GetMapping("/validate/code")
    public Result<Boolean> validateProductCode(
            @Parameter(description = "产品编码", example = "US-STD-30") @RequestParam String productCode,
            @Parameter(description = "排除的产品ID（用于更新时验证）") @RequestParam(required = false) Long excludeId) {
        boolean isUnique = productService.isProductCodeUnique(productCode, excludeId);
        return Result.success(isUnique);
    }
}