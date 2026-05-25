package com.peizhenbao.modules.department.controller;

import com.peizhenbao.common.Result;
import com.peizhenbao.modules.department.entity.Department;
import com.peizhenbao.modules.department.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "前台-科室模块", description = "供用户端展示和查询科室列表")
@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @Operation(summary = "查询指定医院下的所有科室", description = "通过医院 ID 筛选其下的所有科室，结果已接入 Redis 缓存")
    @GetMapping
    public Result<List<Department>> listByHospitalId(@RequestParam Long hospitalId) {
        return Result.success(departmentService.listByHospitalId(hospitalId));
    }
}
