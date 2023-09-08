package com.example.springtest.domain;

import com.example.springtest.json.adapter.LocalDateTimeDeserializer;
import com.example.springtest.json.adapter.LocalDateTimeSerializer;
import com.google.gson.GsonBuilder;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HealthStatus {

    private String environment;
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
