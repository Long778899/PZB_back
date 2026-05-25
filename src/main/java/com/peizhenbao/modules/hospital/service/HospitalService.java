package com.peizhenbao.modules.hospital.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peizhenbao.modules.hospital.entity.Hospital;
import com.peizhenbao.modules.hospital.mapper.HospitalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalMapper hospitalMapper;

    @Cacheable(value = "hospitals", key = "#city + '_' + #page + '_' + #size")
    public Page<Hospital> listHospitals(String city, String keyword, int page, int size) {
        LambdaQueryWrapper<Hospital> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Hospital::getStatus, 1);
        
        if (StringUtils.hasText(city)) {
            wrapper.eq(Hospital::getCity, city);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Hospital::getName, keyword);
        }
        
        wrapper.orderByDesc(Hospital::getCreatedAt);
        
        return hospitalMapper.selectPage(new Page<>(page, size), wrapper);
    }
    
    @Cacheable(value = "hospital_detail", key = "#id")
    public Hospital getById(Long id) {
        return hospitalMapper.selectById(id);
    }
}
