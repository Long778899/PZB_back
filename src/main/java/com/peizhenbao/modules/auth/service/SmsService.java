package com.peizhenbao.modules.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class SmsService {

    /**
     * 模拟发送验证码
     * @param phone 手机号
     * @return 生成的验证码
     */
    public String sendMockSms(String phone) {
        // 生成6位随机验证码
        String code = String.format("%06d", new Random().nextInt(999999));
        log.info("========== 模拟短信发送 ==========");
        log.info("接收手机号: {}", phone);
        log.info("短信验证码: {}", code);
        log.info("==================================");
        return code;
    }
}
