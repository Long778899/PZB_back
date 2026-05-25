package com.peizhenbao.modules.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "修改用户信息请求参数")
public class UpdateUserInfoDTO {
    
    @NotBlank(message = "昵称不能为空")
    @Schema(description = "用户昵称", example = "陪诊小能手")
    private String nickname;
    
    @Schema(description = "用户头像URL", example = "https://example.com/avatar.jpg")
    private String avatar;
    
    @Schema(description = "真实姓名", example = "李四")
    private String realName;
    
    @Schema(description = "身份证号", example = "110105199001011234")
    private String idCard;
}
