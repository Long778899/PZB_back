package com.peizhenbao.modules.doctor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peizhenbao.common.Result;
import com.peizhenbao.modules.doctor.entity.Doctor;
import com.peizhenbao.modules.doctor.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "管理后台-医生模块", description = "后台管理控制台相关的医生维护接口（支持百万级数据）")
@RestController
@RequestMapping("/api/console/doctors")
@RequiredArgsConstructor
public class ConsoleDoctorController {

    private final DoctorService doctorService;
    private final com.peizhenbao.modules.hospital.mapper.HospitalMapper hospitalMapper;

    @Operation(summary = "分页查询医生列表", description = "支持按省、市、区、医院类型/等级、医院ID、科室ID、姓名等多维度查询")
    @GetMapping
    public Result<Page<Doctor>> list(
            @Parameter(description = "省份") @RequestParam(required = false) String province,
            @Parameter(description = "城市") @RequestParam(required = false) String city,
            @Parameter(description = "地区") @RequestParam(required = false) String district,
            @Parameter(description = "医院类型，如综合医院") @RequestParam(required = false) String type,
            @Parameter(description = "医院等级，如三级甲等") @RequestParam(required = false) String grade,
            @Parameter(description = "医院ID") @RequestParam(required = false) Long hospitalId,
            @Parameter(description = "科室ID") @RequestParam(required = false) Long departmentId,
            @Parameter(description = "医生姓名模糊匹配") @RequestParam(required = false) String name,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        
        // 针对百万级数据，如果深分页（page > 1000等），建议前端限制或改用游标分页
        // 目前采用MyBatis Plus默认分页
        Page<Doctor> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Doctor> wrapper = new LambdaQueryWrapper<>();
        
        // 如果携带了任何与医院相关的搜索条件，则先查出医院ID集合
        boolean hasHospitalFilter = StringUtils.hasText(province) || StringUtils.hasText(city) 
                || StringUtils.hasText(district) || StringUtils.hasText(type) || StringUtils.hasText(grade);
                
        if (hasHospitalFilter) {
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.peizhenbao.modules.hospital.entity.Hospital> hospWrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            hospWrapper.select(com.peizhenbao.modules.hospital.entity.Hospital::getId);
            
            if (StringUtils.hasText(province)) hospWrapper.eq(com.peizhenbao.modules.hospital.entity.Hospital::getProvince, province);
            if (StringUtils.hasText(city)) hospWrapper.eq(com.peizhenbao.modules.hospital.entity.Hospital::getCity, city);
            if (StringUtils.hasText(district)) hospWrapper.eq(com.peizhenbao.modules.hospital.entity.Hospital::getDistrict, district);
            if (StringUtils.hasText(type)) hospWrapper.eq(com.peizhenbao.modules.hospital.entity.Hospital::getType, type);
            if (StringUtils.hasText(grade)) hospWrapper.eq(com.peizhenbao.modules.hospital.entity.Hospital::getGrade, grade);
            
            List<Object> hospIds = hospitalMapper.selectObjs(hospWrapper);
            if (hospIds.isEmpty()) {
                return Result.success(new Page<>(page, size)); // 未找到匹配的医院，直接返回空医生列表
            }
            wrapper.in(Doctor::getHospitalId, hospIds);
        }
        
        if (hospitalId != null) {
            wrapper.eq(Doctor::getHospitalId, hospitalId);
        }
        if (departmentId != null) {
            wrapper.eq(Doctor::getDepartmentId, departmentId);
        }
        if (StringUtils.hasText(name)) {
            wrapper.like(Doctor::getName, name);
        }
        
        return Result.success(doctorService.page(pageParam, wrapper));
    }

    @Operation(summary = "新增医生（单条）", description = "单条新增医生数据")
    @PostMapping
    public Result<?> add(@RequestBody Doctor doctor) {
        doctorService.save(doctor);
        return Result.success();
    }

    @Operation(summary = "修改医生", description = "修改指定的医生基础信息")
    @PutMapping
    public Result<?> update(@RequestBody Doctor doctor) {
        if (doctor.getId() == null) {
            return Result.error(400, "医生ID不能为空");
        }
        doctorService.updateById(doctor);
        return Result.success();
    }

    @Operation(summary = "批量新增医生（上传导入模拟）", description = "批量插入医生数据，建议分批提交（如每次500条）以防止内存溢出")
    @PostMapping("/batch")
    public Result<?> addBatch(@RequestBody List<Doctor> doctors) {
        if (doctors == null || doctors.isEmpty()) {
            return Result.error(400, "数据不能为空");
        }
        // 批量插入，指定batchSize为1000
        doctorService.saveBatch(doctors, 1000);
        return Result.success();
    }

    @Operation(summary = "删除医生", description = "根据ID删除医生")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        doctorService.removeById(id);
        return Result.success();
    }

    @Operation(summary = "批量删除医生", description = "根据ID列表批量删除医生")
    @DeleteMapping("/batch")
    public Result<?> deleteBatch(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.error(400, "ID列表不能为空");
        }
        doctorService.removeByIds(ids);
        return Result.success();
    }
}
