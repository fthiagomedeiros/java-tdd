package com.example.springtest.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.springtest.IntegrationTest;
import com.example.springtest.domain.AddressDTO;
import com.example.springtest.domain.Customer;
import com.example.springtest.domain.CustomerDTO;
import com.example.springtest.mapper.CustomerMapper;
import com.example.springtest.repository.CustomerRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

@IntegrationTest
public class UpdateCustomerAddressIT {

    private final String CUSTOMER_URL = "/customer";
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CustomerMapper mapper;

    @Autowired
    private CustomerRepository repository;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
//                .apply(springSecurity())
                .build();
    }


    @Container
    public static PostgreSQLContainer postgres =
            new PostgreSQLContainer(DockerImageName.parse("postgres:alpine:3.18"))
                    .withUsername("springboot")
                    .withPassword("springboot")
                    .withDatabaseName("customer_info");

    @DynamicPropertySource
    static void setDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
    }

    @Test
    @Transactional
    public void testCreateCustomerSuccessfully() throws Exception {
        CustomerDTO body = CustomerDTO.builder()
                .username("johndoe1994")
                .firstName("John")
                .lastName("Doe")
                .cpf("00011122233")
                .fullName("John Doe da Silva")
                .birth(LocalDateTime.parse("23/02/1985 10:00", FORMATTER))
                .build();

        //This is calling two endpoints
        MvcResult response = this.mockMvc.perform(post(CUSTOMER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.toString()))
                .andExpect(status().isCreated())
                .andReturn();

        String customerCreatedId = Objects.requireNonNull(response.getResponse().getHeader("Location"))
                .substring(26);
        Optional<Customer> customerCreated = repository.findById(UUID.fromString(customerCreatedId));

        //Validate customer has been created
        assertTrue(customerCreated.isPresent());
        assertEquals("johndoe1994", customerCreated.get().getUsername());
        assertEquals("John", customerCreated.get().getFirstName());
        assertEquals(LocalDateTime.parse("23/02/1985 10:00", FORMATTER), customerCreated.get().getBirth());
    }

    @Test
    @Transactional
    public void testUpdateCustomerAddress() throws Exception {
        CustomerDTO body = CustomerDTO.builder()
                .username("fmedeiros")
                .firstName("Francisco")
                .lastName("Medeiros")
                .cpf("99999999911")
                .fullName("Francisco Medeiros da Silva")
                .birth(LocalDateTime.parse("23/02/1985 10:22", FORMATTER))
                .build();

        Customer customer = mapper.mapToCustomer(body);
        customer = repository.save(customer);
        UUID id = customer.getId();

        CustomerDTO customerDto = mapper.mapToCustomerDTO(customer);

        AddressDTO addressDTO = AddressDTO.builder()
            .street("Av. da Liberdade, 25")
            .build();

        //This is calling two endpoints
        MvcResult response = this.mockMvc.perform(put(CUSTOMER_URL + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressDTO.toString()))
                .andExpect(status().isOk())
                .andReturn();
    }

}
