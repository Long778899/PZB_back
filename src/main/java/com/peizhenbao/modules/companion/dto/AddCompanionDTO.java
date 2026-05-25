package com.peizhenbao.modules.companion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "后台新增陪诊员请求参数")
public class AddCompanionDTO {

    @NotBlank(message = "陪诊员姓名不能为空")
    @Schema(description = "姓名", example = "张三")
    private String name;

    @Schema(description = "头像URL", example = "https://example.com/avatar.jpg")
    private String avatar;

    @NotNull(message = "性别不能为空")
    @Schema(description = "性别 (1男 2女)", example = "1")
    private Integer gender;

    @NotBlank(message = "手机号不能为空")
    @Schema(description = "联系电话", example = "13800138000")
    private String phone;

    @Schema(description = "服务简介", example = "资深陪诊员，熟悉各大医院流程")
    private String intro;

    @Schema(description = "服务评分", example = "4.9")
    private BigDecimal score;

    @Schema(description = "服务次数", example = "100")
    private Integer serviceCount;

    @NotNull(message = "服务价格不能为空")
    @Schema(description = "单次服务基础价格", example = "199.00")
    private BigDecimal price;
}
