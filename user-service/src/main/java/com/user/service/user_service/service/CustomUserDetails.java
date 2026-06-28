package com.user.service.user_service.service;

import com.user.service.user_service.model.User;
import com.user.service.user_service.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetails implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetails(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username){

        User user = userRepository.findByUsername((username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

//        System.out.println("Username: " + user.getUsername());
//        System.out.println("Role: " + user.getRole());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().toString().replace("ROLE_", ""))
                .build();

    }



}
