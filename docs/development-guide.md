# 开发指南

## 环境搭建

### 1. Java 21 安装

确保已安装Java 21，可通过以下命令检查：

```bash
java -version
```

### 2. IDE配置

推荐使用IntelliJ IDEA或Eclipse作为开发IDE。

#### IntelliJ IDEA配置

1. 导入项目: File -> Open -> 选择项目根目录
2. 设置Project SDK为Java 21
3. 启用自动导入Maven依赖

## 代码规范

### 1. 包结构规范

- `controller`: REST控制器
- `service`: 业务逻辑层
- `entity`: 实体类
- `dto`: 数据传输对象
- `config`: 配置类
- `util`: 工具类
- `exception`: 异常类

### 2. 命名规范

- 类名：使用大驼峰命名法（PascalCase）
- 方法名：使用小驼峰命名法（camelCase）
- 常量：使用全大写字母，单词间用下划线分隔
- 包名：使用小写字母，多个单词用点分隔

### 3. 注释规范

- 类和方法必须有完整的JavaDoc注释
- 复杂业务逻辑需要添加行内注释
- 接口方法需要说明参数和返回值
