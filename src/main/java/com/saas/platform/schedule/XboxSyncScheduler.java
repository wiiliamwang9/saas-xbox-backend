package com.saas.platform.schedule;

import com.saas.platform.dto.Result;
import com.saas.platform.service.XboxSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Xbox系统同步定时任务
 * 定时从Xbox Controller同步节点信息到SaaS后台数据库
 *
 * @author SaaS Xbox Team
 */
@Component
@ConditionalOnProperty(name = "xbox.sync.enabled", havingValue = "true", matchIfMissing = true)
public class XboxSyncScheduler {

    private static final Logger log = LoggerFactory.getLogger(XboxSyncScheduler.class);

    @Autowired
    private XboxSyncService xboxSyncService;

    /**
     * 同步所有节点信息 - 每5分钟执行一次
     */
    @Scheduled(fixedRate = 300000) // 5分钟 = 300000毫秒
    public void syncAllNodes() {
        log.info("定时任务开始：同步Xbox节点信息");
        
        try {
            Result<String> result = xboxSyncService.syncAllNodes();
            
            if (result.isSuccess()) {
                log.info("定时同步节点信息成功: {}", result.getData());
            } else {
                log.error("定时同步节点信息失败: {}", result.getMessage());
            }
            
        } catch (Exception e) {
            log.error("定时同步节点信息异常", e);
        }
    }

    /**
     * 同步节点状态信息 - 每2分钟执行一次
     */
    @Scheduled(fixedRate = 120000) // 2分钟 = 120000毫秒
    public void syncNodeStatus() {
        log.debug("定时任务开始：同步节点状态信息");
        
        try {
            Result<String> result = xboxSyncService.syncNodeStatus();
            
            if (result.isSuccess()) {
                log.debug("定时同步节点状态成功: {}", result.getData());
            } else {
                log.warn("定时同步节点状态失败: {}", result.getMessage());
            }
            
        } catch (Exception e) {
            log.error("定时同步节点状态异常", e);
        }
    }

    /**
     * 同步协议支持信息 - 每30分钟执行一次
     */
    @Scheduled(fixedRate = 1800000) // 30分钟 = 1800000毫秒
    public void syncProtocolInfo() {
        log.info("定时任务开始：同步协议支持信息");
        
        try {
            Result<String> result = xboxSyncService.syncProtocolInfo();
            
            if (result.isSuccess()) {
                log.info("定时同步协议信息成功: {}", result.getData());
            } else {
                log.warn("定时同步协议信息失败: {}", result.getMessage());
            }
            
        } catch (Exception e) {
            log.error("定时同步协议信息异常", e);
        }
    }

    /**
     * 检查Xbox Controller连接状态 - 每1分钟执行一次
     */
    @Scheduled(fixedRate = 60000) // 1分钟 = 60000毫秒
    public void checkXboxConnection() {
        log.debug("定时任务开始：检查Xbox Controller连接");
        
        try {
            Result<Boolean> result = xboxSyncService.checkXboxConnection();
            
            if (result.isSuccess()) {
                if (result.getData()) {
                    log.debug("Xbox Controller连接正常");
                } else {
                    log.warn("Xbox Controller连接异常");
                }
            } else {
                log.error("检查Xbox Controller连接失败: {}", result.getMessage());
            }
            
        } catch (Exception e) {
            log.error("检查Xbox Controller连接异常", e);
        }
    }

    /**
     * 应用启动后延迟30秒执行首次同步
     */
    @Scheduled(initialDelay = 30000, fixedRate = Long.MAX_VALUE)
    public void initialSync() {
        log.info("应用启动后首次同步Xbox节点信息");
        
        try {
            // 检查连接
            Result<Boolean> connectionResult = xboxSyncService.checkXboxConnection();
            if (!connectionResult.isSuccess() || !connectionResult.getData()) {
                log.warn("Xbox Controller暂不可用，跳过首次同步");
                return;
            }
            
            // 执行首次完整同步
            Result<String> syncResult = xboxSyncService.syncAllNodes();
            if (syncResult.isSuccess()) {
                log.info("首次同步完成: {}", syncResult.getData());
            } else {
                log.error("首次同步失败: {}", syncResult.getMessage());
            }
            
        } catch (Exception e) {
            log.error("首次同步异常", e);
        }
    }
}