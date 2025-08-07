#!/bin/bash

# 数据库测试数据初始化脚本
# 为订单管理、客户管理、员工管理系统添加假数据

echo "开始初始化测试数据..."

# 数据库连接配置
DB_HOST=${DB_HOST:-localhost}
DB_PORT=${DB_PORT:-3306}
DB_NAME=${DB_NAME:-saas_xbox}
DB_USER=${DB_USER:-root}
DB_PASSWORD=${DB_PASSWORD:-123456}

# 检查 MySQL 是否可用
echo "检查数据库连接..."
mysql -h$DB_HOST -P$DB_PORT -u$DB_USER -p$DB_PASSWORD -e "SELECT 1;" > /dev/null 2>&1
if [ $? -ne 0 ]; then
    echo "错误：无法连接到数据库。请检查数据库配置。"
    echo "配置信息："
    echo "  主机: $DB_HOST"
    echo "  端口: $DB_PORT"
    echo "  数据库: $DB_NAME"
    echo "  用户: $DB_USER"
    exit 1
fi

echo "数据库连接成功！"

# 检查数据库是否存在，如果不存在则创建
echo "检查数据库 $DB_NAME..."
mysql -h$DB_HOST -P$DB_PORT -u$DB_USER -p$DB_PASSWORD -e "CREATE DATABASE IF NOT EXISTS $DB_NAME CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" 2>/dev/null

# 创建必要的表结构（如果不存在）
echo "创建表结构..."
mysql -h$DB_HOST -P$DB_PORT -u$DB_USER -p$DB_PASSWORD $DB_NAME << 'EOF'
-- 员工表
CREATE TABLE IF NOT EXISTS employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_no VARCHAR(50) NOT NULL UNIQUE COMMENT '员工号',
    username VARCHAR(100) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    real_name VARCHAR(100) NOT NULL COMMENT '真实姓名',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    department_id BIGINT COMMENT '部门ID',
    role VARCHAR(50) NOT NULL COMMENT '角色',
    employee_status VARCHAR(20) DEFAULT '正常' COMMENT '员工状态',
    hire_date DATE COMMENT '入职日期',
    avatar VARCHAR(255) COMMENT '头像URL',
    last_login_time DATETIME COMMENT '最后登录时间',
    last_login_ip VARCHAR(45) COMMENT '最后登录IP',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='员工表';

-- 客户表
CREATE TABLE IF NOT EXISTS customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account VARCHAR(100) NOT NULL UNIQUE COMMENT '客户账号',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    customer_name VARCHAR(100) NOT NULL COMMENT '客户名称',
    customer_type VARCHAR(20) DEFAULT '个人' COMMENT '客户类型',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    parent_id BIGINT COMMENT '父账户ID',
    vip_level VARCHAR(20) DEFAULT '普通' COMMENT 'VIP等级',
    balance DECIMAL(10,2) DEFAULT 0.00 COMMENT '账户余额',
    customer_status VARCHAR(20) DEFAULT '正常' COMMENT '客户状态',
    register_source VARCHAR(50) COMMENT '注册来源',
    manager_id BIGINT COMMENT '客户经理ID',
    avatar VARCHAR(255) COMMENT '头像URL',
    last_login_time DATETIME COMMENT '最后登录时间',
    last_login_ip VARCHAR(45) COMMENT '最后登录IP',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户表';

-- 订单表
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_no VARCHAR(50) NOT NULL UNIQUE COMMENT '订单号',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    customer_account VARCHAR(100) NOT NULL COMMENT '客户账号',
    customer_name VARCHAR(100) NOT NULL COMMENT '客户名称',
    manager_id BIGINT COMMENT '客户经理ID',
    product_id BIGINT NOT NULL COMMENT '产品ID',
    product_name VARCHAR(100) NOT NULL COMMENT '产品名称',
    order_status VARCHAR(20) NOT NULL COMMENT '订单状态',
    payment_status VARCHAR(20) DEFAULT '待付款' COMMENT '支付状态',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
    actual_amount DECIMAL(10,2) NOT NULL COMMENT '实际支付金额',
    discount_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '折扣金额',
    country VARCHAR(50) COMMENT '国家/地区',
    city VARCHAR(50) COMMENT '城市',
    ip_quality VARCHAR(20) COMMENT 'IP质量',
    ip_count INT COMMENT 'IP数量',
    duration_days INT NOT NULL COMMENT '购买时长(天)',
    start_date DATE COMMENT '开始日期',
    end_date DATE COMMENT '结束日期',
    ip_addresses JSON COMMENT 'IP地址列表',
    payment_time DATETIME COMMENT '支付时间',
    expire_time DATETIME COMMENT '过期时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';
EOF

if [ $? -eq 0 ]; then
    echo "表结构创建成功！"
else
    echo "警告：表结构创建可能失败，继续执行数据插入..."
fi

# 清理现有测试数据
echo "清理现有测试数据..."
mysql -h$DB_HOST -P$DB_PORT -u$DB_USER -p$DB_PASSWORD $DB_NAME << 'EOF'
DELETE FROM orders WHERE order_no LIKE 'ORD202408%';
DELETE FROM customers WHERE account LIKE 'user00%';
DELETE FROM employees WHERE employee_no LIKE 'EMP00%';
EOF

# 插入测试数据
echo "插入测试数据..."
mysql -h$DB_HOST -P$DB_PORT -u$DB_USER -p$DB_PASSWORD $DB_NAME < "$(dirname "$0")/test-data.sql"

if [ $? -eq 0 ]; then
    echo "✅ 测试数据初始化成功！"
    echo ""
    echo "已添加的测试数据："
    echo "- 5个员工记录 (EMP001-EMP005)"
    echo "- 5个客户记录 (user001-user005)"
    echo "- 5个订单记录 (ORD202408060001-ORD202408060005)"
    echo ""
    echo "测试账号信息："
    echo "员工账号: admin/123456 (超级管理员)"
    echo "员工账号: manager01/123456 (管理员)"
    echo "员工账号: sales01/123456 (客户经理)"
    echo "客户账号: user001/123456"
    echo "客户账号: user002/123456"
    echo ""
    echo "注意：所有账号的默认密码都是 '123456'"
    
    # 查询数据验证
    echo ""
    echo "数据验证："
    echo "员工数量: $(mysql -h$DB_HOST -P$DB_PORT -u$DB_USER -p$DB_PASSWORD $DB_NAME -sN -e 'SELECT COUNT(*) FROM employees WHERE employee_no LIKE "EMP00%"')"
    echo "客户数量: $(mysql -h$DB_HOST -P$DB_PORT -u$DB_USER -p$DB_PASSWORD $DB_NAME -sN -e 'SELECT COUNT(*) FROM customers WHERE account LIKE "user00%"')"
    echo "订单数量: $(mysql -h$DB_HOST -P$DB_PORT -u$DB_USER -p$DB_PASSWORD $DB_NAME -sN -e 'SELECT COUNT(*) FROM orders WHERE order_no LIKE "ORD202408%"')"
    
else
    echo "❌ 测试数据初始化失败！"
    echo "请检查："
    echo "1. 数据库连接配置是否正确"
    echo "2. 数据库用户是否有足够权限"
    echo "3. 表结构是否存在"
    exit 1
fi

echo ""
echo "测试数据初始化完成！"