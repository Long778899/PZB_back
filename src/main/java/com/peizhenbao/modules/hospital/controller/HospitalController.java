package com.peizhenbao.modules.hospital.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peizhenbao.common.Result;
import com.peizhenbao.modules.hospital.entity.Hospital;
import com.peizhenbao.modules.hospital.service.HospitalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "前台-医院模块", description = "供用户端展示和查询医院列表及详情")
@RestController
@RequestMapping("/api/hospitals")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;

    @Operation(summary = "分页查询医院列表", description = "支持按城市、关键字搜索医院，结果已接入 Redis 缓存")
    @GetMapping
    public Result<Page<Hospital>> list(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        return Result.success(hospitalService.listHospitals(city, keyword, page, size));
    }

    @Operation(summary = "获取医院详情", description = "根据医院 ID 查询指定的医院基础信息")
    @GetMapping("/{id}")
    public Result<Hospital> getDetail(@PathVariable Long id) {
        return Result.success(hospitalService.getById(id));
    }
}
