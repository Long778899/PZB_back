package com.peizhenbao.modules.doctor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("doctors")
public class Doctor implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long hospitalId;
    private Long departmentId;
    private String name;
    private String grade;
    private String educateGrade;
    private String title;
    private String socialPosition;
    private String facultyName;
    private String professionalDirection;
    private String specialize;
    private String introDetail;
    private String headImage;
    private BigDecimal commentRank;
    private Integer totalPatients;
    private String url;
}
