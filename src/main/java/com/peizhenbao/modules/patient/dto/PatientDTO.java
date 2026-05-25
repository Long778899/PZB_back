package com.peizhenbao.modules.patient.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientDTO {
    @NotBlank(message = "就诊人姓名不能为空")
    private String name;
    
    @NotNull(message = "性别不能为空")
    private Integer gender;
    
    private LocalDate birthday;
    
    @NotBlank(message = "联系电话不能为空")
    private String phone;
    
    private String idCard;
    
    @NotBlank(message = "与用户关系不能为空")
    private String relationName;
    
    private String remark;
    
    private Integer isDefault;
}
