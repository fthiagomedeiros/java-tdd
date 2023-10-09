package com.example.springtest.domain;

import com.example.springtest.json.adapter.LocalDateTimeDeserializer;
import com.example.springtest.json.adapter.LocalDateTimeSerializer;
import com.google.gson.GsonBuilder;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AddressDTO {

  private UUID id;

  private String street;

  @Override
  public String toString() {
    return new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
        .create()
        .toJson(this);
  }

}
