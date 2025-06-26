package com.cognizant.healthCareAppointment.service;

import com.cognizant.healthCareAppointment.dto.*;
import com.cognizant.healthCareAppointment.entity.Appointment;
import com.cognizant.healthCareAppointment.entity.AppointmentStatus;
import com.cognizant.healthCareAppointment.entity.Consultation;
import com.cognizant.healthCareAppointment.entity.User;
import com.cognizant.healthCareAppointment.exception.AppointmentNotFoundException;
import com.cognizant.healthCareAppointment.repository.AppointmentRepository;
import com.cognizant.healthCareAppointment.repository.ConsultationRepository;
import com.cognizant.healthCareAppointment.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DoctorService {
    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private ConsultationRepository consultationRepo;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public DoctorInfoResponse getDoctorInfo(Long doctorId) {
        Optional<User> userOpt=userRepository.findById(doctorId);
        if(userOpt.isPresent()){
            User user= userOpt.get();
            DoctorInfoResponse doctorInfoResponse=DoctorInfoResponse.builder()
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .name(user.getName())
                    .build();
            return doctorInfoResponse;
        }
        else{
            return null;
        }
    }

    public ResponseEntity<String> userEditDetails(Long userId, UserEditRequest userEditRequest) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (!passwordEncoder.matches(userEditRequest.getPassword(),user.getPassword())) {
                System.out.println(user.getPassword()+"  "+passwordEncoder.encode(userEditRequest.getPassword())+" "+userEditRequest.getPassword());
                return ResponseEntity.status(401).body("Invalid password.");
            }
            user.setName(userEditRequest.getName());
            user.setPhone(userEditRequest.getPhone());
            userRepository.save(user);
            return ResponseEntity.ok("User profile updated successfully.");
        } else {
            return ResponseEntity.status(404).body("User not found.");
        }
    }

    public List<AppointmentResponseDTO> getDoctorAppointments(Long doctorId) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        List<Appointment> appointments = appointmentRepo.findByDoctor_UserIdAndDate(doctorId,today);

        for (Appointment a : appointments) {
            if (a.getStatus() == com.cognizant.healthCareAppointment.entity.AppointmentStatus.BOOKED) {
                if ( a.getTimeSlot().isBefore(now)) {
                    a.setStatus(com.cognizant.healthCareAppointment.entity.AppointmentStatus.CANCELLED);
                    appointmentRepo.save(a);
                }
            }
        }
        return appointments.stream().map(a -> new AppointmentResponseDTO(a.getAppointmentId(), a.getDoctor().getName(), a.getPatient().getName(), a.getDate(), a.getTimeSlot(), a.getStatus().name())).toList();
    }

    public ResponseEntity<String> addConsultation( ConsultationRequest request) {

        Appointment appointment = appointmentRepo.findById(request.getAppointmentId())
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment with ID "+request.getAppointmentId()+" not found"));

        if (appointment.getStatus() != AppointmentStatus.BOOKED) {

            return ResponseEntity.badRequest().body("Appointment already completed or invalid");
        }

        Consultation consultation = new Consultation();
        consultation.setAppointment(appointment);
        consultation.setNotes(request.getNotes());
        consultation.setPrescription(request.getPrescription());
        consultationRepo.save(consultation);

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepo.save(appointment);

        return ResponseEntity.ok("Consultation added and appointment marked as COMPLETED");
    }

    public List<ConsultationResponseDTO> getDoctorConsultations( Long doctorId) {
        List<Appointment> doctorAppointments = appointmentRepo.findByDoctor_UserId(doctorId);

        List<Long> appointmentIds = doctorAppointments.stream()
                .map(Appointment::getAppointmentId)
                .toList();

        List<Consultation>consultations= consultationRepo.findByAppointment_AppointmentIdIn(appointmentIds);
        return consultations.stream().map(con -> new ConsultationResponseDTO(con.getConsultationId(), con.getAppointment().getAppointmentId(),con.getAppointment().getPatient().getName(),con.getAppointment().getDoctor().getName(),con.getNotes(),con.getPrescription(),con.getAppointment().getDate() )).toList();
    }


    public ResponseEntity<String> updateConsultation(Long id, ConsultationUpdateRequest request) {
        Optional<Consultation> consultationOpt = consultationRepo.findById(id);
        if (consultationOpt.isPresent()) {
            Consultation consultation = consultationOpt.get();
            consultation.setNotes(request.getNotes());
            consultation.setPrescription(request.getPrescriptions());
            consultationRepo.save(consultation);
            return ResponseEntity.ok("Consultation updated successfully.");
        } else {
            return ResponseEntity.status(404).body("Consultation not found.");
        }
    }

}
