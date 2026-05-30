package com.peizhenbao.modules.doctor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.peizhenbao.modules.department.entity.Department;
import com.peizhenbao.modules.department.mapper.DepartmentMapper;
import com.peizhenbao.modules.doctor.dto.DoctorVO;
import com.peizhenbao.modules.doctor.entity.Doctor;
import com.peizhenbao.modules.doctor.mapper.DoctorMapper;
import com.peizhenbao.modules.doctor.service.DoctorService;
import com.peizhenbao.modules.hospital.entity.Hospital;
import com.peizhenbao.modules.hospital.mapper.HospitalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl extends ServiceImpl<DoctorMapper, Doctor> implements DoctorService {

    private final DoctorMapper doctorMapper;
    private final HospitalMapper hospitalMapper;
    private final DepartmentMapper departmentMapper;

    @Override
    @Cacheable(value = "doctors_search", key = "#keyword + '_' + #hospitalId + '_' + #departmentId + '_' + #city + '_' + #page + '_' + #size", condition = "#page == 1")
    public Page<DoctorVO> searchDoctors(String keyword, Long hospitalId, Long departmentId, String city, int page, int size) {
        LambdaQueryWrapper<Doctor> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Doctor::getName, keyword);
        }
        if (hospitalId != null) {
            wrapper.eq(Doctor::getHospitalId, hospitalId);
        }
        if (departmentId != null) {
            wrapper.eq(Doctor::getDepartmentId, departmentId);
        }

        // 处理基于城市的过滤（如果传了城市，但没传具体医院，需要先查出该城市的医院ID集合）
        if (StringUtils.hasText(city) && hospitalId == null) {
            LambdaQueryWrapper<Hospital> hWrapper = new LambdaQueryWrapper<>();
            hWrapper.eq(Hospital::getCity, city);
            List<Long> hIds = hospitalMapper.selectList(hWrapper).stream().map(Hospital::getId).collect(Collectors.toList());
            if (hIds.isEmpty()) {
                return new Page<>(page, size); // 该城市无医院，直接返回空结果
            }
            wrapper.in(Doctor::getHospitalId, hIds);
        }

        Page<Doctor> doctorPage = doctorMapper.selectPage(new Page<>(page, size), wrapper);

        // 组装 VO
        List<DoctorVO> voList = doctorPage.getRecords().stream().map(doctor -> {
            DoctorVO vo = new DoctorVO();
            BeanUtils.copyProperties(doctor, vo);

            if (doctor.getHospitalId() != null) {
                Hospital hospital = hospitalMapper.selectById(doctor.getHospitalId());
                if (hospital != null) {
                    vo.setHospitalName(hospital.getName());
                    vo.setHospitalCity(hospital.getCity());
                    vo.setHospitalGrade(hospital.getGrade());
                }
            }

            if (doctor.getDepartmentId() != null) {
                Department dept = departmentMapper.selectById(doctor.getDepartmentId());
                if (dept != null) {
                    vo.setDepartmentName(dept.getName());
                }
            }

            return vo;
        }).collect(Collectors.toList());

        Page<DoctorVO> resultPage = new Page<>(page, size, doctorPage.getTotal());
        resultPage.setRecords(voList);
        return resultPage;
    }
}
