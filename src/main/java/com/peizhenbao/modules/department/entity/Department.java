package com.peizhenbao.modules.department.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("departments")
public class Department implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long hospitalId;
    private String name;
    private String category;
    private String intro;
    private String introShort;
    private Integer totalDoctorCnt;
    private Long facultyId;
    private String facultyName;
    private String url;
    
    // Original system fields that were missing
    private Integer status;
    private java.time.LocalDateTime createdAt;
}
