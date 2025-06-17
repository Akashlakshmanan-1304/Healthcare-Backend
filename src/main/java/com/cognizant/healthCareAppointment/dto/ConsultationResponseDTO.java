package com.cognizant.healthCareAppointment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ConsultationResponseDTO {
    private Long consultationId;
    private Long appointmentId;
    private String patientName;
    private String doctorName;
    private String notes;
    private String prescriptions;
    private LocalDate date;
}
