package com.peizhenbao.modules.hospital.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;

@Data
@TableName("hospitals")
public class Hospital implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String alias;
    private String grade;
    private String type;
    private String category;
    private String address;
    private String phone;
    private String areaCode;
    private String province;
    private String provinceCode;
    private String city;
    private String district;
    private String intro;
    private String url;
    private LocalDateTime scrapedAt;
    private Integer departmentCount;
    private Integer doctorCount;
    
    // Original system fields that were missing
    private String levelName;
    private String image;
    private Integer status;
    private LocalDateTime createdAt;
}
