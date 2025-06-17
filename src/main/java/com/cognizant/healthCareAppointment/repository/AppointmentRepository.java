package com.cognizant.healthCareAppointment.repository;

import com.cognizant.healthCareAppointment.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctor_UserIdAndDate(Long doctorId, LocalDate date);
    boolean existsByDoctor_UserIdAndDateAndTimeSlot(Long doctorId, LocalDate date, LocalTime timeSlot);
    List<Appointment> findByDoctor_UserIdAndDateAndTimeSlot(Long doctorId, LocalDate date, LocalTime timeSlot);
    List<Appointment> findByDoctor_UserId(Long doctorId);

    List<Appointment> findByPatient_UserId(Long patientId);

}