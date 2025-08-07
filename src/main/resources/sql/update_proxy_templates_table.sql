-- 更新proxy_templates表以支持sing-box配置
-- 基于3xui面板的Inbound设计

-- 添加sing-box相关字段
ALTER TABLE proxy_templates 
ADD COLUMN tag VARCHAR(100) NOT NULL COMMENT 'Inbound标识' AFTER id,
ADD COLUMN protocol_type VARCHAR(50) NOT NULL COMMENT '协议类型(vless,vmess,trojan等)' AFTER protocols,
ADD COLUMN listen_address VARCHAR(50) DEFAULT '::' COMMENT '监听地址' AFTER protocol_type,
ADD COLUMN listen_port INT NOT NULL COMMENT '监听端口' AFTER listen_address,
ADD COLUMN users_config JSON COMMENT '用户配置(JSON格式)' AFTER listen_port,
ADD COLUMN transport_config JSON COMMENT '传输层配置(JSON格式)' AFTER users_config,
ADD COLUMN tls_config JSON COMMENT 'TLS配置(JSON格式)' AFTER transport_config,
ADD COLUMN reality_config JSON COMMENT 'Reality配置(JSON格式)' AFTER tls_config,
ADD COLUMN sniff_config JSON COMMENT '嗅探配置(JSON格式)' AFTER reality_config,
ADD COLUMN mux_config JSON COMMENT '多路复用配置(JSON格式)' AFTER sniff_config,
ADD COLUMN route_tag VARCHAR(50) COMMENT '路由标签' AFTER mux_config,
ADD COLUMN singbox_config JSON COMMENT 'sing-box完整配置(JSON格式)' AFTER route_tag;

-- 添加索引
ALTER TABLE proxy_templates ADD INDEX idx_tag (tag);
ALTER TABLE proxy_templates ADD INDEX idx_protocol_type (protocol_type);
ALTER TABLE proxy_templates ADD INDEX idx_listen_port (listen_port);

-- 添加唯一约束
ALTER TABLE proxy_templates ADD UNIQUE KEY uk_tag (tag);

-- 更新现有数据的默认值（如果表中已有数据）
UPDATE proxy_templates SET 
    tag = CONCAT('inbound-', LOWER(REPLACE(product_name, ' ', '-')), '-', id),
    protocol_type = CASE 
        WHEN protocols LIKE '%vless%' THEN 'vless'
        WHEN protocols LIKE '%vmess%' THEN 'vmess' 
        WHEN protocols LIKE '%trojan%' THEN 'trojan'
        WHEN protocols LIKE '%shadowsocks%' THEN 'shadowsocks'
        ELSE 'vless'
    END,
    listen_port = 443 + id,
    route_tag = 'direct'
WHERE tag IS NULL OR tag = '';

-- 创建配置模板示例数据
INSERT INTO proxy_templates (
    product_name, tag, protocol_type, listen_address, listen_port,
    default_price, country, proxy_mode, stock, status,
    mainland_direct, protocols, allocation_mode,
    users_config, transport_config, tls_config, reality_config,
    sniff_config, mux_config, route_tag, singbox_config
) VALUES (
    'VLESS Reality 示例模板',
    'inbound-vless-reality-example',
    'vless',
    '::',
    443,
    29.99,
    '美国',
    '直连代理',
    -1,
    '下架',
    false,
    'vless',
    '随机',
    JSON_OBJECT(
        'users', JSON_ARRAY(
            JSON_OBJECT(
                'uuid', 'xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx',
                'name', 'user@example.com',
                'flow', '',
                'email', 'user@example.com',
                'limit', JSON_OBJECT(
                    'speed', 0,
                    'total', '100GB',
                    'expiry', '2025-12-31T00:00:00Z'
                )
            )
        )
    ),
    JSON_OBJECT(
        'type', 'ws',
        'path', '/websocket',
        'headers', JSON_OBJECT('Host', 'yourdomain.com')
    ),
    JSON_OBJECT(
        'enabled', true,
        'server_name', 'yourdomain.com',
        'alpn', JSON_ARRAY('h2', 'http/1.1')
    ),
    JSON_OBJECT(
        'enabled', true,
        'handshake', JSON_OBJECT(
            'server', 'target.com',
            'server_port', 443
        ),
        'private_key', 'xxxxxxxxxxxxxxxxxxxxxxxx',
        'short_id', JSON_ARRAY('abcd', 'efgh'),
        'fingerprint', 'chrome',
        'server_names', JSON_ARRAY('target.com')
    ),
    JSON_OBJECT(
        'enabled', true,
        'override_destination', true,
        'domains', JSON_ARRAY('tls', 'http')
    ),
    JSON_OBJECT(
        'enabled', false
    ),
    'direct',
    JSON_OBJECT(
        'tag', 'inbound-vless-reality-example',
        'type', 'vless',
        'listen', '::',
        'listen_port', 443,
        'users', JSON_ARRAY(
            JSON_OBJECT(
                'uuid', 'xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx',
                'name', 'user@example.com',
                'flow', '',
                'email', 'user@example.com'
            )
        ),
        'transport', JSON_OBJECT(
            'type', 'ws',
            'path', '/websocket',
            'headers', JSON_OBJECT('Host', 'yourdomain.com')
        ),
        'tls', JSON_OBJECT(
            'enabled', true,
            'server_name', 'yourdomain.com',
            'alpn', JSON_ARRAY('h2', 'http/1.1'),
            'reality', JSON_OBJECT(
                'enabled', true,
                'handshake', JSON_OBJECT(
                    'server', 'target.com',
                    'server_port', 443
                ),
                'private_key', 'xxxxxxxxxxxxxxxxxxxxxxxx',
                'short_id', JSON_ARRAY('abcd', 'efgh'),
                'fingerprint', 'chrome',
                'server_names', JSON_ARRAY('target.com')
            )
        ),
        'sniff', JSON_OBJECT(
            'enabled', true,
            'override_destination', true,
            'domains', JSON_ARRAY('tls', 'http')
        ),
        'mux', JSON_OBJECT(
            'enabled', false
        ),
        'route_tag', 'direct'
    )
) ON DUPLICATE KEY UPDATE product_name = VALUES(product_name);