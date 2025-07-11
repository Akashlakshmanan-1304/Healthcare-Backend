package com.cognizant.healthCareAppointment.service;

import com.cognizant.healthCareAppointment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(user-> User.withUsername(user.getEmail())
                        .password(user.getPassword())
                        .roles(user.getRole())
                        .build()
                ).orElseThrow(()->new UsernameNotFoundException("User not found"));

    }
}
