package com.peizhenbao.modules.payment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("payments")
public class Payment {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orderId;
    private String payNo;
    private String payType; // WX, ALI, BALANCE
    private BigDecimal payAmount;
    private Integer payStatus; // 0支付中 1成功 2失败
    private String transactionId;
    private LocalDateTime createdAt;
}
