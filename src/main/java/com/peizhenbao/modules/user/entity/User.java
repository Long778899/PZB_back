package com.peizhenbao.modules.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("users")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String phone;
    private String username;
    private String email;
    private String wechatOpenid;
    private String alipayUserId;
    private String password;
    private String nickname;
    private String avatar;
    private String realName;
    private String idCard;
    private Integer authStatus; // 0未认证 1审核中 2已认证 3驳回
    private BigDecimal balance;
    private Integer status; // 0禁用 1正常
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
