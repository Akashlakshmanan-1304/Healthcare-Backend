package com.cognizant.healthCareAppointment.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Data
public class BookRequest {
    @NotNull(message = "Patient ID is required")
    private Long patientId;
    @NotNull(message = "Doctor ID is required")
    private Long doctorId;
    private @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    LocalTime timeSlot;
}
