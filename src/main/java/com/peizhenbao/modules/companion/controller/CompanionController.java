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
}
