package com.peizhenbao.modules.auth.service;

import org.springframework.stereotype.Service;

@Service
public class ThirdPartyAuthService {

    /**
     * 模拟验证微信授权码并获取 OpenID
     * @param code 微信前端传来的 code
     * @return 模拟的 openId
     */
    public String getWechatOpenId(String code) {
        // 在真实环境中，这里应该调用微信API: https://api.weixin.qq.com/sns/jscode2session
        // 这里为了打通流程，直接将 code 加上前缀作为 openid
        return "mock_wx_openid_" + code;
    }

    /**
     * 模拟验证支付宝授权码并获取 UserId
     * @param authCode 支付宝前端传来的 authCode
     * @return 模拟的 alipayUserId
     */
    public String getAlipayUserId(String authCode) {
        // 在真实环境中，这里应该调用支付宝 SDK 的 alipay.system.oauth.token 接口
        return "mock_ali_userid_" + authCode;
    }
}
