package com.example.springtest.domain;

import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HealthStatus {
    private String status;
    private LocalDateTime hour;

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }
}
