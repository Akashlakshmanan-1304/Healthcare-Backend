package com.cognizant.healthCareAppointment.service;

import com.cognizant.healthCareAppointment.dto.LoginRequest;
import com.cognizant.healthCareAppointment.dto.LoginResponse;
import com.cognizant.healthCareAppointment.dto.RegisterRequest;
import com.cognizant.healthCareAppointment.dto.RegisterResponse;
import com.cognizant.healthCareAppointment.entity.User;
import com.cognizant.healthCareAppointment.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtService jwtService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    public ResponseEntity<RegisterResponse> register(RegisterRequest request) {


        User user=User.builder()
                .name(request.getName())
                .role(request.getRole())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        try{
            userRepository.save(user);
        }
        catch (Exception e)
        {
            log.error("Oops! Already a user");
            return ResponseEntity.badRequest().body(new RegisterResponse("Email already exists","null-token"));
        }
        String token=jwtService.generateToken(user);
        log.info("User Registration Phase Run Successful");
        return ResponseEntity.ok(new RegisterResponse("User registered successfully as "+ request.getRole(),token));
    }


    public ResponseEntity<LoginResponse> login(LoginRequest request) {

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
            User user=userRepository.findByEmail(request.getEmail()).orElseThrow(()->new RuntimeException("User not found"));
            String token=jwtService.generateToken(user);
            log.info("User Login Phase Run Successful");
            return ResponseEntity.ok(new LoginResponse("Login successful as " + user.getRole(),token));

        }
        catch(Exception e){
            log.error("Oops! Invalid Credentials"+e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("Invalid email or password","null-token"));

        }

       }
}
