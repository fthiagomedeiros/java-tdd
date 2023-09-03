package com.example.springtest.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "CUSTOMERS", schema = "customer_info")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Customer {

  @Id
  @Column(name = "id", columnDefinition = "uuid")
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String firstName;
  private String lastName;
  private String username;

  @Size(min = 11, max = 11)
  private String cpf;

  private String fullName;

  private LocalDateTime birth;
}
