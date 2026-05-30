package com.peizhenbao.modules.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Order {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    private Long userId;
    private Long patientId;
    private Long hospitalId;
    private Long departmentId;
    private Long companionId;
    
    // 高并发架构反范式冗余字段
    private Integer dispatchType; // 1=抢单 2=指定派单
    private String patientName;
    private String patientPhone;
    private String hospitalName;
    private String departmentName;
    private String companionName;
    
    private LocalDate appointmentDate;
    private String appointmentTime;
    private String serviceContent;
    private String noticeContent;
    private BigDecimal amount;
    private Integer payStatus; // 0待支付 1已支付 2已退款
    private Integer orderStatus; // 0待支付 1已支付待接单 2待接单 3已接单 4服务中 5已完成 6已取消 7已退款 8售后处理中
    
    private BigDecimal platformFee; // 平台抽成
    private BigDecimal companionIncome; // 陪诊员预期收益
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
