package com.peizhenbao.modules.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "支付宝登录传输对象")
public class AlipayLoginDTO {
    @Schema(description = "支付宝授权码", requiredMode = Schema.RequiredMode.REQUIRED, example = "mock_alipay_code")
    @NotBlank(message = "授权码不能为空")
    private String authCode;
}
