package com.peizhenbao.modules.auth.controller;

import com.peizhenbao.common.Result;
import com.peizhenbao.modules.auth.dto.LoginDTO;
import com.peizhenbao.modules.auth.dto.RegisterDTO;
import com.peizhenbao.modules.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public Result<?> register(@Validated @RequestBody RegisterDTO dto) {
        authService.register(dto);
        return Result.success();
    }

    @PostMapping("/login")
    public Result<Map<String, String>> login(@Validated @RequestBody LoginDTO dto) {
        String token = authService.login(dto);
        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        return Result.success(data);
    }
}
