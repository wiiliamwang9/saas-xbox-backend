-- 为proxy_templates表添加缺失的字段

-- 添加sing-box相关字段
ALTER TABLE proxy_templates 
ADD COLUMN tag VARCHAR(100) NOT NULL DEFAULT 'default-inbound' COMMENT 'Inbound标识' AFTER mainland_direct,
ADD COLUMN protocol_type VARCHAR(50) NOT NULL DEFAULT 'vless' COMMENT '协议类型' AFTER tag,
ADD COLUMN listen_address VARCHAR(50) DEFAULT '::' COMMENT '监听地址' AFTER protocol_type,
ADD COLUMN listen_port INT NOT NULL DEFAULT 443 COMMENT '监听端口' AFTER listen_address,
ADD COLUMN users_config JSON COMMENT '用户配置(JSON格式)' AFTER listen_port,
ADD COLUMN transport_config JSON COMMENT '传输层配置(JSON格式)' AFTER users_config,
ADD COLUMN tls_config JSON COMMENT 'TLS配置(JSON格式)' AFTER transport_config,
ADD COLUMN reality_config JSON COMMENT 'Reality配置(JSON格式)' AFTER tls_config,
ADD COLUMN sniff_config JSON COMMENT '嗅探配置(JSON格式)' AFTER reality_config,
ADD COLUMN mux_config JSON COMMENT '多路复用配置(JSON格式)' AFTER sniff_config,
ADD COLUMN route_tag VARCHAR(100) COMMENT '路由标签' AFTER mux_config,
ADD COLUMN singbox_config JSON COMMENT 'sing-box完整配置(JSON格式)' AFTER route_tag;

-- 更新现有记录的必填字段默认值
UPDATE proxy_templates 
SET 
    tag = CONCAT('inbound-', id),
    protocol_type = 'vless',
    listen_address = '::',
    listen_port = 443 + id
WHERE tag IS NULL OR tag = 'default-inbound';