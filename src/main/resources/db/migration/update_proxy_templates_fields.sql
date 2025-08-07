-- 更新 proxy_templates 表字段
-- 1. 修改 proxy_mode 字段的选项
-- 2. 添加 product_description 字段
-- 3. 添加 bandwidth_billing_mode 字段

USE `saas_xbox`;

-- 修改 proxy_mode 字段的枚举值
ALTER TABLE `proxy_templates` 
MODIFY COLUMN `proxy_mode` enum('不受限','黑名单','白名单') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '模式';

-- 添加 product_description 字段
ALTER TABLE `proxy_templates` 
ADD COLUMN `product_description` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品介绍' AFTER `description`;

-- 添加 bandwidth_billing_mode 字段
ALTER TABLE `proxy_templates` 
ADD COLUMN `bandwidth_billing_mode` enum('流量计费','宽带计费') COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '宽带计费模式' AFTER `product_description`;

-- 更新现有数据的 proxy_mode 值（将旧值映射到新值）
UPDATE `proxy_templates` SET `proxy_mode` = '不受限' WHERE `proxy_mode` = '直连代理';
UPDATE `proxy_templates` SET `proxy_mode` = '黑名单' WHERE `proxy_mode` = '中转代理';

-- 添加注释以说明更改
-- proxy_mode 字段含义更新：
-- - 不受限：不限制任何访问
-- - 黑名单：禁止访问黑名单中的地址
-- - 白名单：只允许访问白名单中的地址