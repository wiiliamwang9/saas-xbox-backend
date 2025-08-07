package com.saas.platform.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.saas.platform.dto.Result;
import com.saas.platform.entity.ProxyTemplate;
import com.saas.platform.service.ProxyTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 代理产品模板管理控制器
 * 
 * @author SaaS Xbox Team
 * @since 2024-08-04
 */
@Tag(name = "代理产品模板管理", description = "代理产品模板管理相关接口")
@RestController
@RequestMapping("/proxy-templates")
@Validated
public class ProxyTemplateController {

    @Autowired
    private ProxyTemplateService proxyTemplateService;

    /**
     * 分页查询产品模板列表
     */
    @Operation(summary = "分页查询产品模板列表", description = "支持按产品名称、使用场景、国家、状态筛选")
    @GetMapping("/page")
    public Result<IPage<ProxyTemplate>> getTemplatePage(
            @Parameter(description = "当前页", example = "1") @RequestParam(defaultValue = "1") Long current,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") Long size,
            @Parameter(description = "产品名称") @RequestParam(required = false) String productName,
            @Parameter(description = "使用场景") @RequestParam(required = false) String usageScenario,
            @Parameter(description = "国家") @RequestParam(required = false) String country,
            @Parameter(description = "状态") @RequestParam(required = false) String status) {
        IPage<ProxyTemplate> page = proxyTemplateService.getTemplatePage(current, size, productName, usageScenario, country, status);
        return Result.success(page);
    }

    /**
     * 查询上架状态的产品模板列表
     */
    @Operation(summary = "查询上架状态的产品模板列表", description = "获取所有上架状态的产品模板")
    @GetMapping("/enabled")
    public Result<List<ProxyTemplate>> getEnabledTemplates() {
        List<ProxyTemplate> templates = proxyTemplateService.getEnabledTemplates();
        return Result.success(templates);
    }

    /**
     * 按国家查询产品模板列表
     */
    @Operation(summary = "按国家查询产品模板列表", description = "根据国家筛选产品模板")
    @GetMapping("/country/{country}")
    public Result<List<ProxyTemplate>> getByCountry(
            @Parameter(description = "国家", example = "美国") @PathVariable String country) {
        List<ProxyTemplate> templates = proxyTemplateService.getByCountry(country);
        return Result.success(templates);
    }

    /**
     * 按使用场景查询产品模板列表
     */
    @Operation(summary = "按使用场景查询产品模板列表", description = "根据使用场景筛选产品模板")
    @GetMapping("/scenario/{usageScenario}")
    public Result<List<ProxyTemplate>> getByUsageScenario(
            @Parameter(description = "使用场景", example = "社交媒体") @PathVariable String usageScenario) {
        List<ProxyTemplate> templates = proxyTemplateService.getByUsageScenario(usageScenario);
        return Result.success(templates);
    }

    /**
     * 根据ID查询模板详情
     */
    @Operation(summary = "查询模板详情", description = "根据模板ID获取详细信息")
    @GetMapping("/{id}")
    public Result<ProxyTemplate> getTemplateById(
            @Parameter(description = "模板ID", example = "1") @PathVariable @NotNull Long id) {
        ProxyTemplate template = proxyTemplateService.getById(id);
        if (template == null) {
            return Result.error("产品模板不存在");
        }
        return Result.success(template);
    }

    /**
     * 创建产品模板
     */
    @Operation(summary = "创建产品模板", description = "新增产品模板信息")
    @PostMapping
    public Result<Boolean> createTemplate(@Valid @RequestBody ProxyTemplate template) {
        // 验证产品名称唯一性
        if (!proxyTemplateService.isProductNameUnique(template.getProductName(), null)) {
            return Result.error("产品名称已存在");
        }
        
        // 验证Inbound标识唯一性
        if (template.getTag() != null && !proxyTemplateService.isTagUnique(template.getTag(), null)) {
            return Result.error("Inbound标识已存在");
        }
        
        // 验证端口可用性
        if (template.getListenPort() != null && !proxyTemplateService.isPortAvailable(template.getListenPort(), null)) {
            return Result.error("监听端口已被占用");
        }

        boolean success = proxyTemplateService.createTemplate(template);
        if (success) {
            return Result.success("产品模板创建成功", true);
        } else {
            return Result.error("产品模板创建失败");
        }
    }

    /**
     * 更新产品模板信息
     */
    @Operation(summary = "更新产品模板信息", description = "修改产品模板信息")
    @PutMapping("/{id}")
    public Result<Boolean> updateTemplate(
            @Parameter(description = "模板ID", example = "1") @PathVariable @NotNull Long id,
            @RequestBody ProxyTemplate template) {
        
        // 验证模板是否存在
        ProxyTemplate existingTemplate = proxyTemplateService.getById(id);
        if (existingTemplate == null) {
            return Result.error("产品模板不存在");
        }

        // 合并更新字段，只更新非空字段
        if (template.getProductName() != null) {
            // 验证产品名称唯一性（排除当前模板）
            if (!proxyTemplateService.isProductNameUnique(template.getProductName(), id)) {
                return Result.error("产品名称已存在");
            }
            existingTemplate.setProductName(template.getProductName());
        }
        
        // sing-box相关字段更新
        if (template.getTag() != null) {
            if (!proxyTemplateService.isTagUnique(template.getTag(), id)) {
                return Result.error("Inbound标识已存在");
            }
            existingTemplate.setTag(template.getTag());
        }
        if (template.getProtocolType() != null) {
            existingTemplate.setProtocolType(template.getProtocolType());
        }
        if (template.getListenAddress() != null) {
            existingTemplate.setListenAddress(template.getListenAddress());
        }
        if (template.getListenPort() != null) {
            if (!proxyTemplateService.isPortAvailable(template.getListenPort(), id)) {
                return Result.error("监听端口已被占用");
            }
            existingTemplate.setListenPort(template.getListenPort());
        }
        if (template.getUsersConfig() != null) {
            existingTemplate.setUsersConfig(template.getUsersConfig());
        }
        if (template.getTransportConfig() != null) {
            existingTemplate.setTransportConfig(template.getTransportConfig());
        }
        if (template.getTlsConfig() != null) {
            existingTemplate.setTlsConfig(template.getTlsConfig());
        }
        if (template.getRealityConfig() != null) {
            existingTemplate.setRealityConfig(template.getRealityConfig());
        }
        if (template.getSniffConfig() != null) {
            existingTemplate.setSniffConfig(template.getSniffConfig());
        }
        if (template.getMuxConfig() != null) {
            existingTemplate.setMuxConfig(template.getMuxConfig());
        }
        if (template.getRouteTag() != null) {
            existingTemplate.setRouteTag(template.getRouteTag());
        }
        if (template.getSingboxConfig() != null) {
            existingTemplate.setSingboxConfig(template.getSingboxConfig());
        }
        if (template.getProtocolConfigs() != null) {
            existingTemplate.setProtocolConfigs(template.getProtocolConfigs());
        }
        
        // 原有字段更新
        if (template.getDefaultPrice() != null) {
            existingTemplate.setDefaultPrice(template.getDefaultPrice());
        }
        if (template.getCountry() != null) {
            existingTemplate.setCountry(template.getCountry());
        }
        if (template.getSpecifiedNodes() != null) {
            existingTemplate.setSpecifiedNodes(template.getSpecifiedNodes());
        }
        if (template.getProxyMode() != null) {
            existingTemplate.setProxyMode(template.getProxyMode());
        }
        if (template.getStock() != null) {
            existingTemplate.setStock(template.getStock());
        }
        if (template.getMainlandDirect() != null) {
            existingTemplate.setMainlandDirect(template.getMainlandDirect());
        }
        if (template.getProtocols() != null) {
            existingTemplate.setProtocols(template.getProtocols());
        }
        if (template.getAllocationMode() != null) {
            existingTemplate.setAllocationMode(template.getAllocationMode());
        }
        if (template.getDomain() != null) {
            existingTemplate.setDomain(template.getDomain());
        }
        if (template.getLoadBalanceAlgorithm() != null) {
            existingTemplate.setLoadBalanceAlgorithm(template.getLoadBalanceAlgorithm());
        }
        if (template.getMaxConnections() != null) {
            existingTemplate.setMaxConnections(template.getMaxConnections());
        }
        if (template.getUsageScenario() != null) {
            existingTemplate.setUsageScenario(template.getUsageScenario());
        }
        if (template.getDescription() != null) {
            existingTemplate.setDescription(template.getDescription());
        }
        if (template.getProductDescription() != null) {
            existingTemplate.setProductDescription(template.getProductDescription());
        }
        if (template.getBandwidthBillingMode() != null) {
            existingTemplate.setBandwidthBillingMode(template.getBandwidthBillingMode());
        }
        if (template.getExtraConfig() != null) {
            existingTemplate.setExtraConfig(template.getExtraConfig());
        }
        if (template.getImageUrl() != null) {
            existingTemplate.setImageUrl(template.getImageUrl());
        }
        if (template.getStatus() != null) {
            existingTemplate.setStatus(template.getStatus());
        }

        boolean success = proxyTemplateService.updateTemplate(existingTemplate);
        if (success) {
            return Result.success("产品模板更新成功", true);
        } else {
            return Result.error("产品模板更新失败");
        }
    }

    /**
     * 删除产品模板
     */
    @Operation(summary = "删除产品模板", description = "根据ID删除产品模板")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteTemplate(
            @Parameter(description = "模板ID", example = "1") @PathVariable @NotNull Long id) {
        
        // 验证模板是否存在
        ProxyTemplate existingTemplate = proxyTemplateService.getById(id);
        if (existingTemplate == null) {
            return Result.error("产品模板不存在");
        }

        boolean success = proxyTemplateService.deleteTemplate(id);
        if (success) {
            return Result.success("产品模板删除成功", true);
        } else {
            return Result.error("产品模板删除失败");
        }
    }

    /**
     * 批量删除产品模板
     */
    @Operation(summary = "批量删除产品模板", description = "根据ID列表批量删除产品模板")
    @DeleteMapping("/batch")
    public Result<Boolean> batchDeleteTemplates(
            @Parameter(description = "模板ID列表") @RequestBody @NotEmpty List<Long> ids) {
        boolean success = proxyTemplateService.batchDeleteTemplates(ids);
        if (success) {
            return Result.success("批量删除成功", true);
        } else {
            return Result.error("批量删除失败");
        }
    }

    /**
     * 批量更新产品模板状态
     */
    @Operation(summary = "批量更新产品模板状态", description = "批量修改产品模板状态（上架/下架）")
    @PutMapping("/batch/status")
    public Result<Boolean> batchUpdateStatus(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Object> idsObj = (List<Object>) request.get("ids");
            String status = (String) request.get("status");
            
            if (idsObj == null || idsObj.isEmpty()) {
                return Result.error("ID列表不能为空");
            }
            
            if (status == null || status.trim().isEmpty()) {
                return Result.error("状态不能为空");
            }
            
            // 转换ID列表
            List<Long> ids = idsObj.stream()
                .map(id -> {
                    if (id instanceof Integer) {
                        return ((Integer) id).longValue();
                    } else if (id instanceof Long) {
                        return (Long) id;
                    } else {
                        return Long.valueOf(id.toString());
                    }
                })
                .toList();
            
            boolean success = proxyTemplateService.batchUpdateStatus(ids, status);
            if (success) {
                return Result.success("批量更新状态成功", true);
            } else {
                return Result.error("批量更新状态失败");
            }
        } catch (Exception e) {
            return Result.error("请求参数格式错误: " + e.getMessage());
        }
    }

    /**
     * 复制产品模板
     */
    @Operation(summary = "复制产品模板", description = "复制指定产品模板")
    @PostMapping("/{id}/copy")
    public Result<Boolean> copyTemplate(
            @Parameter(description = "模板ID", example = "1") @PathVariable @NotNull Long id,
            @Parameter(description = "新产品名称") @RequestParam String newProductName) {
        
        boolean success = proxyTemplateService.copyTemplate(id, newProductName);
        if (success) {
            return Result.success("产品模板复制成功", true);
        } else {
            return Result.error("产品模板复制失败");
        }
    }

    /**
     * 获取产品模板统计信息
     */
    @Operation(summary = "获取产品模板统计信息", description = "获取产品模板相关的统计数据")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getTemplateStatistics() {
        Map<String, Object> statistics = proxyTemplateService.getTemplateStatistics();
        return Result.success(statistics);
    }
    
    /**
     * 生成sing-box配置
     */
    @Operation(summary = "生成sing-box配置", description = "根据模板ID生成sing-box Inbound配置")
    @GetMapping("/{id}/singbox-config")
    public Result<Map<String, Object>> generateSingboxConfig(
            @Parameter(description = "模板ID", example = "1") @PathVariable @NotNull Long id) {
        
        ProxyTemplate template = proxyTemplateService.getById(id);
        if (template == null) {
            return Result.error("产品模板不存在");
        }
        
        Map<String, Object> singboxConfig = proxyTemplateService.buildSingboxConfig(template);
        return Result.success(singboxConfig);
    }
    
    /**
     * 验证配置有效性
     */
    @Operation(summary = "验证配置有效性", description = "验证sing-box配置的有效性")
    @PostMapping("/validate-config")
    public Result<Map<String, Object>> validateConfig(@RequestBody Map<String, Object> config) {
        Map<String, Object> result = proxyTemplateService.validateSingboxConfig(config);
        return Result.success(result);
    }
    
    /**
     * 按协议类型查询模板
     */
    @Operation(summary = "按协议类型查询模板", description = "根据协议类型筛选产品模板")
    @GetMapping("/protocol/{protocolType}")
    public Result<List<ProxyTemplate>> getByProtocolType(
            @Parameter(description = "协议类型", example = "vless") @PathVariable String protocolType) {
        List<ProxyTemplate> templates = proxyTemplateService.getByProtocolType(protocolType);
        return Result.success(templates);
    }

    /**
     * 上传模板图片
     */
    @Operation(summary = "上传模板图片", description = "上传代理模板的展示图片")
    @PostMapping("/upload-image")
    public Result<String> uploadTemplateImage(
            @Parameter(description = "图片文件") @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return Result.error("请选择要上传的图片");
            }

            // 检查文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return Result.error("仅支持图片文件");
            }

            // 检查文件大小 (5MB)
            if (file.getSize() > 5 * 1024 * 1024) {
                return Result.error("图片文件大小不能超过5MB");
            }

            String imageUrl = proxyTemplateService.uploadTemplateImage(file);
            if (imageUrl != null) {
                return Result.success("图片上传成功", imageUrl);
            } else {
                return Result.error("图片上传失败");
            }
        } catch (Exception e) {
            return Result.error("图片上传失败: " + e.getMessage());
        }
    }
}