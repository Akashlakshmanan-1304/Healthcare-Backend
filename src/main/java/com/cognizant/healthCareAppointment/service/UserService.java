package com.cognizant.healthCareAppointment.service;

import com.cognizant.healthCareAppointment.dto.AppointmentResponseDTO;
import com.cognizant.healthCareAppointment.dto.ConsultationResponseDTO;
import com.cognizant.healthCareAppointment.dto.UserInfoResponse;
import com.cognizant.healthCareAppointment.entity.Appointment;
import com.cognizant.healthCareAppointment.entity.Consultation;
import com.cognizant.healthCareAppointment.entity.User;
import com.cognizant.healthCareAppointment.repository.AppointmentRepository;
import com.cognizant.healthCareAppointment.repository.ConsultationRepository;
import com.cognizant.healthCareAppointment.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    @Autowired
    private AppointmentRepository appointmentRepo;
    @Autowired
    private ConsultationRepository consultationRepo;
    @Autowired
    private UserRepository userRepository;


    public List<ConsultationResponseDTO> getPatientConsultations( Long patientId) {
        List<Appointment> patientAppointments = appointmentRepo.findByPatient_UserId(patientId);

        List<Long> appointmentIds = patientAppointments.stream()
                .map(Appointment::getAppointmentId)
                .toList();

        List<Consultation>consultations= consultationRepo.findByAppointment_AppointmentIdIn(appointmentIds);
        return consultations.stream().map(con -> new ConsultationResponseDTO(con.getConsultationId(), con.getAppointment().getAppointmentId(),con.getAppointment().getPatient().getName(),con.getAppointment().getDoctor().getName(),con.getNotes(),con.getPrescription(),con.getAppointment().getDate() )).toList();

    }

    public List<AppointmentResponseDTO> getDoctorAppointments(Long patientId) {
        List<Appointment> appointments = appointmentRepo.findByPatient_UserId(patientId);
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        for (Appointment a : appointments) {
            if (a.getStatus() == com.cognizant.healthCareAppointment.entity.AppointmentStatus.BOOKED) {
                if (a.getDate().isBefore(today) || (a.getDate().isEqual(today) && a.getTimeSlot().isBefore(now))) {
                    a.setStatus(com.cognizant.healthCareAppointment.entity.AppointmentStatus.CANCELLED);
                    appointmentRepo.save(a);
                }
            }
        }
        return appointments.stream().map(a -> new AppointmentResponseDTO(a.getAppointmentId(), a.getDoctor().getName(), a.getPatient().getName(), a.getDate(), a.getTimeSlot(), a.getStatus().name())).toList();
    }
    public UserInfoResponse userDetails(Long userId) {
        Optional<User> userOpt=userRepository.findById(userId);
        if(userOpt.isPresent()){
            User user= userOpt.get();
            UserInfoResponse userInfoResponse=UserInfoResponse.builder()
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .name(user.getName())
                    .build();
            return userInfoResponse;
        }
        else{
            return null;
        }
    }
}
