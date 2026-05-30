package com.peizhenbao.modules.hospital.controller;

import com.peizhenbao.common.Result;
import com.peizhenbao.modules.hospital.dto.AddHospitalDTO;
import com.peizhenbao.modules.hospital.entity.Hospital;
import com.peizhenbao.modules.hospital.mapper.HospitalMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "管理后台-医院模块", description = "后台管理控制台相关的医院维护接口")
@RestController
@RequestMapping("/api/console/hospitals")
@RequiredArgsConstructor
public class ConsoleHospitalController {

    private final HospitalMapper hospitalMapper;

    @Operation(summary = "分页查询医院列表", description = "支持按省、市、区、类型、等级等多维度查询医院")
    @GetMapping
    public Result<com.baomidou.mybatisplus.extension.plugins.pagination.Page<Hospital>> list(
            @Parameter(description = "省份") @RequestParam(required = false) String province,
            @Parameter(description = "城市") @RequestParam(required = false) String city,
            @Parameter(description = "地区") @RequestParam(required = false) String district,
            @Parameter(description = "医院类型，如综合医院") @RequestParam(required = false) String type,
            @Parameter(description = "医院等级，如三级甲等") @RequestParam(required = false) String grade,
            @Parameter(description = "医院名称模糊查询") @RequestParam(required = false) String name,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Hospital> pageParam = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size);
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Hospital> wrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        
        if (org.springframework.util.StringUtils.hasText(province)) {
            wrapper.eq(Hospital::getProvince, province);
        }
        if (org.springframework.util.StringUtils.hasText(city)) {
            wrapper.eq(Hospital::getCity, city);
        }
        if (org.springframework.util.StringUtils.hasText(district)) {
            wrapper.eq(Hospital::getDistrict, district);
        }
        if (org.springframework.util.StringUtils.hasText(type)) {
            wrapper.eq(Hospital::getType, type);
        }
        if (org.springframework.util.StringUtils.hasText(grade)) {
            wrapper.eq(Hospital::getGrade, grade);
        }
        if (org.springframework.util.StringUtils.hasText(name)) {
            wrapper.like(Hospital::getName, name);
        }
        
        return Result.success(hospitalMapper.selectPage(pageParam, wrapper));
    }

    @Operation(summary = "新增医院", description = "后台管理员添加新的医院信息（包含名称、城市、级别、图片、电话等）")
    @PostMapping
    public Result<?> addHospital(@Validated @RequestBody AddHospitalDTO dto) {
        Hospital hospital = new Hospital();
        hospital.setName(dto.getName());
        hospital.setLevelName(dto.getLevelName());
        hospital.setProvince(dto.getProvince());
        hospital.setCity(dto.getCity());
        hospital.setAddress(dto.getAddress());
        hospital.setPhone(dto.getPhone());
        hospital.setImage(dto.getImage());
        hospital.setStatus(1); // 默认上线
        hospital.setCreatedAt(LocalDateTime.now());
        
        hospitalMapper.insert(hospital);
        return Result.success();
    }
}
