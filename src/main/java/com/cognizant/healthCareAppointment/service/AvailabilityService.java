package com.cognizant.healthCareAppointment.service;

import com.cognizant.healthCareAppointment.dto.AvailabilityRequest;
import com.cognizant.healthCareAppointment.entity.Appointment;
import com.cognizant.healthCareAppointment.entity.Availability;
import com.cognizant.healthCareAppointment.repository.AppointmentRepository;
import com.cognizant.healthCareAppointment.repository.AvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AvailabilityService {
    @Autowired
    private AvailabilityRepository availabilityRepo;
    @Autowired
    private AppointmentRepository appointmentRepo;

    public ResponseEntity<String> updateAvailability(AvailabilityRequest request) {
        LocalDate localDate = request.getDate();
        if (request.getIsAvailable()) {
            LocalTime newEndTime = request.getEndTime();

            List<Appointment> appointments = appointmentRepo.findByDoctor_UserIdAndDate(request.getDoctorId(), localDate);

            boolean hasLateAppointment = appointments.stream()
                    .anyMatch(a -> a.getTimeSlot().isAfter(newEndTime));

            if (hasLateAppointment) {
                return ResponseEntity.badRequest().body("Cannot update availability. One or more appointments are booked beyond the new end time.");
            }
        }

        // ✅ Step 2: Either insert new or update existing availability
        Optional<Availability> existing = Optional.ofNullable(availabilityRepo.findByDoctorIdAndDate(request.getDoctorId(), localDate));

        Availability availability = existing.orElse(new Availability());
        availability.setDoctorId(request.getDoctorId());
        availability.setDate(localDate);
        availability.setIsAvailable(request.getIsAvailable());
        double diff = Duration.between(request.getStartTime(), request.getEndTime()).toMinutes() / 60.0;
    if(diff<5){
    return ResponseEntity.badRequest().body("Cannot update availability. Minimum work hours is 5HRS.");
    }
        if (request.getIsAvailable()) {
            availability.setStartTime(request.getStartTime());
            availability.setEndTime(request.getEndTime());
        } else {
            availability.setStartTime(null);
            availability.setEndTime(null);
        }

        availabilityRepo.save(availability);

        return existing.isPresent() ? ResponseEntity.ok("Availability updated") :ResponseEntity.ok( "Availability added");
    }
}
