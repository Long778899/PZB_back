package com.peizhenbao.modules.auth.controller;

import com.peizhenbao.common.Result;
import com.peizhenbao.modules.auth.dto.*;
import com.peizhenbao.modules.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "前台-鉴权模块", description = "用户登录、注册及短信接口")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "用户注册", description = "用户通过手机号和密码进行注册")
    @PostMapping("/register")
    public Result<?> register(@Validated @RequestBody RegisterDTO dto) {
        authService.register(dto);
        return Result.success();
    }

    @Operation(summary = "密码登录", description = "用户通过手机号和密码登录")
    @PostMapping("/login/password")
    public Result<Map<String, String>> loginWithPassword(@Validated @RequestBody LoginDTO dto) {
        String token = authService.loginWithPassword(dto);
        return Result.success(Map.of("token", token));
    }

    @Operation(summary = "发送短信验证码", description = "用于短信登录、绑定手机号等")
    @PostMapping("/send-sms")
    public Result<?> sendSms(@Validated @RequestBody SendSmsDTO dto) {
        authService.sendSms(dto);
        return Result.success();
    }

    @Operation(summary = "短信验证码登录", description = "通过手机号和短信验证码登录，未注册则自动注册")
    @PostMapping("/login/sms")
    public Result<Map<String, String>> loginWithSms(@Validated @RequestBody SmsLoginDTO dto) {
        String token = authService.loginWithSms(dto);
        return Result.success(Map.of("token", token));
    }

    @Operation(summary = "微信快捷登录", description = "通过微信授权码登录")
    @PostMapping("/login/wechat")
    public Result<Map<String, String>> loginWithWechat(@Validated @RequestBody WechatLoginDTO dto) {
        String token = authService.loginWithWechat(dto);
        return Result.success(Map.of("token", token));
    }

    @Operation(summary = "支付宝快捷登录", description = "通过支付宝授权码登录")
    @PostMapping("/login/alipay")
    public Result<Map<String, String>> loginWithAlipay(@Validated @RequestBody AlipayLoginDTO dto) {
        String token = authService.loginWithAlipay(dto);
        return Result.success(Map.of("token", token));
    }
}
