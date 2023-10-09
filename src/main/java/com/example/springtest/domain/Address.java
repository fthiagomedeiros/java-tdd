package com.example.springtest.domain;


import com.example.springtest.json.adapter.LocalDateTimeDeserializer;
import com.example.springtest.json.adapter.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.GsonBuilder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CUSTOMER_ADDRESS", schema = "customer_info")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Address {

  @Id
  @JsonIgnore
  @Column(name = "id", columnDefinition = "uuid")
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String street;

  @ManyToOne
  @MapsId
  @JoinColumn(name = "id")
  private Customer customer;

  @Override
  public String toString() {
    return new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
        .create()
        .toJson(this);
  }
}
