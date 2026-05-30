#!/bin/bash

# ==========================================
# 陪诊宝 (peizhenbao) 后端自动化部署脚本
# 建议将此脚本放在服务器的项目根目录下
# ==========================================

# 设置一些变量
PROJECT_DIR=$(pwd)
APP_NAME="peizhenbao-backend"
JAR_NAME="peizhenbao-backend-0.0.1-SNAPSHOT.jar"
TARGET_DIR="$PROJECT_DIR/target"
LOG_FILE="$PROJECT_DIR/app.log"

# 带颜色的日志输出函数
function log_info() {
    echo -e "\n\033[32m[INFO] $(date '+%Y-%m-%d %H:%M:%S') - $1 \033[0m"
}

function log_error() {
    echo -e "\n\033[31m[ERROR] $(date '+%Y-%m-%d %H:%M:%S') - $1 \033[0m"
}

log_info "========== 开始自动化部署流程 =========="

# 1. 拉取最新代码
log_info "1. 正在从 Git 仓库拉取最新代码..."
git pull origin main
if [ $? -ne 0 ]; then
    log_error "代码拉取失败，请检查网络或 Git 配置！"
    exit 1
fi

# 2. 编译打包项目
log_info "2. 开始使用 Maven 编译并打包项目..."
mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    log_error "Maven 打包失败，请检查代码是否有编译错误！"
    exit 1
fi

# 3. 查找并结束旧的 Java 进程
log_info "3. 正在查找正在运行的老应用进程..."
PID=$(ps -ef | grep "$JAR_NAME" | grep -v grep | awk '{print $2}')

if [ -z "$PID" ]; then
    log_info "未发现运行中的 $APP_NAME 进程。"
else
    log_info "发现运行中的进程 PID=$PID，准备停止..."
    kill -9 $PID
    log_info "旧进程已成功停止。"
fi

# 4. 启动新应用
log_info "4. 准备启动新版本..."
cd "$TARGET_DIR" || exit

# 使用 nohup 后台启动，将日志输出到项目根目录的 app.log 中
nohup java -jar "$JAR_NAME" > "$LOG_FILE" 2>&1 &

NEW_PID=$!
log_info "项目启动成功！新的进程 PID 为: $NEW_PID"

# 5. 提示查看日志
log_info "========== 部署流程结束 =========="
log_info "您可以随时使用以下命令查看应用的实时运行日志："
echo -e "\033[36m    tail -f $LOG_FILE \033[0m\n"
