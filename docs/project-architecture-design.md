# SaaS静态住宅IP平台 - 项目架构设计文档

## 项目概述

### 1.1 项目背景
基于现有Xbox节点控制器系统，构建一个完整的SaaS静态住宅IP平台，为企业和个人用户提供专业的IP代理服务。

### 1.2 核心功能
- **管理端**：产品管理、IP池管理、节点管理、订单管理、售后管理、客户管理、员工管理、营销管理、系统管理
- **客户端**：登陆注册、订单管理、静态住宅IP、系统管理、个人中心、反馈建议

### 1.3 技术栈选择
- **后端**: Spring Boot 3.3.0 + Java 21
- **前端**: Next.js 15 + React 18 + TypeScript + Tailwind CSS
- **数据库**: MySQL 8.0 + Redis 6.0
- **节点控制**: Xbox Controller (Go语言) + Agent
- **消息队列**: RabbitMQ (用于订单处理、通知等)
- **文件存储**: MinIO/阿里云OSS (用于文档、图片存储)

## 整体系统架构

### 2.1 系统架构图

```
┌─────────────────────────────────────────────────────────────────┐
│                        客户端层 (Client Layer)                   │
├─────────────────────────────────────────────────────────────────┤
│  管理端Web       │  客户端Web      │  移动端APP    │  API文档      │
│  (Next.js)       │  (Next.js)      │  (可选)       │  (Swagger)    │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                      网关层 (Gateway Layer)                     │
├─────────────────────────────────────────────────────────────────┤
│           Nginx 反向代理 + 负载均衡 + SSL终端               │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                     应用服务层 (Application Layer)              │
├─────────────────────────────────────────────────────────────────┤
│                    SaaS Backend Service                         │
│                    (Spring Boot 3.3.0)                         │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐│
│  │ 用户管理模块 │ │ 产品管理模块 │ │ 订单管理模块 │ │ IP池管理模块 ││
│  │ User Module │ │Product Module│ │Order Module │ │ IP Module   ││
│  └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘│
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐│
│  │ 节点管理模块 │ │ 支付管理模块 │ │ 售后管理模块 │ │ 系统管理模块 ││
│  │ Node Module │ │Payment Module│ │Support Module│ │System Module││
│  └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘│
└─────────────────────────────────────────────────────────────────┘
                                │
                ┌───────────────┼───────────────┐
                ▼               ▼               ▼
┌─────────────────────┐ ┌─────────────────┐ ┌─────────────────┐
│    数据存储层        │ │   外部服务层     │ │   节点控制层     │
│  (Storage Layer)    │ │(External Layer) │ │(Node Control)   │
├─────────────────────┤ ├─────────────────┤ ├─────────────────┤
│ ┌─────────────────┐ │ │ ┌─────────────┐ │ │ ┌─────────────┐ │
│ │     MySQL       │ │ │ │   支付网关   │ │ │ │Xbox Controller│ │
│ │   (主数据库)     │ │ │ │ (微信/支付宝) │ │ │ │  (8080端口)  │ │
│ └─────────────────┘ │ │ └─────────────┘ │ │ └─────────────┘ │
│ ┌─────────────────┐ │ │ ┌─────────────┐ │ │        │        │
│ │     Redis       │ │ │ │   短信服务   │ │ │        ▼        │
│ │   (缓存/会话)    │ │ │ │  (阿里云)    │ │ │ ┌─────────────┐ │
│ └─────────────────┘ │ │ └─────────────┘ │ │ │ Agent Nodes │ │
│ ┌─────────────────┐ │ │ ┌─────────────┐ │ │ │ (8081端口)  │ │
│ │   RabbitMQ      │ │ │ │   邮件服务   │ │ │ │ (分布式节点) │ │
│ │   (消息队列)     │ │ │ │   (SMTP)     │ │ │ └─────────────┘ │
│ └─────────────────┘ │ │ └─────────────┘ │ └─────────────────┘
│ ┌─────────────────┐ │ │ ┌─────────────┐ │
│ │    MinIO        │ │ │ │   监控告警   │ │
│ │   (文件存储)     │ │ │ │ (Prometheus) │ │
│ └─────────────────┘ │ │ └─────────────┘ │
└─────────────────────┘ └─────────────────┘
```

### 2.2 服务端口规划
- **SaaS Backend**: 8080 (Spring Boot应用)
- **Xbox Controller**: 8080 (节点控制器API)
- **Xbox Agent**: 8081 (各节点Agent)
- **Frontend Admin**: 3000 (管理端前端)
- **Frontend Client**: 3001 (客户端前端)
- **MySQL**: 3306
- **Redis**: 6379
- **RabbitMQ**: 5672

### 2.3 核心服务模块

#### 2.3.1 用户管理服务
- 用户注册/登录/权限管理
- 管理员权限控制
- 客户信息管理

#### 2.3.2 产品管理服务
- 住宅IP产品配置
- 套餐定价管理
- 地区IP池管理

#### 2.3.3 订单管理服务
- 订单创建/支付/确认
- 订单状态流转
- 自动开通服务

#### 2.3.4 节点管理服务
- 与Xbox Controller对接
- 节点状态监控
- IP资源分配

#### 2.3.5 支付管理服务
- 集成多种支付方式
- 支付回调处理
- 财务统计报表

## 数据库设计

### 3.1 数据库选择说明
- **MySQL 8.0**: 主数据库，存储业务数据
- **Redis 6.0**: 缓存数据库，存储会话、临时数据
- **分库分表策略**: 订单表按时间分表，用户表按ID分表

### 3.2 核心数据表设计

#### 3.2.1 用户相关表

```sql
-- 用户基础信息表
CREATE TABLE `users` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` varchar(50) NOT NULL COMMENT '用户名',
    `email` varchar(100) NOT NULL COMMENT '邮箱',
    `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
    `password_hash` varchar(255) NOT NULL COMMENT '密码哈希',
    `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
    `user_type` tinyint NOT NULL DEFAULT '1' COMMENT '用户类型：1-普通用户，2-VIP用户，3-企业用户',
    `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-正常，2-待验证',
    `balance` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '账户余额',
    `avatar_url` varchar(255) DEFAULT NULL COMMENT '头像URL',
    `register_ip` varchar(45) DEFAULT NULL COMMENT '注册IP',
    `last_login_at` datetime DEFAULT NULL COMMENT '最后登录时间',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted_at` datetime DEFAULT NULL COMMENT '删除时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`),
    KEY `idx_phone` (`phone`),
    KEY `idx_status` (`status`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户基础信息表';

-- 管理员表
CREATE TABLE `admins` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
    `username` varchar(50) NOT NULL COMMENT '管理员用户名',
    `email` varchar(100) NOT NULL COMMENT '邮箱',
    `password_hash` varchar(255) NOT NULL COMMENT '密码哈希',
    `real_name` varchar(50) NOT NULL COMMENT '真实姓名',
    `role_id` bigint NOT NULL COMMENT '角色ID',
    `department` varchar(50) DEFAULT NULL COMMENT '部门',
    `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
    `last_login_at` datetime DEFAULT NULL COMMENT '最后登录时间',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`),
    KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 角色权限表
CREATE TABLE `roles` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `name` varchar(50) NOT NULL COMMENT '角色名称',
    `description` varchar(200) DEFAULT NULL COMMENT '角色描述',
    `permissions` json DEFAULT NULL COMMENT '权限列表(JSON格式)',
    `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限表';
```

#### 3.2.2 产品相关表

```sql
-- 产品表
CREATE TABLE `products` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '产品ID',
    `name` varchar(100) NOT NULL COMMENT '产品名称',
    `description` text COMMENT '产品描述',
    `type` varchar(20) NOT NULL COMMENT '产品类型：residential_ip, datacenter_ip',
    `region` varchar(50) NOT NULL COMMENT '地区：US, UK, JP等',
    `bandwidth` int DEFAULT NULL COMMENT '带宽限制(Mbps)',
    `concurrent_limit` int DEFAULT NULL COMMENT '并发连接数限制',
    `rotation_time` int DEFAULT NULL COMMENT 'IP轮换时间(分钟)',
    `price_monthly` decimal(8,2) NOT NULL COMMENT '月付价格',
    `price_quarterly` decimal(8,2) DEFAULT NULL COMMENT '季付价格',
    `price_yearly` decimal(8,2) DEFAULT NULL COMMENT '年付价格',
    `stock` int NOT NULL DEFAULT '0' COMMENT '库存数量',
    `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-下架，1-上架',
    `sort_order` int NOT NULL DEFAULT '0' COMMENT '排序权重',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_type_region` (`type`, `region`),
    KEY `idx_status` (`status`),
    KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品表';

-- IP池表
CREATE TABLE `ip_pools` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'IP池ID',
    `name` varchar(100) NOT NULL COMMENT 'IP池名称',
    `region` varchar(50) NOT NULL COMMENT '地区',
    `ip_count` int NOT NULL DEFAULT '0' COMMENT 'IP数量',
    `available_count` int NOT NULL DEFAULT '0' COMMENT '可用IP数量',
    `node_id` varchar(50) DEFAULT NULL COMMENT '关联的节点ID(Xbox)',
    `quality_score` decimal(3,1) DEFAULT '0.0' COMMENT '质量评分(0-10)',
    `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-维护，1-正常，2-异常',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_region` (`region`),
    KEY `idx_node_id` (`node_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='IP池表';

-- IP详情表
CREATE TABLE `ip_addresses` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'IP地址ID',
    `pool_id` bigint NOT NULL COMMENT '所属IP池ID',
    `ip_address` varchar(45) NOT NULL COMMENT 'IP地址',
    `port` int DEFAULT NULL COMMENT '端口',
    `username` varchar(50) DEFAULT NULL COMMENT '认证用户名',
    `password` varchar(100) DEFAULT NULL COMMENT '认证密码',
    `country` varchar(10) DEFAULT NULL COMMENT '国家代码',
    `city` varchar(50) DEFAULT NULL COMMENT '城市',
    `isp` varchar(50) DEFAULT NULL COMMENT 'ISP供应商',
    `speed_mbps` decimal(6,2) DEFAULT NULL COMMENT '测速结果(Mbps)',
    `success_rate` decimal(5,2) DEFAULT '100.00' COMMENT '成功率(%)',
    `last_check_at` datetime DEFAULT NULL COMMENT '最后检测时间',
    `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-可用，2-占用，3-异常',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_ip_port` (`ip_address`, `port`),
    KEY `idx_pool_id` (`pool_id`),
    KEY `idx_status` (`status`),
    KEY `idx_country_city` (`country`, `city`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='IP地址详情表';
```

#### 3.2.3 订单相关表

```sql
-- 订单表
CREATE TABLE `orders` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `order_no` varchar(32) NOT NULL COMMENT '订单号',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `product_id` bigint NOT NULL COMMENT '产品ID',
    `product_name` varchar(100) NOT NULL COMMENT '产品名称快照',
    `quantity` int NOT NULL DEFAULT '1' COMMENT '购买数量',
    `unit_price` decimal(8,2) NOT NULL COMMENT '单价',
    `total_amount` decimal(10,2) NOT NULL COMMENT '订单总额',
    `discount_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '优惠金额',
    `paid_amount` decimal(10,2) NOT NULL COMMENT '实付金额',
    `payment_method` varchar(20) DEFAULT NULL COMMENT '支付方式：alipay,wechat,balance',
    `billing_cycle` varchar(20) NOT NULL COMMENT '计费周期：monthly,quarterly,yearly',
    `start_date` date NOT NULL COMMENT '服务开始日期',
    `end_date` date NOT NULL COMMENT '服务结束日期',
    `status` tinyint NOT NULL DEFAULT '0' COMMENT '订单状态：0-待支付，1-已支付，2-服务中，3-已完成，4-已取消，5-已退款',
    `paid_at` datetime DEFAULT NULL COMMENT '支付时间',
    `activated_at` datetime DEFAULT NULL COMMENT '激活时间',
    `expired_at` datetime DEFAULT NULL COMMENT '过期时间',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_status` (`status`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 服务实例表
CREATE TABLE `service_instances` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '服务实例ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `order_id` bigint NOT NULL COMMENT '订单ID',
    `product_id` bigint NOT NULL COMMENT '产品ID',
    `instance_name` varchar(100) NOT NULL COMMENT '实例名称',
    `allocated_ips` json DEFAULT NULL COMMENT '分配的IP列表(JSON格式)',
    `proxy_host` varchar(100) DEFAULT NULL COMMENT '代理主机',
    `proxy_port` int DEFAULT NULL COMMENT '代理端口',
    `auth_username` varchar(50) DEFAULT NULL COMMENT '认证用户名',
    `auth_password` varchar(100) DEFAULT NULL COMMENT '认证密码',
    `traffic_used` bigint NOT NULL DEFAULT '0' COMMENT '已使用流量(MB)',
    `traffic_limit` bigint DEFAULT NULL COMMENT '流量限制(MB)',
    `concurrent_limit` int DEFAULT NULL COMMENT '并发连接数限制',
    `last_used_at` datetime DEFAULT NULL COMMENT '最后使用时间',
    `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-停用，1-正常，2-暂停，3-过期',
    `expires_at` datetime NOT NULL COMMENT '过期时间',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_status` (`status`),
    KEY `idx_expires_at` (`expires_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务实例表';
```

#### 3.2.4 节点管理表

```sql
-- 节点信息表 (与Xbox系统对接)
CREATE TABLE `nodes` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '节点ID',
    `xbox_agent_id` varchar(50) NOT NULL COMMENT 'Xbox Agent ID',
    `name` varchar(100) NOT NULL COMMENT '节点名称',
    `hostname` varchar(100) DEFAULT NULL COMMENT '主机名',
    `ip_address` varchar(45) DEFAULT NULL COMMENT '节点IP地址',
    `region` varchar(50) NOT NULL COMMENT '地区',
    `city` varchar(50) DEFAULT NULL COMMENT '城市',
    `isp` varchar(50) DEFAULT NULL COMMENT 'ISP供应商',
    `capacity` int NOT NULL DEFAULT '0' COMMENT '容量(最大连接数)',
    `current_load` int NOT NULL DEFAULT '0' COMMENT '当前负载',
    `cpu_usage` decimal(5,2) DEFAULT NULL COMMENT 'CPU使用率',
    `memory_usage` decimal(5,2) DEFAULT NULL COMMENT '内存使用率',
    `bandwidth_mbps` int DEFAULT NULL COMMENT '带宽(Mbps)',
    `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-离线，1-在线，2-维护，3-异常',
    `last_heartbeat_at` datetime DEFAULT NULL COMMENT '最后心跳时间',
    `version` varchar(20) DEFAULT NULL COMMENT '版本号',
    `metadata` json DEFAULT NULL COMMENT '扩展元数据',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_xbox_agent_id` (`xbox_agent_id`),
    KEY `idx_region` (`region`),
    KEY `idx_status` (`status`),
    KEY `idx_last_heartbeat` (`last_heartbeat_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='节点信息表';

-- 节点监控历史表
CREATE TABLE `node_metrics_history` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `node_id` bigint NOT NULL COMMENT '节点ID',
    `cpu_usage` decimal(5,2) DEFAULT NULL COMMENT 'CPU使用率',
    `memory_usage` decimal(5,2) DEFAULT NULL COMMENT '内存使用率',
    `disk_usage` decimal(5,2) DEFAULT NULL COMMENT '磁盘使用率',
    `network_rx` bigint DEFAULT NULL COMMENT '网络接收字节',
    `network_tx` bigint DEFAULT NULL COMMENT '网络发送字节',
    `active_connections` int DEFAULT NULL COMMENT '活跃连接数',
    `recorded_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
    PRIMARY KEY (`id`),
    KEY `idx_node_id_time` (`node_id`, `recorded_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='节点监控历史表';
```

#### 3.2.5 支付财务表

```sql
-- 支付记录表
CREATE TABLE `payments` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '支付记录ID',
    `order_id` bigint NOT NULL COMMENT '订单ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `payment_no` varchar(32) NOT NULL COMMENT '支付流水号',
    `third_party_no` varchar(64) DEFAULT NULL COMMENT '第三方支付流水号',
    `payment_method` varchar(20) NOT NULL COMMENT '支付方式：alipay,wechat,balance',
    `amount` decimal(10,2) NOT NULL COMMENT '支付金额',
    `currency` varchar(3) NOT NULL DEFAULT 'CNY' COMMENT '货币类型',
    `status` tinyint NOT NULL DEFAULT '0' COMMENT '支付状态：0-待支付，1-支付成功，2-支付失败，3-已退款',
    `paid_at` datetime DEFAULT NULL COMMENT '支付成功时间',
    `callback_data` json DEFAULT NULL COMMENT '支付回调数据',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_payment_no` (`payment_no`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付记录表';

-- 财务流水表
CREATE TABLE `financial_records` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '流水ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `order_id` bigint DEFAULT NULL COMMENT '关联订单ID',
    `type` tinyint NOT NULL COMMENT '类型：1-充值，2-消费，3-退款，4-提现',
    `amount` decimal(10,2) NOT NULL COMMENT '金额',
    `balance_before` decimal(10,2) NOT NULL COMMENT '变动前余额',
    `balance_after` decimal(10,2) NOT NULL COMMENT '变动后余额',
    `description` varchar(200) DEFAULT NULL COMMENT '描述',
    `reference_no` varchar(64) DEFAULT NULL COMMENT '关联单号',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_type` (`type`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='财务流水表';
```

#### 3.2.6 售后支持表

```sql
-- 工单表
CREATE TABLE `tickets` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '工单ID',
    `ticket_no` varchar(20) NOT NULL COMMENT '工单号',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `assigned_admin_id` bigint DEFAULT NULL COMMENT '分配的客服ID',
    `category` varchar(20) NOT NULL COMMENT '分类：technical,billing,general',
    `priority` tinyint NOT NULL DEFAULT '2' COMMENT '优先级：1-高，2-中，3-低',
    `subject` varchar(200) NOT NULL COMMENT '主题',
    `description` text NOT NULL COMMENT '问题描述',
    `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：1-待处理，2-处理中，3-等待用户，4-已解决，5-已关闭',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `closed_at` datetime DEFAULT NULL COMMENT '关闭时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_ticket_no` (`ticket_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_assigned_admin_id` (`assigned_admin_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工单表';

-- 工单回复表
CREATE TABLE `ticket_replies` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '回复ID',
    `ticket_id` bigint NOT NULL COMMENT '工单ID',
    `user_id` bigint DEFAULT NULL COMMENT '用户ID(用户回复时)',
    `admin_id` bigint DEFAULT NULL COMMENT '管理员ID(客服回复时)',
    `content` text NOT NULL COMMENT '回复内容',
    `attachments` json DEFAULT NULL COMMENT '附件列表',
    `is_internal` tinyint NOT NULL DEFAULT '0' COMMENT '是否内部备注：0-否，1-是',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_ticket_id` (`ticket_id`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工单回复表';
```

#### 3.2.7 系统配置表

```sql
-- 系统配置表
CREATE TABLE `system_configs` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
    `config_key` varchar(100) NOT NULL COMMENT '配置键',
    `config_value` text DEFAULT NULL COMMENT '配置值',
    `config_type` varchar(20) NOT NULL DEFAULT 'string' COMMENT '配置类型：string,int,boolean,json',
    `description` varchar(200) DEFAULT NULL COMMENT '配置描述',
    `is_public` tinyint NOT NULL DEFAULT '0' COMMENT '是否公开：0-否，1-是',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- 操作日志表
CREATE TABLE `operation_logs` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    `user_id` bigint DEFAULT NULL COMMENT '用户ID',
    `admin_id` bigint DEFAULT NULL COMMENT '管理员ID',
    `action` varchar(50) NOT NULL COMMENT '操作动作',
    `resource_type` varchar(50) DEFAULT NULL COMMENT '资源类型',
    `resource_id` varchar(50) DEFAULT NULL COMMENT '资源ID',
    `ip_address` varchar(45) DEFAULT NULL COMMENT '操作IP',
    `user_agent` varchar(500) DEFAULT NULL COMMENT '用户代理',
    `request_data` json DEFAULT NULL COMMENT '请求数据',
    `response_data` json DEFAULT NULL COMMENT '响应数据',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_admin_id` (`admin_id`),
    KEY `idx_action` (`action`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';
```

### 3.3 数据库索引优化策略

#### 3.3.1 主要索引
- 所有主键使用自增ID
- 外键字段添加索引
- 常用查询字段添加复合索引
- 时间字段添加索引用于范围查询

#### 3.3.2 分表策略
- `orders`: 按月分表 (orders_202401, orders_202402...)
- `operation_logs`: 按月分表
- `node_metrics_history`: 按月分表

#### 3.3.3 缓存策略
- 用户会话信息: Redis (30分钟)
- 产品信息: Redis (1小时)
- IP池状态: Redis (5分钟)
- 系统配置: Redis (1小时)

## 后端API设计

### 4.1 API设计原则
- RESTful风格
- 统一响应格式
- 版本控制 (/api/v1/)
- JWT认证
- 接口限流
- 参数校验

### 4.2 统一响应格式

```java
public class Result<T> {
    private Integer code;        // 状态码
    private String message;      // 消息
    private T data;             // 数据
    private Long timestamp;     // 时间戳
    private String requestId;   // 请求ID(用于追踪)
}
```

### 4.3 核心API设计

#### 4.3.1 用户认证API

```yaml
# 用户注册
POST /api/v1/auth/register
Request:
  username: string
  email: string
  password: string
  phone?: string
  code: string        # 验证码

# 用户登录
POST /api/v1/auth/login
Request:
  username: string
  password: string
  captcha: string
Response:
  token: string
  refreshToken: string
  user: UserInfo

# 管理员登录
POST /api/v1/admin/auth/login
Request:
  username: string
  password: string
  captcha: string

# 刷新令牌
POST /api/v1/auth/refresh
Request:
  refreshToken: string
Response:
  token: string
  refreshToken: string
```

#### 4.3.2 产品管理API

```yaml
# 获取产品列表 (客户端)
GET /api/v1/products
Query:
  type?: string       # 产品类型
  region?: string     # 地区
  page?: int
  limit?: int
Response:
  products: Product[]
  total: int
  page: int
  limit: int

# 产品管理 (管理端)
GET /api/v1/admin/products      # 列表
POST /api/v1/admin/products     # 创建
PUT /api/v1/admin/products/{id} # 更新
DELETE /api/v1/admin/products/{id} # 删除
```

#### 4.3.3 订单管理API

```yaml
# 创建订单
POST /api/v1/orders
Request:
  productId: number
  quantity: number
  billingCycle: string   # monthly/quarterly/yearly
  couponCode?: string

# 订单支付
POST /api/v1/orders/{orderId}/pay
Request:
  paymentMethod: string  # alipay/wechat/balance

# 获取用户订单列表
GET /api/v1/orders
Query:
  status?: string
  page?: int
  limit?: int

# 管理端订单管理  
GET /api/v1/admin/orders        # 列表
GET /api/v1/admin/orders/{id}   # 详情
PUT /api/v1/admin/orders/{id}/status # 更新状态
```

#### 4.3.4 服务实例API

```yaml
# 获取用户服务实例
GET /api/v1/services
Query:
  status?: string
  page?: int
  limit?: int

# 获取实例详情
GET /api/v1/services/{id}
Response:
  id: number
  instanceName: string
  allocatedIps: string[]
  proxyHost: string
  proxyPort: number
  authUsername: string
  authPassword: string
  trafficUsed: number
  trafficLimit: number
  status: string
  expiresAt: string

# 重置实例认证信息
POST /api/v1/services/{id}/reset-auth

# 获取使用统计
GET /api/v1/services/{id}/stats
Query:
  startDate?: string
  endDate?: string
```

#### 4.3.5 节点管理API (与Xbox对接)

```yaml
# 节点列表 (管理端)
GET /api/v1/admin/nodes
Query:
  region?: string
  status?: string
  page?: int
  limit?: int

# 节点详情
GET /api/v1/admin/nodes/{id}
Response:
  id: number
  xboxAgentId: string
  name: string
  region: string
  capacity: number
  currentLoad: number
  cpuUsage: number
  memoryUsage: number
  status: string
  lastHeartbeatAt: string

# 节点监控数据
GET /api/v1/admin/nodes/{id}/metrics
Query:
  startTime?: string
  endTime?: string
  
# 同步Xbox节点数据
POST /api/v1/admin/nodes/sync-xbox

# 节点操作 (通过Xbox API)
POST /api/v1/admin/nodes/{id}/restart  # 重启节点
POST /api/v1/admin/nodes/{id}/update-config  # 更新配置
```

#### 4.3.6 IP池管理API

```yaml
# IP池列表 (管理端)
GET /api/v1/admin/ip-pools
Query:
  region?: string
  status?: string
  page?: int
  limit?: int

# IP池详情
GET /api/v1/admin/ip-pools/{id}
Response:
  id: number
  name: string
  region: string
  ipCount: number
  availableCount: number
  nodeId: string
  qualityScore: number
  status: string

# IP地址列表
GET /api/v1/admin/ip-pools/{poolId}/ips
Query:
  status?: string
  country?: string
  page?: int
  limit?: int

# 测试IP质量
POST /api/v1/admin/ip-pools/{poolId}/test-quality

# IP地址管理
POST /api/v1/admin/ip-addresses     # 添加IP
PUT /api/v1/admin/ip-addresses/{id} # 更新IP状态
DELETE /api/v1/admin/ip-addresses/{id} # 删除IP
```

#### 4.3.7 工单系统API

```yaml
# 创建工单
POST /api/v1/tickets
Request:
  category: string
  priority: number
  subject: string
  description: string
  attachments?: string[]

# 获取用户工单列表
GET /api/v1/tickets
Query:
  status?: string
  page?: int
  limit?: int

# 工单详情
GET /api/v1/tickets/{id}

# 工单回复
POST /api/v1/tickets/{id}/replies
Request:
  content: string
  attachments?: string[]

# 管理端工单管理
GET /api/v1/admin/tickets          # 工单列表
PUT /api/v1/admin/tickets/{id}/assign # 分配客服
POST /api/v1/admin/tickets/{id}/reply  # 客服回复
PUT /api/v1/admin/tickets/{id}/close   # 关闭工单
```

### 4.4 与Xbox系统对接方案

#### 4.4.1 对接架构

```java
@Service
public class XboxIntegrationService {
    
    private final RestTemplate xboxRestTemplate;
    private final XboxConfig xboxConfig;
    
    // 同步节点信息
    public void syncNodes() {
        // 调用Xbox Controller API获取节点列表
        String url = xboxConfig.getControllerUrl() + "/api/v1/agents";
        XboxAgentResponse response = xboxRestTemplate.getForObject(url, XboxAgentResponse.class);
        
        // 更新本地节点信息
        updateLocalNodes(response.getData().getAgents());
    }
    
    // 获取节点监控数据
    public NodeMetrics getNodeMetrics(String agentId) {
        String url = xboxConfig.getControllerUrl() + "/api/v1/agents/" + agentId;
        return xboxRestTemplate.getForObject(url, NodeMetrics.class);
    }
    
    // 更新节点配置
    public void updateNodeConfig(String agentId, String config) {
        String url = xboxConfig.getControllerUrl() + "/api/v1/configs";
        ConfigRequest request = new ConfigRequest(agentId, config);
        xboxRestTemplate.postForObject(url, request, ConfigResponse.class);
    }
}
```

#### 4.4.2 定时同步任务

```java
@Component
public class XboxSyncScheduler {
    
    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    public void syncNodeStatus() {
        xboxIntegrationService.syncNodes();
    }
    
    @Scheduled(fixedRate = 300000) // 每5分钟执行一次
    public void collectMetrics() {
        nodeService.collectAllNodeMetrics();
    }
}
```

#### 4.4.3 数据映射关系

| SaaS系统 | Xbox系统 | 说明 |
|---------|----------|------|
| nodes.xbox_agent_id | agent.id | 节点标识映射 |
| nodes.status | agent.status | 状态映射 |
| nodes.last_heartbeat_at | agent.last_heartbeat | 心跳时间 |
| ip_pools.node_id | agent.id | IP池关联节点 |

## 项目开发计划

### 5.1 开发阶段规划

#### 第一阶段：基础架构搭建 (2周)
**目标：完成项目基础架构和核心功能**

**Week 1:**
- [x] 完善Spring Boot项目初始化
- [ ] 数据库设计和创建
- [ ] 基础实体类和Repository开发
- [ ] 统一异常处理和响应格式
- [ ] JWT认证和权限控制
- [ ] 基础工具类开发

**Week 2:**
- [ ] 用户管理模块开发
- [ ] 管理员登录功能
- [ ] 基础API接口开发
- [ ] 与Xbox系统对接基础框架
- [ ] 前端项目环境搭建
- [ ] 基础页面开发

#### 第二阶段：核心业务功能 (3周)

**Week 3:**
- [ ] 产品管理模块
  - [ ] 产品CRUD操作
  - [ ] 产品分类和定价
  - [ ] 库存管理
- [ ] IP池管理模块
  - [ ] IP池基础管理
  - [ ] 与Xbox节点关联
  - [ ] IP质量检测

**Week 4:**
- [ ] 订单管理模块
  - [ ] 订单创建和支付流程
  - [ ] 支付接口集成 (支付宝/微信)
  - [ ] 订单状态管理
- [ ] 服务实例管理
  - [ ] 服务自动开通
  - [ ] IP资源分配
  - [ ] 用户服务管理

**Week 5:**
- [ ] 节点管理模块
  - [ ] Xbox API集成完善
  - [ ] 节点监控和告警
  - [ ] 节点配置管理
- [ ] 前端管理界面开发
  - [ ] 产品管理页面
  - [ ] 订单管理页面
  - [ ] 节点管理页面

#### 第三阶段：客户端功能 (2周)

**Week 6:**
- [ ] 客户注册登录
- [ ] 产品购买流程
- [ ] 支付集成
- [ ] 个人中心
- [ ] 服务实例管理

**Week 7:**
- [ ] 工单系统
- [ ] 使用统计和报表
- [ ] 反馈建议功能
- [ ] 客户端UI优化

#### 第四阶段：高级功能和优化 (2周)

**Week 8:**
- [ ] 员工管理系统
- [ ] 权限角色管理
- [ ] 营销管理功能
- [ ] 优惠券系统

**Week 9:**
- [ ] 系统监控和日志
- [ ] 性能优化
- [ ] 安全加固
- [ ] 部署和运维

### 5.2 技术里程碑

#### 5.2.1 后端里程碑
- [ ] **M1** - 基础架构完成 (Week 2)
- [ ] **M2** - 核心业务API完成 (Week 5)  
- [ ] **M3** - Xbox对接完成 (Week 6)
- [ ] **M4** - 完整功能上线 (Week 9)

#### 5.2.2 前端里程碑
- [ ] **M1** - 管理端基础页面 (Week 3)
- [ ] **M2** - 客户端基础功能 (Week 6)
- [ ] **M3** - 完整UI/UX (Week 8)
- [ ] **M4** - 性能优化和发布 (Week 9)

### 5.3 团队分工建议

#### 5.3.1 后端开发 (2人)
- **Person A**: 用户管理、订单管理、支付系统
- **Person B**: 产品管理、节点管理、Xbox对接

#### 5.3.2 前端开发 (2人)  
- **Person C**: 管理端界面开发
- **Person D**: 客户端界面开发

#### 5.3.3 测试和运维 (1人)
- **Person E**: 测试、部署、运维、文档

### 5.4 质量保证

#### 5.4.1 代码质量
- 代码审查 (Code Review)
- 单元测试覆盖率 > 80%
- 集成测试
- API文档自动生成

#### 5.4.2 性能要求
- 接口响应时间 < 500ms
- 支持并发用户数 > 1000
- 数据库查询优化
- 缓存策略实施

#### 5.4.3 安全要求
- SQL注入防护
- XSS攻击防护  
- 接口限流
- 数据加密存储

### 5.5 部署方案

#### 5.5.1 开发环境
- Docker Compose本地部署
- 热重载开发模式
- 本地数据库和缓存

#### 5.5.2 生产环境
- Kubernetes集群部署
- MySQL主从架构
- Redis集群
- Nginx负载均衡
- 日志收集和监控

## 风险评估和解决方案

### 6.1 技术风险

#### 6.1.1 Xbox系统对接风险
**风险**: Xbox API变更或不稳定
**解决方案**: 
- 建立适配器模式，降低耦合
- 实现本地缓存机制
- Xbox API版本控制策略

#### 6.1.2 高并发性能风险
**风险**: 订单高峰期系统压力
**解决方案**:
- 数据库读写分离
- Redis缓存优化
- 消息队列异步处理
- 限流和熔断机制

### 6.2 业务风险

#### 6.2.1 支付安全风险
**风险**: 支付过程数据泄露
**解决方案**:
- HTTPS全链路加密
- 支付数据脱敏
- 审计日志记录
- 第三方支付SDK安全更新

#### 6.2.2 IP资源管理风险
**风险**: IP资源分配冲突
**解决方案**:
- 数据库事务控制
- 分布式锁机制
- IP池状态实时同步
- 异常恢复机制

### 6.3 进度风险

#### 6.3.1 开发进度风险
**风险**: 功能复杂度超预期
**解决方案**:
- 敏捷开发，迭代交付
- 功能优先级管理
- 技术难点提前验证
- 团队技能培训

## 总结

本项目架构设计基于现有的Xbox节点控制器系统，构建了一个完整的SaaS静态住宅IP平台。通过合理的系统架构、数据库设计和API规划，可以实现高性能、高可用的服务。

### 关键成功因素
1. **系统架构合理**: 微服务化设计，模块解耦
2. **数据库设计优化**: 合理索引，分表策略
3. **Xbox系统对接**: 稳定的API集成方案
4. **安全性保障**: 全面的安全防护措施
5. **性能优化**: 缓存、异步、限流等策略
6. **团队协作**: 明确分工，规范开发流程

按照本设计文档执行，预计可在9周内完成完整平台的开发和上线。