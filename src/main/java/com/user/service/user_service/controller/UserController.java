package com.user.service.user_service.controller;

import com.user.service.user_service.DTO.UserRequest;
import com.user.service.user_service.model.User;
import com.user.service.user_service.security.JwtUtility;
import com.user.service.user_service.service.CustomUserDetails;
import com.user.service.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    private final CustomUserDetails userDetailsService;
    private final  AuthenticationManager authenticationManager;
    private final JwtUtility  jwtUtility;


    UserController(UserService userService, CustomUserDetails userDetailsService, AuthenticationManager authenticationManager, JwtUtility jwtUtility) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtUtility = jwtUtility;

    }

    @PostMapping("/register")
    public User registerUser(@Valid @RequestBody UserRequest userRequest){

        return  userService.registerUser(userRequest);

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserRequest userRequest){

        try{

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userRequest.getUsername(),
                            userRequest.getPassword()
                    )
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(userRequest.getUsername());

            String token = jwtUtility.generateToken(userDetails);

            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        catch(BadCredentialsException ex){
            System.err.println("Invalid username or password");
            throw new BadCredentialsException("Invalid username or password");
        }


    }

    @GetMapping("/list")
    public ResponseEntity<?> getUserList(){

        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


}
