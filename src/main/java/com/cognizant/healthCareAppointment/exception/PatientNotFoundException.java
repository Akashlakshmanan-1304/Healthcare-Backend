package com.cognizant.healthCareAppointment.exception;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(String msg) {
        super(msg);
    }
}