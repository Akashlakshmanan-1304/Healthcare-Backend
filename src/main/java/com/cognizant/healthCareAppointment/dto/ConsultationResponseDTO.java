package com.cognizant.healthCareAppointment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ConsultationResponseDTO {
    @NotNull(message = "Consultation ID is required")
    private Long consultationId;
    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;
    @NotBlank(message = "Patient name is required")
    @Pattern(regexp = "^[A-Za-z ]+$",message = "Name must contains only letters and spaces")
    private String patientName;
    @NotBlank(message = "Doctor name is required")
    @Pattern(regexp = "^[A-Za-z ]+$",message = "Name must contains only letters and spaces")
    private String doctorName;
    @NotBlank(message = "Notes are required")
    private String notes;
    @NotBlank(message = "Prescriptions are required")
    private String prescriptions;
    private LocalDate date;
}
