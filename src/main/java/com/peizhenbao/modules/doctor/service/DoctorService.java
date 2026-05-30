package com.peizhenbao.modules.doctor.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.peizhenbao.modules.doctor.dto.DoctorVO;
import com.peizhenbao.modules.doctor.entity.Doctor;

public interface DoctorService extends IService<Doctor> {
    Page<DoctorVO> searchDoctors(String keyword, Long hospitalId, Long departmentId, String city, int page, int size);
}
