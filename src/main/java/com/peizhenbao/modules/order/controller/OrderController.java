package com.peizhenbao.modules.order.controller;

import com.peizhenbao.common.Result;
import com.peizhenbao.modules.order.dto.CreateOrderDTO;
import com.peizhenbao.modules.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "前台-订单模块", description = "用户陪诊订单的创建、取消与状态查询")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "创建订单", description = "用户选择医院、科室、就诊人和陪诊员后发起下单（含防超卖、防重复提交功能）")
    @PostMapping("/create")
    public Result<String> createOrder(@Validated @RequestBody CreateOrderDTO dto) {
        String orderNo = orderService.createOrder(dto);
        return Result.success(orderNo);
    }
}
