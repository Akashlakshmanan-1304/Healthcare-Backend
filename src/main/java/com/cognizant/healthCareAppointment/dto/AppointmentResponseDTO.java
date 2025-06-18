package com.cognizant.healthCareAppointment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@Data
public class AppointmentResponseDTO {
    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;
    @NotBlank(message = "Doctor name is required")
    private String doctorName;
    @NotBlank(message = "Patient name is required")
    private String patientName;
    private LocalDate date;
    private LocalTime time;
    @NotBlank(message = "Status is required")
    private String Status;

}
