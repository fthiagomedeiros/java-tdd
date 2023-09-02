package com.example.springtest.controllers;

import com.example.springtest.domain.HealthStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<HealthStatus> getHealth() {
        HealthStatus status = new HealthStatus("UP");
        return ResponseEntity
                .ok()
                .body(status);
    }

}
