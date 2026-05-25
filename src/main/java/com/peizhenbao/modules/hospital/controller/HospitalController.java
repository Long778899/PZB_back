package com.peizhenbao.modules.hospital.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peizhenbao.common.Result;
import com.peizhenbao.modules.hospital.entity.Hospital;
import com.peizhenbao.modules.hospital.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hospitals")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;

    @GetMapping
    public Result<Page<Hospital>> list(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        return Result.success(hospitalService.listHospitals(city, keyword, page, size));
    }

    @GetMapping("/{id}")
    public Result<Hospital> getDetail(@PathVariable Long id) {
        return Result.success(hospitalService.getById(id));
    }
}
