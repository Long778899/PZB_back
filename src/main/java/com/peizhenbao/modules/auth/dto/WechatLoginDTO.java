package com.peizhenbao.modules.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "微信登录传输对象")
public class WechatLoginDTO {
    @Schema(description = "微信授权码", requiredMode = Schema.RequiredMode.REQUIRED, example = "mock_wechat_code")
    @NotBlank(message = "授权码不能为空")
    private String code;
}
