package com.peizhenbao.modules.companion.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peizhenbao.modules.companion.entity.Companion;
import com.peizhenbao.modules.companion.mapper.CompanionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanionService {

    private final CompanionMapper companionMapper;

    public Page<Companion> listCompanions(int page, int size, Integer gender) {
        LambdaQueryWrapper<Companion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Companion::getStatus, 1); // 1接单中
        
        if (gender != null) {
            wrapper.eq(Companion::getGender, gender);
        }
        
        wrapper.orderByDesc(Companion::getScore)
               .orderByDesc(Companion::getServiceCount);
               
        return companionMapper.selectPage(new Page<>(page, size), wrapper);
    }
    
    public Companion getDetail(Long id) {
        return companionMapper.selectById(id);
    }
}
