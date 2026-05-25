package com.peizhenbao.modules.patient.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("patients")
public class Patient {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String name;
    private Integer gender; // 1男 2女
    private LocalDate birthday;
    private String phone;
    private String idCard;
    private String relationName;
    private String remark;
    private Integer isDefault; // 0否 1是
    private LocalDateTime createdAt;
}
