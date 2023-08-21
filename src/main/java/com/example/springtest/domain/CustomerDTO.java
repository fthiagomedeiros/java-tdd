package com.example.springtest.domain;

import com.google.gson.Gson;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
