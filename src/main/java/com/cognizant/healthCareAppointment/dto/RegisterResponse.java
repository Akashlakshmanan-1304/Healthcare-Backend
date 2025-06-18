package com.cognizant.healthCareAppointment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    @NotBlank(message = "Message is required")
    private String message;
    @NotBlank(message = "Token is required")
    private  String token;

}
