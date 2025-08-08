-- 为 proxy_templates 表添加模板图片URL字段

USE `saas_xbox`;

-- 添加 image_url 字段
ALTER TABLE `proxy_templates` 
ADD COLUMN `image_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '模板图片URL' AFTER `bandwidth_billing_mode`;

-- 添加索引以提升查询性能
-- CREATE INDEX `idx_proxy_templates_image_url` ON `proxy_templates`(`image_url`);

-- 添加注释以说明更改
-- image_url 字段用于存储模板的展示图片URL
-- 支持http/https协议的图片链接，最大长度500字符