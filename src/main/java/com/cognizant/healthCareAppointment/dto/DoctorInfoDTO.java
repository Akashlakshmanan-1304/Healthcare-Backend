package com.cognizant.healthCareAppointment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DoctorInfoDTO {
    @NotNull(message = "Doctor ID is required")
    private Long doctorId;
    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[A-Za-z ]+$",message = "Name must contains only letters and spaces")
    private String name;
    @NotBlank(message = "Specialization is required")
    private String phone;
    @NotBlank(message = "Phone is required")
    @Size(min=10,max=10,message = "Phone Number must be 10 digit")
    private String email;
}
