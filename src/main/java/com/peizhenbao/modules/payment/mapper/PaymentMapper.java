package com.peizhenbao.modules.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.peizhenbao.modules.payment.entity.Payment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper extends BaseMapper<Payment> {
}
