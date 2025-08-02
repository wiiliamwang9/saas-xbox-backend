-- 更新节点表结构以支持完整的Node Entity类
-- 创建时间: 2024-08-01
-- 版本: 1.1.0

USE `saas_xbox`;

-- 备份原表数据（如果存在）
DROP TABLE IF EXISTS `nodes_backup`;
CREATE TABLE `nodes_backup` AS SELECT * FROM `nodes` WHERE 1=1;

-- 删除原表并重新创建
DROP TABLE IF EXISTS `nodes`;
CREATE TABLE `nodes` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '节点ID',
  `node_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '节点名称',
  `node_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '节点编码',
  `server_ip` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '服务器IP',
  `country` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '国家',
  `region` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地区',
  `city` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '城市',
  `ssh_port` int NOT NULL DEFAULT 22 COMMENT 'SSH端口号',
  `password` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `domain` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '域名',
  `node_type` enum('完整节点','转发节点','解密节点') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '节点类型',
  `combination_type` enum('独立节点','组合节点','落地节点') COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '组合方式',
  `remark` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `agent_status` enum('已部署','未部署','部署中') COLLATE utf8mb4_unicode_ci DEFAULT '未部署' COMMENT 'Agent部署状态',
  `node_status` enum('运行中','维护中','故障','停用') COLLATE utf8mb4_unicode_ci DEFAULT '运行中' COMMENT '节点状态',
  `max_connections` int DEFAULT 1000 COMMENT '最大连接数',
  `current_connections` int DEFAULT 0 COMMENT '当前连接数',
  `bandwidth_mbps` int DEFAULT NULL COMMENT '带宽(Mbps)',
  `cpu_usage` decimal(5,2) DEFAULT NULL COMMENT 'CPU使用率',
  `memory_usage` decimal(5,2) DEFAULT NULL COMMENT '内存使用率',
  `disk_usage` decimal(5,2) DEFAULT NULL COMMENT '磁盘使用率',
  `network_latency` int DEFAULT NULL COMMENT '网络延迟(ms)',
  `last_check_time` timestamp NULL DEFAULT NULL COMMENT '最后检查时间',
  `provider` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '服务商',
  `monthly_cost` decimal(10,2) DEFAULT NULL COMMENT '月费用',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_node_code` (`node_code`),
  UNIQUE KEY `uk_server_ip` (`server_ip`),
  KEY `idx_country` (`country`),
  KEY `idx_city` (`city`),
  KEY `idx_node_status` (`node_status`),
  KEY `idx_node_type` (`node_type`),
  KEY `idx_agent_status` (`agent_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='节点表';

-- 插入测试数据
INSERT INTO `nodes` (
  `node_name`, `node_code`, `server_ip`, `country`, `region`, `city`, 
  `ssh_port`, `password`, `domain`, `node_type`, `combination_type`, 
  `remark`, `agent_status`, `node_status`, `max_connections`, 
  `current_connections`, `bandwidth_mbps`, `cpu_usage`, `memory_usage`, 
  `disk_usage`, `network_latency`, `provider`, `monthly_cost`
) VALUES
('洛杉矶节点1', 'LA-NODE-01', '45.32.128.10', '美国', '加利福尼亚', '洛杉矶', 
 22, 'Test123456', 'node1.example.com', '完整节点', '独立节点', 
 '高性能节点', '已部署', '运行中', 2000, 
 1250, 1000, 45.20, 62.50, 
 35.80, 15, 'Vultr', 299.00),

('纽约节点1', 'NY-NODE-01', '104.207.142.35', '美国', '纽约州', '纽约', 
 22, 'Secure123', 'node2.example.com', '完整节点', '独立节点', 
 '东海岸节点', '已部署', '运行中', 1500, 
 800, 500, 32.10, 45.30, 
 28.90, 12, 'DigitalOcean', 199.00),

('新加坡节点1', 'SG-NODE-01', '139.180.202.88', '新加坡', NULL, '新加坡', 
 22, 'Password123', 'node3.example.com', '转发节点', '组合节点', 
 '亚洲中转节点', '未部署', '停用', 1000, 
 0, 200, 0.00, 0.00, 
 0.00, NULL, 'Linode', 150.00),

('东京节点1', 'TK-NODE-01', '172.104.88.23', '日本', '关东', '东京', 
 22, 'Tokyo2024', 'node4.example.com', '解密节点', '落地节点', 
 '日本解密节点', '部署中', '维护中', 800, 
 0, 100, 15.80, 25.40, 
 45.60, 8, 'Vultr', 180.00);