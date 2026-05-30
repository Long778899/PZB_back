package com.peizhenbao.modules.wallet.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("wallet_transactions")
public class WalletTransaction {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long companionId;
    private Long orderId;
    private BigDecimal amount; // 金额，正数收入，负数支出
    private Integer type; // 1=订单分润 2=提现支出 3=违规扣款
    private LocalDateTime createdAt;
}
