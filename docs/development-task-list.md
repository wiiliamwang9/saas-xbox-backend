# SaaS静态住宅IP平台 - 开发任务清单

## 第一阶段：基础架构搭建 (Week 1-2)

### Week 1: 后端基础架构

#### 1.1 项目初始化 ✅
- [x] Spring Boot项目搭建
- [x] Maven配置和依赖管理
- [x] 项目目录结构规划

#### 1.2 数据库环境搭建
- [ ] MySQL 8.0环境部署
- [ ] Redis 6.0环境部署  
- [ ] 数据库表结构创建脚本
- [ ] 初始化数据脚本
- [ ] 数据库连接配置

**SQL脚本位置**: `/root/wl/code/saas-xbox-backend/sql/`
```bash
mkdir -p /root/wl/code/saas-xbox-backend/sql
# 创建以下SQL文件:
# - 01_create_database.sql
# - 02_create_tables.sql  
# - 03_create_indexes.sql
# - 04_insert_initial_data.sql
```

#### 1.3 基础架构代码
- [ ] 统一响应结果类 `Result<T>` ✅
- [ ] 全局异常处理器 ✅
- [ ] 业务异常类 ✅
- [ ] 基础实体类 `BaseEntity`
- [ ] 工具类：`DateUtils`, `StringUtils`, `ValidationUtils`
- [ ] 常量类：`Constants`, `ErrorCodes`

#### 1.4 配置管理
- [ ] 多环境配置 (dev/test/prod)
- [ ] 数据库配置
- [ ] Redis配置
- [ ] 日志配置 (Logback)
- [ ] 跨域配置
- [ ] Swagger API文档配置

### Week 2: 认证授权和基础服务

#### 2.1 认证授权模块
- [ ] JWT工具类开发
- [ ] 用户认证服务 (`AuthService`)
- [ ] 权限控制注解 (`@RequirePermission`)
- [ ] 登录拦截器 (`AuthInterceptor`)
- [ ] 权限校验切面 (`PermissionAspect`)

**实现文件**:
```
src/main/java/com/saas/platform/
├── auth/
│   ├── JwtTokenProvider.java
│   ├── AuthService.java
│   ├── AuthController.java
│   └── AuthInterceptor.java
├── security/
│   ├── SecurityConfig.java
│   ├── PermissionAspect.java
│   └── RequirePermission.java
```

#### 2.2 用户管理模块
- [ ] 用户实体类 (`User`, `Admin`, `Role`)
- [ ] 用户Repository (`UserRepository`)
- [ ] 用户服务类 (`UserService`)
- [ ] 用户管理API接口 (`UserController`)
- [ ] 密码加密和验证
- [ ] 用户状态管理

#### 2.3 系统配置模块
- [ ] 系统配置实体 (`SystemConfig`)
- [ ] 配置管理服务 (`ConfigService`)
- [ ] 配置缓存机制
- [ ] 配置管理API

#### 2.4 基础工具服务
- [ ] 文件上传服务 (`FileUploadService`)
- [ ] 邮件发送服务 (`EmailService`)
- [ ] 短信发送服务 (`SmsService`)
- [ ] 验证码服务 (`CaptchaService`)

## 第二阶段：核心业务功能 (Week 3-5)

### Week 3: 产品和IP池管理

#### 3.1 产品管理模块
- [ ] 产品实体类 (`Product`)
- [ ] 产品Repository和Service
- [ ] 产品管理API
  - [ ] `GET /api/v1/admin/products` - 产品列表
  - [ ] `POST /api/v1/admin/products` - 创建产品
  - [ ] `PUT /api/v1/admin/products/{id}` - 更新产品
  - [ ] `DELETE /api/v1/admin/products/{id}` - 删除产品
  - [ ] `GET /api/v1/products` - 客户端产品列表
- [ ] 产品分类管理
- [ ] 产品库存管理
- [ ] 产品定价策略

#### 3.2 IP池管理模块
- [ ] IP池实体类 (`IpPool`, `IpAddress`)
- [ ] IP池Repository和Service
- [ ] IP池管理API
  - [ ] `GET /api/v1/admin/ip-pools` - IP池列表
  - [ ] `POST /api/v1/admin/ip-pools` - 创建IP池
  - [ ] `GET /api/v1/admin/ip-pools/{id}/ips` - IP地址列表
  - [ ] `POST /api/v1/admin/ip-addresses` - 添加IP地址
  - [ ] `POST /api/v1/admin/ip-pools/{id}/test-quality` - 质量检测
- [ ] IP质量检测服务
- [ ] IP分配算法
- [ ] IP状态管理

#### 3.3 Xbox系统对接基础
- [ ] Xbox配置类 (`XboxConfig`)
- [ ] Xbox API客户端 (`XboxApiClient`)
- [ ] Xbox数据模型映射
- [ ] 节点信息同步服务 (`NodeSyncService`)
- [ ] 基础对接测试

**对接配置**:
```yaml
xbox:
  controller:
    url: http://localhost:8080
    timeout: 30000
  sync:
    enabled: true
    interval: 60000  # 60秒同步一次
```

### Week 4: 订单和支付系统

#### 4.1 订单管理模块
- [ ] 订单实体类 (`Order`, `OrderItem`)
- [ ] 订单Repository和Service  
- [ ] 订单状态机 (`OrderStateMachine`)
- [ ] 订单管理API
  - [ ] `POST /api/v1/orders` - 创建订单
  - [ ] `GET /api/v1/orders` - 用户订单列表
  - [ ] `GET /api/v1/orders/{id}` - 订单详情
  - [ ] `POST /api/v1/orders/{id}/cancel` - 取消订单
  - [ ] `GET /api/v1/admin/orders` - 管理端订单列表
- [ ] 订单自动确认逻辑
- [ ] 订单统计报表

#### 4.2 支付系统模块
- [ ] 支付实体类 (`Payment`)
- [ ] 支付服务抽象接口 (`PaymentService`)
- [ ] 支付宝集成 (`AlipayService`)
- [ ] 微信支付集成 (`WechatPayService`) 
- [ ] 余额支付 (`BalancePayService`)
- [ ] 支付回调处理
- [ ] 支付API接口
  - [ ] `POST /api/v1/orders/{id}/pay` - 发起支付
  - [ ] `POST /api/v1/payments/alipay/callback` - 支付宝回调
  - [ ] `POST /api/v1/payments/wechat/callback` - 微信回调

#### 4.3 财务管理模块
- [ ] 财务记录实体 (`FinancialRecord`)
- [ ] 财务服务 (`FinancialService`)
- [ ] 用户余额管理
- [ ] 财务统计API
- [ ] 对账功能

### Week 5: 服务实例和节点管理

#### 5.1 服务实例管理
- [ ] 服务实例实体 (`ServiceInstance`)
- [ ] 服务开通逻辑 (`ServiceProvisioningService`)
- [ ] IP资源分配服务 (`IpAllocationService`)
- [ ] 服务实例API
  - [ ] `GET /api/v1/services` - 用户服务列表
  - [ ] `GET /api/v1/services/{id}` - 服务详情
  - [ ] `POST /api/v1/services/{id}/reset-auth` - 重置认证
  - [ ] `GET /api/v1/services/{id}/stats` - 使用统计
- [ ] 服务状态监控
- [ ] 自动续费逻辑

#### 5.2 节点管理模块
- [ ] 节点实体类 (`Node`)
- [ ] 节点监控服务 (`NodeMonitorService`)
- [ ] Xbox API集成完善
- [ ] 节点管理API
  - [ ] `GET /api/v1/admin/nodes` - 节点列表
  - [ ] `GET /api/v1/admin/nodes/{id}` - 节点详情
  - [ ] `GET /api/v1/admin/nodes/{id}/metrics` - 监控数据
  - [ ] `POST /api/v1/admin/nodes/sync-xbox` - 同步Xbox数据
  - [ ] `POST /api/v1/admin/nodes/{id}/restart` - 重启节点
- [ ] 节点告警机制
- [ ] 负载均衡算法

#### 5.3 定时任务调度
- [ ] Xbox数据同步定时任务
- [ ] 节点监控数据收集
- [ ] 订单状态检查任务
- [ ] 服务到期检查任务
- [ ] 系统健康检查任务

```java
@Component
public class ScheduledTasks {
    
    @Scheduled(fixedRate = 60000) // 每分钟
    public void syncXboxNodes() {}
    
    @Scheduled(fixedRate = 300000) // 每5分钟  
    public void collectMetrics() {}
    
    @Scheduled(cron = "0 0 1 * * ?") // 每天凌晨1点
    public void checkExpiredServices() {}
}
```

## 第三阶段：客户端功能 (Week 6-7)

### Week 6: 前端管理界面

#### 6.1 管理端页面开发
- [ ] 登录页面优化
- [ ] 仪表板页面数据对接
- [ ] 产品管理页面
  - [ ] 产品列表页面
  - [ ] 产品编辑页面
  - [ ] 产品详情页面
- [ ] IP池管理页面
  - [ ] IP池列表页面
  - [ ] IP地址管理页面
  - [ ] 质量检测页面
- [ ] 订单管理页面
  - [ ] 订单列表页面
  - [ ] 订单详情页面
  - [ ] 订单统计页面

#### 6.2 节点管理界面
- [ ] 节点列表页面
- [ ] 节点详情页面
- [ ] 节点监控图表
- [ ] 节点操作页面
- [ ] 告警管理页面

#### 6.3 管理功能界面
- [ ] 用户管理页面
- [ ] 角色权限管理
- [ ] 系统配置页面
- [ ] 操作日志页面
- [ ] 财务管理页面

### Week 7: 客户端界面

#### 7.1 客户端基础页面
- [ ] 用户注册页面
- [ ] 用户登录页面
- [ ] 个人中心页面
- [ ] 账户设置页面

#### 7.2 产品和订单页面
- [ ] 产品展示页面
- [ ] 购买流程页面
- [ ] 支付页面集成
- [ ] 订单历史页面
- [ ] 服务管理页面

#### 7.3 支持功能页面
- [ ] 工单系统页面
- [ ] 反馈建议页面
- [ ] 帮助文档页面
- [ ] 使用统计页面

## 第四阶段：高级功能和优化 (Week 8-9)

### Week 8: 高级功能开发

#### 8.1 工单系统
- [ ] 工单实体类 (`Ticket`, `TicketReply`)
- [ ] 工单服务 (`TicketService`)
- [ ] 工单API开发
- [ ] 工单分配逻辑
- [ ] 邮件通知集成

#### 8.2 营销管理
- [ ] 优惠券系统 (`Coupon`)
- [ ] 营销活动管理 (`Campaign`)
- [ ] 邀请码系统 (`InviteCode`)
- [ ] 分销系统 (可选)

#### 8.3 员工管理
- [ ] 员工权限细化
- [ ] 部门管理
- [ ] 操作日志审计
- [ ] 绩效统计

#### 8.4 系统监控
- [ ] 系统指标收集
- [ ] 告警通知机制
- [ ] 日志聚合分析
- [ ] 性能监控大盘

### Week 9: 测试和部署

#### 9.1 测试完善
- [ ] 单元测试补充
- [ ] 集成测试
- [ ] 接口测试
- [ ] 性能测试
- [ ] 安全测试

#### 9.2 部署准备
- [ ] Docker镜像构建
- [ ] Kubernetes部署配置
- [ ] 数据库迁移脚本
- [ ] 监控配置
- [ ] 日志配置

#### 9.3 上线准备
- [ ] 生产环境配置
- [ ] 域名和SSL证书
- [ ] CDN配置
- [ ] 备份策略
- [ ] 应急预案

## 开发规范和工具

### 代码规范
```bash
# 代码格式化
mvn spotless:apply

# 代码检查
mvn checkstyle:check

# 测试覆盖率
mvn jacoco:report
```

### Git提交规范
```bash
# 功能开发
git commit -m "feat: 添加用户注册功能"

# 问题修复  
git commit -m "fix: 修复订单支付回调问题"

# 重构
git commit -m "refactor: 重构IP池分配算法"

# 文档
git commit -m "docs: 更新API文档"
```

### 分支管理
```bash
main          # 主分支 (生产环境)
develop       # 开发分支 (测试环境)
feature/*     # 功能分支
hotfix/*      # 热修复分支
release/*     # 发布分支
```

### 开发工具推荐
- **IDE**: IntelliJ IDEA Ultimate
- **数据库**: DBeaver / Navicat
- **API测试**: Postman / Insomnia  
- **版本控制**: Git + GitKraken
- **项目管理**: Jira / Trello
- **文档**: Notion / Confluence

## 质量检查清单

### 每周检查项
- [ ] 代码审查完成
- [ ] 单元测试通过
- [ ] API文档更新
- [ ] 数据库变更记录
- [ ] 性能指标达标
- [ ] 安全检查通过

### 每阶段检查项
- [ ] 功能需求完成度
- [ ] 用户体验测试
- [ ] 兼容性测试
- [ ] 压力测试
- [ ] 安全渗透测试
- [ ] 部署文档完善

## 项目依赖管理

### 核心依赖版本
```xml
<properties>
    <spring-boot.version>3.3.0</spring-boot.version>
    <mysql.version>8.0.33</mysql.version>
    <redis.version>3.3.0</redis.version>
    <jwt.version>0.12.5</jwt.version>
    <swagger.version>2.2.0</swagger.version>
    <hutool.version>5.8.28</hutool.version>
</properties>
```

### 开发工具依赖
```xml
<dependencies>
    <!-- 开发工具 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
    </dependency>
    
    <!-- 配置处理 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-configuration-processor</artifactId>
    </dependency>
    
    <!-- 测试框架 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## 总结

本开发任务清单按照项目架构设计文档制定，涵盖了从基础架构到完整功能的所有开发任务。建议按照周计划逐步执行，每周进行进度检查和质量评估，确保项目按时高质量交付。

**下一步行动**:
1. 创建项目Git仓库
2. 搭建开发环境
3. 开始Week 1的任务执行
4. 建立每日站会机制
5. 设置项目管理工具