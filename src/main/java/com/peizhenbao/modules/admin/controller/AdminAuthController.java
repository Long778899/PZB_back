package com.peizhenbao.modules.admin.controller;

import com.peizhenbao.common.Result;
import com.peizhenbao.modules.admin.dto.AdminLoginDTO;
import com.peizhenbao.modules.admin.service.AdminAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    @PostMapping("/login")
    public Result<Map<String, String>> login(@RequestBody AdminLoginDTO dto) {
        String token = adminAuthService.login(dto);
        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        return Result.success(data);
    }
}
