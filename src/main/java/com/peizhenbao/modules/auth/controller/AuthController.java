package com.peizhenbao.modules.auth.controller;

import com.peizhenbao.common.Result;
import com.peizhenbao.modules.auth.dto.LoginDTO;
import com.peizhenbao.modules.auth.dto.RegisterDTO;
import com.peizhenbao.modules.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "前台-鉴权模块", description = "用户登录和注册接口")
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

    @Operation(summary = "用户登录", description = "用户通过手机号和密码登录，成功后返回 JWT Token")
    @PostMapping("/login")
    public Result<Map<String, String>> login(@Validated @RequestBody LoginDTO dto) {
        String token = authService.login(dto);
        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        return Result.success(data);
    }
}
