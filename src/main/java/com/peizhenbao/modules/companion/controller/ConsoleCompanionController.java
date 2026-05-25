package com.peizhenbao.modules.companion.controller;

import com.peizhenbao.common.Result;
import com.peizhenbao.modules.companion.dto.AddCompanionDTO;
import com.peizhenbao.modules.companion.entity.Companion;
import com.peizhenbao.modules.companion.mapper.CompanionMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "管理后台-陪诊员模块", description = "后台管理控制台相关的陪诊员维护接口")
@RestController
@RequestMapping("/api/console/companions")
@RequiredArgsConstructor
public class ConsoleCompanionController {

    private final CompanionMapper companionMapper;

    @Operation(summary = "新增陪诊员", description = "后台管理员添加新的陪诊员信息（包含名称、头像、性别、价格、评分等）")
    @PostMapping
    public Result<?> addCompanion(@Validated @RequestBody AddCompanionDTO dto) {
        Companion companion = new Companion();
        companion.setName(dto.getName());
        companion.setAvatar(dto.getAvatar());
        companion.setGender(dto.getGender());
        companion.setPhone(dto.getPhone());
        companion.setIntro(dto.getIntro());
        companion.setScore(dto.getScore() != null ? dto.getScore() : new java.math.BigDecimal("5.0"));
        companion.setServiceCount(dto.getServiceCount() != null ? dto.getServiceCount() : 0);
        companion.setPrice(dto.getPrice());
        companion.setStatus(1); // 默认接单中
        companion.setCreatedAt(LocalDateTime.now());
        
        companionMapper.insert(companion);
        return Result.success();
    }
}
