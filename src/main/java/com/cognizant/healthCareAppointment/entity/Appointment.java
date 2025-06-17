package com.cognizant.healthCareAppointment.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;
    @ManyToOne
    @JoinColumn(name="patient_id",nullable=false)
    private User patient;
    @ManyToOne
    @JoinColumn(name="doctor_id",nullable=false)
    private User doctor;

    private LocalDate date;
    private LocalTime timeSlot;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

}