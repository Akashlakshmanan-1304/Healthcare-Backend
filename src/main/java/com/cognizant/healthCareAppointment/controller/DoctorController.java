package com.cognizant.healthCareAppointment.controller;

import com.cognizant.healthCareAppointment.dto.AppointmentResponseDTO;
import com.cognizant.healthCareAppointment.dto.ConsultationRequest;
import com.cognizant.healthCareAppointment.dto.ConsultationResponseDTO;
import com.cognizant.healthCareAppointment.dto.DoctorInfoResponse;
import com.cognizant.healthCareAppointment.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping("/{doctorId}/appointments")
    public List<AppointmentResponseDTO> getDoctorAppointments(@PathVariable Long doctorId) {

        return doctorService.getDoctorAppointments(doctorId);
    }

    @PostMapping("/consultation")
    public ResponseEntity<String> addConsultation(@Valid @RequestBody ConsultationRequest request, BindingResult result) {
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(result.getFieldError().getDefaultMessage());
        }
        return doctorService.addConsultation(request);
    }
    @GetMapping("/{doctorId}/consultations")
    public List<ConsultationResponseDTO> getDoctorConsultations(@PathVariable Long doctorId) {

        return doctorService.getDoctorConsultations(doctorId);
    }
    @GetMapping("/{doctorId}/details")
    public  DoctorInfoResponse getDoctorInfo(@PathVariable Long doctorId){
        return doctorService.getDoctorInfo(doctorId);
    }
}