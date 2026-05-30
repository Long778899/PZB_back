package com.peizhenbao.modules.doctor.dto;

import com.peizhenbao.modules.doctor.entity.Doctor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "医生视图对象，包含关联的医院和科室信息")
public class DoctorVO extends Doctor {
    
    @Schema(description = "所属医院名称")
    private String hospitalName;
    
    @Schema(description = "所属医院所在城市")
    private String hospitalCity;
    
    @Schema(description = "所属科室名称")
    private String departmentName;
    
    @Schema(description = "医院等级")
    private String hospitalGrade;
}
