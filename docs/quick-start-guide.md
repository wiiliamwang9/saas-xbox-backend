# SaaS静态住宅IP平台 - 快速启动指南

## 项目概述

本项目是一个基于Spring Boot的SaaS静态住宅IP代理服务平台，集成Xbox节点控制器系统，为用户提供专业的IP代理服务。

## 环境要求

### 开发环境
- **Java**: 21+
- **Maven**: 3.9+
- **MySQL**: 8.0+
- **Redis**: 6.0+
- **Node.js**: 18+ (前端开发)
- **Git**: 2.30+

### 开发工具推荐
- **IDE**: IntelliJ IDEA Ultimate 2024+
- **数据库工具**: DBeaver / Navicat
- **API测试**: Postman
- **版本控制**: Git + SourceTree/GitKraken

## 快速开始

### 1. 项目初始化

#### 1.1 克隆项目
```bash
cd ~/wl/code
git clone <repository-url> saas-xbox-backend
cd saas-xbox-backend
```

#### 1.2 运行初始化脚本
```bash
# 如果还没有运行过初始化脚本
./initBackendEnv.sh
# 选择默认项目名称: saas-xbox-backend
# 选择默认包名: com.saas.platform
```

### 2. 数据库配置

#### 2.1 创建数据库
```sql
-- 连接MySQL (使用root用户)
mysql -u root -p

-- 创建数据库
CREATE DATABASE saas_xbox DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户 (可选)
CREATE USER 'saas_user'@'localhost' IDENTIFIED BY 'saas_password';
GRANT ALL PRIVILEGES ON saas_xbox.* TO 'saas_user'@'localhost';
FLUSH PRIVILEGES;
```

#### 2.2 配置数据库连接
编辑 `src/main/resources/application-dev.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/saas_xbox?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8
    username: saas_user
    password: saas_password
    driver-class-name: com.mysql.cj.jdbc.Driver
    
  # JPA配置
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
        format_sql: true
```

#### 2.3 配置Redis连接
```yaml
spring:
  redis:
    host: localhost
    port: 6379
    database: 0
    timeout: 5000
    password:  # 如果有密码请填写
    jedis:
      pool:
        max-active: 20
        max-wait: -1
        max-idle: 10
        min-idle: 0
```

### 3. 项目依赖配置

#### 3.1 更新pom.xml
在现有基础上添加以下依赖：

```xml
<dependencies>
    <!-- 现有依赖保持不变 -->
    
    <!-- 数据库相关 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    
    <!-- 安全和认证 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.12.5</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.12.5</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.12.5</version>
        <scope>runtime</scope>
    </dependency>
    
    <!-- API文档 -->
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.2.0</version>
    </dependency>
    
    <!-- 工具库 -->
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-lang3</artifactId>
    </dependency>
    <dependency>
        <groupId>commons-codec</groupId>
        <artifactId>commons-codec</artifactId>
    </dependency>
    
    <!-- 开发工具 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-configuration-processor</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

### 4. 项目结构调整

#### 4.1 创建标准目录结构
```bash
mkdir -p src/main/java/com/saas/platform/{auth,config,security}
mkdir -p src/main/java/com/saas/platform/{entity,repository,service}
mkdir -p src/main/java/com/saas/platform/{dto/request,dto/response}
mkdir -p src/main/java/com/saas/platform/constant
mkdir -p src/main/resources/{sql,config}
mkdir -p src/test/java/com/saas/platform
```

#### 4.2 项目结构说明
```
src/main/java/com/saas/platform/
├── SaasxboxbackendApplication.java    # ✅ 已存在
├── auth/                               # 认证授权
│   ├── AuthController.java
│   ├── AuthService.java
│   └── JwtTokenProvider.java
├── config/                             # ✅ 已存在 - 配置类
│   ├── SecurityConfig.java
│   ├── RedisConfig.java
│   └── SwaggerConfig.java
├── constant/                           # 常量定义
│   ├── Constants.java
│   └── ErrorCodes.java
├── controller/                         # ✅ 已存在 - 控制器
│   ├── HealthController.java          # ✅ 已存在
│   ├── AdminController.java
│   └── UserController.java
├── dto/                               # ✅ 已存在 - DTO
│   ├── Result.java                    # ✅ 已存在
│   ├── request/
│   │   ├── LoginRequest.java
│   │   └── RegisterRequest.java
│   └── response/
│       ├── LoginResponse.java
│       └── UserInfoResponse.java
├── entity/                            # ✅ 已存在 - 实体类
│   ├── BaseEntity.java
│   ├── User.java
│   └── Admin.java
├── exception/                         # ✅ 已存在 - 异常处理
│   ├── BusinessException.java         # ✅ 已存在
│   └── GlobalExceptionHandler.java    # ✅ 已存在
├── repository/                        # 数据访问层
│   ├── UserRepository.java
│   └── AdminRepository.java
├── security/                          # 安全配置
│   ├── AuthInterceptor.java
│   └── PermissionAspect.java
├── service/                           # ✅ 已存在 - 服务层
│   ├── UserService.java
│   └── AdminService.java
└── util/                              # ✅ 已存在 - 工具类
    ├── DateUtils.java
    ├── StringUtils.java
    └── ValidationUtils.java
```

### 5. 核心类开发

#### 5.1 基础实体类
创建 `src/main/java/com/saas/platform/entity/BaseEntity.java`:
```java
package com.saas.platform.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public LocalDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }
    
    public boolean isDeleted() { return deletedAt != null; }
}
```

#### 5.2 用户实体类
创建 `src/main/java/com/saas/platform/entity/User.java`:
```java
package com.saas.platform.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User extends BaseEntity {
    
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;
    
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(name = "phone", length = 20)
    private String phone;
    
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
    
    @Column(name = "real_name", length = 50)
    private String realName;
    
    @Column(name = "user_type", nullable = false)
    private Integer userType = 1; // 1-普通用户，2-VIP用户，3-企业用户
    
    @Column(name = "status", nullable = false)
    private Integer status = 1; // 0-禁用，1-正常，2-待验证
    
    @Column(name = "balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;
    
    @Column(name = "avatar_url")
    private String avatarUrl;
    
    @Column(name = "register_ip", length = 45)
    private String registerIp;
    
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
    
    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    
    public Integer getUserType() { return userType; }
    public void setUserType(Integer userType) { this.userType = userType; }
    
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    
    public String getRegisterIp() { return registerIp; }
    public void setRegisterIp(String registerIp) { this.registerIp = registerIp; }
    
    public LocalDateTime getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; }
}
```

#### 5.3 JWT工具类
创建 `src/main/java/com/saas/platform/auth/JwtTokenProvider.java`:
```java
package com.saas.platform.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {
    
    @Value("${jwt.secret:mySecretKey123456789012345678901234567890}")
    private String jwtSecret;
    
    @Value("${jwt.expiration:86400000}") // 24小时
    private long jwtExpirationMs;
    
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
    
    public String generateToken(Long userId, String username) {
        Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationMs);
        
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("username", username)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }
    
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }
    
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("username", String.class);
    }
    
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
```

### 6. 运行和测试

#### 6.1 启动应用
```bash
# 使用Maven启动
mvn spring-boot:run

# 或使用开发脚本
./scripts/dev/start.sh
```

#### 6.2 验证启动成功
访问以下URL验证应用启动：
- 健康检查: http://localhost:8080/api/health
- API文档: http://localhost:8080/swagger-ui.html
- 应用首页: http://localhost:8080/api

#### 6.3 初始化数据
```bash
# 如果使用JPA自动建表，表会自动创建
# 如果需要手动执行SQL，运行以下命令
mysql -u saas_user -p saas_xbox < src/main/resources/sql/init.sql
```

### 7. 开发流程

#### 7.1 Git工作流
```bash
# 创建功能分支
git checkout -b feature/user-management

# 提交代码
git add .
git commit -m "feat: 添加用户管理基础功能"

# 推送到远程
git push origin feature/user-management

# 创建Pull Request进行代码审查
```

#### 7.2 开发规范
- 每个功能模块独立分支开发
- 遵循RESTful API设计规范
- 统一使用Result<T>包装响应数据
- 所有API添加Swagger文档注解
- 编写单元测试和集成测试

#### 7.3 测试规范
```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=UserServiceTest

# 生成测试覆盖率报告
mvn jacoco:report
```

### 8. 常见问题解决

#### 8.1 数据库连接问题
```yaml
# 如果遇到时区问题，在URL中添加时区参数
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/saas_xbox?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true
```

#### 8.2 Redis连接问题
```bash
# 启动Redis服务
redis-server

# 或使用Docker启动
docker run -d --name redis -p 6379:6379 redis:6-alpine
```

#### 8.3 端口冲突问题
```yaml
# 修改应用端口
server:
  port: 8081  # 改为其他可用端口
```

### 9. 下一步开发计划

完成快速启动后，请按照以下顺序进行开发：

1. **完善用户管理模块** (Day 1-2)
   - UserRepository和UserService实现
   - 用户注册/登录API
   - 密码加密和验证

2. **实现认证授权** (Day 3-4)
   - JWT令牌生成和验证
   - 登录拦截器
   - 权限控制注解

3. **开发基础管理功能** (Day 5-7)
   - 系统配置管理
   - 操作日志记录
   - 健康检查完善

4. **开始核心业务开发** (Week 2)
   - 按照development-task-list.md执行

### 10. 联系和支持

如遇到技术问题，请按以下方式寻求帮助：

1. **查看文档**: 先查看项目文档和架构设计
2. **搜索Issues**: 查看项目Issue列表
3. **团队沟通**: 通过团队沟通工具求助
4. **技术调研**: Google/Stack Overflow

---

**祝开发顺利！** 🚀

记住：好的开始是成功的一半。认真阅读文档，规范开发流程，注重代码质量，相信我们能够构建出一个优秀的SaaS平台！