package com.cognizant.healthCareAppointment.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message="Name is required")
    @Pattern(regexp = "^[A-Za-z ]+$",message = "Name must contains only letters and spaces")
    private String name;

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "Doctor|Patient",message="Role must be Doctor or Patient")
    private String role;
    @NotBlank(message = "Email is required")
    @Email(message="Invalid email format")
    private String email;
    @NotBlank(message = "Phone is required")
    @Size(min=10,max=10,message = "Phone Number must be 10 digit")
    private String phone;
    @NotBlank(message = "Password is required")
    @Size(min=6,message = "Password must be at least 6 characters")
    private String password;
}