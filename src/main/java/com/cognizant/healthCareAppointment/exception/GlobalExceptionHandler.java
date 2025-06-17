package com.cognizant.healthCareAppointment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AppointmentNotFoundException.class)
public ResponseEntity<String> handleAppointmentNotFound(AppointmentNotFoundException ex){
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
}
@ExceptionHandler(HttpMessageNotReadableException.class)
public ResponseEntity<String> handleMessageNotReadable(HttpMessageNotReadableException ex)
{
        String message="Invalid Request format. May be Date/time format!";
        return ResponseEntity.badRequest().body(message);
}
@ExceptionHandler(PatientNotFoundException.class)
public ResponseEntity<String> handlePatientNotFound(PatientNotFoundException ex)
{
        return ResponseEntity.badRequest().body(ex.getMessage());
}
@ExceptionHandler(DoctorNotFoundException.class)
    public ResponseEntity<String> handleDoctorNotFound(DoctorNotFoundException ex)
    {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
    @ExceptionHandler(AvailabilityNotFoundException.class)
    public ResponseEntity<String> handleAvailabilityNotFound(AvailabilityNotFoundException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
