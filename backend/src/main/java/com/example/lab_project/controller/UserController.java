package com.example.lab_project.controller;

import java.util.Objects;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.lab_project.model.User;
import com.example.lab_project.repository.UserRepository;
import com.example.lab_project.service.UserService;

@RestController
public class UserController {
    private final UserRepository userRepository;

    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        // return userRepository.save(user);
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return userService.verify(user);
    //    var u = userRepository.findByUserName(user.getUserName());
    //    if(!Objects.isNull(u))
    //        return "success";
    //    return "failure";
    }
}
