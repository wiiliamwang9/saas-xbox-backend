-- 删除节点表中的city字段
-- 创建时间: 2024-08-01
-- 版本: 1.2.0

USE `saas_xbox`;

-- 删除city字段
ALTER TABLE `nodes` DROP COLUMN `city`;