package com.cognizant.healthCareAppointment.exception;

public class DoctorNotFoundException extends RuntimeException{
    public DoctorNotFoundException(String msg){
        super(msg);
    }
}
