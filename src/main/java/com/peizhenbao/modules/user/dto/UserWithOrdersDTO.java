package com.peizhenbao.modules.user.dto;

import com.peizhenbao.modules.order.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "后台控制台-包含订单历史的用户信息")
public class UserWithOrdersDTO {
    
    @Schema(description = "用户ID")
    private Long id;
    
    @Schema(description = "手机号")
    private String phone;
    
    @Schema(description = "昵称")
    private String nickname;
    
    @Schema(description = "头像")
    private String avatar;
    
    @Schema(description = "真实姓名")
    private String realName;
    
    @Schema(description = "余额")
    private BigDecimal balance;
    
    @Schema(description = "注册时间")
    private LocalDateTime createdAt;
    
    @Schema(description = "该用户的历史订单列表")
    private List<Order> orders;
}
