package com.peizhenbao.modules.user.controller;

import com.peizhenbao.common.Result;
import com.peizhenbao.exception.BusinessException;
import com.peizhenbao.modules.user.dto.UpdatePasswordDTO;
import com.peizhenbao.modules.user.dto.UpdateUserInfoDTO;
import com.peizhenbao.modules.user.entity.User;
import com.peizhenbao.modules.user.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户中心模块", description = "前台用户获取信息、修改信息与修改密码接口")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    private Long getCurrentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的个人基本资料")
    @GetMapping("/info")
    public Result<User> getUserInfo() {
        User user = userMapper.selectById(getCurrentUserId());
        if (user != null) {
            user.setPassword(null); // 安全脱敏
        }
        return Result.success(user);
    }

    @Operation(summary = "修改用户信息", description = "修改当前登录用户的昵称、头像、真实姓名等基本信息")
    @PutMapping("/info")
    public Result<?> updateUserInfo(@Validated @RequestBody UpdateUserInfoDTO dto) {
        User user = new User();
        user.setId(getCurrentUserId());
        user.setNickname(dto.getNickname());
        user.setAvatar(dto.getAvatar());
        user.setRealName(dto.getRealName());
        user.setIdCard(dto.getIdCard());
        
        userMapper.updateById(user);
        return Result.success();
    }

    @Operation(summary = "修改密码", description = "用户通过提供旧密码来设置新密码")
    @PutMapping("/password")
    public Result<?> updatePassword(@Validated @RequestBody UpdatePasswordDTO dto) {
        User user = userMapper.selectById(getCurrentUserId());
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new BusinessException("原密码不正确");
        }
        
        User updateObj = new User();
        updateObj.setId(getCurrentUserId());
        updateObj.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userMapper.updateById(updateObj);
        
        return Result.success();
    }
}
