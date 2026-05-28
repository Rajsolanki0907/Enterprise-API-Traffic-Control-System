package com.raj.traffic_control_system.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateLimiterController {

    @GetMapping("/limited")
    public String limitedApi() {

        return "Request Allowed";
    }
}