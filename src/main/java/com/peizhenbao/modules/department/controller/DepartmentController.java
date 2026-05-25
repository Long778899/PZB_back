package com.peizhenbao.modules.department.controller;

import com.peizhenbao.common.Result;
import com.peizhenbao.modules.department.entity.Department;
import com.peizhenbao.modules.department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    public Result<List<Department>> listByHospitalId(@RequestParam Long hospitalId) {
        return Result.success(departmentService.listByHospitalId(hospitalId));
    }
}
