package com.peizhenbao.modules.wallet.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("wallet_logs")
public class WalletLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String type; // RECHARGE, CONSUME, REFUND
    private BigDecimal amount;
    private BigDecimal balance;
    private String remark;
    private LocalDateTime createdAt;
}
