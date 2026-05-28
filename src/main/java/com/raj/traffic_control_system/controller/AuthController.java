package com.raj.traffic_control_system.controller;

import com.raj.traffic_control_system.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Map<String, String> login(
            @RequestParam String username,
            @RequestParam String role
    ) {

        String token =
                jwtUtil.generateToken(username, role);

        return Map.of(
                "token",
                token
        );
    }
}