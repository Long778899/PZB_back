package com.peizhenbao.modules.doctor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.peizhenbao.modules.doctor.entity.Doctor;
import com.peizhenbao.modules.doctor.mapper.DoctorMapper;
import com.peizhenbao.modules.doctor.service.DoctorService;
import org.springframework.stereotype.Service;

@Service
public class DoctorServiceImpl extends ServiceImpl<DoctorMapper, Doctor> implements DoctorService {
}
