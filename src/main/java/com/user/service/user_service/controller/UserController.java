package com.user.service.user_service.controller;

import com.user.service.user_service.model.User;
import com.user.service.user_service.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user){

        return  userService.registerUser(user);

    }

    @GetMapping("/login")
    public User login(@RequestBody User user){
        return userService.login(user.getEmail(),user.getPassword());
    }
}
