package com.peizhenbao.modules.order.controller;

import com.peizhenbao.common.Result;
import com.peizhenbao.modules.order.dto.CreateOrderDTO;
import com.peizhenbao.modules.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public Result<String> createOrder(@Validated @RequestBody CreateOrderDTO dto) {
        String orderNo = orderService.createOrder(dto);
        return Result.success(orderNo);
    }
}
