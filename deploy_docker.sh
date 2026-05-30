#!/bin/bash
# 陪诊宝 - Docker化一键部署脚本

echo "🚀 开始构建并部署陪诊宝后端 (Docker 模式)..."

# 1. 编译打包 (跳过测试以加快速度)
echo "📦 正在执行 Maven 打包..."
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Maven 打包失败，请检查代码！"
    exit 1
fi

echo "✅ 打包成功，准备构建并重启容器..."

# 2. 使用 Docker Compose 构建镜像并重启服务
# -d 后台运行, --build 强制重新构建镜像
docker-compose up -d --build pzb-backend

if [ $? -ne 0 ]; then
    echo "❌ Docker 部署失败！"
    exit 1
fi

echo "🎉 部署完成！后端服务正在容器中启动。"
echo "可以使用 'docker logs -f pzb-backend' 查看实时运行日志。"
