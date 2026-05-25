package com.peizhenbao.modules.department.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.peizhenbao.modules.department.entity.Department;
import com.peizhenbao.modules.department.mapper.DepartmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentMapper departmentMapper;

    @Cacheable(value = "departments", key = "#hospitalId")
    public List<Department> listByHospitalId(Long hospitalId) {
        return departmentMapper.selectList(new LambdaQueryWrapper<Department>()
                .eq(Department::getHospitalId, hospitalId)
                .eq(Department::getStatus, 1)
                .orderByDesc(Department::getCreatedAt));
    }
}
