package com.peizhenbao.modules.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "修改密码请求参数")
public class UpdatePasswordDTO {

    @NotBlank(message = "原密码不能为空")
    @Schema(description = "原密码", example = "oldpassword123")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Schema(description = "新密码", example = "newpassword456")
    private String newPassword;
}
