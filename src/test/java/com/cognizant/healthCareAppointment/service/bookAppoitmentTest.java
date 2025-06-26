package com.cognizant.healthCareAppointment.service;
import com.cognizant.healthCareAppointment.dto.BookRequest;
import com.cognizant.healthCareAppointment.entity.*;
import com.cognizant.healthCareAppointment.exception.DoctorNotFoundException;
import com.cognizant.healthCareAppointment.exception.PatientNotFoundException;
import com.cognizant.healthCareAppointment.repository.AppointmentRepository;
import com.cognizant.healthCareAppointment.repository.AvailabilityRepository;
import com.cognizant.healthCareAppointment.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class bookAppoitmentTest {

    @InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private AppointmentRepository appointmentRepo;
    @Mock
    private AvailabilityRepository availabilityRepo;
    @Mock
    private UserRepository userRepo;

    private BookRequest bookRequest;
    private User patient;
    private User doctor;
    private Availability availability;

    @BeforeEach
    void setUp() {
        bookRequest = new BookRequest();
        bookRequest.setPatientId(4L);
        bookRequest.setDoctorId(5L);
        bookRequest.setTimeSlot(LocalTime.of(10, 30));

        patient = new User();
        patient.setUserId(4L);

        doctor = new User();
        doctor.setUserId(5L);

        availability = new Availability();
        availability.setDoctorId(5L);
        availability.setDate(LocalDate.now().plusDays(1));
        availability.setStartTime(LocalTime.of(9, 0));
        availability.setEndTime(LocalTime.of(17, 0));
        availability.setIsAvailable(true);
    }

    @Test
    void testBookAppointment_Success() {
        when(appointmentRepo.findByDoctor_UserIdAndDateAndTimeSlot(anyLong(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(availabilityRepo.findByDoctorIdAndDate(anyLong(), any()))
                .thenReturn(availability);
        when(userRepo.findById(4L)).thenReturn(Optional.of(patient));
        when(userRepo.findById(5L)).thenReturn(Optional.of(doctor));
        when(appointmentRepo.save(any(Appointment.class))).thenReturn(new Appointment());

        ResponseEntity<String> response = appointmentService.bookAppointment(bookRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Appointment booked successfully", response.getBody());
    }

    @Test
    void testBookAppointment_SlotAlreadyBooked() {
        Appointment booked = new Appointment();
        booked.setStatus(AppointmentStatus.BOOKED);
        when(appointmentRepo.findByDoctor_UserIdAndDateAndTimeSlot(anyLong(), any(), any()))
                .thenReturn(List.of(booked));

        ResponseEntity<String> response = appointmentService.bookAppointment(bookRequest);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Slot already booked", response.getBody());
    }

    @Test
    void testBookAppointment_BeyondEndTime() {
        // Set end time before the requested slot
        availability.setEndTime(LocalTime.of(10, 0)); // End time before requested slot
        when(appointmentRepo.findByDoctor_UserIdAndDateAndTimeSlot(anyLong(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(availabilityRepo.findByDoctorIdAndDate(anyLong(), any()))
                .thenReturn(availability);

        ResponseEntity<String> response = appointmentService.bookAppointment(bookRequest);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Cannot book appointment beyond doctor's available end time", response.getBody());
    }

    @Test
    void testBookAppointment_PatientNotFound() {
        when(appointmentRepo.findByDoctor_UserIdAndDateAndTimeSlot(anyLong(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(availabilityRepo.findByDoctorIdAndDate(anyLong(), any()))
                .thenReturn(availability);
        when(userRepo.findById(4L)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> appointmentService.bookAppointment(bookRequest));
    }

    @Test
    void testBookAppointment_DoctorNotFound() {
        when(appointmentRepo.findByDoctor_UserIdAndDateAndTimeSlot(anyLong(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(availabilityRepo.findByDoctorIdAndDate(anyLong(), any()))
                .thenReturn(availability);
        when(userRepo.findById(4L)).thenReturn(Optional.of(patient));
        when(userRepo.findById(5L)).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class, () -> appointmentService.bookAppointment(bookRequest));
    }


}
