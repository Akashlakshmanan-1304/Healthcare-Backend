package com.cognizant.healthCareAppointment.exception;

public class AvailabilityNotFoundException extends RuntimeException{
    public AvailabilityNotFoundException(String msg){
        super(msg);
    }
}
