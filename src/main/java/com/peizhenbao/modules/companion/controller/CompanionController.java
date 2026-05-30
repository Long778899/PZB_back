package com.peizhenbao.modules.companion.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peizhenbao.common.Result;
import com.peizhenbao.modules.companion.entity.Companion;
import com.peizhenbao.modules.companion.service.CompanionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "前台-陪诊员模块", description = "供用户端展示和查询陪诊员列表及详情")
@RestController
@RequestMapping("/api/companions")
@RequiredArgsConstructor
public class CompanionController {

    private final CompanionService companionService;

    @Operation(summary = "分页查询陪诊员列表", description = "根据评分和服务次数动态排序展示陪诊员列表，支持按性别筛选")
    @GetMapping
    public Result<Page<Companion>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer gender) {
        
        return Result.success(companionService.listCompanions(page, size, gender));
    }

    @Operation(summary = "获取陪诊员详情", description = "查询指定陪诊员的详细服务介绍与评价信息")
    @GetMapping("/{id}")
    public Result<Companion> getDetail(@PathVariable Long id) {
        return Result.success(companionService.getDetail(id));
    }

    @Operation(summary = "陪诊员自主注册", description = "前台用户申请成为陪诊员，包含资质等扩展信息填写")
    @PostMapping("/register")
    public Result<?> register(@org.springframework.validation.annotation.Validated @RequestBody com.peizhenbao.modules.companion.dto.RegisterCompanionDTO dto,
                              @org.springframework.beans.factory.annotation.Autowired com.peizhenbao.modules.companion.mapper.CompanionMapper companionMapper) {
        Companion companion = new Companion();
        companion.setName(dto.getName());
        companion.setPhone(dto.getPhone());
        companion.setGender(dto.getGender() != null ? dto.getGender() : 0);
        
        companion.setIdCard(dto.getIdCard());
        companion.setDrivingLicense(dto.getDrivingLicense());
        companion.setHasWheelchair(dto.getHasWheelchair() != null ? dto.getHasWheelchair() : 0);
        companion.setIsVeteran(dto.getIsVeteran() != null ? dto.getIsVeteran() : 0);
        companion.setHasNursingExperience(dto.getHasNursingExperience() != null ? dto.getHasNursingExperience() : 0);
        companion.setSelfDescription(dto.getSelfDescription());
        
        // 自主注册的默认状态为 0 (休息/待审核)
        companion.setStatus(0);
        // 初始化一些基础数值
        companion.setScore(new java.math.BigDecimal("5.0"));
        companion.setServiceCount(0);
        companion.setPrice(new java.math.BigDecimal("0.0")); // 等待审核后定价或自己稍后补充
        companion.setCreatedAt(java.time.LocalDateTime.now());
        
        companionMapper.insert(companion);
        return Result.success();
    }
}
