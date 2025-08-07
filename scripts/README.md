# 测试数据初始化说明

## 概述

本目录包含用于初始化SaaS Xbox平台测试数据的脚本，为订单管理、客户管理和员工管理系统提供假数据进行功能测试。

## 文件说明

- `test-data.sql` - 包含所有测试数据的SQL插入语句
- `init-test-data.sh` - 自动化初始化脚本，包含表结构创建和数据插入
- `README.md` - 本说明文件

## 测试数据包含内容

### 1. 员工数据 (5条记录)
- **EMP001** - 张三 (超级管理员) - admin/123456
- **EMP002** - 李四 (管理员) - manager01/123456
- **EMP003** - 王五 (客户经理) - sales01/123456
- **EMP004** - 赵六 (财务) - finance01/123456
- **EMP005** - 孙七 (普通员工) - staff01/123456 [已离职]

### 2. 客户数据 (5条记录)
- **user001** - 张小明 (个人客户，普通VIP，余额1500元)
- **user002** - 李小红 (企业客户，银牌VIP，余额5800.5元)
- **user003** - 王小军 (工作室客户，金牌VIP，余额12800.75元)
- **user004** - 刘小华 (个人客户，钻石VIP，余额25600元) [已冻结]
- **user005** - 陈小丽 (企业客户，普通VIP，余额0元) [黑名单]

### 3. 订单数据 (5条记录)
- **ORD202408060001** - 张小明的美国标准IP包月订单 (正常)
- **ORD202408060002** - 李小红的香港优质IP包季订单 (正常)
- **ORD202408060003** - 王小军的日本独享IP包年订单 (已暂停)
- **ORD202408060004** - 刘小华的新加坡标准IP包半年订单 (人工核验)
- **ORD202408060005** - 陈小丽的英国优质IP包月订单 (已过期)

## 使用方法

### 方法一：自动化脚本（推荐）

1. 确保MySQL服务正在运行
2. 配置数据库连接参数（可选）：
   ```bash
   export DB_HOST=localhost
   export DB_PORT=3306
   export DB_NAME=saas_xbox
   export DB_USER=root
   export DB_PASSWORD=your_password
   ```
3. 运行初始化脚本：
   ```bash
   cd /path/to/saas-xbox-backend
   ./scripts/init-test-data.sh
   ```

### 方法二：手动执行SQL

1. 连接到MySQL数据库
2. 创建数据库（如果不存在）：
   ```sql
   CREATE DATABASE IF NOT EXISTS saas_xbox CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   USE saas_xbox;
   ```
3. 确保表结构存在（参考init-test-data.sh中的CREATE TABLE语句）
4. 执行测试数据SQL：
   ```bash
   mysql -u root -p saas_xbox < scripts/test-data.sql
   ```

## 测试场景

这些测试数据支持以下测试场景：

### 员工管理测试
- ✅ 按员工号搜索 (EMP001-EMP005)
- ✅ 按用户名搜索 (admin, manager01, sales01等)
- ✅ 按真实姓名搜索 (张三, 李四, 王五等)
- ✅ 按手机号搜索 (138001380xx系列)
- ✅ 按邮箱搜索 (@example.com域名)
- ✅ 按角色筛选 (超级管理员, 管理员, 客户经理, 财务, 普通员工)
- ✅ 按状态筛选 (正常, 离职)

### 客户管理测试
- ✅ 按客户账号搜索 (user001-user005)
- ✅ 按客户名称搜索 (张小明, 李小红等)
- ✅ 按手机号搜索 (139001390xx系列)
- ✅ 按邮箱搜索 (不同域名邮箱)
- ✅ 按客户类型筛选 (个人, 企业, 工作室)
- ✅ 按VIP等级筛选 (普通, 银牌, 金牌, 钻石)
- ✅ 按状态筛选 (正常, 冻结, 黑名单)
- ✅ 按注册来源筛选 (官网注册, 推广注册, 线下开发)

### 订单管理测试
- ✅ 按订单号搜索 (ORD202408060xxx)
- ✅ 按客户账号搜索 (user00x)
- ✅ 按客户名称搜索
- ✅ 按订单状态筛选 (正常, 已暂停, 已过期, 人工核验)
- ✅ 按支付状态筛选 (已付款, 待付款, 已退款)
- ✅ 按国家筛选 (美国, 香港, 日本, 新加坡, 英国)
- ✅ 按IP质量筛选 (标准, 优质, 独享)
- ✅ 按时间范围筛选
- ✅ 按IP地址搜索

## 注意事项

1. **密码安全**：所有测试账号的密码都是 `123456`，已经过BCrypt加密存储
2. **数据清理**：脚本运行前会自动清理现有的测试数据
3. **表结构**：脚本会自动创建必要的表结构，但建议在正式环境中先手动验证
4. **权限要求**：数据库用户需要有CREATE、INSERT、DELETE权限
5. **字符集**：使用utf8mb4字符集，支持emoji和特殊字符

## 故障排除

### 常见问题

1. **数据库连接失败**
   - 检查MySQL服务是否启动
   - 验证数据库连接参数
   - 确认数据库用户权限

2. **表结构错误**
   - 检查现有表结构是否与脚本兼容
   - 手动创建表结构后再插入数据

3. **数据插入失败**
   - 检查是否有重复的唯一索引冲突
   - 验证数据格式是否正确

### 验证数据插入成功

```sql
-- 检查员工数据
SELECT COUNT(*) as employee_count FROM employees WHERE employee_no LIKE 'EMP00%';

-- 检查客户数据
SELECT COUNT(*) as customer_count FROM customers WHERE account LIKE 'user00%';

-- 检查订单数据
SELECT COUNT(*) as order_count FROM orders WHERE order_no LIKE 'ORD202408%';
```

## 扩展测试数据

如需添加更多测试数据，可以：
1. 修改 `test-data.sql` 文件添加更多INSERT语句
2. 保持数据的一致性和关联性
3. 使用不同的业务场景数据进行全面测试

---

**创建时间**: 2024-08-06  
**适用版本**: SaaS Xbox Platform v1.0  
**维护者**: SaaS Xbox Team