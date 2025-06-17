package com.cognizant.healthCareAppointment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@Data
public class AppointmentResponseDTO {
    private Long appointmentId;
    private String doctorName;
    private String patientName;
    private LocalDate date;
    private LocalTime time;
    private String Status;

}
