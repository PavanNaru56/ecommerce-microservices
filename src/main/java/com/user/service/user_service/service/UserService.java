package com.user.service.user_service.service;

import com.user.service.user_service.model.User;
import com.user.service.user_service.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user){
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
}
