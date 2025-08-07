-- 创建 proxy_templates 表
-- 代理产品模板管理表

USE `saas_xbox`;

DROP TABLE IF EXISTS `proxy_templates`;
CREATE TABLE `proxy_templates` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '模板ID',
  `product_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '产品名称',
  `default_price` decimal(10,2) NOT NULL COMMENT '默认价格(美元)',
  `country` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '国家',
  `specified_nodes` int DEFAULT '0' COMMENT '指定节点数量',
  `proxy_mode` enum('直连代理','中转代理') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '代理模式',
  `stock` int DEFAULT '-1' COMMENT '库存数量(-1表示不限)',
  `status` enum('上架','下架') COLLATE utf8mb4_unicode_ci DEFAULT '下架' COMMENT '产品状态',
  `mainland_direct` tinyint(1) DEFAULT '0' COMMENT '大陆直连',
  `protocols` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '可选协议(多选,逗号分隔)',
  `allocation_mode` enum('顺序','随机','权重') COLLATE utf8mb4_unicode_ci DEFAULT '随机' COMMENT '分配模式',
  `domain` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '绑定域名',
  `load_balance_algorithm` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '负载均衡算法',
  `max_connections` int DEFAULT '-1' COMMENT '最大连接数',
  `usage_scenario` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '使用场景',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '产品描述',
  `extra_config` json DEFAULT NULL COMMENT '扩展配置',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_product_name` (`product_name`),
  KEY `idx_country` (`country`),
  KEY `idx_status` (`status`),
  KEY `idx_proxy_mode` (`proxy_mode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='代理产品模板表';

-- 插入一些示例数据
INSERT INTO `proxy_templates` (`product_name`, `default_price`, `country`, `specified_nodes`, `proxy_mode`, `stock`, `status`, `mainland_direct`, `protocols`, `allocation_mode`, `usage_scenario`, `description`) VALUES
('美国静态住宅IP', 29.99, '美国', 5, '直连代理', 1000, '上架', 1, 'HTTP,HTTPS,SOCKS5', '随机', '社交媒体', '高质量美国住宅IP，适合社交媒体营销'),
('欧洲数据中心IP', 19.99, '德国', 3, '中转代理', 500, '上架', 0, 'HTTP,SOCKS5,Shadowsocks', '顺序', '电商平台', '稳定的欧洲数据中心IP资源'),
('亚太地区混合IP', 35.00, '新加坡', 10, '直连代理', -1, '上架', 1, 'HTTP,HTTPS,SOCKS5,VMess,Trojan', '权重', '综合业务', '亚太地区多协议支持的混合IP池');