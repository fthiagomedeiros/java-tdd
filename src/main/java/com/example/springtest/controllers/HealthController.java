package com.example.springtest.controllers;

import com.example.springtest.domain.HealthStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/health")
public class HealthController {

  @GetMapping
  public ResponseEntity<HealthStatus> getHealth() {
    HealthStatus status = HealthStatus.builder()
        .status("UP")
        .hour(LocalDateTime.now())
        .build();

    return ResponseEntity
        .ok()
        .body(status);
  }

}
