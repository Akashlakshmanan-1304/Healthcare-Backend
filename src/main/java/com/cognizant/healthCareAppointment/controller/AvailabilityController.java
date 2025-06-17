package com.cognizant.healthCareAppointment.controller;

import com.cognizant.healthCareAppointment.dto.AvailabilityRequest;
import com.cognizant.healthCareAppointment.service.AvailabilityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {
    @Autowired
    private AvailabilityService availabilityService;


    @PostMapping("/update-availability")
    public ResponseEntity<String> updateAvailability(@Valid @RequestBody AvailabilityRequest request, BindingResult result) {
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(result.getFieldError().getDefaultMessage());
        }
        return availabilityService.updateAvailability(request);
    }
}

