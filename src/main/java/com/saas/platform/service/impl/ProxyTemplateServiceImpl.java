package com.saas.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saas.platform.entity.ProxyTemplate;
import com.saas.platform.exception.BusinessException;
import com.saas.platform.mapper.ProxyTemplateMapper;
import com.saas.platform.service.ProxyTemplateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代理产品模板服务实现类 - 支持sing-box配置
 * 基于3xui面板的Inbound设计，支持完整的sing-box协议配置
 * 
 * @author SaaS Xbox Team
 * @since 2024-08-04
 */
@Service  
public class ProxyTemplateServiceImpl extends ServiceImpl<ProxyTemplateMapper, ProxyTemplate> implements ProxyTemplateService {

    @Value("${file.upload.path:/tmp/uploads/}")
    private String uploadPath;

    @Value("${file.upload.url-prefix:http://localhost:8080/files/}")
    private String urlPrefix;

    @Override
    public IPage<ProxyTemplate> getTemplatePage(Long current, Long size, String productName, 
                                              String usageScenario, String country, String status) {
        Page<ProxyTemplate> page = new Page<>(current, size);
        return baseMapper.selectTemplatePage(page, productName, usageScenario, country, status);
    }

    @Override
    public List<ProxyTemplate> getByCountry(String country) {
        if (!StringUtils.hasText(country)) {
            return null;
        }
        return baseMapper.selectByCountry(country);
    }

    @Override
    public List<ProxyTemplate> getByUsageScenario(String usageScenario) {
        if (!StringUtils.hasText(usageScenario)) {
            return null;
        }
        return baseMapper.selectByUsageScenario(usageScenario);
    }

    @Override
    public List<ProxyTemplate> getEnabledTemplates() {
        return baseMapper.selectEnabledTemplates();
    }

    @Override
    @Transactional
    public boolean createTemplate(ProxyTemplate template) {
        // 验证产品名称唯一性
        if (!isProductNameUnique(template.getProductName(), null)) {
            throw new BusinessException("产品名称已存在");
        }
        
        // 设置默认值
        if (!StringUtils.hasText(template.getStatus())) {
            template.setStatus("下架");
        }
        
        if (template.getSpecifiedNodes() == null) {
            template.setSpecifiedNodes(0);
        }
        
        if (template.getStock() == null) {
            template.setStock(-1);
        }
        
        if (template.getMaxConnections() == null) {
            template.setMaxConnections(-1);
        }
        
        if (template.getMainlandDirect() == null) {
            template.setMainlandDirect(false);
        }
        
        if (!StringUtils.hasText(template.getAllocationMode())) {
            template.setAllocationMode("随机");
        }
        
        return save(template);
    }

    @Override
    @Transactional
    public boolean updateTemplate(ProxyTemplate template) {
        // 验证模板是否存在
        ProxyTemplate existTemplate = getById(template.getId());
        if (existTemplate == null) {
            throw new BusinessException("产品模板不存在");
        }
        
        // 验证产品名称唯一性
        if (!isProductNameUnique(template.getProductName(), template.getId())) {
            throw new BusinessException("产品名称已存在");
        }
        
        return updateById(template);
    }

    @Override
    @Transactional
    public boolean deleteTemplate(Long id) {
        ProxyTemplate template = getById(id);
        if (template == null) {
            throw new BusinessException("产品模板不存在");
        }
        
        // TODO: 检查是否有关联的配置在使用此模板
        
        return removeById(id);
    }

    @Override
    @Transactional
    public boolean batchDeleteTemplates(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        
        // TODO: 批量检查关联数据
        
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
    public boolean isProductNameUnique(String productName, Long excludeId) {
        if (!StringUtils.hasText(productName)) {
            return false;
        }
        
        LambdaQueryWrapper<ProxyTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProxyTemplate::getProductName, productName);
        
        if (excludeId != null) {
            wrapper.ne(ProxyTemplate::getId, excludeId);
        }
        
        return count(wrapper) == 0;
    }

    @Override
    public Map<String, Object> getTemplateStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        // 总产品模板数
        long totalCount = count();
        result.put("totalCount", totalCount);
        
        // 各国家统计
        List<Map<String, Object>> countryCount = baseMapper.countByCountry();
        result.put("countryCount", countryCount);
        
        // 各使用场景统计
        List<Map<String, Object>> scenarioCount = baseMapper.countByUsageScenario();
        result.put("scenarioCount", scenarioCount);
        
        // 各代理模式统计
        List<Map<String, Object>> proxyModeCount = baseMapper.countByProxyMode();
        result.put("proxyModeCount", proxyModeCount);
        
        // 各状态统计
        List<Map<String, Object>> statusCount = baseMapper.countByStatus();
        result.put("statusCount", statusCount);
        
        // 上架产品数
        long onlineCount = count(new LambdaQueryWrapper<ProxyTemplate>()
                .eq(ProxyTemplate::getStatus, "上架"));
        result.put("onlineCount", onlineCount);
        
        // 下架产品数
        long offlineCount = count(new LambdaQueryWrapper<ProxyTemplate>()
                .eq(ProxyTemplate::getStatus, "下架"));
        result.put("offlineCount", offlineCount);
        
        // 总库存统计（排除无限库存 -1）
        Long totalStock = baseMapper.sumStock();
        result.put("totalStock", totalStock != null ? totalStock : 0);
        
        // 平均价格
        java.math.BigDecimal avgPrice = baseMapper.avgPrice();
        result.put("avgPrice", avgPrice != null ? avgPrice.doubleValue() : 0.0);
        
        return result;
    }

    @Override
    @Transactional
    public boolean copyTemplate(Long id, String newProductName) {
        ProxyTemplate sourceTemplate = getById(id);
        if (sourceTemplate == null) {
            throw new BusinessException("源产品模板不存在");
        }
        
        // 验证新产品名称唯一性
        if (!isProductNameUnique(newProductName, null)) {
            throw new BusinessException("新产品名称已存在");
        }
        
        // 创建副本
        ProxyTemplate newTemplate = new ProxyTemplate();
        newTemplate.setProductName(newProductName);
        newTemplate.setDefaultPrice(sourceTemplate.getDefaultPrice());
        newTemplate.setCountry(sourceTemplate.getCountry());
        newTemplate.setSpecifiedNodes(sourceTemplate.getSpecifiedNodes());
        newTemplate.setProxyMode(sourceTemplate.getProxyMode());
        newTemplate.setStock(sourceTemplate.getStock());
        newTemplate.setMainlandDirect(sourceTemplate.getMainlandDirect());
        newTemplate.setProtocols(sourceTemplate.getProtocols());
        newTemplate.setAllocationMode(sourceTemplate.getAllocationMode());
        newTemplate.setDomain(sourceTemplate.getDomain());
        newTemplate.setLoadBalanceAlgorithm(sourceTemplate.getLoadBalanceAlgorithm());
        newTemplate.setMaxConnections(sourceTemplate.getMaxConnections());
        newTemplate.setUsageScenario(sourceTemplate.getUsageScenario());
        newTemplate.setExtraConfig(sourceTemplate.getExtraConfig());
        newTemplate.setDescription("复制自: " + sourceTemplate.getProductName());
        newTemplate.setStatus("下架"); // 新复制的产品默认为下架状态
        
        // 复制sing-box相关配置
        if (StringUtils.hasText(sourceTemplate.getTag())) {
            newTemplate.setTag(sourceTemplate.getTag() + "-copy");
        }
        newTemplate.setProtocolType(sourceTemplate.getProtocolType());
        newTemplate.setListenAddress(sourceTemplate.getListenAddress());
        if (sourceTemplate.getListenPort() != null) {
            newTemplate.setListenPort(sourceTemplate.getListenPort() + 1); // 端口+1避免冲突
        }
        newTemplate.setUsersConfig(sourceTemplate.getUsersConfig());
        newTemplate.setTransportConfig(sourceTemplate.getTransportConfig());
        newTemplate.setTlsConfig(sourceTemplate.getTlsConfig());
        newTemplate.setRealityConfig(sourceTemplate.getRealityConfig());
        newTemplate.setSniffConfig(sourceTemplate.getSniffConfig());
        newTemplate.setMuxConfig(sourceTemplate.getMuxConfig());
        newTemplate.setRouteTag(sourceTemplate.getRouteTag());
        newTemplate.setSingboxConfig(sourceTemplate.getSingboxConfig());
        
        return save(newTemplate);
    }
    
    @Override
    public boolean isTagUnique(String tag, Long excludeId) {
        if (!StringUtils.hasText(tag)) {
            return true;
        }
        
        LambdaQueryWrapper<ProxyTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProxyTemplate::getTag, tag);
        
        if (excludeId != null) {
            wrapper.ne(ProxyTemplate::getId, excludeId);
        }
        
        return count(wrapper) == 0;
    }
    
    @Override
    public boolean isPortAvailable(Integer port, Long excludeId) {
        if (port == null) {
            return true;
        }
        
        LambdaQueryWrapper<ProxyTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProxyTemplate::getListenPort, port);
        
        if (excludeId != null) {
            wrapper.ne(ProxyTemplate::getId, excludeId);
        }
        
        return count(wrapper) == 0;
    }
    
    @Override
    public List<ProxyTemplate> getByProtocolType(String protocolType) {
        if (!StringUtils.hasText(protocolType)) {
            return null;
        }
        
        LambdaQueryWrapper<ProxyTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProxyTemplate::getProtocolType, protocolType)
               .eq(ProxyTemplate::getStatus, "上架");
        
        return list(wrapper);
    }
    
    @Override
    public Map<String, Object> buildSingboxConfig(ProxyTemplate template) {
        Map<String, Object> config = new HashMap<>();
        
        // 基本配置
        config.put("tag", template.getTag());
        config.put("type", template.getProtocolType());
        config.put("listen", template.getListenAddress() != null ? template.getListenAddress() : "::");
        config.put("listen_port", template.getListenPort());
        
        // 用户配置
        if (template.getUsersConfig() != null) {
            config.put("users", template.getUsersConfig().get("users"));
        }
        
        // 传输层配置
        if (template.getTransportConfig() != null) {
            config.put("transport", template.getTransportConfig());
        }
        
        // TLS配置
        if (template.getTlsConfig() != null) {
            Map<String, Object> tlsConfig = new HashMap<>(template.getTlsConfig());
            
            // 如果启用了Reality，添加Reality配置
            if (template.getRealityConfig() != null && 
                Boolean.TRUE.equals(template.getRealityConfig().get("enabled"))) {
                tlsConfig.put("reality", template.getRealityConfig());
            }
            
            config.put("tls", tlsConfig);
        }
        
        // 嗅探配置
        if (template.getSniffConfig() != null) {
            config.put("sniff", template.getSniffConfig());
        }
        
        // 多路复用配置
        if (template.getMuxConfig() != null) {
            config.put("mux", template.getMuxConfig());
        }
        
        // 路由标签
        if (StringUtils.hasText(template.getRouteTag())) {
            config.put("route_tag", template.getRouteTag());
        }
        
        return config;
    }
    
    @Override
    public Map<String, Object> validateSingboxConfig(Map<String, Object> config) {
        Map<String, Object> result = new HashMap<>();
        boolean isValid = true;
        java.util.List<String> errors = new java.util.ArrayList<>();
        
        // 验证必填字段
        if (!config.containsKey("tag") || !StringUtils.hasText((String) config.get("tag"))) {
            errors.add("tag字段不能为空");
            isValid = false;
        }
        
        if (!config.containsKey("type") || !StringUtils.hasText((String) config.get("type"))) {
            errors.add("type字段不能为空");
            isValid = false;
        }
        
        if (!config.containsKey("listen_port")) {
            errors.add("listen_port字段不能为空");
            isValid = false;
        } else {
            Object portObj = config.get("listen_port");
            if (portObj instanceof Integer) {
                int port = (Integer) portObj;
                if (port < 1 || port > 65535) {
                    errors.add("端口号必须在1-65535之间");
                    isValid = false;
                }
            } else {
                errors.add("端口号必须为整数");
                isValid = false;
            }
        }
        
        // 验证协议类型
        String type = (String) config.get("type");
        if (StringUtils.hasText(type)) {
            java.util.Set<String> validTypes = java.util.Set.of(
                "vless", "vmess", "trojan", "shadowsocks", "shadowsocks-2022", 
                "hysteria", "hysteria2", "tuic", "naive"
            );
            if (!validTypes.contains(type)) {
                errors.add("不支持的协议类型: " + type);
                isValid = false;
            }
        }
        
        result.put("valid", isValid);
        result.put("errors", errors);
        
        return result;
    }

    @Override
    public String uploadTemplateImage(MultipartFile file) {
        try {
            // 创建上传目录
            String dateDir = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String fullUploadPath = uploadPath + "template-images/" + dateDir;
            File uploadDir = new File(fullUploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 获取文件扩展名
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            // 生成唯一文件名
            String fileName = UUID.randomUUID().toString() + extension;
            String relativePath = "template-images/" + dateDir + "/" + fileName;
            Path filePath = Paths.get(fullUploadPath, fileName);

            // 保存文件
            Files.write(filePath, file.getBytes());

            // 返回访问URL
            return urlPrefix + relativePath;
        } catch (IOException e) {
            throw new BusinessException("文件上传失败: " + e.getMessage());
        }
    }
}