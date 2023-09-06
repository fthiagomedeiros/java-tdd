package com.example.springtest.controllers;

import com.example.springtest.domain.HealthStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/health")
public class HealthController {

  private final Logger logger = LoggerFactory.getLogger(HealthController.class);

  @GetMapping
  public ResponseEntity<HealthStatus> getHealth() {
    logger.info("Getting health status");

    HealthStatus status = HealthStatus.builder()
        .status("UP")
        .hour(LocalDateTime.now())
        .build();

    logger.info(status.toString());

    return ResponseEntity
        .ok()
        .body(status);
  }

}
