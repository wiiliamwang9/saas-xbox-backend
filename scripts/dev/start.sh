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
