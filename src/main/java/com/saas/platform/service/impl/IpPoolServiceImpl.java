package com.saas.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saas.platform.entity.IpPool;
import com.saas.platform.exception.BusinessException;
import com.saas.platform.mapper.IpPoolMapper;
import com.saas.platform.service.IpPoolService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * IP池服务实现类
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Service
public class IpPoolServiceImpl extends ServiceImpl<IpPoolMapper, IpPool> implements IpPoolService {

    @Override
    public IPage<IpPool> getIpPoolPage(Long current, Long size, String ipAddress, String country,
                                     String city, String ipQuality, String ipStatus, Long nodeId) {
        Page<IpPool> page = new Page<>(current, size);
        return baseMapper.selectIpPoolPage(page, ipAddress, country, city, ipQuality, ipStatus, nodeId);
    }

    @Override
    public IpPool getByIpAddress(String ipAddress) {
        if (!StringUtils.hasText(ipAddress)) {
            return null;
        }
        return baseMapper.selectByIpAddress(ipAddress);
    }

    @Override
    public List<IpPool> getAvailableIps(String country, String ipQuality, Integer limit) {
        return baseMapper.selectAvailableIps(country, ipQuality, limit);
    }

    @Override
    public List<IpPool> getByNodeId(Long nodeId) {
        if (nodeId == null) {
            return null;
        }
        return baseMapper.selectByNodeId(nodeId);
    }

    @Override
    public List<IpPool> getByOrderId(Long orderId) {
        if (orderId == null) {
            return null;
        }
        return baseMapper.selectByOrderId(orderId);
    }

    @Override
    @Transactional
    public boolean createIpPool(IpPool ipPool) {
        // 验证IP地址唯一性
        if (!isIpAddressUnique(ipPool.getIpAddress(), null)) {
            throw new BusinessException("IP地址已存在");
        }
        
        // 设置默认值
        if (!StringUtils.hasText(ipPool.getIpStatus())) {
            ipPool.setIpStatus("可用");
        }
        if (!StringUtils.hasText(ipPool.getIpQuality())) {
            ipPool.setIpQuality("普通");
        }
        
        return save(ipPool);
    }

    @Override
    @Transactional
    public boolean updateIpPool(IpPool ipPool) {
        // 验证IP是否存在
        IpPool existIpPool = getById(ipPool.getId());
        if (existIpPool == null) {
            throw new BusinessException("IP不存在");
        }
        
        // 验证IP地址唯一性
        if (!isIpAddressUnique(ipPool.getIpAddress(), ipPool.getId())) {
            throw new BusinessException("IP地址已存在");
        }
        
        return updateById(ipPool);
    }

    @Override
    @Transactional
    public boolean deleteIpPool(Long id) {
        IpPool ipPool = getById(id);
        if (ipPool == null) {
            throw new BusinessException("IP不存在");
        }
        
        // 检查IP是否被订单占用
        if ("占用".equals(ipPool.getIpStatus())) {
            throw new BusinessException("IP正在被使用，无法删除");
        }
        
        return removeById(id);
    }

    @Override
    @Transactional
    public boolean batchDeleteIpPools(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        
        // TODO: 批量检查IP是否被占用
        
        return removeByIds(ids);
    }

    @Override
    @Transactional
    public int batchImportIps(List<IpPool> ipPools) {
        if (ipPools == null || ipPools.isEmpty()) {
            return 0;
        }
        
        int successCount = 0;
        for (IpPool ipPool : ipPools) {
            try {
                if (isIpAddressUnique(ipPool.getIpAddress(), null)) {
                    // 设置默认值
                    if (!StringUtils.hasText(ipPool.getIpStatus())) {
                        ipPool.setIpStatus("可用");
                    }
                    if (!StringUtils.hasText(ipPool.getIpQuality())) {
                        ipPool.setIpQuality("普通");
                    }
                    
                    if (save(ipPool)) {
                        successCount++;
                    }
                }
            } catch (Exception e) {
                // 记录导入失败的IP，继续处理下一个
                System.err.println("导入IP失败: " + ipPool.getIpAddress() + ", 错误: " + e.getMessage());
            }
        }
        
        return successCount;
    }

    @Override
    @Transactional
    public boolean batchAssignToOrder(List<Long> ipIds, Long orderId) {
        if (ipIds == null || ipIds.isEmpty() || orderId == null) {
            return false;
        }
        
        return baseMapper.batchAssignToOrder(ipIds, orderId) > 0;
    }

    @Override
    @Transactional
    public boolean batchReleaseIps(List<Long> ipIds) {
        if (ipIds == null || ipIds.isEmpty()) {
            return false;
        }
        
        return baseMapper.batchReleaseIps(ipIds) > 0;
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
    public Map<String, Object> testIpConnectivity(String ipAddress) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            long startTime = System.currentTimeMillis();
            InetAddress address = InetAddress.getByName(ipAddress);
            boolean reachable = address.isReachable(5000); // 5秒超时
            long latency = System.currentTimeMillis() - startTime;
            
            result.put("ipAddress", ipAddress);
            result.put("reachable", reachable);
            result.put("latency", latency);
            result.put("testTime", LocalDateTime.now());
            result.put("testResult", reachable ? "成功" : "失败");
            result.put("testMessage", reachable ? "连接正常" : "连接超时");
            
            // 更新测试结果到数据库
            baseMapper.updateTestResult(ipAddress, result.get("testResult").toString(), 
                                      (int) latency, result.get("testMessage").toString());
            
        } catch (IOException e) {
            result.put("ipAddress", ipAddress);
            result.put("reachable", false);
            result.put("latency", -1);
            result.put("testTime", LocalDateTime.now());
            result.put("testResult", "失败");
            result.put("testMessage", "测试异常: " + e.getMessage());
            
            // 更新测试结果到数据库
            baseMapper.updateTestResult(ipAddress, "失败", -1, "测试异常: " + e.getMessage());
        }
        
        return result;
    }

    @Override
    public Map<String, Object> batchTestIps(List<Long> ipIds) {
        Map<String, Object> result = new HashMap<>();
        
        if (ipIds == null || ipIds.isEmpty()) {
            result.put("totalCount", 0);
            result.put("successCount", 0);
            result.put("failCount", 0);
            return result;
        }
        
        List<IpPool> ipPools = listByIds(ipIds);
        int totalCount = ipPools.size();
        int successCount = 0;
        int failCount = 0;
        
        for (IpPool ipPool : ipPools) {
            Map<String, Object> testResult = testIpConnectivity(ipPool.getIpAddress());
            if ((Boolean) testResult.get("reachable")) {
                successCount++;
            } else {
                failCount++;
            }
        }
        
        result.put("totalCount", totalCount);
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("successRate", totalCount > 0 ? (double) successCount / totalCount * 100 : 0);
        
        return result;
    }

    @Override
    @Transactional
    public List<IpPool> autoAssignIps(String country, String ipQuality, Integer count, Long orderId) {
        List<IpPool> availableIps = getAvailableIps(country, ipQuality, count);
        
        if (availableIps.size() < count) {
            throw new BusinessException("可用IP数量不足，需要: " + count + ", 可用: " + availableIps.size());
        }
        
        List<Long> ipIds = availableIps.stream().map(IpPool::getId).collect(Collectors.toList());
        
        if (batchAssignToOrder(ipIds, orderId)) {
            return availableIps;
        } else {
            throw new BusinessException("IP分配失败");
        }
    }

    @Override
    @Transactional
    public IpPool replaceIp(Long oldIpId, Long orderId, String reason) {
        IpPool oldIp = getById(oldIpId);
        if (oldIp == null) {
            throw new BusinessException("原IP不存在");
        }
        
        // 查找相同质量的可用IP
        List<IpPool> availableIps = getAvailableIps(oldIp.getCountry(), oldIp.getIpQuality(), 1);
        if (availableIps.isEmpty()) {
            throw new BusinessException("没有可用的替换IP");
        }
        
        IpPool newIp = availableIps.get(0);
        
        // 释放旧IP
        batchReleaseIps(List.of(oldIpId));
        
        // 分配新IP
        batchAssignToOrder(List.of(newIp.getId()), orderId);
        
        return newIp;
    }

    @Override
    public List<IpPool> getFailedTestIps() {
        return baseMapper.selectFailedTestIps();
    }

    @Override
    public List<IpPool> getUntestedIps(Integer hours) {
        if (hours == null || hours <= 0) {
            hours = 24; // 默认24小时
        }
        return baseMapper.selectUntestedIps(hours);
    }

    @Override
    public Map<String, Object> scheduleIpCheck() {
        Map<String, Object> result = new HashMap<>();
        
        // 获取需要测试的IP（24小时未测试的）
        List<IpPool> untestedIps = getUntestedIps(24);
        
        if (untestedIps.isEmpty()) {
            result.put("message", "没有需要检查的IP");
            result.put("checkedCount", 0);
            return result;
        }
        
        List<Long> ipIds = untestedIps.stream().map(IpPool::getId).collect(Collectors.toList());
        Map<String, Object> testResult = batchTestIps(ipIds);
        
        result.put("message", "IP检查完成");
        result.put("checkedCount", testResult.get("totalCount"));
        result.put("successCount", testResult.get("successCount"));
        result.put("failCount", testResult.get("failCount"));
        result.put("successRate", testResult.get("successRate"));
        
        return result;
    }

    @Override
    public Map<String, Object> getIpPoolStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        // 总IP数
        long totalCount = count();
        result.put("totalCount", totalCount);
        
        // 各状态IP数量
        List<Map<String, Object>> statusCount = baseMapper.countByStatus();
        result.put("statusCount", statusCount);
        
        // 按国家统计IP数量
        List<Map<String, Object>> countryCount = baseMapper.countByCountry();
        result.put("countryCount", countryCount);
        
        // 按IP质量统计数量
        List<Map<String, Object>> qualityCount = baseMapper.countByQuality();
        result.put("qualityCount", qualityCount);
        
        // 可用IP数
        long availableCount = count(new LambdaQueryWrapper<IpPool>()
                .eq(IpPool::getIpStatus, "可用"));
        result.put("availableCount", availableCount);
        
        // 占用IP数
        long usedCount = count(new LambdaQueryWrapper<IpPool>()
                .eq(IpPool::getIpStatus, "占用"));
        result.put("usedCount", usedCount);
        
        return result;
    }

    @Override
    public boolean isIpAddressUnique(String ipAddress, Long excludeId) {
        if (!StringUtils.hasText(ipAddress)) {
            return false;
        }
        
        LambdaQueryWrapper<IpPool> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IpPool::getIpAddress, ipAddress);
        
        if (excludeId != null) {
            wrapper.ne(IpPool::getId, excludeId);
        }
        
        return count(wrapper) == 0;
    }

    @Override
    public List<IpPool> filterAvailableIps(String country, String city, String ipQuality, 
                                          Integer minBandwidth, Integer count) {
        LambdaQueryWrapper<IpPool> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IpPool::getIpStatus, "可用");
        
        if (StringUtils.hasText(country)) {
            wrapper.eq(IpPool::getCountry, country);
        }
        if (StringUtils.hasText(city)) {
            wrapper.eq(IpPool::getCity, city);
        }
        if (StringUtils.hasText(ipQuality)) {
            wrapper.eq(IpPool::getIpQuality, ipQuality);
        }
        if (minBandwidth != null && minBandwidth > 0) {
            wrapper.ge(IpPool::getBandwidthMbps, minBandwidth);
        }
        
        wrapper.orderByDesc(IpPool::getCreatedAt);
        
        if (count != null && count > 0) {
            wrapper.last("LIMIT " + count);
        }
        
        return list(wrapper);
    }
}