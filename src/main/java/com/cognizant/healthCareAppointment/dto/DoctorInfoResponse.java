package com.cognizant.healthCareAppointment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorInfoResponse {
    private String name;
    private String email;
    private String phone;
}
