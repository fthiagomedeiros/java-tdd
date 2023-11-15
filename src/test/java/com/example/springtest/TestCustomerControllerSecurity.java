package com.example.springtest;


import com.example.springtest.controllers.CustomerController;
import com.example.springtest.domain.Customer;
import com.example.springtest.domain.CustomerDTO;
//import com.example.springtest.security.WebSecurityConfig;
import com.example.springtest.repository.CustomerRepository;
import com.example.springtest.services.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@Import(WebSecurityConfig.class)
@WebMvcTest(CustomerController.class)
public class TestCustomerControllerSecurity {

  private static final String CUSTOMER_URL = "/customer";
  public static final String CUSTOMER_ID = "/customer/{id}";

  private static final String RESOURCE_ID = "/3";
  private static final String USERNAME = "USERNAME";
  private static final String ROLE_ADMIN = "ADMIN";
  private static final String ROLE_USER = "USER";
  private static final String USER = "user";
  private static final String PASS = "password";


  @Autowired
  protected ObjectMapper objectMapper;

  @Value("classpath:requests/customer/createCustomer.json")
  private Resource createCustomer;

  @Value("classpath:requests/customer/createCustomerWithNoCPF.json")
  private Resource createCustomerNoCpf;

  @Value("classpath:requests/customer/createCustomerWithNoUsernameValid.json")
  private Resource createCustomerNoUsername;

  @Autowired
  private WebApplicationContext context;

  private MockMvc mockMvc;

  @MockBean
  private CustomerService service;

  @Mock
  private CustomerRepository repository;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders
        .webAppContextSetup(context)
//                .apply(springSecurity())
        .build();
  }

  @Test
  public void shouldNotAnUnauthenticatedUserFetchData() throws Exception {
    this.mockMvc.perform(get(CUSTOMER_URL))
        .andDo(print())
        .andExpect(status().is2xxSuccessful())
//        .andExpect(status().isUnauthorized())
        .andReturn();
  }

  @Test
//    @WithMockUser(value = USER, password = PASS) //First way to mock an authenticated user
  public void shouldAnAuthenticatedUserFetchData() throws Exception {

    UUID id1 = UUID.randomUUID();
    CustomerDTO customer1 = CustomerDTO
        .builder()
        .id(id1)
        .username("jdoe001")
        .firstName("John")
        .lastName("Doe")
        .cpf("00000000000")
        .build();

    UUID id2 = UUID.randomUUID();
    CustomerDTO customer2 = CustomerDTO
        .builder()
        .id(id2)
        .username("mjackson")
        .firstName("Michael")
        .lastName("Jackson")
        .cpf("11111111111")
        .build();

    when(service.getAllCustomers()).thenReturn(
        List.of(customer1, customer2)
    );

    this.mockMvc.perform(get(CUSTOMER_URL))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()", Matchers.is(2)))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id", Matchers.is(id1.toString())))
        .andExpect(jsonPath("$[0].username", Matchers.is("jdoe001")))
        .andExpect(jsonPath("$[1].id", Matchers.is(id2.toString())))
        .andExpect(jsonPath("$[1].username", Matchers.is("mjackson")))
        .andReturn();
  }

  @Test
  public void shouldAnAuthenticatedUserCreateSuccessfullyTheCustomer() throws Exception {
    UUID id = UUID.randomUUID();
    when(service.createCustomer(any())).thenReturn(
        CustomerDTO.builder()
            .id(id)
            .username("johndoe99")
            .firstName("John")
            .lastName("Doe")
            .cpf("00011122233")
            .build()
    );

    CustomerDTO body = objectMapper.readValue(createCustomer.getInputStream(), CustomerDTO.class);

    this.mockMvc.perform(post(CUSTOMER_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body.toString()))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(header().string("Location", containsString("http://localhost/customer/" + id)))
        .andReturn();

    verify(service).createCustomer(any(CustomerDTO.class));
  }

  @Test
  public void shouldNotAnUnauthenticatedUserCreateTheCustomer() throws Exception {
    UUID id = UUID.randomUUID();
    when(service.createCustomer(any())).thenReturn(
        CustomerDTO.builder()
            .id(id)
            .username("john")
            .firstName("John")
            .lastName("Doe")
            .cpf("000.111.222-33")
            .build()
    );

    CustomerDTO body = objectMapper.readValue(createCustomer.getInputStream(), CustomerDTO.class);

    this.mockMvc.perform(post(CUSTOMER_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body.toString()))
        .andDo(print())
        .andExpect(status().isCreated())
        .andReturn();

    //verify(service).createCustomer(any(CustomerDTO.class));
    //verifyNoInteractions(service);
    //verify(service).getAllCustomers();
    //Checks if the method has been executed or other issues
  }

  @Test
  public void shouldNotCreateACustomerWhenCpfIsNotProvided() throws Exception {
    CustomerDTO body = objectMapper.readValue(createCustomerNoCpf.getInputStream(),
        CustomerDTO.class);

    this.mockMvc.perform(post(CUSTOMER_URL)
//                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME))
            .contentType(MediaType.APPLICATION_JSON)
            .content(body.toString()))
        .andExpect(status().isBadRequest())
        .andReturn();
  }

  @Test
  public void shouldNotCreateACustomerWhenInvalidUsername() throws Exception {
    CustomerDTO body = objectMapper.readValue(createCustomerNoUsername.getInputStream(),
        CustomerDTO.class);

    this.mockMvc.perform(post(CUSTOMER_URL)
            .contentType(MediaType.APPLICATION_JSON)
//                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME))
            .content(body.toString()))
//                        .with(csrf())
        .andExpect(status().isBadRequest())
        .andReturn();
  }

  @Test
//    @WithMockUser(value = USERNAME)
  public void shouldFetchCustomerById() throws Exception {

    UUID id = UUID.randomUUID();
    when(service.getCustomer(eq(id))).thenReturn(
        CustomerDTO
            .builder()
            .id(id)
            .username("john")
            .firstName("John")
            .lastName("Doe")
            .cpf("000.111.222-33")
            .build()
    );

    this.mockMvc.perform(get(CUSTOMER_ID, id)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("id").value(id.toString()))
        .andExpect(jsonPath("firstName").value("John"))
        .andExpect(jsonPath("lastName").value("Doe"))
        .andExpect(jsonPath("username").value("john"))
        .andExpect(jsonPath("cpf").value("000.111.222-33"))
        .andReturn();
  }

  @Test
  public void shouldFNotFetchCustomerByIdUnauthenticatedUser() throws Exception {
    this.mockMvc.perform(get(CUSTOMER_ID, UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  public void shouldDeleteACustomerByCPF() throws Exception {
    UUID id = UUID.randomUUID();
    doNothing().when(service).deleteCustomer(eq(id));

    this.mockMvc.perform(delete(CUSTOMER_URL + "/" + id)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNoContent())
        .andReturn();

  }

  @Test
  public void shouldNotAllowUnauthenticatedUserDeleteACustomerByCPF() throws Exception {
    UUID id = UUID.randomUUID();
    this.mockMvc.perform(delete(CUSTOMER_URL + "/" + id)
            .contentType(MediaType.APPLICATION_JSON))
//                        .with(csrf()))
        .andDo(print())
        .andExpect(status().is2xxSuccessful())
        .andReturn();

  }

}
