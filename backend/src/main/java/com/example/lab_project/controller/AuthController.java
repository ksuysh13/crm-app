package com.example.lab_project.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/login")
    public Map<String, String> auth(Principal principal) {
        System.out.println(principal.getName());
        return Collections.singletonMap("role", 
            principal.getName().equals("admin") ? "ADMIN" : "USER");
    }
}
