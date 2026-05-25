package com.peizhenbao.modules.payment.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.peizhenbao.common.Result;
import com.peizhenbao.exception.BusinessException;
import com.peizhenbao.modules.order.entity.Order;
import com.peizhenbao.modules.order.mapper.OrderMapper;
import com.peizhenbao.modules.payment.entity.Payment;
import com.peizhenbao.modules.payment.mapper.PaymentMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Tag(name = "前台-支付模块", description = "模拟第三方支付回调，用于测试订单流转")
@Slf4j
@RestController
@RequestMapping("/api/payment/mock")
@RequiredArgsConstructor
public class MockPaymentController {

    private final OrderMapper orderMapper;
    private final PaymentMapper paymentMapper;

    @Operation(summary = "模拟微信/支付宝支付成功回调", description = "根据订单号模拟支付成功，生成支付流水并流转订单状态为已支付（该接口具备幂等性处理）")
    @PostMapping("/pay")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> mockPay(@RequestParam String orderNo) {
        Order order = orderMapper.selectOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderNo, orderNo));
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (order.getPayStatus() == 1) {
            throw new BusinessException("订单已支付，请勿重复支付");
        }

        // 1. 更新订单状态
        order.setPayStatus(1);
        order.setOrderStatus(1); // 1已支付待接单
        orderMapper.updateById(order);

        // 2. 插入支付流水 (幂等，通过 orderId 和 payNo)
        Payment payment = new Payment();
        payment.setOrderId(order.getId());
        payment.setPayNo("PAY" + System.currentTimeMillis());
        payment.setPayType("WX_MOCK");
        payment.setPayAmount(order.getAmount());
        payment.setPayStatus(1); // 成功
        payment.setTransactionId(UUID.randomUUID().toString().replace("-", ""));
        payment.setCreatedAt(LocalDateTime.now());
        paymentMapper.insert(payment);

        log.info("Mock支付成功, OrderNo: {}, Amount: {}", orderNo, order.getAmount());
        return Result.success();
    }
}
