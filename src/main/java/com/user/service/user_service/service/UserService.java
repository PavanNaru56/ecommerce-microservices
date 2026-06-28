package com.user.service.user_service.service;

import com.user.service.user_service.DTO.UserRequest;
import com.user.service.user_service.model.Role;
import com.user.service.user_service.model.User;
import com.user.service.user_service.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(UserRequest userRequest){

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        if(userRequest.getEmail().equals("admin@gmail.com")){

            user.setRole(Role.ROLE_ADMIN);

        }else{
            user.setRole(Role.ROLE_USER);
        }

        return  userRepository.save(user);

    }

    public User login(String email, String password){

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!user.getPassword().equals(password)){
            throw new RuntimeException("Incorrect password");
        }

        return user;

    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
}
