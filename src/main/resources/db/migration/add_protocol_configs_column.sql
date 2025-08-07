-- 为proxy_templates表添加protocol_configs字段
-- 用于存储前端协议详细配置信息

ALTER TABLE proxy_templates 
ADD COLUMN protocol_configs JSON COMMENT '协议配置(JSON格式)' AFTER extra_config;

-- 为已存在的记录设置默认值
UPDATE proxy_templates 
SET protocol_configs = JSON_OBJECT() 
WHERE protocol_configs IS NULL;