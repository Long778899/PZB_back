package com.peizhenbao.modules.department.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peizhenbao.common.Result;
import com.peizhenbao.modules.department.entity.Department;
import com.peizhenbao.modules.department.mapper.DepartmentMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "管理后台-科室模块", description = "后台管理控制台相关的科室维护接口")
@RestController
@RequestMapping("/api/console/departments")
@RequiredArgsConstructor
public class ConsoleDepartmentController {

    private final DepartmentMapper departmentMapper;

    @Operation(summary = "分页查询科室列表", description = "支持按医院ID、科室名称模糊查询")
    @GetMapping
    public Result<Page<Department>> list(
            @Parameter(description = "医院ID") @RequestParam(required = false) Long hospitalId,
            @Parameter(description = "科室名称模糊查询") @RequestParam(required = false) String name,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        
        Page<Department> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        
        if (hospitalId != null) {
            wrapper.eq(Department::getHospitalId, hospitalId);
        }
        if (StringUtils.hasText(name)) {
            wrapper.like(Department::getName, name);
        }
        wrapper.orderByDesc(Department::getCreatedAt);
        
        return Result.success(departmentMapper.selectPage(pageParam, wrapper));
    }

    @Operation(summary = "新增科室", description = "后台管理员添加新的科室信息")
    @PostMapping
    public Result<?> addDepartment(@RequestBody Department department) {
        if (department.getHospitalId() == null || !StringUtils.hasText(department.getName())) {
            return Result.error(400, "医院ID和科室名称不能为空");
        }
        department.setStatus(1);
        department.setCreatedAt(LocalDateTime.now());
        departmentMapper.insert(department);
        return Result.success();
    }

    @Operation(summary = "修改科室", description = "后台管理员更新科室信息")
    @PutMapping
    public Result<?> updateDepartment(@RequestBody Department department) {
        if (department.getId() == null) {
            return Result.error(400, "科室ID不能为空");
        }
        departmentMapper.updateById(department);
        return Result.success();
    }

    @Operation(summary = "删除科室", description = "根据科室ID删除指定科室")
    @DeleteMapping("/{id}")
    public Result<?> deleteDepartment(@PathVariable Long id) {
        departmentMapper.deleteById(id);
        return Result.success();
    }
}
