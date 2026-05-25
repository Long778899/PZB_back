package com.peizhenbao.modules.hospital.controller;

import com.peizhenbao.common.Result;
import com.peizhenbao.modules.hospital.dto.AddHospitalDTO;
import com.peizhenbao.modules.hospital.entity.Hospital;
import com.peizhenbao.modules.hospital.mapper.HospitalMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "管理后台-医院模块", description = "后台管理控制台相关的医院维护接口")
@RestController
@RequestMapping("/api/console/hospitals")
@RequiredArgsConstructor
public class ConsoleHospitalController {

    private final HospitalMapper hospitalMapper;

    @Operation(summary = "新增医院", description = "后台管理员添加新的医院信息（包含名称、城市、级别、图片、电话等）")
    @PostMapping
    public Result<?> addHospital(@Validated @RequestBody AddHospitalDTO dto) {
        Hospital hospital = new Hospital();
        hospital.setName(dto.getName());
        hospital.setLevelName(dto.getLevelName());
        hospital.setProvince(dto.getProvince());
        hospital.setCity(dto.getCity());
        hospital.setAddress(dto.getAddress());
        hospital.setPhone(dto.getPhone());
        hospital.setImage(dto.getImage());
        hospital.setStatus(1); // 默认上线
        hospital.setCreatedAt(LocalDateTime.now());
        
        hospitalMapper.insert(hospital);
        return Result.success();
    }
}
