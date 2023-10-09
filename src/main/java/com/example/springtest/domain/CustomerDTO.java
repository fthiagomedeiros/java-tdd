package com.example.springtest.domain;

import com.example.springtest.json.adapter.LocalDateTimeDeserializer;
import com.example.springtest.json.adapter.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.GsonBuilder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CustomerDTO {

  private UUID id;
  private String firstName;
  private String lastName;

  @NotBlank
  @NotNull
  @Size(min = 8, max = 100)
  private String username;

  @NotBlank
  @Size(min = 11, max = 11)
  private String cpf;

  private String fullName;

  /**
   * using this annotation JsonFormat we inform the format of the LocalDateTime passed as parameter
   * It means the first payload would be accepted, but the second one wouldn't
   * <p>
   * { "firstName": "Francisco", "lastName": "Medeiros", "username": "fmedeiro00", "cpf":
   * "33525666811", "fullName": "Francisco Thiago", "birth": "23/02/1985 11:11" }
   * <p>
   * { "firstName": "Joao", "lastName": "Medeiros", "username": "fmedeiro01", "cpf": "99999999900",
   * "fullName": "Joao Medeiros", "birth": "23/02/1983" << missing HH:mm }
   */
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
  private LocalDateTime birth;

  @Override
  public String toString() {
    return new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
        .create()
        .toJson(this);
  }
}
