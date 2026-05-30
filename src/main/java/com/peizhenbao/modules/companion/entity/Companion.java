package com.peizhenbao.modules.companion.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("companions")
public class Companion {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String avatar;
    private Integer gender; // 1男 2女
    private String phone;
    private String intro;
    private BigDecimal score;
    private Integer serviceCount;
    private BigDecimal price;
    private Integer status; // 0休息/待审核 1接单中 2已禁用
    private String idCard;
    private String drivingLicense;
    private Integer hasWheelchair; // 0无 1有
    private Integer isVeteran; // 0否 1是
    private Integer hasNursingExperience; // 0无 1有
    private String selfDescription;
    private BigDecimal commissionRate; // 抽成比例，如 0.80
    private LocalDateTime createdAt;
}
