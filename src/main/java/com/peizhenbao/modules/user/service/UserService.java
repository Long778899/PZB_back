package com.peizhenbao.modules.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.peizhenbao.exception.BusinessException;
import com.peizhenbao.modules.auth.service.ThirdPartyAuthService;
import com.peizhenbao.modules.user.dto.BindAlipayDTO;
import com.peizhenbao.modules.user.dto.BindEmailDTO;
import com.peizhenbao.modules.user.dto.BindPhoneDTO;
import com.peizhenbao.modules.user.dto.BindWechatDTO;
import com.peizhenbao.modules.user.entity.User;
import com.peizhenbao.modules.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final StringRedisTemplate redisTemplate;
    private final ThirdPartyAuthService thirdPartyAuthService;

    private static final String SMS_CODE_PREFIX = "sms:code:";

    public void bindPhone(Long userId, BindPhoneDTO dto) {
        String key = SMS_CODE_PREFIX + dto.getPhone();
        String cachedCode = redisTemplate.opsForValue().get(key);
        if (cachedCode == null || !cachedCode.equals(dto.getCode())) {
            throw new BusinessException("验证码错误或已过期");
        }

        Long count = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getPhone, dto.getPhone()));
        if (count > 0) {
            throw new BusinessException("该手机号已被其他账号绑定");
        }

        User user = new User();
        user.setId(userId);
        user.setPhone(dto.getPhone());
        userMapper.updateById(user);
        
        redisTemplate.delete(key);
    }

    public void bindEmail(Long userId, BindEmailDTO dto) {
        Long count = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getEmail, dto.getEmail()));
        if (count > 0) {
            throw new BusinessException("该邮箱已被其他账号绑定");
        }

        User user = new User();
        user.setId(userId);
        user.setEmail(dto.getEmail());
        userMapper.updateById(user);
    }

    public void bindWechat(Long userId, BindWechatDTO dto) {
        String openId = thirdPartyAuthService.getWechatOpenId(dto.getCode());
        Long count = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getWechatOpenid, openId));
        if (count > 0) {
            throw new BusinessException("该微信已被其他账号绑定");
        }

        User user = new User();
        user.setId(userId);
        user.setWechatOpenid(openId);
        userMapper.updateById(user);
    }

    public void bindAlipay(Long userId, BindAlipayDTO dto) {
        String alipayUserId = thirdPartyAuthService.getAlipayUserId(dto.getAuthCode());
        Long count = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getAlipayUserId, alipayUserId));
        if (count > 0) {
            throw new BusinessException("该支付宝已被其他账号绑定");
        }

        User user = new User();
        user.setId(userId);
        user.setAlipayUserId(alipayUserId);
        userMapper.updateById(user);
    }
}
