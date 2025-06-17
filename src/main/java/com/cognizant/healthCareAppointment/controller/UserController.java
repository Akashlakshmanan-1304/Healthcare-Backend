package com.cognizant.healthCareAppointment.controller;

import com.cognizant.healthCareAppointment.dto.AppointmentResponseDTO;
import com.cognizant.healthCareAppointment.dto.ConsultationResponseDTO;
import com.cognizant.healthCareAppointment.dto.UserInfoResponse;
import com.cognizant.healthCareAppointment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{patientId}/appointments")
    public List<AppointmentResponseDTO> getDoctorAppointments(@PathVariable Long patientId) {

        return userService.getDoctorAppointments(patientId);
    }
    @GetMapping("/{patientId}/consultations")
    public List<ConsultationResponseDTO> getPatientConsultations(@PathVariable Long patientId) {

        return userService.getPatientConsultations(patientId);
    }
    @GetMapping("/{userId}/details")
    public UserInfoResponse getUserInfo(@PathVariable Long userId){
        return userService.userDetails(userId);
    }
}