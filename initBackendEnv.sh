#!/bin/bash

# SaaS系统Java后端初始化脚本
# 专注于Spring Boot项目的基础设置，不包含MySQL配置
# Author: Assistant
# Version: 2.0

set -e  # 遇到错误立即退出

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 日志函数
log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

log_step() {
    echo -e "${BLUE}[STEP]${NC} $1"
}

# 检查当前用户权限
check_user() {
    if [[ $EUID -eq 0 ]]; then
        log_warn "当前使用root用户运行脚本，建议使用普通用户以避免权限问题"
        read -p "是否继续使用root用户? (y/N): " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            exit 1
        fi
    fi
    log_info "当前用户: $(whoami)"
}

# 检测操作系统类型
detect_os() {
    if [[ -f /etc/os-release ]]; then
        . /etc/os-release
        OS=$NAME
        VER=$VERSION_ID
    elif type lsb_release >/dev/null 2>&1; then
        OS=$(lsb_release -si)
        VER=$(lsb_release -sr)
    else
        OS=$(uname -s)
        VER=$(uname -r)
    fi
    
    log_info "检测到操作系统: $OS $VER"
}

# 检查基础工具
check_basic_tools() {
    log_step "检查基础工具..."
    
    tools=("curl" "wget" "unzip" "git")
    missing_tools=()
    
    for tool in "${tools[@]}"; do
        if ! command -v $tool &> /dev/null; then
            missing_tools+=($tool)
        fi
    done
    
    if [ ${#missing_tools[@]} -eq 0 ]; then
        log_info "所有基础工具已安装"
    else
        log_warn "缺少工具: ${missing_tools[*]}"
        log_info "请先安装缺少的工具，或联系系统管理员"
        read -p "是否继续安装? (y/N): " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            exit 1
        fi
    fi
}

# 安装Java 21
install_java() {
    log_step "安装Java 21..."
    
    # 检查Java是否已安装
    if command -v java &> /dev/null; then
        JAVA_VERSION=$(java -version 2>&1 | head -n1 | awk -F '"' '{print $2}')
        log_info "检测到已安装Java版本: $JAVA_VERSION"
        
        # 检查是否为Java 21
        if [[ $JAVA_VERSION == 21.* ]]; then
            log_info "Java 21已安装，跳过安装步骤"
            return 0
        fi
        
        read -p "是否重新安装Java 21? (y/N): " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            return 0
        fi
    fi
    
    # 创建Java安装目录
    JAVA_HOME_DIR="$HOME/java"
    mkdir -p $JAVA_HOME_DIR
    
    # 下载并安装OpenJDK 21
    log_info "下载OpenJDK 21..."
    cd /tmp
    
    # 根据系统架构选择下载链接
    ARCH=$(uname -m)
    if [[ "$ARCH" == "x86_64" ]]; then
        JDK_URL="https://download.java.net/java/GA/jdk21.0.2/f2283984656d49d69e91c558476027ac/13/GPL/openjdk-21.0.2_linux-x64_bin.tar.gz"
        JDK_FILE="openjdk-21.0.2_linux-x64_bin.tar.gz"
    elif [[ "$ARCH" == "aarch64" ]]; then
        JDK_URL="https://download.java.net/java/GA/jdk21.0.2/f2283984656d49d69e91c558476027ac/13/GPL/openjdk-21.0.2_linux-aarch64_bin.tar.gz"
        JDK_FILE="openjdk-21.0.2_linux-aarch64_bin.tar.gz"
    else
        log_error "不支持的系统架构: $ARCH"
        exit 1
    fi
    
    wget $JDK_URL
    tar -xzf $JDK_FILE -C $JAVA_HOME_DIR --strip-components=1
    
    # 设置JAVA_HOME环境变量
    echo "" >> ~/.bashrc
    echo "# Java 21 Environment" >> ~/.bashrc
    echo "export JAVA_HOME=$JAVA_HOME_DIR" >> ~/.bashrc
    echo "export PATH=\$JAVA_HOME/bin:\$PATH" >> ~/.bashrc
    
    # 立即生效
    export JAVA_HOME=$JAVA_HOME_DIR
    export PATH=$JAVA_HOME/bin:$PATH
    
    log_info "Java 21安装完成"
    java -version
}

# 安装Maven
install_maven() {
    log_step "安装Maven..."
    
    # 检查Maven是否已安装
    if command -v mvn &> /dev/null; then
        MVN_VERSION=$(mvn -version | head -n1)
        log_info "检测到已安装Maven: $MVN_VERSION"
        read -p "是否重新安装Maven? (y/N): " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            return 0
        fi
    fi
    
    # 下载并安装Maven 3.9.6
    MAVEN_VERSION="3.9.6"
    MAVEN_URL="https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz"
    MAVEN_HOME_DIR="$HOME/maven"
    
    mkdir -p $MAVEN_HOME_DIR
    cd /tmp
    wget $MAVEN_URL
    tar xzf apache-maven-${MAVEN_VERSION}-bin.tar.gz -C $MAVEN_HOME_DIR --strip-components=1
    
    # 设置Maven环境变量
    echo "" >> ~/.bashrc
    echo "# Maven Environment" >> ~/.bashrc
    echo "export M2_HOME=$MAVEN_HOME_DIR" >> ~/.bashrc
    echo "export MAVEN_HOME=$MAVEN_HOME_DIR" >> ~/.bashrc
    echo "export PATH=\$M2_HOME/bin:\$PATH" >> ~/.bashrc
    
    # 立即生效
    export M2_HOME=$MAVEN_HOME_DIR
    export MAVEN_HOME=$MAVEN_HOME_DIR
    export PATH=$M2_HOME/bin:$PATH
    
    log_info "Maven安装完成"
    mvn -version
}

# 创建项目结构
create_project_structure() {
    log_step "创建SaaS项目结构..."
    
    read -p "请输入项目名称 (默认: saas-xbox-backend): " PROJECT_NAME
    PROJECT_NAME=${PROJECT_NAME:-saas-xbox-backend}
    
    read -p "请输入包名 (默认: com.saas.platform): " PACKAGE_NAME
    PACKAGE_NAME=${PACKAGE_NAME:-com.saas.platform}
    
    # 转换包名为目录结构
    PACKAGE_PATH=$(echo $PACKAGE_NAME | tr '.' '/')
    
    # 设置项目根目录路径（设为全局变量）
    export PROJECT_ROOT="$HOME/wl/code/$PROJECT_NAME"
    export PROJECT_NAME
    export PACKAGE_NAME
    export PACKAGE_PATH
    
    # 创建项目根目录
    mkdir -p "$PROJECT_ROOT"
    cd "$PROJECT_ROOT"
    
    log_info "项目将创建在: $PROJECT_ROOT"
    
    # 创建标准Maven项目结构
    mkdir -p src/main/java/$PACKAGE_PATH
    mkdir -p src/main/resources
    mkdir -p src/main/resources/static
    mkdir -p src/main/resources/templates
    mkdir -p src/test/java/$PACKAGE_PATH
    mkdir -p src/test/resources
    
    # 创建子模块目录结构
    mkdir -p src/main/java/$PACKAGE_PATH/{config,controller,service,entity,dto,util,exception}
    
    # 创建配置文件目录
    mkdir -p src/main/resources/config
    mkdir -p scripts
    mkdir -p docs
    
    log_info "项目结构创建完成: $PROJECT_NAME"
}

# 创建pom.xml
create_pom_xml() {
    log_step "创建pom.xml文件..."
    
cat > pom.xml << EOF
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.3.0</version>
        <relativePath/>
    </parent>

    <groupId>$PACKAGE_NAME</groupId>
    <artifactId>$PROJECT_NAME</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <name>$PROJECT_NAME</name>
    <description>SaaS Platform Backend Service</description>
    <packaging>jar</packaging>

    <properties>
        <java.version>21</java.version>
        <hutool.version>5.8.28</hutool.version>
    </properties>

    <dependencies>
        <!-- Spring Boot Starters -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <!-- JSON Processing -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-core</artifactId>
        </dependency>

        <!-- Utilities -->
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>\${hutool.version}</version>
        </dependency>

        <!-- Test Dependencies -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.13.0</version>
                <configuration>
                    <source>21</source>
                    <target>21</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
EOF

    log_info "pom.xml文件创建完成"
}

# 创建Spring Boot主类
create_main_class() {
    log_step "创建Spring Boot主类..."
    
    MAIN_CLASS_DIR="src/main/java/$PACKAGE_PATH"
    CLASS_NAME=$(echo $PROJECT_NAME | sed 's/-//g' | sed 's/\b\w/\U&/g')Application
    
cat > $MAIN_CLASS_DIR/${CLASS_NAME}.java << EOF
package $PACKAGE_NAME;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SaaS Platform Application
 * 
 * @author System Generator
 * @version 1.0.0
 */
@SpringBootApplication
public class ${CLASS_NAME} {
    
    public static void main(String[] args) {
        SpringApplication.run(${CLASS_NAME}.class, args);
    }
}
EOF

    log_info "主类创建完成: ${CLASS_NAME}.java"
}

# 创建配置文件
create_config_files() {
    log_step "创建配置文件..."
    
    # application.yml
cat > src/main/resources/application.yml << EOF
server:
  port: 8080
  servlet:
    context-path: /api
    encoding:
      charset: UTF-8
      enabled: true

spring:
  application:
    name: $PROJECT_NAME
  
  profiles:
    active: dev
  
  jackson:
    time-zone: GMT+8
    date-format: yyyy-MM-dd HH:mm:ss
    serialization:
      write-dates-as-timestamps: false

# 日志配置
logging:
  level:
    root: INFO
    $PACKAGE_NAME: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n"

# 监控配置
management:
  endpoints:
    web:
      exposure:
        include: health,info
  endpoint:
    health:
      show-details: when_authorized
EOF

    # application-dev.yml
cat > src/main/resources/application-dev.yml << EOF
server:
  port: 8080

logging:
  level:
    root: DEBUG
    $PACKAGE_NAME: DEBUG
EOF

    log_info "配置文件创建完成"
}

# 创建基础代码结构
create_basic_code_structure() {
    log_step "创建基础代码结构..."
    
    # 创建通用响应类
cat > src/main/java/$PACKAGE_PATH/dto/Result.java << EOF
package $PACKAGE_NAME.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;

/**
 * 统一响应结果
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer code;
    private String message;
    private T data;
    private Long timestamp;
    
    public Result() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public Result(Integer code, String message) {
        this();
        this.code = code;
        this.message = message;
    }
    
    public Result(Integer code, String message, T data) {
        this(code, message);
        this.data = data;
    }
    
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功");
    }
    
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }
    
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }
    
    public static <T> Result<T> error() {
        return new Result<>(500, "操作失败");
    }
    
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message);
    }
    
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message);
    }
    
    // Getters and Setters
    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
    
    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
}
EOF

    # 创建全局异常处理器
cat > src/main/java/$PACKAGE_PATH/exception/GlobalExceptionHandler.java << EOF
package $PACKAGE_NAME.exception;

import $PACKAGE_NAME.dto.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e) {
        logger.error("系统异常", e);
        return Result.error("系统异常，请联系管理员");
    }
    
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleValidationException(Exception e) {
        logger.error("参数校验异常", e);
        return Result.error(400, "参数校验失败");
    }
    
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleBusinessException(BusinessException e) {
        logger.error("业务异常: {}", e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }
}
EOF

    # 创建业务异常类
cat > src/main/java/$PACKAGE_PATH/exception/BusinessException.java << EOF
package $PACKAGE_NAME.exception;

/**
 * 业务异常
 */
public class BusinessException extends RuntimeException {
    
    private Integer code;
    
    public BusinessException(String message) {
        super(message);
        this.code = 400;
    }
    
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
    
    public Integer getCode() {
        return code;
    }
}
EOF

    # 创建健康检查控制器
cat > src/main/java/$PACKAGE_PATH/controller/HealthController.java << EOF
package $PACKAGE_NAME.controller;

import $PACKAGE_NAME.dto.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查控制器
 */
@RestController
@RequestMapping("/health")
public class HealthController {
    
    @GetMapping
    public Result<Map<String, Object>> health() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "UP");
        data.put("timestamp", LocalDateTime.now());
        data.put("service", "SaaS Platform Backend");
        data.put("version", "1.0.0");
        
        return Result.success("系统运行正常", data);
    }
}
EOF

    log_info "基础代码结构创建完成"
}

# 创建开发环境配置
create_dev_environment() {
    log_step "创建开发环境配置..."
    
    # 创建本地开发脚本目录
    mkdir -p scripts/dev
    
    # 创建启动脚本
cat > scripts/dev/start.sh << 'EOF'
#!/bin/bash

echo "启动SaaS Platform开发环境..."

# 检查Java环境
if ! command -v java &> /dev/null; then
    echo "错误: 未找到Java环境"
    exit 1
fi

# 检查Maven环境
if ! command -v mvn &> /dev/null; then
    echo "错误: 未找到Maven环境"
    exit 1
fi

# 设置开发环境变量
export SPRING_PROFILES_ACTIVE=dev

# 启动应用
echo "正在启动应用..."
mvn spring-boot:run
EOF

    chmod +x scripts/dev/start.sh
    
    # 创建构建脚本
cat > scripts/dev/build.sh << 'EOF'
#!/bin/bash

echo "构建SaaS Platform项目..."

# 清理并编译
mvn clean compile

# 运行测试
mvn test

# 打包
mvn package -DskipTests

echo "构建完成！"
EOF

    chmod +x scripts/dev/build.sh
    
    log_info "开发环境配置创建完成"
}

# 创建README文档
create_readme_and_docs() {
    log_step "创建项目文档..."
    
    # 创建README.md
cat > README.md << EOF
# SaaS Platform Backend

一个基于Spring Boot 3.3.0 + Java 21的现代化SaaS平台后端服务。

## 技术栈

- **Java**: 21
- **Spring Boot**: 3.3.0
- **Maven**: 3.9.6

## 项目结构

\`\`\`
$PROJECT_NAME/
├── src/
│   ├── main/
│   │   ├── java/$PACKAGE_PATH/
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
\`\`\`

## 快速开始

### 1. 环境准备

- Java 21
- Maven 3.9+

### 2. 启动应用

\`\`\`bash
# 使用Maven启动
./scripts/dev/start.sh

# 或者直接使用Maven命令
mvn spring-boot:run
\`\`\`

### 3. 访问应用

- 应用地址: http://localhost:8080/api
- 健康检查: http://localhost:8080/api/health

## 开发指南

### 构建项目

\`\`\`bash
./scripts/dev/build.sh
\`\`\`

### 配置文件说明

- \`application.yml\`: 主配置文件
- \`application-dev.yml\`: 开发环境配置

### API接口规范

所有API返回格式统一使用 \`Result<T>\` 包装：

\`\`\`json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": 1703000000000
}
\`\`\`

## 部署说明

### 生产环境部署

1. 构建JAR包: \`mvn clean package -DskipTests\`
2. 启动应用: \`java -jar target/*.jar --spring.profiles.active=dev\`

## 贡献指南

1. Fork 项目
2. 创建特性分支
3. 提交更改
4. 推送到分支
5. 创建 Pull Request

## 许可证

MIT License
EOF

    # 创建开发指南
cat > docs/development-guide.md << EOF
# 开发指南

## 环境搭建

### 1. Java 21 安装

确保已安装Java 21，可通过以下命令检查：

\`\`\`bash
java -version
\`\`\`

### 2. IDE配置

推荐使用IntelliJ IDEA或Eclipse作为开发IDE。

#### IntelliJ IDEA配置

1. 导入项目: File -> Open -> 选择项目根目录
2. 设置Project SDK为Java 21
3. 启用自动导入Maven依赖

## 代码规范

### 1. 包结构规范

- \`controller\`: REST控制器
- \`service\`: 业务逻辑层
- \`entity\`: 实体类
- \`dto\`: 数据传输对象
- \`config\`: 配置类
- \`util\`: 工具类
- \`exception\`: 异常类

### 2. 命名规范

- 类名：使用大驼峰命名法（PascalCase）
- 方法名：使用小驼峰命名法（camelCase）
- 常量：使用全大写字母，单词间用下划线分隔
- 包名：使用小写字母，多个单词用点分隔

### 3. 注释规范

- 类和方法必须有完整的JavaDoc注释
- 复杂业务逻辑需要添加行内注释
- 接口方法需要说明参数和返回值
EOF

    log_info "项目文档创建完成"
}

# 主函数
main() {
    log_info "开始初始化SaaS平台后端项目..."
    
    # 检查用户权限
    check_user
    
    # 检测操作系统
    detect_os
    
    # 检查基础工具
    check_basic_tools
    
    # 安装Java 21
    install_java
    
    # 安装Maven
    install_maven
    
    # 创建项目结构
    create_project_structure
    
    # 创建pom.xml
    create_pom_xml
    
    # 创建Spring Boot主类
    create_main_class
    
    # 创建配置文件
    create_config_files
    
    # 创建基础代码结构
    create_basic_code_structure
    
    # 创建开发环境配置
    create_dev_environment
    
    # 创建文档
    create_readme_and_docs
    
    log_info "项目初始化完成！"
    log_info "项目位置: $PROJECT_ROOT"
    log_info "启动命令: cd $PROJECT_ROOT && ./scripts/dev/start.sh"
    log_info "构建命令: cd $PROJECT_ROOT && ./scripts/dev/build.sh"
}

# 执行主函数
main "$@"