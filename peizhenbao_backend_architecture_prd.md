# 陪诊宝 后端系统开发文档（Backend PRD）

# 一、项目概述

## 项目名称
陪诊宝

## 项目类型
医疗陪诊服务平台后端系统

## 后端目标

为 Flutter Android/iOS 客户端提供：

- 用户认证
- 实名认证
- 医院与科室管理
- 陪诊员管理
- 订单管理
- 支付系统
- 钱包系统
- 消息通知
- 退款售后
- App版本管理

等完整 RESTful API 服务。

---

# 二、推荐技术架构

## 推荐方案（长期稳定）

### 技术栈

- Java 21
- Spring Boot 3.x
- Spring Security
- JWT
- MyBatis Plus
- Redis
- MySQL 8
- RabbitMQ
- MinIO / OSS
- Docker
- Nginx
- Swagger/OpenAPI

---

## 可选方案（开发更快）

- NestJS
- PostgreSQL
- Prisma ORM

---

# 三、系统架构设计

## 架构模式

采用：

- RESTful API
- Clean Architecture
- 分层架构
- RBAC 权限模型

---

## 分层结构

```text
Controller
    ↓
Service
    ↓
Repository / Mapper
    ↓
Database
```

---

# 四、项目目录结构

```text
src/main/java/com/peizhenbao/

├── common/
├── config/
├── security/
├── exception/
├── utils/
├── modules/
│   ├── auth/
│   ├── user/
│   ├── patient/
│   ├── hospital/
│   ├── department/
│   ├── companion/
│   ├── order/
│   ├── payment/
│   ├── wallet/
│   ├── refund/
│   ├── message/
│   ├── upload/
│   └── version/
├── scheduler/
└── PeizhenbaoApplication.java
```

---

# 五、数据库设计

# 1. 用户表 users

```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    phone VARCHAR(20) UNIQUE,
    password VARCHAR(255),
    nickname VARCHAR(50),
    avatar VARCHAR(255),
    real_name VARCHAR(50),
    id_card VARCHAR(30),
    auth_status TINYINT DEFAULT 0,
    balance DECIMAL(10,2) DEFAULT 0,
    status TINYINT DEFAULT 1,
    created_at DATETIME,
    updated_at DATETIME
);
```

---

# 2. 就诊人表 patients

```sql
CREATE TABLE patients (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    name VARCHAR(50),
    gender TINYINT,
    birthday DATE,
    phone VARCHAR(20),
    id_card VARCHAR(30),
    relation_name VARCHAR(50),
    remark TEXT,
    is_default TINYINT DEFAULT 0,
    created_at DATETIME
);
```

---

# 3. 医院表 hospitals

```sql
CREATE TABLE hospitals (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    level_name VARCHAR(50),
    province VARCHAR(50),
    city VARCHAR(50),
    address VARCHAR(255),
    longitude DECIMAL(10,6),
    latitude DECIMAL(10,6),
    status TINYINT DEFAULT 1,
    created_at DATETIME
);
```

---

# 4. 科室表 departments

```sql
CREATE TABLE departments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    hospital_id BIGINT,
    name VARCHAR(100),
    description TEXT,
    status TINYINT DEFAULT 1,
    created_at DATETIME
);
```

---

# 5. 陪诊员表 companions

```sql
CREATE TABLE companions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50),
    avatar VARCHAR(255),
    gender TINYINT,
    phone VARCHAR(20),
    intro TEXT,
    score DECIMAL(3,2) DEFAULT 5.0,
    service_count INT DEFAULT 0,
    price DECIMAL(10,2),
    status TINYINT DEFAULT 1,
    created_at DATETIME
);
```

---

# 6. 订单表 orders

```sql
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(50) UNIQUE,
    user_id BIGINT,
    patient_id BIGINT,
    hospital_id BIGINT,
    department_id BIGINT,
    companion_id BIGINT,
    appointment_date DATE,
    appointment_time VARCHAR(50),
    service_content TEXT,
    notice_content TEXT,
    amount DECIMAL(10,2),
    pay_status TINYINT DEFAULT 0,
    order_status TINYINT DEFAULT 0,
    created_at DATETIME,
    updated_at DATETIME
);
```

---

# 7. 支付记录表 payments

```sql
CREATE TABLE payments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT,
    pay_no VARCHAR(100),
    pay_type VARCHAR(20),
    pay_amount DECIMAL(10,2),
    pay_status TINYINT DEFAULT 0,
    transaction_id VARCHAR(100),
    created_at DATETIME
);
```

---

# 8. 钱包流水表 wallet_logs

```sql
CREATE TABLE wallet_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    type VARCHAR(20),
    amount DECIMAL(10,2),
    balance DECIMAL(10,2),
    remark VARCHAR(255),
    created_at DATETIME
);
```

---

# 9. 退款表 refunds

```sql
CREATE TABLE refunds (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT,
    user_id BIGINT,
    refund_no VARCHAR(100),
    refund_amount DECIMAL(10,2),
    reason TEXT,
    status TINYINT DEFAULT 0,
    created_at DATETIME
);
```

---

# 10. 消息表 messages

```sql
CREATE TABLE messages (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    title VARCHAR(255),
    content TEXT,
    is_read TINYINT DEFAULT 0,
    created_at DATETIME
);
```

---

# 六、用户认证模块

## 登录方式

支持：

- 手机号验证码登录
- 手机号密码登录
- 微信登录
- Apple 登录

---

## JWT认证

Token机制：

- AccessToken
- RefreshToken

---

## 密码加密

使用：

- BCryptPasswordEncoder

---

# 七、实名认证模块

## 功能

用户实名认证。

---

## 上传内容

- 身份证正面
- 身份证反面

---

## 状态

- 未认证
- 审核中
- 已认证
- 驳回

---

# 八、医院与科室模块

## 功能

后台维护医院与科室。

客户端通过接口获取。

---

## 医院接口

### 获取医院列表

```http
GET /api/hospitals
```

支持：

- 分页
- 搜索
- 城市筛选

---

## 科室接口

```http
GET /api/departments
```

---

# 九、陪诊员模块

## 功能

- 陪诊员列表
- 陪诊员详情
- 陪诊员评价
- 收藏陪诊员

---

# 十、订单模块

## 创建订单流程

1. 选择医院
2. 选择科室
3. 选择陪诊员
4. 选择时间
5. 提交订单
6. 支付订单

---

## 订单状态

```text
0 待支付
1 已支付
2 待接单
3 已接单
4 服务中
5 已完成
6 已取消
7 已退款
8 售后处理中
```

---

## 自动任务

需要定时任务：

- 自动取消超时未支付订单
- 自动关闭售后
- 自动完成订单

---

# 十一、支付模块

## 第一阶段支付方式

- 微信支付
- 支付宝支付
- 余额支付

---

## 支付流程

### 用户下单

生成支付订单。

### 返回支付参数

客户端调起支付。

### 支付回调

第三方回调后：

- 修改支付状态
- 修改订单状态
- 发送消息通知
- 写入钱包流水

---

# 十二、退款模块

## 用户退款

用户可提交退款申请。

---

## 平台审核

后台审核退款。

---

## 自动退款

支持：

- 微信原路退款
- 支付宝原路退款

---

# 十三、钱包模块

## 功能

- 余额查询
- 充值
- 提现
- 消费记录

---

## 钱包逻辑

订单支付时：

- 扣除余额
- 写入流水
- 更新订单状态

---

# 十四、消息通知模块

## 消息类型

- 系统通知
- 订单通知
- 支付通知
- 退款通知

---

## 推送方式

- App Push
- WebSocket
- 短信通知

---

# 十五、文件上传模块

## 支持上传

- 用户头像
- 身份证图片
- 退款凭证

---

## 文件存储

推荐：

- MinIO
- 阿里云 OSS
- 腾讯云 COS

---

# 十六、App版本管理模块

## 功能

- 发布新版本
- 强制更新
- 更新日志

---

## 接口

```http
GET /api/version/latest
```

---

# 十七、Redis设计

## 用途

- Token缓存
- 验证码缓存
- 热门医院缓存
- 订单锁
- 防重复提交

---

# 十八、RabbitMQ设计

## 用途

- 异步发送消息
- 支付回调处理
- 自动取消订单
- 推送通知

---

# 十九、Swagger接口文档

必须支持：

- Swagger UI
- OpenAPI 3

接口统一：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

---

# 二十、统一异常处理

## 全局异常处理

包括：

- 参数异常
- 登录失效
- 权限不足
- 业务异常
- 文件上传异常
- 支付异常

---

# 二十一、安全要求

## 必须支持

- JWT认证
- HTTPS
- 防SQL注入
- XSS过滤
- 接口限流
- 防重复提交
- 敏感数据脱敏

---

# 二十二、日志系统

## 使用

- Logback

---

## 日志分类

- 请求日志
- 错误日志
- 支付日志
- 登录日志
- 操作日志

---

# 二十三、Docker部署

## 必须支持

- Dockerfile
- Docker Compose
- 多环境配置

---

## 环境

- dev
- test
- prod

---

# 二十四、Nginx配置

## 支持

- HTTPS
- 反向代理
- 文件上传
- Gzip压缩

---

# 二十五、后台管理系统（后续）

后续需要独立后台系统：

## 模块

- 用户管理
- 陪诊员管理
- 医院管理
- 科室管理
- 订单管理
- 财务管理
- 退款审核
- 消息管理
- Banner管理
- App版本管理

---

# 二十六、后端开发规范

## 代码规范

- Controller 不写业务逻辑
- Service 层处理业务
- DTO/VO 分离
- 统一返回对象
- 统一异常处理
- 接口参数校验

---

## 命名规范

- 驼峰命名
- RESTful 风格
- 模块化开发

---

# 二十七、MVP阶段优先开发

第一阶段：

1. 登录注册
2. JWT认证
3. 实名认证
4. 就诊人管理
5. 医院与科室
6. 陪诊员列表
7. 创建订单
8. 支付系统
9. 钱包系统
10. 消息通知
11. App版本管理

---

# 二十八、未来扩展方向

第二阶段：

- 陪诊员端
- 实时聊天
- 实时定位
- AI客服
- AI推荐
- 智能派单
- 视频问诊
- 优惠券系统
- 积分系统
- 分销系统

---

# 二十九、高并发与高可用架构要求

# 1. 高并发要求

系统必须支持高并发访问。

目标：

- 支持大规模用户同时在线
- 支持高峰期订单创建
- 支持高并发支付回调
- 支持高并发消息通知
- 支持订单并发控制

---

## 核心方案

### Redis缓存

用于：

- 热门医院缓存
- Token缓存
- 验证码缓存
- 陪诊员列表缓存
- 用户信息缓存

要求：

- 所有热点数据必须缓存
- 支持缓存过期机制
- 支持缓存更新策略

---

## MQ异步削峰

使用 RabbitMQ。

场景：

- 支付回调
- 短信发送
- 消息推送
- 自动取消订单
- 自动退款通知

要求：

- 消息可靠投递
- 消息重试机制
- 死信队列
- 消费幂等处理

---

## 数据库优化

要求：

- 所有高频字段建立索引
- 避免全表扫描
- 分页查询优化
- 慢SQL监控
- 支持读写分离
- 支持后期分库分表

---

## 分布式锁

使用 Redis 分布式锁。

场景：

- 防止重复下单
- 防止重复支付
- 防止重复退款
- 防止订单状态冲突

---

## 接口限流

要求：

- 防止恶意请求
- 防止短信接口刷取
- 防止支付接口攻击

建议：

- Redis + Lua
- Sentinel

---

# 2. 高可用要求

系统必须支持：

- 服务高可用
- 接口容错
- 自动恢复
- 灾难恢复

---

## 服务容错

要求：

- 接口超时控制
- 熔断机制
- 降级机制
- 重试机制

建议：

- Resilience4j

---

## 异常恢复

要求：

- MQ失败重试
- 支付回调补偿
- 订单状态补偿
- 定时任务补偿

---

## 文件存储高可用

支持：

- OSS
- COS
- S3
- MinIO

要求：

- CDN加速
- 文件防盗链
- 自动备份

---

# 3. 并发安全要求

系统必须保证：

- 数据一致性
- 订单一致性
- 支付一致性
- 钱包余额一致性

---

## 必须支持

- 乐观锁
- 幂等处理
- 事务管理
- 分布式事务预留

---

## 幂等场景

以下接口必须保证幂等：

- 支付回调
- 创建订单
- 余额扣款
- 退款接口
- 提现接口

---

# 三十、代码开发规范

# 1. 开发规范

必须遵循：

- Clean Code
- SOLID原则
- DDD思想（预留）
- RESTful 风格
- 分层架构
- 模块化开发

---

# 2. Controller规范

Controller层：

- 不允许写业务逻辑
- 只负责参数接收
- 只负责结果返回
- 参数统一校验

---

# 3. Service规范

Service层：

- 处理所有业务逻辑
- 事务统一管理
- 禁止直接返回Entity
- 必须使用DTO/VO

---

# 4. Repository规范

要求：

- SQL统一管理
- 避免重复SQL
- 禁止拼接危险SQL

---

# 5. DTO/VO规范

必须区分：

- DTO
- VO
- Entity
- Request
- Response

禁止直接暴露数据库实体。

---

# 6. API规范

统一：

- RESTful 风格
- JSON格式
- 统一状态码
- 统一错误码

---

## 返回格式

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

---

# 7. 异常处理规范

必须支持：

- 全局异常处理
- 统一错误码
- 统一错误日志
- 参数异常处理
- 业务异常处理

---

# 8. 日志规范

必须记录：

- 登录日志
- 请求日志
- 支付日志
- 订单日志
- 错误日志

要求：

- TraceId链路追踪
- 日志分级
- 敏感数据脱敏

---

# 9. 安全开发规范

必须支持：

- JWT认证
- HTTPS
- XSS过滤
- SQL注入防护
- CSRF防护
- 文件上传校验
- 接口签名（预留）

---

# 10. Git规范

分支规范：

```text
main
release
develop
feature/*
hotfix/*
```

---

## Commit规范

```text
feat: 新功能
fix: 修复Bug
refactor: 重构
perf: 性能优化
docs: 文档更新
style: 代码格式
```

---

# 11. 单元测试规范

要求：

- Service层必须可测试
- 核心逻辑必须测试
- 支付逻辑必须测试
- 订单逻辑必须测试

建议覆盖率：

- 70%以上

---

# 12. AI Studio 开发要求（重要）

AI Studio 生成代码时必须：

- 严格模块化
- 严格分层
- 严格遵循Clean Architecture
- 所有接口必须可扩展
- 所有模块低耦合
- 所有异常统一处理
- 所有接口支持并发安全
- 所有支付逻辑支持幂等
- 所有订单逻辑支持事务
- 所有代码必须可维护
- 所有代码必须支持生产环境

---

# 三十一、最终目标

构建一个：

构建一个：

- 商业级
- 高并发
- 高可扩展
- 安全稳定
- 支持长期运营

的医疗陪诊服务平台后端系统。

