-- 测试数据脚本
-- 为订单管理、客户管理、员工管理系统添加假数据

-- 1. 员工测试数据
INSERT INTO employees (employee_no, username, password, real_name, phone, email, department_id, role, employee_status, hire_date, avatar, last_login_time, last_login_ip, created_at, updated_at) VALUES
('EMP001', 'admin', '$2a$10$9QHRnPv3u2aV4FXqvE2Q6.WxK8HJhPz3tGTkZXmJwQJm8GK2fLb76', '张三', '13800138001', 'zhangsan@example.com', 1, '超级管理员', '正常', '2024-01-15', NULL, '2024-08-06 10:30:00', '192.168.1.100', NOW(), NOW()),
('EMP002', 'manager01', '$2a$10$9QHRnPv3u2aV4FXqvE2Q6.WxK8HJhPz3tGTkZXmJwQJm8GK2fLb76', '李四', '13800138002', 'lisi@example.com', 2, '管理员', '正常', '2024-02-01', NULL, '2024-08-06 09:15:00', '192.168.1.101', NOW(), NOW()),
('EMP003', 'sales01', '$2a$10$9QHRnPv3u2aV4FXqvE2Q6.WxK8HJhPz3tGTkZXmJwQJm8GK2fLb76', '王五', '13800138003', 'wangwu@example.com', 3, '客户经理', '正常', '2024-03-10', NULL, '2024-08-06 08:45:00', '192.168.1.102', NOW(), NOW()),
('EMP004', 'finance01', '$2a$10$9QHRnPv3u2aV4FXqvE2Q6.WxK8HJhPz3tGTkZXmJwQJm8GK2fLb76', '赵六', '13800138004', 'zhaoliu@example.com', 4, '财务', '正常', '2024-04-20', NULL, '2024-08-05 17:30:00', '192.168.1.103', NOW(), NOW()),
('EMP005', 'staff01', '$2a$10$9QHRnPv3u2aV4FXqvE2Q6.WxK8HJhPz3tGTkZXmJwQJm8GK2fLb76', '孙七', '13800138005', 'sunqi@example.com', 5, '普通员工', '离职', '2024-05-15', NULL, '2024-07-20 16:00:00', '192.168.1.104', NOW(), NOW());

-- 2. 客户测试数据
INSERT INTO customers (account, password, customer_name, customer_type, phone, email, parent_id, vip_level, balance, customer_status, register_source, manager_id, avatar, last_login_time, last_login_ip, created_at, updated_at) VALUES
('user001', '$2a$10$9QHRnPv3u2aV4FXqvE2Q6.WxK8HJhPz3tGTkZXmJwQJm8GK2fLb76', '张小明', '个人', '13900139001', 'zhangxiaoming@email.com', NULL, '普通', 1500.00, '正常', '官网注册', 3, NULL, '2024-08-06 11:20:00', '120.123.234.45', NOW(), NOW()),
('user002', '$2a$10$9QHRnPv3u2aV4FXqvE2Q6.WxK8HJhPz3tGTkZXmJwQJm8GK2fLb76', '李小红', '企业', '13900139002', 'lixiaohong@company.com', NULL, '银牌', 5800.50, '正常', '推广注册', 3, NULL, '2024-08-06 10:45:00', '121.234.123.56', NOW(), NOW()),
('user003', '$2a$10$9QHRnPv3u2aV4FXqvE2Q6.WxK8HJhPz3tGTkZXmJwQJm8GK2fLb76', '王小军', '工作室', '13900139003', 'wangxiaojun@studio.com', NULL, '金牌', 12800.75, '正常', '线下开发', 3, NULL, '2024-08-06 09:30:00', '122.345.678.89', NOW(), NOW()),
('user004', '$2a$10$9QHRnPv3u2aV4FXqvE2Q6.WxK8HJhPz3tGTkZXmJwQJm8GK2fLb76', '刘小华', '个人', '13900139004', 'liuxiaohua@email.com', NULL, '钻石', 25600.00, '冻结', '官网注册', 3, NULL, '2024-08-05 14:20:00', '123.456.789.12', NOW(), NOW()),
('user005', '$2a$10$9QHRnPv3u2aV4FXqvE2Q6.WxK8HJhPz3tGTkZXmJwQJm8GK2fLb76', '陈小丽', '企业', '13900139005', 'chenxiaoli@corp.com', NULL, '普通', 0.00, '黑名单', '推广注册', 3, NULL, '2024-08-04 16:10:00', '124.567.890.23', NOW(), NOW());

-- 3. 订单测试数据
INSERT INTO orders (order_no, customer_id, customer_account, customer_name, manager_id, product_id, product_name, order_status, payment_status, total_amount, actual_amount, discount_amount, country, city, ip_quality, ip_count, duration_days, start_date, end_date, ip_addresses, payment_time, expire_time, created_at, updated_at) VALUES
('ORD202408060001', 1, 'user001', '张小明', 3, 1, '美国标准IP包-月', '正常', '已付款', 199.00, 159.00, 40.00, '美国', '洛杉矶', '标准', 10, 30, '2024-08-01', '2024-08-31', '["192.168.1.10", "192.168.1.11", "192.168.1.12"]', '2024-08-01 10:30:00', '2024-08-31 23:59:59', NOW(), NOW()),
('ORD202408060002', 2, 'user002', '李小红', 3, 2, '香港优质IP包-季', '正常', '已付款', 899.00, 799.00, 100.00, '香港', '香港', '优质', 50, 90, '2024-08-01', '2024-10-30', '["10.0.1.20", "10.0.1.21", "10.0.1.22"]', '2024-08-01 14:20:00', '2024-10-30 23:59:59', NOW(), NOW()),
('ORD202408060003', 3, 'user003', '王小军', 3, 3, '日本独享IP包-年', '已暂停', '已付款', 2999.00, 2499.00, 500.00, '日本', '东京', '独享', 100, 365, '2024-07-01', '2025-06-30', '["172.16.1.30", "172.16.1.31", "172.16.1.32"]', '2024-07-01 09:15:00', '2025-06-30 23:59:59', NOW(), NOW()),
('ORD202408060004', 4, 'user004', '刘小华', 3, 4, '新加坡标准IP包-半年', '人工核验', '待付款', 1399.00, 1399.00, 0.00, '新加坡', '新加坡', '标准', 25, 180, '2024-08-06', '2025-02-02', '[]', NULL, '2025-02-02 23:59:59', NOW(), NOW()),
('ORD202408060005', 5, 'user005', '陈小丽', 3, 5, '英国优质IP包-月', '已过期', '已退款', 299.00, 0.00, 0.00, '英国', '伦敦', '优质', 15, 30, '2024-06-01', '2024-06-30', '["203.0.113.40", "203.0.113.41"]', '2024-06-01 16:45:00', '2024-06-30 23:59:59', NOW(), NOW());

-- 注意：这里的数据基于假设的表结构
-- 实际使用时请根据真实的数据库表结构调整字段名和数据类型
-- 所有的密码都使用了 BCrypt 加密，原始密码为 "123456"