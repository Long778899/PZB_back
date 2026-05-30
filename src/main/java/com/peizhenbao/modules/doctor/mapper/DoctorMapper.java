package com.peizhenbao.modules.doctor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.peizhenbao.modules.doctor.entity.Doctor;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DoctorMapper extends BaseMapper<Doctor> {
}
