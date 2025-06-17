package com.cognizant.healthCareAppointment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AvailabilityRequest {

        @NotNull(message = "Doctor ID is required")
        private Long doctorId;
        @NotNull(message = "Date is required")
        private LocalDate date;
        private Boolean isAvailable;
        private LocalTime startTime;
        private LocalTime endTime;



}
