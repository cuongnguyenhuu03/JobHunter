package com.nhc.JobHunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhc.JobHunter.service.EmailService;
import com.nhc.JobHunter.util.anotation.ApiMessage;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/emails")
    @ApiMessage("Send simple email")
    public String sendSimpleEmail() {
        this.emailService.sendSimpleEmail();
        return new String();
    }

}
