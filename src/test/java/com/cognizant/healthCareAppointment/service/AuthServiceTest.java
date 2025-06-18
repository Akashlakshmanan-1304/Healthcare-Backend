package com.cognizant.healthCareAppointment.service;

import com.cognizant.healthCareAppointment.dto.RegisterRequest;
import com.cognizant.healthCareAppointment.dto.RegisterResponse;
import com.cognizant.healthCareAppointment.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepo;

    @Test
    void testUserRegistration() {
        RegisterRequest request = new RegisterRequest();
        request.setName("TestUser");
        request.setEmail("test@example.com");
        request.setPhone("9999999999");
        request.setRole("Patient");
        request.setPassword("test123");

        ResponseEntity<RegisterResponse> response = authService.register(request);
        assertEquals(200, response.getStatusCodeValue());
        Assertions.assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("User registered successfully"));

        // Clean up
        userRepo.delete(Objects.requireNonNull(userRepo.findByEmail("test@example.com").orElse(null)));
    }
}