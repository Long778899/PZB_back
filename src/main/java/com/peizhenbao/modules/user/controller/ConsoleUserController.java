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
}
