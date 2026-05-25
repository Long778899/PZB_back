-- 陪诊宝数据库初始化脚本
CREATE DATABASE IF NOT EXISTS `peizhenbao` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `peizhenbao`;

-- 1. 用户表 users
CREATE TABLE IF NOT EXISTS `users` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `phone` VARCHAR(20) UNIQUE COMMENT '手机号',
    `password` VARCHAR(255) COMMENT '密码',
    `nickname` VARCHAR(50) COMMENT '昵称',
    `avatar` VARCHAR(255) COMMENT '头像',
    `real_name` VARCHAR(50) COMMENT '真实姓名',
    `id_card` VARCHAR(30) COMMENT '身份证号',
    `auth_status` TINYINT DEFAULT 0 COMMENT '认证状态: 0未认证 1审核中 2已认证 3驳回',
    `balance` DECIMAL(10,2) DEFAULT 0.00 COMMENT '钱包余额',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0禁用 1正常',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 就诊人表 patients
CREATE TABLE IF NOT EXISTS `patients` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '所属用户ID',
    `name` VARCHAR(50) NOT NULL COMMENT '就诊人姓名',
    `gender` TINYINT COMMENT '性别: 1男 2女',
    `birthday` DATE COMMENT '出生日期',
    `phone` VARCHAR(20) COMMENT '联系电话',
    `id_card` VARCHAR(30) COMMENT '身份证号',
    `relation_name` VARCHAR(50) COMMENT '与用户关系',
    `remark` TEXT COMMENT '备注（如过敏史等）',
    `is_default` TINYINT DEFAULT 0 COMMENT '是否默认: 0否 1是',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='就诊人表';

-- 3. 医院表 hospitals
CREATE TABLE IF NOT EXISTS `hospitals` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(255) NOT NULL COMMENT '医院名称',
    `level_name` VARCHAR(50) COMMENT '医院等级（如三甲）',
    `province` VARCHAR(50) COMMENT '省份',
    `city` VARCHAR(50) COMMENT '城市',
    `address` VARCHAR(255) COMMENT '详细地址',
    `longitude` DECIMAL(10,6) COMMENT '经度',
    `latitude` DECIMAL(10,6) COMMENT '纬度',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0下线 1上线',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_city` (`city`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医院表';

-- 4. 科室表 departments
CREATE TABLE IF NOT EXISTS `departments` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `hospital_id` BIGINT NOT NULL COMMENT '所属医院ID',
    `name` VARCHAR(100) NOT NULL COMMENT '科室名称',
    `description` TEXT COMMENT '科室描述',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0下线 1上线',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_hospital_id` (`hospital_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科室表';

-- 5. 陪诊员表 companions
CREATE TABLE IF NOT EXISTS `companions` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `avatar` VARCHAR(255) COMMENT '头像',
    `gender` TINYINT COMMENT '性别: 1男 2女',
    `phone` VARCHAR(20) UNIQUE COMMENT '手机号',
    `intro` TEXT COMMENT '自我介绍',
    `score` DECIMAL(3,2) DEFAULT 5.00 COMMENT '综合评分',
    `service_count` INT DEFAULT 0 COMMENT '服务次数',
    `price` DECIMAL(10,2) NOT NULL COMMENT '基础服务价格',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0休息 1接单中 2已禁用',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='陪诊员表';

-- 6. 订单表 orders
CREATE TABLE IF NOT EXISTS `orders` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `order_no` VARCHAR(50) UNIQUE NOT NULL COMMENT '订单编号',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `patient_id` BIGINT NOT NULL COMMENT '就诊人ID',
    `hospital_id` BIGINT NOT NULL COMMENT '医院ID',
    `department_id` BIGINT NOT NULL COMMENT '科室ID',
    `companion_id` BIGINT NOT NULL COMMENT '陪诊员ID',
    `appointment_date` DATE NOT NULL COMMENT '预约日期',
    `appointment_time` VARCHAR(50) NOT NULL COMMENT '预约时间段',
    `service_content` TEXT COMMENT '服务内容',
    `notice_content` TEXT COMMENT '注意事项',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
    `pay_status` TINYINT DEFAULT 0 COMMENT '支付状态: 0待支付 1已支付 2已退款',
    `order_status` TINYINT DEFAULT 0 COMMENT '订单状态: 0待支付 1已支付待接单 2待接单 3已接单 4服务中 5已完成 6已取消 7已退款 8售后处理中',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_companion_id` (`companion_id`),
    INDEX `idx_order_status` (`order_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 7. 支付记录表 payments
CREATE TABLE IF NOT EXISTS `payments` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `order_id` BIGINT NOT NULL COMMENT '订单ID',
    `pay_no` VARCHAR(100) UNIQUE NOT NULL COMMENT '支付流水号',
    `pay_type` VARCHAR(20) NOT NULL COMMENT '支付方式: WX(微信) ALI(支付宝) BALANCE(余额)',
    `pay_amount` DECIMAL(10,2) NOT NULL COMMENT '支付金额',
    `pay_status` TINYINT DEFAULT 0 COMMENT '状态: 0支付中 1支付成功 2支付失败',
    `transaction_id` VARCHAR(100) COMMENT '第三方交易凭证号',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付记录表';

-- 8. 钱包流水表 wallet_logs
CREATE TABLE IF NOT EXISTS `wallet_logs` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `type` VARCHAR(20) NOT NULL COMMENT '类型: RECHARGE(充值) CONSUME(消费) REFUND(退款) WITHDRAW(提现)',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '变动金额(可正可负)',
    `balance` DECIMAL(10,2) NOT NULL COMMENT '变动后余额',
    `remark` VARCHAR(255) COMMENT '备注',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='钱包流水表';

-- 9. 退款表 refunds
CREATE TABLE IF NOT EXISTS `refunds` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `order_id` BIGINT NOT NULL COMMENT '订单ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `refund_no` VARCHAR(100) UNIQUE NOT NULL COMMENT '退款单号',
    `refund_amount` DECIMAL(10,2) NOT NULL COMMENT '退款金额',
    `reason` TEXT COMMENT '退款原因',
    `status` TINYINT DEFAULT 0 COMMENT '状态: 0申请中 1同意退款 2拒绝退款 3退款成功',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退款表';

-- 10. 消息表 messages
CREATE TABLE IF NOT EXISTS `messages` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '接收用户ID',
    `title` VARCHAR(255) NOT NULL COMMENT '消息标题',
    `content` TEXT COMMENT '消息内容',
    `is_read` TINYINT DEFAULT 0 COMMENT '是否已读: 0未读 1已读',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_user_id_read` (`user_id`, `is_read`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息表';
