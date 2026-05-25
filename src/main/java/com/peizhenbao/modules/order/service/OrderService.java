package com.peizhenbao.modules.order.service;

import com.peizhenbao.exception.BusinessException;
import com.peizhenbao.modules.companion.entity.Companion;
import com.peizhenbao.modules.companion.mapper.CompanionMapper;
import com.peizhenbao.modules.order.dto.CreateOrderDTO;
import com.peizhenbao.modules.order.entity.Order;
import com.peizhenbao.modules.order.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
    private final CompanionMapper companionMapper;
    private final RedissonClient redissonClient;

    private Long getCurrentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Transactional(rollbackFor = Exception.class)
    public String createOrder(CreateOrderDTO dto) {
        Long userId = getCurrentUserId();
        
        // 防重锁，同一个用户对同一个陪诊员在同一天不能频繁下单
        String lockKey = "order:create:" + userId + ":" + dto.getCompanionId() + ":" + dto.getAppointmentDate();
        RLock lock = redissonClient.getLock(lockKey);
        
        try {
            if (lock.tryLock(0, 10, TimeUnit.SECONDS)) {
                // 获取陪诊员信息计算价格
                Companion companion = companionMapper.selectById(dto.getCompanionId());
                if (companion == null || companion.getStatus() != 1) {
                    throw new BusinessException("该陪诊员暂时无法接单");
                }

                Order order = new Order();
                order.setOrderNo(generateOrderNo());
                order.setUserId(userId);
                order.setPatientId(dto.getPatientId());
                order.setHospitalId(dto.getHospitalId());
                order.setDepartmentId(dto.getDepartmentId());
                order.setCompanionId(dto.getCompanionId());
                order.setAppointmentDate(dto.getAppointmentDate());
                order.setAppointmentTime(dto.getAppointmentTime());
                order.setServiceContent(dto.getServiceContent());
                order.setNoticeContent(dto.getNoticeContent());
                order.setAmount(companion.getPrice());
                
                order.setPayStatus(0); // 待支付
                order.setOrderStatus(0); // 待支付
                
                order.setCreatedAt(LocalDateTime.now());
                order.setUpdatedAt(LocalDateTime.now());
                
                orderMapper.insert(order);
                
                // TODO: 发送延迟消息到 RabbitMQ，30分钟未支付自动取消订单
                log.info("用户下单成功, OrderNo: {}", order.getOrderNo());
                
                return order.getOrderNo();
            } else {
                throw new BusinessException("系统繁忙，请勿重复下单");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException("系统繁忙");
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private String generateOrderNo() {
        return "PZB" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }
}
