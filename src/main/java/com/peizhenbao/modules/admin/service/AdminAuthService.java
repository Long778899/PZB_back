package com.peizhenbao.modules.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.peizhenbao.exception.BusinessException;
import com.peizhenbao.modules.admin.dto.AdminLoginDTO;
import com.peizhenbao.modules.admin.entity.Admin;
import com.peizhenbao.modules.admin.mapper.AdminMapper;
import com.peizhenbao.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public String login(AdminLoginDTO dto) {
        Admin admin = adminMapper.selectOne(new LambdaQueryWrapper<Admin>().eq(Admin::getUsername, dto.getUsername()));
        
        if (admin == null || !passwordEncoder.matches(dto.getPassword(), admin.getPassword())) {
            throw new BusinessException("管理员账号不存在或密码错误");
        }
        
        if (admin.getStatus() != null && admin.getStatus() == 0) {
            throw new BusinessException("该管理员账号已被禁用");
        }
        
        // 签发带有 ADMIN 标记的专属 Token
        return jwtUtils.generateAdminToken(admin.getId(), admin.getUsername(), admin.getRoleId());
    }
}
