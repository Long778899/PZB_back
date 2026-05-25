package com.peizhenbao.modules.hospital.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

@Data
@TableName("hospitals")
public class Hospital implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String levelName;
    private String province;
    private String city;
    private String address;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String phone;
    private String image;
    private Integer status; // 0下线 1上线
    private LocalDateTime createdAt;
}
