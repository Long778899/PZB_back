package com.peizhenbao.modules.hospital.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "后台新增医院请求参数")
public class AddHospitalDTO {
    
    @NotBlank(message = "医院名称不能为空")
    @Schema(description = "医院名称", example = "北京市第一人民医院")
    private String name;

    @NotBlank(message = "医院等级不能为空")
    @Schema(description = "医院等级", example = "三级甲等")
    private String levelName;

    @NotBlank(message = "省份不能为空")
    @Schema(description = "省份", example = "北京市")
    private String province;

    @NotBlank(message = "城市不能为空")
    @Schema(description = "城市", example = "北京市")
    private String city;

    @NotBlank(message = "详细地址不能为空")
    @Schema(description = "详细地址", example = "朝阳区建国路1号")
    private String address;

    @Schema(description = "联系电话", example = "010-12345678")
    private String phone;

    @Schema(description = "医院展示图片URL", example = "https://example.com/image.jpg")
    private String image;
}
