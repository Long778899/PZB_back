package com.peizhenbao.modules.patient.controller;

import com.peizhenbao.common.Result;
import com.peizhenbao.modules.patient.dto.PatientDTO;
import com.peizhenbao.modules.patient.entity.Patient;
import com.peizhenbao.modules.patient.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public Result<?> addPatient(@Validated @RequestBody PatientDTO dto) {
        patientService.addPatient(dto);
        return Result.success();
    }

    @GetMapping
    public Result<List<Patient>> listPatients() {
        return Result.success(patientService.listPatients());
    }

    @DeleteMapping("/{id}")
    public Result<?> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return Result.success();
    }
}
