package com.example.springtest.services;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.springtest.domain.Customer;
import com.example.springtest.domain.CustomerDTO;
import com.example.springtest.extension.MapStructsResolver;
import com.example.springtest.mapper.CustomerMapper;
import com.example.springtest.repository.CustomerRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Setter
@ExtendWith({MapStructsResolver.class, MockitoExtension.class})
public class CustomerServiceTest {

  @Mock
  private CustomerRepository repository;

  private CustomerMapper mapper;

  @InjectMocks
  private CustomerService cut;

  private Customer customer;

  @BeforeEach
  public void setUp() {
    //work around to inject the mapper a service dependencies
    cut = new CustomerService(repository, mapper);
    customer = Customer.builder()
        .id(UUID.randomUUID())
        .firstName("Francisco")
        .lastName("Medeiros")
        .username("fmedeiros")
        .cpf("11111111111")
        .fullName("Francisco Silva Medeiros")
        .birth(LocalDateTime.of(1985, 2, 23, 0, 1))
        .build();
  }

  @DisplayName("Testing for the entity customer and extensions")
  @Test
  void firstTestValidation() {
    //In fact there is no reason to test this method, once all this code is generated automatically
    //by MapStructs and mocking the response from JpaRepository
    when(repository.findAll()).thenReturn(List.of(customer));

    //Fetch all customers
    List<CustomerDTO> items = cut.getAllCustomers();
    assertThat(items.size()).isEqualTo(1);
    assertThat(items.size()).isNotNull();

    //Check number of invocations
    verify(repository, times(1)).findAll();
  }

}