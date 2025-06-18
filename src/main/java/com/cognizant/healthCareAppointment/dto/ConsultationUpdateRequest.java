package com.cognizant.healthCareAppointment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultationUpdateRequest {
    @NotBlank(message = "Consultation ID is required")
    private String prescriptions;
    @NotBlank(message = "Appointment ID is required")
    private String notes;
}
