package com.peizhenbao.modules.patient.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.peizhenbao.modules.patient.dto.PatientDTO;
import com.peizhenbao.modules.patient.entity.Patient;
import com.peizhenbao.modules.patient.mapper.PatientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientMapper patientMapper;

    private Long getCurrentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Transactional(rollbackFor = Exception.class)
    public void addPatient(PatientDTO dto) {
        Long userId = getCurrentUserId();
        
        // 如果设置为默认，先把其他的取消默认
        if (dto.getIsDefault() != null && dto.getIsDefault() == 1) {
            clearDefault(userId);
        }

        Patient patient = new Patient();
        patient.setUserId(userId);
        patient.setName(dto.getName());
        patient.setGender(dto.getGender());
        patient.setBirthday(dto.getBirthday());
        patient.setPhone(dto.getPhone());
        patient.setIdCard(dto.getIdCard());
        patient.setRelationName(dto.getRelationName());
        patient.setRemark(dto.getRemark());
        patient.setIsDefault(dto.getIsDefault() == null ? 0 : dto.getIsDefault());
        patient.setCreatedAt(LocalDateTime.now());
        
        patientMapper.insert(patient);
    }

    public List<Patient> listPatients() {
        Long userId = getCurrentUserId();
        return patientMapper.selectList(new LambdaQueryWrapper<Patient>()
                .eq(Patient::getUserId, userId)
                .orderByDesc(Patient::getIsDefault)
                .orderByDesc(Patient::getCreatedAt));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deletePatient(Long id) {
        Long userId = getCurrentUserId();
        patientMapper.delete(new LambdaQueryWrapper<Patient>()
                .eq(Patient::getId, id)
                .eq(Patient::getUserId, userId));
    }

    private void clearDefault(Long userId) {
        patientMapper.update(null, new LambdaUpdateWrapper<Patient>()
                .set(Patient::getIsDefault, 0)
                .eq(Patient::getUserId, userId));
    }
}
