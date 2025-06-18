package com.cognizant.healthCareAppointment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoResponse {
    @NotNull(message = "Name is required")
    @Pattern(regexp = "^[A-Za-z ]+$",message = "Name must contains only letters and spaces")
private String name;
    @NotNull(message = "Email is required")
    private String email;
    @NotNull(message = "Phone is required")
    @Size(min=10,max=10,message = "Phone Number must be 10 digit")
    private String phone;

}
