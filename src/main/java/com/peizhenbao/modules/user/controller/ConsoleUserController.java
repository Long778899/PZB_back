package com.peizhenbao.modules.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peizhenbao.common.Result;
import com.peizhenbao.modules.order.entity.Order;
import com.peizhenbao.modules.order.mapper.OrderMapper;
import com.peizhenbao.modules.user.dto.UserWithOrdersDTO;
import com.peizhenbao.modules.user.entity.User;
import com.peizhenbao.modules.user.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "管理后台-用户模块", description = "后台管理控制台查询全部用户信息及订单流水")
@RestController
@RequestMapping("/api/console/users")
@RequiredArgsConstructor
public class ConsoleUserController {

    private final UserMapper userMapper;
    private final OrderMapper orderMapper;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "查询所有用户详情列表", description = "分页查询全平台用户基础信息，并关联查询包含他们的历史订单流水记录")
    @GetMapping
    public Result<Page<UserWithOrdersDTO>> listUsersWithOrders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
            
        Page<User> userPage = userMapper.selectPage(new Page<>(page, size), new LambdaQueryWrapper<User>().orderByDesc(User::getCreatedAt));
        
        List<UserWithOrdersDTO> dtoList = userPage.getRecords().stream().map(user -> {
            UserWithOrdersDTO dto = new UserWithOrdersDTO();
            BeanUtils.copyProperties(user, dto);
            // 剔除敏感信息
            dto.setOrders(orderMapper.selectList(new LambdaQueryWrapper<Order>()
                    .eq(Order::getUserId, user.getId())
                    .orderByDesc(Order::getCreatedAt)));
            return dto;
        }).collect(Collectors.toList());
        
        Page<UserWithOrdersDTO> resultPage = new Page<>(page, size, userPage.getTotal());
        resultPage.setRecords(dtoList);
        
        return Result.success(resultPage);
    }

    @Operation(summary = "新增用户", description = "后台管理员手动添加新用户")
    @org.springframework.web.bind.annotation.PostMapping
    public Result<?> addUser(@org.springframework.web.bind.annotation.RequestBody User user) {
        if (!org.springframework.util.StringUtils.hasText(user.getUsername())) {
            return Result.error(400, "用户名不能为空");
        }
        // 后台新建用户，默认初始密码为 123456
        if (!org.springframework.util.StringUtils.hasText(user.getPassword())) {
            user.setPassword(passwordEncoder.encode("123456"));
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setCreatedAt(java.time.LocalDateTime.now());
        user.setStatus(1);
        userMapper.insert(user);
        return Result.success();
    }

    @Operation(summary = "修改用户信息", description = "后台管理员更新用户资料")
    @org.springframework.web.bind.annotation.PutMapping
    public Result<?> updateUser(@org.springframework.web.bind.annotation.RequestBody User user) {
        if (user.getId() == null) {
            return Result.error(400, "用户ID不能为空");
        }
        // 如果密码被修改，则需要加密
        if (org.springframework.util.StringUtils.hasText(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(null); // 不更新密码
        }
        userMapper.updateById(user);
        return Result.success();
    }

    @Operation(summary = "删除用户", description = "后台管理员删除指定用户")
    @org.springframework.web.bind.annotation.DeleteMapping("/{id}")
    public Result<?> deleteUser(@org.springframework.web.bind.annotation.PathVariable Long id) {
        userMapper.deleteById(id);
        return Result.success();
    }
}
