-- SaaS Xbox Backend 数据库表结构
-- 创建时间: 2024-07-31
-- 版本: 1.0.0

-- 设置字符集和存储引擎
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS `saas_xbox` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `saas_xbox`;

-- =============================================
-- 1. 基础管理表
-- =============================================

-- 部门表
DROP TABLE IF EXISTS `departments`;
CREATE TABLE `departments` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `dept_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '部门名称',
  `parent_id` bigint DEFAULT NULL COMMENT '父部门ID',
  `dept_level` int DEFAULT '1' COMMENT '部门层级',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `dept_status` enum('正常','停用') COLLATE utf8mb4_unicode_ci DEFAULT '正常' COMMENT '部门状态',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_dept_status` (`dept_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门表';

-- 员工表
DROP TABLE IF EXISTS `employees`;
CREATE TABLE `employees` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '员工ID',
  `employee_no` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '员工号',
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `real_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '真实姓名',
  `phone` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `department_id` bigint DEFAULT NULL COMMENT '部门ID',
  `role` enum('超级管理员','管理员','客户经理','普通员工','财务') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色',
  `employee_status` enum('正常','离职','停用') COLLATE utf8mb4_unicode_ci DEFAULT '正常' COMMENT '员工状态',
  `hire_date` date DEFAULT NULL COMMENT '入职日期',
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像URL',
  `last_login_time` timestamp NULL DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '最后登录IP',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_employee_no` (`employee_no`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_department_id` (`department_id`),
  KEY `idx_role` (`role`),
  KEY `idx_employee_status` (`employee_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='员工表';

-- =============================================
-- 2. 客户管理表
-- =============================================

-- 客户表
DROP TABLE IF EXISTS `customers`;
CREATE TABLE `customers` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '客户ID',
  `account` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户账号',
  `password` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `customer_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户名称',
  `customer_type` enum('个人','企业','工作室') COLLATE utf8mb4_unicode_ci DEFAULT '个人' COMMENT '客户类型',
  `phone` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `parent_id` bigint DEFAULT NULL COMMENT '父账户ID(子账户关联)',
  `vip_level` enum('普通','银牌','金牌','钻石') COLLATE utf8mb4_unicode_ci DEFAULT '普通' COMMENT 'VIP等级',
  `balance` decimal(10,2) DEFAULT '0.00' COMMENT '账户余额',
  `customer_status` enum('正常','冻结','黑名单') COLLATE utf8mb4_unicode_ci DEFAULT '正常' COMMENT '客户状态',
  `register_source` enum('官网注册','推广注册','线下开发') COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '注册来源',
  `manager_id` bigint DEFAULT NULL COMMENT '客户经理ID',
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像URL',
  `last_login_time` timestamp NULL DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '最后登录IP',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_account` (`account`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_manager_id` (`manager_id`),
  KEY `idx_customer_status` (`customer_status`),
  KEY `idx_vip_level` (`vip_level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户表';

-- 意向客户表
DROP TABLE IF EXISTS `intention_customers`;
CREATE TABLE `intention_customers` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '意向客户ID',
  `customer_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户名称',
  `customer_source` enum('线上注册','线下拜访','活动导入','其他') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户来源',
  `customer_type` enum('个人','企业','工作室') COLLATE utf8mb4_unicode_ci DEFAULT '个人' COMMENT '客户类型',
  `phone` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `wechat` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信号',
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `company_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '公司名称',
  `manager_id` bigint NOT NULL COMMENT '客户经理ID',
  `intention_level` enum('潜在客户','意向客户','准客户','签约客户') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '意向级别',
  `related_accounts` json DEFAULT NULL COMMENT '关联账号(JSON数组)',
  `is_deal` tinyint(1) DEFAULT '0' COMMENT '是否成交',
  `discount` decimal(5,2) DEFAULT '0.00' COMMENT '折扣(0-100)',
  `remark` text COLLATE utf8mb4_unicode_ci COMMENT '备注',
  `follow_up_time` timestamp NULL DEFAULT NULL COMMENT '下次跟进时间',
  `last_contact_time` timestamp NULL DEFAULT NULL COMMENT '最后联系时间',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`),
  KEY `idx_manager_id` (`manager_id`),
  KEY `idx_intention_level` (`intention_level`),
  KEY `idx_customer_source` (`customer_source`),
  KEY `idx_is_deal` (`is_deal`),
  KEY `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='意向客户表';

-- 客户余额记录表
DROP TABLE IF EXISTS `customer_balance_logs`;
CREATE TABLE `customer_balance_logs` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `customer_id` bigint NOT NULL COMMENT '客户ID',
  `change_type` enum('充值','消费','退款','调整') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '变动类型',
  `change_amount` decimal(10,2) NOT NULL COMMENT '变动金额',
  `balance_before` decimal(10,2) NOT NULL COMMENT '变动前余额',
  `balance_after` decimal(10,2) NOT NULL COMMENT '变动后余额',
  `related_order_id` bigint DEFAULT NULL COMMENT '关联订单ID',
  `operator_id` bigint DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '操作人姓名',
  `remark` text COLLATE utf8mb4_unicode_ci COMMENT '备注',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_change_type` (`change_type`),
  KEY `idx_related_order_id` (`related_order_id`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户余额记录表';

-- =============================================
-- 3. 产品管理表
-- =============================================

-- 产品表
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '产品ID',
  `product_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '产品名称',
  `product_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '产品编码',
  `ip_quality` enum('标准','优质','独享') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'IP质量',
  `ip_count` int NOT NULL COMMENT 'IP数量',
  `duration_days` int NOT NULL COMMENT '时长(天)',
  `bandwidth_mbps` int DEFAULT NULL COMMENT '带宽(Mbps)',
  `original_price` decimal(10,2) NOT NULL COMMENT '原价',
  `current_price` decimal(10,2) NOT NULL COMMENT '现价',
  `country_limit` json DEFAULT NULL COMMENT '国家限制(JSON)',
  `product_status` enum('上架','下架','停售') COLLATE utf8mb4_unicode_ci DEFAULT '上架' COMMENT '产品状态',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '产品描述',
  `features` json DEFAULT NULL COMMENT '产品特性(JSON)',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_product_code` (`product_code`),
  KEY `idx_ip_quality` (`ip_quality`),
  KEY `idx_product_status` (`product_status`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品表';

-- =============================================
-- 4. 订单管理表
-- =============================================

-- 订单表
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单号',
  `customer_id` bigint NOT NULL COMMENT '客户ID',
  `customer_account` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户账号',
  `customer_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户名称',
  `manager_id` bigint DEFAULT NULL COMMENT '客户经理ID',
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `product_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '产品名称',
  `order_status` enum('正常','已暂停','已过期','人工核验','待付款','已退款') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单状态',
  `payment_status` enum('待付款','已付款','已退款','部分退款') COLLATE utf8mb4_unicode_ci DEFAULT '待付款' COMMENT '支付状态',
  `total_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
  `actual_amount` decimal(10,2) NOT NULL COMMENT '实际支付金额',
  `discount_amount` decimal(10,2) DEFAULT '0.00' COMMENT '折扣金额',
  `country` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '国家/地区',
  `city` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '城市',
  `ip_quality` enum('标准','优质','独享') COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'IP质量',
  `ip_count` int DEFAULT NULL COMMENT 'IP数量',
  `duration_days` int NOT NULL COMMENT '购买时长',
  `start_date` date DEFAULT NULL COMMENT '开始日期',
  `end_date` date DEFAULT NULL COMMENT '结束日期',
  `ip_addresses` json DEFAULT NULL COMMENT 'IP地址列表',
  `payment_time` timestamp NULL DEFAULT NULL COMMENT '支付时间',
  `expire_time` timestamp NULL DEFAULT NULL COMMENT '过期时间',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_manager_id` (`manager_id`),
  KEY `idx_order_status` (`order_status`),
  KEY `idx_payment_status` (`payment_status`),
  KEY `idx_payment_time` (`payment_time`),
  KEY `idx_expire_time` (`expire_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- IP更换记录表
DROP TABLE IF EXISTS `ip_replace_logs`;
CREATE TABLE `ip_replace_logs` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `customer_id` bigint NOT NULL COMMENT '客户ID',
  `customer_account` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '操作账户',
  `original_ip` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '原始IP',
  `new_ip` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '新IP',
  `replace_reason` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更换原因',
  `test_result` enum('成功','失败') COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '测试结果',
  `test_message` text COLLATE utf8mb4_unicode_ci COMMENT '测试信息',
  `replace_status` enum('成功','失败','回滚') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '更换状态',
  `operator_id` bigint DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '操作人姓名',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_original_ip` (`original_ip`),
  KEY `idx_new_ip` (`new_ip`),
  KEY `idx_replace_status` (`replace_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='IP更换记录表';

-- =============================================
-- 5. IP池管理表
-- =============================================

-- IP池表
DROP TABLE IF EXISTS `ip_pools`;
CREATE TABLE `ip_pools` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `ip_address` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'IP地址',
  `country` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '国家',
  `region` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地区',
  `city` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '城市',
  `ip_quality` enum('标准','优质','独享') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'IP质量',
  `ip_status` enum('可用','占用','故障','测试中','维护中') COLLATE utf8mb4_unicode_ci DEFAULT '可用' COMMENT 'IP状态',
  `port_range` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '端口范围',
  `bandwidth_mbps` int DEFAULT NULL COMMENT '带宽(Mbps)',
  `provider` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '供应商',
  `cost_price` decimal(10,2) DEFAULT NULL COMMENT '成本价格',
  `node_id` bigint DEFAULT NULL COMMENT '关联节点ID',
  `current_order_id` bigint DEFAULT NULL COMMENT '当前占用订单ID',
  `last_test_time` timestamp NULL DEFAULT NULL COMMENT '最后测试时间',
  `test_result` enum('成功','失败') COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '测试结果',
  `test_latency` int DEFAULT NULL COMMENT '测试延迟(ms)',
  `test_message` text COLLATE utf8mb4_unicode_ci COMMENT '测试信息',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_ip_address` (`ip_address`),
  KEY `idx_country` (`country`),
  KEY `idx_city` (`city`),
  KEY `idx_ip_quality` (`ip_quality`),
  KEY `idx_ip_status` (`ip_status`),
  KEY `idx_node_id` (`node_id`),
  KEY `idx_current_order_id` (`current_order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='IP池表';

-- =============================================
-- 6. 节点管理表
-- =============================================

-- 节点表
DROP TABLE IF EXISTS `nodes`;
CREATE TABLE `nodes` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '节点ID',
  `node_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '节点名称',
  `node_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '节点编码',
  `server_ip` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '服务器IP',
  `country` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '国家',
  `region` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地区',
  `city` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '城市',
  `node_type` enum('物理节点','虚拟节点') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '节点类型',
  `node_status` enum('运行中','维护中','故障','停用') COLLATE utf8mb4_unicode_ci DEFAULT '运行中' COMMENT '节点状态',
  `max_connections` int DEFAULT '1000' COMMENT '最大连接数',
  `current_connections` int DEFAULT '0' COMMENT '当前连接数',
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
  KEY `idx_server_ip` (`server_ip`),
  KEY `idx_country` (`country`),
  KEY `idx_city` (`city`),
  KEY `idx_node_status` (`node_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='节点表';

-- =============================================
-- 7. 销售管理表
-- =============================================

-- 销售记录表
DROP TABLE IF EXISTS `sales_records`;
CREATE TABLE `sales_records` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `order_no` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单号',
  `customer_id` bigint NOT NULL COMMENT '客户ID',
  `customer_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户名称',
  `manager_id` bigint NOT NULL COMMENT '客户经理ID',
  `manager_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户经理姓名',
  `product_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '产品名称',
  `actual_price` decimal(10,2) NOT NULL COMMENT '实际成交价',
  `original_price` decimal(10,2) NOT NULL COMMENT '原价',
  `discount_amount` decimal(10,2) DEFAULT '0.00' COMMENT '折扣金额',
  `country` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '国家/地区',
  `city` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '城市',
  `ip_quality` enum('标准','优质','独享') COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'IP质量',
  `duration_days` int DEFAULT NULL COMMENT '购买时长(天)',
  `deal_time` timestamp NOT NULL COMMENT '成交时间',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_id` (`order_id`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_manager_id` (`manager_id`),
  KEY `idx_deal_time` (`deal_time`),
  KEY `idx_country` (`country`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='销售记录表';

-- 销售提成表
DROP TABLE IF EXISTS `sales_commissions`;
CREATE TABLE `sales_commissions` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '提成ID',
  `manager_id` bigint NOT NULL COMMENT '客户经理ID',
  `manager_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户经理姓名',
  `sales_account` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '销售账号',
  `order_id` bigint NOT NULL COMMENT '关联订单ID',
  `order_no` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单号',
  `customer_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户名称',
  `order_amount` decimal(10,2) NOT NULL COMMENT '订单成交价',
  `order_date` timestamp NOT NULL COMMENT '订单成交日期',
  `order_quantity` int NOT NULL COMMENT '订单数量',
  `commission_rule_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '提成规则名称',
  `commission_rate` decimal(5,2) NOT NULL COMMENT '提成比例',
  `commission_amount` decimal(10,2) NOT NULL COMMENT '提成金额',
  `calculation_method` enum('按订单','按数量','按阶梯') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '提成计算方式',
  `order_payment_status` enum('待付款','已付款','已退款') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单支付状态',
  `commission_status` enum('待结算','已结算','不结算') COLLATE utf8mb4_unicode_ci DEFAULT '待结算' COMMENT '提成状态',
  `settlement_time` timestamp NULL DEFAULT NULL COMMENT '结算时间',
  `settlement_remark` text COLLATE utf8mb4_unicode_ci COMMENT '结算备注',
  `settlement_operator_id` bigint DEFAULT NULL COMMENT '结算操作人ID',
  `settlement_operator_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '结算操作人姓名',
  `commission_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提成时间',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_manager_id` (`manager_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_commission_status` (`commission_status`),
  KEY `idx_order_date` (`order_date`),
  KEY `idx_settlement_time` (`settlement_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='销售提成表';

-- =============================================
-- 8. 售后管理表
-- =============================================

-- 售后工单表
DROP TABLE IF EXISTS `support_tickets`;
CREATE TABLE `support_tickets` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '工单ID',
  `ticket_no` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '工单号',
  `customer_id` bigint NOT NULL COMMENT '客户ID',
  `customer_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户名称',
  `customer_account` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户账号',
  `order_id` bigint DEFAULT NULL COMMENT '关联订单ID',
  `issue_type` enum('技术问题','账务问题','产品咨询','其他') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '问题类型',
  `priority` enum('低','中','高','紧急') COLLATE utf8mb4_unicode_ci DEFAULT '中' COMMENT '优先级',
  `title` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '问题标题',
  `description` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '问题描述',
  `attachments` json DEFAULT NULL COMMENT '附件列表(JSON)',
  `assigned_to` bigint DEFAULT NULL COMMENT '分配给(员工ID)',
  `assigned_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '分配给姓名',
  `ticket_status` enum('待处理','处理中','已解决','已关闭') COLLATE utf8mb4_unicode_ci DEFAULT '待处理' COMMENT '工单状态',
  `solution` text COLLATE utf8mb4_unicode_ci COMMENT '解决方案',
  `customer_rating` tinyint DEFAULT NULL COMMENT '客户评分(1-5)',
  `customer_feedback` text COLLATE utf8mb4_unicode_ci COMMENT '客户反馈',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `assigned_at` timestamp NULL DEFAULT NULL COMMENT '分配时间',
  `resolved_at` timestamp NULL DEFAULT NULL COMMENT '解决时间',
  `closed_at` timestamp NULL DEFAULT NULL COMMENT '关闭时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_ticket_no` (`ticket_no`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_assigned_to` (`assigned_to`),
  KEY `idx_ticket_status` (`ticket_status`),
  KEY `idx_issue_type` (`issue_type`),
  KEY `idx_priority` (`priority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='售后工单表';

-- 工单回复表
DROP TABLE IF EXISTS `support_ticket_replies`;
CREATE TABLE `support_ticket_replies` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '回复ID',
  `ticket_id` bigint NOT NULL COMMENT '工单ID',
  `reply_type` enum('客户回复','员工回复','系统消息') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '回复类型',
  `replier_id` bigint DEFAULT NULL COMMENT '回复人ID',
  `replier_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '回复人姓名',
  `reply_content` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '回复内容',
  `attachments` json DEFAULT NULL COMMENT '附件列表(JSON)',
  `is_internal` tinyint(1) DEFAULT '0' COMMENT '是否内部备注',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_ticket_id` (`ticket_id`),
  KEY `idx_reply_type` (`reply_type`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='工单回复表';

-- =============================================
-- 9. 系统日志表
-- =============================================

-- 操作日志表
DROP TABLE IF EXISTS `operation_logs`;
CREATE TABLE `operation_logs` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `operator_id` bigint DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '操作人姓名',
  `operator_type` enum('员工','客户','系统') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作人类型',
  `module_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '模块名称',
  `operation_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作类型',
  `target_id` bigint DEFAULT NULL COMMENT '目标ID',
  `target_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '目标名称',
  `before_data` json DEFAULT NULL COMMENT '变更前数据',
  `after_data` json DEFAULT NULL COMMENT '变更后数据',
  `operation_desc` text COLLATE utf8mb4_unicode_ci COMMENT '操作描述',
  `ip_address` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '操作IP',
  `user_agent` text COLLATE utf8mb4_unicode_ci COMMENT '用户代理',
  `execution_time` int DEFAULT NULL COMMENT '执行时间(ms)',
  `is_success` tinyint(1) DEFAULT '1' COMMENT '是否成功',
  `error_message` text COLLATE utf8mb4_unicode_ci COMMENT '错误信息',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_operator_id` (`operator_id`),
  KEY `idx_operator_type` (`operator_type`),
  KEY `idx_module_name` (`module_name`),
  KEY `idx_operation_type` (`operation_type`),
  KEY `idx_target_id` (`target_id`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- =============================================
-- 10. 系统配置表
-- =============================================

-- 系统配置表
DROP TABLE IF EXISTS `system_configs`;
CREATE TABLE `system_configs` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_key` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '配置键',
  `config_value` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '配置值',
  `config_type` enum('字符串','数字','布尔','JSON') COLLATE utf8mb4_unicode_ci DEFAULT '字符串' COMMENT '配置类型',
  `config_group` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置分组',
  `config_desc` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置描述',
  `is_public` tinyint(1) DEFAULT '0' COMMENT '是否公开',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`),
  KEY `idx_config_group` (`config_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- =============================================
-- 11. 初始化数据
-- =============================================

-- 插入初始部门数据
INSERT INTO `departments` (`dept_name`, `parent_id`, `dept_level`, `sort_order`) VALUES
('总部', NULL, 1, 1),
('技术部', 1, 2, 1),
('销售部', 1, 2, 2),
('财务部', 1, 2, 3),
('客服部', 1, 2, 4);

-- 插入初始员工数据（默认密码: 123456）
INSERT INTO `employees` (`employee_no`, `username`, `password`, `real_name`, `department_id`, `role`, `hire_date`) VALUES
('E001', 'admin', '$2a$10$7rQQI4EWojCajvs7t0n1s.DvZMOWKUOzRqj8KjYjGmJKL5YxCYeFW', '超级管理员', 1, '超级管理员', '2024-01-01'),
('E002', 'manager', '$2a$10$7rQQI4EWojCajvs7t0n1s.DvZMOWKUOzRqj8KjYjGmJKL5YxCYeFW', '系统管理员', 1, '管理员', '2024-01-01'),
('E003', 'sales001', '$2a$10$7rQQI4EWojCajvs7t0n1s.DvZMOWKUOzRqj8KjYjGmJKL5YxCYeFW', '客户经理一', 3, '客户经理', '2024-01-01'),
('E004', 'finance001', '$2a$10$7rQQI4EWojCajvs7t0n1s.DvZMOWKUOzRqj8KjYjGmJKL5YxCYeFW', '财务专员', 4, '财务', '2024-01-01');

-- 插入初始系统配置
INSERT INTO `system_configs` (`config_key`, `config_value`, `config_type`, `config_group`, `config_desc`) VALUES
('site.name', 'SaaS Xbox 管理平台', '字符串', 'basic', '网站名称'),
('site.logo', '/images/logo.png', '字符串', 'basic', '网站Logo'),
('commission.default_rate', '5.00', '数字', 'sales', '默认提成比例(%)'),
('order.auto_expire_days', '30', '数字', 'order', '订单自动过期天数'),
('ip.test_timeout', '5000', '数字', 'ip', 'IP测试超时时间(ms)');

SET FOREIGN_KEY_CHECKS = 1;