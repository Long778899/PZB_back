package com.peizhenbao.modules.patient.controller;

import com.peizhenbao.common.Result;
import com.peizhenbao.modules.patient.dto.PatientDTO;
import com.peizhenbao.modules.patient.entity.Patient;
import com.peizhenbao.modules.patient.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "前台-就诊人模块", description = "用户就诊人信息的增删改查")
@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @Operation(summary = "新增就诊人", description = "当前登录用户添加新的就诊人信息，支持设置默认就诊人")
    @PostMapping
    public Result<?> addPatient(@Validated @RequestBody PatientDTO dto) {
        patientService.addPatient(dto);
        return Result.success();
    }

    @Operation(summary = "获取就诊人列表", description = "获取当前登录用户的所有就诊人列表")
    @GetMapping
    public Result<List<Patient>> listPatients() {
        return Result.success(patientService.listPatients());
    }

    @Operation(summary = "删除就诊人", description = "根据就诊人ID删除信息")
    @DeleteMapping("/{id}")
    public Result<?> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return Result.success();
    }
}
