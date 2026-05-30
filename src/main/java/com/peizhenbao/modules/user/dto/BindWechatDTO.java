package com.peizhenbao.modules.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "绑定微信传输对象")
public class BindWechatDTO {
    @Schema(description = "微信授权码", requiredMode = Schema.RequiredMode.REQUIRED, example = "mock_wechat_code")
    @NotBlank(message = "授权码不能为空")
    private String code;
}
