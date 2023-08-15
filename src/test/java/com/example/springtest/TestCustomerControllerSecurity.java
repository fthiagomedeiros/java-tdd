//package com.example.springtest;
//
//
//import com.example.springtest.controllers.CustomerController;
//import com.example.springtest.domain.CustomerDTO;
//import com.example.springtest.security.WebSecurityConfig;
//import com.example.springtest.services.CustomerService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.hamcrest.Matchers;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.core.io.Resource;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.List;
//
//import static org.hamcrest.Matchers.containsString;
//import static org.hamcrest.Matchers.hasSize;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(CustomerController.class)
//@Import(WebSecurityConfig.class)
//public class TestCustomerControllerSecurity {
//
//    private static final String CUSTOMER_URL = "/customer";
//    private static final String RESOURCE_ID = "/3";
//    private static final String USERNAME = "USERNAME";
//    private static final String ROLE_ADMIN = "ADMIN";
//    private static final String ROLE_USER = "USER";
//    private static final String USER = "user";
//    private static final String PASS = "password";
//
//    @Autowired
//    protected ObjectMapper objectMapper;
//
//    @Value("classpath:requests/customer/createCustomer.json")
//    private Resource createCustomer;
//
//    @Value("classpath:requests/customer/createCustomerWithNoCPF.json")
//    private Resource createCustomerNoCpf;
//
//    @Value("classpath:requests/customer/createCustomerWithNoUsernameValid.json")
//    private Resource createCustomerNoUsername;
//
//    @Autowired
//    private WebApplicationContext context;
//
//    private MockMvc mockMvc;
//
//    @MockBean
//    private CustomerService service;
//
//    @BeforeEach
//    public void setup() {
//        this.mockMvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .apply(springSecurity())
//                .build();
//    }
//
//    @Test
//    public void shouldNotAnUnauthenticatedUserFetchData() throws Exception {
//        this.mockMvc.perform(get(CUSTOMER_URL))
//                .andDo(print())
//                .andExpect(status().isUnauthorized())
//                .andReturn();
//    }
//
//    @Test
//    @WithMockUser(value = USER, password = PASS) //First way to mock an authenticated user
//    public void shouldAnAuthenticatedUserFetchData() throws Exception {
//
//        CustomerDTO customer1 = CustomerDTO
//                .builder()
//                .id(1L)
//                .username("jdoe")
//                .firstName("John")
//                .lastName("Doe")
//                .cpf("000.000.000-00")
//                .build();
//
//        CustomerDTO customer2 = CustomerDTO
//                .builder()
//                .id(2L)
//                .username("mjackson")
//                .firstName("Michael")
//                .lastName("Jackson")
//                .cpf("111.111.111-11")
//                .build();
//
//        when(service.getAllCustomers()).thenReturn(
//                List.of(customer1, customer2)
//        );
//
//        this.mockMvc.perform(get(CUSTOMER_URL))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()", Matchers.is(2)))
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
//                .andExpect(jsonPath("$[0].username", Matchers.is("jdoe")))
//                .andExpect(jsonPath("$[1].id", Matchers.is(2)))
//                .andExpect(jsonPath("$[1].username", Matchers.is("mjackson")))
//                .andReturn();
//    }
//
//    @Test
//    public void shouldAnAuthenticatedUserCreateSuccessfullyTheCustomer() throws Exception {
//        when(service.createCustomer(any())).thenReturn(
//                CustomerDTO.builder()
//                        .id(2L)
//                        .username("john")
//                        .firstName("John")
//                        .lastName("Doe")
//                        .cpf("000.111.222-33")
//                        .build()
//        );
//
//        CustomerDTO body = objectMapper.readValue(createCustomer.getInputStream(), CustomerDTO.class);
//
//        this.mockMvc.perform(post(CUSTOMER_URL)
//                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASS))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body.toString()))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(header().exists("Location"))
//                .andExpect(header().string("Location", containsString("http://localhost/customer/2")))
//                .andReturn();
//
//        verify(service).createCustomer(any(CustomerDTO.class));
//    }
//
//    @Test
//    public void shouldNotAnUnauthenticatedUserCreateTheCustomer() throws Exception {
//        when(service.createCustomer(any())).thenReturn(
//                CustomerDTO.builder()
//                        .id(2L)
//                        .username("john")
//                        .firstName("John")
//                        .lastName("Doe")
//                        .cpf("000.111.222-33")
//                        .build()
//        );
//
//        CustomerDTO body = objectMapper.readValue(createCustomer.getInputStream(), CustomerDTO.class);
//
//        this.mockMvc.perform(post(CUSTOMER_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body.toString()))
//                .andDo(print())
//                .andExpect(status().isUnauthorized())
//                .andReturn();
//
//        //verify(service).createCustomer(any(CustomerDTO.class));
//        //verifyNoInteractions(service);
//        //verify(service).getAllCustomers();
//        //Checks if the method has been executed or other issues
//    }
//
//    @Test
//    public void shouldNotCreateACustomerWhenCpfIsNotProvided() throws Exception {
//        CustomerDTO body = objectMapper.readValue(createCustomerNoCpf.getInputStream(), CustomerDTO.class);
//
//        this.mockMvc.perform(post(CUSTOMER_URL)
//                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body.toString()))
//                .andExpect(status().isBadRequest())
//                .andReturn();
//    }
//
//    @Test
//    public void shouldNotCreateACustomerWhenInvalidUsername() throws Exception {
//        CustomerDTO body = objectMapper.readValue(createCustomerNoUsername.getInputStream(), CustomerDTO.class);
//
//        this.mockMvc.perform(post(CUSTOMER_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME))
//                        .content(body.toString())
//                        .with(csrf()))
//                .andExpect(status().isBadRequest())
//                .andReturn();
//    }
//
//    @Test
//    @WithMockUser(value = USERNAME)
//    public void shouldFetchCustomerById() throws Exception {
//        when(service.getCustomer(any())).thenReturn(
//                CustomerDTO
//                        .builder()
//                        .id(3L)
//                        .username("john")
//                        .firstName("John")
//                        .lastName("Doe")
//                        .cpf("000.111.222-33")
//                        .build()
//        );
//
//        this.mockMvc.perform(get(CUSTOMER_URL + "/3")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("id").value(3))
//                .andExpect(jsonPath("firstName").value("John"))
//                .andExpect(jsonPath("lastName").value("Doe"))
//                .andExpect(jsonPath("username").value("john"))
//                .andExpect(jsonPath("cpf").value("000.111.222-33"))
//                .andReturn();
//    }
//
//    @Test
//    public void shouldFNotFetchCustomerByIdUnauthenticatedUser() throws Exception {
//        this.mockMvc.perform(get(CUSTOMER_URL + "/3")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    @WithMockUser(username = USER)
//    public void shouldDeleteACustomerByCPF() throws Exception {
//        when(service.deleteCustomer(any())).thenReturn(
//                CustomerDTO
//                        .builder()
//                        .id(3L)
//                        .username("john")
//                        .firstName("John")
//                        .lastName("Doe")
//                        .cpf("000.111.222-33")
//                        .build()
//        );
//
//        this.mockMvc.perform(delete(CUSTOMER_URL + RESOURCE_ID)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isNoContent())
//                .andReturn();
//
//    }
//
//    @Test
//    public void shouldNotAllowUnauthenticatedUserDeleteACustomerByCPF() throws Exception {
//        this.mockMvc.perform(delete(CUSTOMER_URL + RESOURCE_ID)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .with(csrf()))
//                .andDo(print())
//                .andExpect(status().isUnauthorized())
//                .andReturn();
//
//    }
//
//}
