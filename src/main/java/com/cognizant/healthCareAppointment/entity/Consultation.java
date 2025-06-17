package com.cognizant.healthCareAppointment.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "consultation")
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long consultationId;
    @OneToOne
    @JoinColumn(name = "appointment_id",nullable=false,unique=true)
    private Appointment appointment;
    private String notes;
    private String prescription;

}