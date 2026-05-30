package com.peizhenbao.modules.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("admins")
public class Admin {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private Long roleId;
    private Integer status; // 0=禁用 1=启用
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
