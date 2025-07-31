# SaaS Platform Backend

一个基于Spring Boot 3.3.0 + Java 21的现代化SaaS平台后端服务。

## 技术栈

- **Java**: 21
- **Spring Boot**: 3.3.0
- **Maven**: 3.9.6

## 项目结构

```
saas-xbox-backend/
├── src/
│   ├── main/
│   │   ├── java/com/saas/platform/
│   │   │   ├── config/          # 配置类
│   │   │   ├── controller/      # 控制器
│   │   │   ├── service/         # 服务层
│   │   │   ├── entity/          # 实体类
│   │   │   ├── dto/             # 数据传输对象
│   │   │   ├── util/            # 工具类
│   │   │   └── exception/       # 异常处理
│   │   └── resources/
│   │       ├── application.yml  # 主配置文件
│   │       └── application-dev.yml  # 开发环境配置
│   └── test/
├── scripts/
│   └── dev/
│       ├── start.sh            # 启动脚本
│       └── build.sh            # 构建脚本
├── docs/                       # 项目文档
├── pom.xml                     # Maven配置
└── README.md                   # 项目说明
```

## 快速开始

### 1. 环境准备

- Java 21
- Maven 3.9+

### 2. 启动应用

```bash
# 使用Maven启动
./scripts/dev/start.sh

# 或者直接使用Maven命令
mvn spring-boot:run
```

### 3. 访问应用

- 应用地址: http://localhost:8080/api
- 健康检查: http://localhost:8080/api/health

## 开发指南

### 构建项目

```bash
./scripts/dev/build.sh
```

### 配置文件说明

- `application.yml`: 主配置文件
- `application-dev.yml`: 开发环境配置

### API接口规范

所有API返回格式统一使用 `Result<T>` 包装：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": 1703000000000
}
```

## 部署说明

### 生产环境部署

1. 构建JAR包: `mvn clean package -DskipTests`
2. 启动应用: `java -jar target/*.jar --spring.profiles.active=dev`

## 贡献指南

1. Fork 项目
2. 创建特性分支
3. 提交更改
4. 推送到分支
5. 创建 Pull Request

## 许可证

MIT License
