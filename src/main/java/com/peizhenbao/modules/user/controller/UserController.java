package com.peizhenbao.modules.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.peizhenbao.common.Result;
import com.peizhenbao.exception.BusinessException;
import com.peizhenbao.modules.user.dto.*;
import com.peizhenbao.modules.user.entity.User;
import com.peizhenbao.modules.user.mapper.UserMapper;
import com.peizhenbao.modules.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户中心模块", description = "前台用户获取信息、修改信息与账号绑定接口")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

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

    @Operation(summary = "修改用户信息", description = "修改当前登录用户的用户名、邮箱、昵称、头像等基本信息")
    @PutMapping("/info")
    public Result<?> updateUserInfo(@Validated @RequestBody UpdateUserInfoDTO dto) {
        // 校验用户名唯一性
        if (StringUtils.hasText(dto.getUsername())) {
            Long count = userMapper.selectCount(new LambdaQueryWrapper<User>()
                    .eq(User::getUsername, dto.getUsername())
                    .ne(User::getId, getCurrentUserId()));
            if (count > 0) {
                throw new BusinessException("用户名已存在");
            }
        }
        
        User user = new User();
        user.setId(getCurrentUserId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
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

    @Operation(summary = "绑定/换绑手机号", description = "通过验证码绑定新的手机号")
    @PostMapping("/bind/phone")
    public Result<?> bindPhone(@Validated @RequestBody BindPhoneDTO dto) {
        userService.bindPhone(getCurrentUserId(), dto);
        return Result.success();
    }

    @Operation(summary = "绑定邮箱", description = "绑定邮箱")
    @PostMapping("/bind/email")
    public Result<?> bindEmail(@Validated @RequestBody BindEmailDTO dto) {
        userService.bindEmail(getCurrentUserId(), dto);
        return Result.success();
    }

    @Operation(summary = "绑定微信", description = "绑定微信账号")
    @PostMapping("/bind/wechat")
    public Result<?> bindWechat(@Validated @RequestBody BindWechatDTO dto) {
        userService.bindWechat(getCurrentUserId(), dto);
        return Result.success();
    }

    @Operation(summary = "绑定支付宝", description = "绑定支付宝账号")
    @PostMapping("/bind/alipay")
    public Result<?> bindAlipay(@Validated @RequestBody BindAlipayDTO dto) {
        userService.bindAlipay(getCurrentUserId(), dto);
        return Result.success();
    }
}
