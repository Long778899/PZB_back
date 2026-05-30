package com.peizhenbao.modules.doctor.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peizhenbao.common.Result;
import com.peizhenbao.modules.doctor.dto.DoctorVO;
import com.peizhenbao.modules.doctor.entity.Doctor;
import com.peizhenbao.modules.doctor.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "前台-医生模块", description = "供用户端展示和查询医生列表及详情")
@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @Operation(summary = "分页查询医生列表", description = "支持按关键字（模糊搜索姓名）、城市、医院、科室查询，返回带有所属医院、科室信息的医生数据")
    @GetMapping
    public Result<Page<DoctorVO>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long hospitalId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        return Result.success(doctorService.searchDoctors(keyword, hospitalId, departmentId, city, page, size));
    }

    @Operation(summary = "获取医生详情", description = "根据医生 ID 查询指定的医生基础信息")
    @GetMapping("/{id}")
    public Result<Doctor> getDetail(@PathVariable Long id) {
        return Result.success(doctorService.getById(id));
    }
}
