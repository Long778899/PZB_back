package com.peizhenbao.modules.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateOrderDTO {
    @NotNull(message = "就诊人ID不能为空")
    private Long patientId;
    
    @NotNull(message = "医院ID不能为空")
    private Long hospitalId;
    
    @NotNull(message = "科室ID不能为空")
    private Long departmentId;
    
    @NotNull(message = "陪诊员ID不能为空")
    private Long companionId;
    
    @NotNull(message = "预约日期不能为空")
    private LocalDate appointmentDate;
    
    @NotNull(message = "预约时间段不能为空")
    private String appointmentTime;
    
    private String serviceContent;
    
    private String noticeContent;
}
