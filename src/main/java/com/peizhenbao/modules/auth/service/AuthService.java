package com.peizhenbao.modules.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.peizhenbao.exception.BusinessException;
import com.peizhenbao.modules.auth.dto.*;
import com.peizhenbao.modules.user.entity.User;
import com.peizhenbao.modules.user.mapper.UserMapper;
import com.peizhenbao.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final StringRedisTemplate redisTemplate;
    private final SmsService smsService;
    private final ThirdPartyAuthService thirdPartyAuthService;

    private static final String SMS_CODE_PREFIX = "sms:code:";

    public void register(RegisterDTO dto) {
        Long count = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getPhone, dto.getPhone()));
        if (count > 0) {
            throw new BusinessException("手机号已被注册");
        }

        User user = new User();
        user.setPhone(dto.getPhone());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setAuthStatus(0);
        user.setBalance(BigDecimal.ZERO);
        user.setStatus(1);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        userMapper.insert(user);
    }

    public String loginWithPassword(LoginDTO dto) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getPhone, dto.getPhone()));
        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException("用户不存在或密码错误");
        }
        checkUserStatus(user);
        return jwtUtils.generateToken(user.getId(), user.getPhone());
    }

    public void sendSms(SendSmsDTO dto) {
        String key = SMS_CODE_PREFIX + dto.getPhone();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            throw new BusinessException("验证码已发送，请稍后再试");
        }
        String code = smsService.sendMockSms(dto.getPhone());
        redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
    }

    public String loginWithSms(SmsLoginDTO dto) {
        String key = SMS_CODE_PREFIX + dto.getPhone();
        String cachedCode = redisTemplate.opsForValue().get(key);
        if (cachedCode == null || !cachedCode.equals(dto.getCode())) {
            throw new BusinessException("验证码错误或已过期");
        }
        
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getPhone, dto.getPhone()));
        if (user == null) {
            // 自动注册
            user = new User();
            user.setPhone(dto.getPhone());
            user.setAuthStatus(0);
            user.setBalance(BigDecimal.ZERO);
            user.setStatus(1);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            userMapper.insert(user);
        } else {
            checkUserStatus(user);
        }
        
        redisTemplate.delete(key);
        return jwtUtils.generateToken(user.getId(), user.getPhone());
    }

    public String loginWithWechat(WechatLoginDTO dto) {
        String openId = thirdPartyAuthService.getWechatOpenId(dto.getCode());
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getWechatOpenid, openId));
        if (user == null) {
            user = new User();
            user.setWechatOpenid(openId);
            user.setAuthStatus(0);
            user.setBalance(BigDecimal.ZERO);
            user.setStatus(1);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            userMapper.insert(user);
        } else {
            checkUserStatus(user);
        }
        return jwtUtils.generateToken(user.getId(), user.getPhone() == null ? openId : user.getPhone());
    }

    public String loginWithAlipay(AlipayLoginDTO dto) {
        String alipayUserId = thirdPartyAuthService.getAlipayUserId(dto.getAuthCode());
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getAlipayUserId, alipayUserId));
        if (user == null) {
            user = new User();
            user.setAlipayUserId(alipayUserId);
            user.setAuthStatus(0);
            user.setBalance(BigDecimal.ZERO);
            user.setStatus(1);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            userMapper.insert(user);
        } else {
            checkUserStatus(user);
        }
        return jwtUtils.generateToken(user.getId(), user.getPhone() == null ? alipayUserId : user.getPhone());
    }

    private void checkUserStatus(User user) {
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }
    }
}
