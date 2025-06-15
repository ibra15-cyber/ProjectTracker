package com.ibra.projecttracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/oauth2/success")
public class SuccessController {

    @GetMapping
    public String success() {
        return "Welcome to Project Tracker API";
    }
}
