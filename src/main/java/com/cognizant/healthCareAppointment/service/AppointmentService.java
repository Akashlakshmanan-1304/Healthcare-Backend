package com.cognizant.healthCareAppointment.service;

import com.cognizant.healthCareAppointment.dto.BookRequest;
import com.cognizant.healthCareAppointment.dto.DoctorInfoDTO;
import com.cognizant.healthCareAppointment.entity.Appointment;
import com.cognizant.healthCareAppointment.entity.AppointmentStatus;
import com.cognizant.healthCareAppointment.entity.Availability;
import com.cognizant.healthCareAppointment.entity.User;
import com.cognizant.healthCareAppointment.exception.AppointmentNotFoundException;
import com.cognizant.healthCareAppointment.exception.AvailabilityNotFoundException;
import com.cognizant.healthCareAppointment.exception.DoctorNotFoundException;
import com.cognizant.healthCareAppointment.exception.PatientNotFoundException;
import com.cognizant.healthCareAppointment.repository.AppointmentRepository;
import com.cognizant.healthCareAppointment.repository.AvailabilityRepository;
import com.cognizant.healthCareAppointment.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AppointmentService {
    @Autowired
    private AvailabilityRepository availabilityRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private UserRepository userRepo;

    public List<DoctorInfoDTO> getAvailableDoctorsForTomorrow() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Availability> available = availabilityRepo.findByDate(tomorrow);
        List<Long> doctorIds = new ArrayList<>();
        for (Availability a : available) {
            if (a.getIsAvailable() != null && a.getIsAvailable()) {
                LocalTime start = a.getStartTime();
                LocalTime end = a.getEndTime();
                boolean hasSlot = false;
                LocalTime time = start;
                while (time.plusMinutes(30).compareTo(end) <= 0) {
                    List<Appointment> appointmentsForSlot = appointmentRepo.findByDoctor_UserIdAndDateAndTimeSlot(a.getDoctorId(), tomorrow, time);
                    boolean allCancelledOrEmpty = appointmentsForSlot.isEmpty() || appointmentsForSlot.stream().allMatch(appt -> appt.getStatus() == AppointmentStatus.CANCELLED);
                    if (allCancelledOrEmpty) {
                        hasSlot = true;
                        break;
                    }
                    time = time.plusMinutes(30);
                }
                if (hasSlot) {
                    doctorIds.add(a.getDoctorId());
                }
            }
        }
        List<User> doctors = userRepo.findAllById(doctorIds);
        return doctors.stream().map(doc -> new DoctorInfoDTO(doc.getUserId(), doc.getName(),doc.getPhone(),doc.getEmail())).toList();
    }

    public List<String> getAvailableSlots(Long doctorId) {

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Availability availability = availabilityRepo.findByDoctorIdAndDate(doctorId, tomorrow);
        if(availability==null){
           throw new AvailabilityNotFoundException("No Availability found, add Availability");
        }

        List<String> slots = new ArrayList<>();

            if (availability.getIsAvailable()) {
                LocalTime start = availability.getStartTime();
                LocalTime end = availability.getEndTime();

                Duration totalDuration = Duration.between(start, end);
                long minutes = totalDuration.toMinutes();

                LocalTime breakStart = null;
                LocalTime breakEnd = null;
                if (minutes > 300) {
                    long midMinutes = minutes / 2;
                    breakStart = start.plusMinutes(midMinutes);
                    breakEnd = breakStart.plusHours(1);
                }
                log.info("Calculated Start time : {}", breakStart);
                log.info("Calculated End time : {}", breakEnd);
                LocalTime time = start;
                while (time.plusMinutes(30).compareTo(end) <= 0) {
                    if (breakStart != null && !time.isBefore(breakStart) && time.isBefore(breakEnd)) {
                        time = breakEnd;
                        continue;
                    }

                    List<Appointment> appointmentsForSlot = appointmentRepo.findByDoctor_UserIdAndDateAndTimeSlot(doctorId, tomorrow, time);
                    boolean allCancelledOrEmpty = appointmentsForSlot.isEmpty() || appointmentsForSlot.stream().allMatch(appt -> appt.getStatus() == AppointmentStatus.CANCELLED);
                    if (allCancelledOrEmpty) {
                        slots.add(time + " - " + time.plusMinutes(30));
                    }
                    time = time.plusMinutes(30);
                }
            }

        return slots;

    }

    public ResponseEntity<String> bookAppointment(
            BookRequest request
    ) {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Appointment> appointmentsForSlot = appointmentRepo.findByDoctor_UserIdAndDateAndTimeSlot(request.getDoctorId(), tomorrow, request.getTimeSlot());
        boolean slotBooked = appointmentsForSlot.stream().anyMatch(appt -> appt.getStatus() != AppointmentStatus.CANCELLED);
        if (slotBooked) {
            return ResponseEntity.badRequest().body("Slot already booked");
        }

        // Check if the requested time slot is within the doctor's available end time
        Availability availability = availabilityRepo.findByDoctorIdAndDate(request.getDoctorId(), tomorrow);
        if (availability == null || availability.getEndTime() == null || request.getTimeSlot().isAfter(availability.getEndTime()) || request.getTimeSlot().equals(availability.getEndTime())) {
            return ResponseEntity.badRequest().body("Cannot book appointment beyond doctor's available end time");
        }
        User patient = userRepo.findById(request.getPatientId()).orElseThrow(() -> new PatientNotFoundException("Patient with ID " + request.getPatientId() + " not found"));
        User doctor = userRepo.findById(request.getDoctorId()).orElseThrow(() -> new DoctorNotFoundException("Doctor with ID " + request.getDoctorId() + " not found"));
        Appointment a = new Appointment();
        a.setPatient(patient);
        a.setDoctor(doctor);
        a.setDate(tomorrow);
        a.setTimeSlot(request.getTimeSlot());
        a.setStatus(AppointmentStatus.BOOKED);
        appointmentRepo.save(a);
        return ResponseEntity.ok("Appointment booked successfully");
    }

    public ResponseEntity<String> cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepo.findById(appointmentId).orElseThrow(() -> new AppointmentNotFoundException("Appointment of ID " + appointmentId + " not found"));
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            return ResponseEntity.badRequest().body("Appointment already Cancelled");
        }
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepo.save(appointment);

        return ResponseEntity.ok("Appointment Cancelled");
    }

}