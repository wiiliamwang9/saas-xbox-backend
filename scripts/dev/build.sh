#!/bin/bash

echo "构建SaaS Platform项目..."

# 清理并编译
mvn clean compile

# 运行测试
mvn test

# 打包
mvn package -DskipTests

echo "构建完成！"
