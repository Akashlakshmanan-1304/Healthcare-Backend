package com.cognizant.healthCareAppointment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConsultationRequest {
    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;
    @NotBlank(message = "Notes is required")
    private String notes;
    @NotBlank(message = "Prescription is required")
    private String prescription;
}