package com.peizhenbao.modules.companion.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peizhenbao.common.Result;
import com.peizhenbao.modules.companion.entity.Companion;
import com.peizhenbao.modules.companion.service.CompanionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companions")
@RequiredArgsConstructor
public class CompanionController {

    private final CompanionService companionService;

    @GetMapping
    public Result<Page<Companion>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer gender) {
        
        return Result.success(companionService.listCompanions(page, size, gender));
    }

    @GetMapping("/{id}")
    public Result<Companion> getDetail(@PathVariable Long id) {
        return Result.success(companionService.getDetail(id));
    }
}
