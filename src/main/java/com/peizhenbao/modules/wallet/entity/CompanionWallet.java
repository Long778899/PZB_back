package com.peizhenbao.modules.wallet.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("companion_wallets")
public class CompanionWallet {
    @TableId
    private Long companionId; // 陪诊员ID，同时作为主键
    private BigDecimal balance; // 可提现余额
    private BigDecimal frozenBalance; // 冻结余额（订单未完成时担保）
    private BigDecimal totalRevenue; // 历史总收益
    
    @Version
    private Integer version; // 乐观锁版本号
    
    private LocalDateTime updatedAt; // 最后更新时间
}
