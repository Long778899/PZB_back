package com.peizhenbao.modules.companion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "前台陪诊员自主注册请求参数")
public class RegisterCompanionDTO {

    @NotBlank(message = "真实姓名不能为空")
    @Schema(description = "真实姓名", example = "张三")
    private String name;

    @NotBlank(message = "手机号不能为空")
    @Schema(description = "联系电话", example = "13800138000")
    private String phone;

    @Schema(description = "性别 (1男 2女)", example = "1")
    private Integer gender;

    @Schema(description = "身份证号", example = "110105199001011234")
    private String idCard;

    @Schema(description = "驾照信息", example = "C1")
    private String drivingLicense;

    @Schema(description = "是否有轮椅", example = "1")
    private Integer hasWheelchair;

    @Schema(description = "是否是退役军人", example = "0")
    private Integer isVeteran;

    @Schema(description = "是否有过护理经验", example = "1")
    private Integer hasNursingExperience;

    @Schema(description = "自我描述", example = "性格开朗，熟悉各大医院流程，提供轮椅...")
    private String selfDescription;
}
