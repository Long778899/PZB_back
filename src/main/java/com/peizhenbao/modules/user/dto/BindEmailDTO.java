package com.peizhenbao.modules.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "绑定邮箱传输对象")
public class BindEmailDTO {
    @Schema(description = "邮箱地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "test@example.com")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
}
