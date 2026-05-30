# 使用官方的 OpenJDK 21 作为基础镜像 (与 pom.xml 中的 java.version 匹配)
FROM eclipse-temurin:21-jdk-alpine

# 设置工作目录
WORKDIR /app

# 声明构建参数，默认指向 target 下的 jar
ARG JAR_FILE=target/*.jar

# 将构建好的 jar 包复制到镜像内部，并重命名为 app.jar
COPY ${JAR_FILE} app.jar

# 暴露端口，与 application.yml 中的 server.port 保持一致
EXPOSE 8080

# 启动参数，添加一些 JVM 优化参数以适应容器环境
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]
