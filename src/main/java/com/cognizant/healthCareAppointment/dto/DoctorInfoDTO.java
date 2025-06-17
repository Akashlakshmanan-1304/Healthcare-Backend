package com.cognizant.healthCareAppointment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DoctorInfoDTO {
    private Long doctorId;
    private String name;
    private String phone;
    private String email;
}
