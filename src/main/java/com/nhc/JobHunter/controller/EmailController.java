package com.nhc.JobHunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhc.JobHunter.service.SubscriberService;
import com.nhc.JobHunter.util.anotation.ApiMessage;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1")
public class EmailController {

    private final SubscriberService subscriberService;

    public EmailController(
            SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @GetMapping("/emails")
    @ApiMessage("Send simple email")
    // @Scheduled(cron = "0 0 0 1W * *")
    // @Transactional
    public String sendSimpleEmail() {
        this.subscriberService.sendSubscribersEmailJobs();
        return new String();
    }

}
