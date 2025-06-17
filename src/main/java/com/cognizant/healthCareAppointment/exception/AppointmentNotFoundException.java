package com.cognizant.healthCareAppointment.exception;

public class AppointmentNotFoundException extends RuntimeException{
public AppointmentNotFoundException(String message){
    super(message);
}
}
