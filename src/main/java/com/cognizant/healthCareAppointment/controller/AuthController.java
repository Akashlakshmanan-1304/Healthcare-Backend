package com.cognizant.healthCareAppointment.controller;


import com.cognizant.healthCareAppointment.dto.LoginRequest;
import com.cognizant.healthCareAppointment.dto.LoginResponse;
import com.cognizant.healthCareAppointment.dto.RegisterRequest;
import com.cognizant.healthCareAppointment.dto.RegisterResponse;
import com.cognizant.healthCareAppointment.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")

public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request, BindingResult result) {
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(new RegisterResponse(result.getFieldError().getDefaultMessage(),"null-token"));
        }
        return authService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request, BindingResult result) {
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(new LoginResponse(result.getFieldError().getDefaultMessage(),"null-token"));
        }
        return authService.login(request);
    }

}
