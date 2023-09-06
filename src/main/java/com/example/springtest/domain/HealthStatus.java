package com.example.springtest.domain;

import com.example.springtest.json.adapter.LocalDateTimeDeserializer;
import com.example.springtest.json.adapter.LocalDateTimeSerializer;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HealthStatus {
    private String status;
    private LocalDateTime hour;

    @Override
    public String toString() {
        return new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
            .create()
            .toJson(this);
    }
}
