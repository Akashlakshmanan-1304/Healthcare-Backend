package com.cognizant.healthCareAppointment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEditRequest {
    private String name;
    private String phone;
    private String password;

}
