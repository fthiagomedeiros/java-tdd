package com.example.springtest.integration;

import com.example.springtest.IntegrationTest;
import com.example.springtest.controllers.CustomerController;
import com.example.springtest.domain.Customer;
import com.example.springtest.domain.CustomerDTO;
import com.example.springtest.mapper.CustomerMapper;
import com.example.springtest.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class CreateCustomerIT {

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
    public void testCreateCustomerWithSameCpf() throws Exception {
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

        Customer customerWithCpf = repository.findByCpfOrUsername(body.getCpf(), body.getUsername());

        //This is calling two endpoints
        this.mockMvc.perform(post(CUSTOMER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.toString()))
                .andExpect(status().isUnprocessableEntity());
    }


    @Test
    @Transactional
    public void testFetchCustomerSuccessfully() throws Exception {
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

        //This one was loaded just above
        this.mockMvc.perform(get(CUSTOMER_URL + "/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstName").value("Francisco"))
                .andExpect(jsonPath("username").value("fmedeiros"));
    }

    @Test
    @Transactional
    @Sql("/scripts/CREATE_CUSTOMER.sql")
    public void testFetchAllCustomerSuccessfully() throws Exception {
        this.mockMvc.perform(get(CUSTOMER_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testNotFoundCustomer() throws Exception {
        UUID id = UUID.randomUUID();

        //This one was loaded just above
        this.mockMvc.perform(get(CUSTOMER_URL + "/" + id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @Sql("/scripts/CREATE_CUSTOMER.sql")
    public void contextLoads() {
        assertNotNull(context);
        String[] definitions = context.getBeanDefinitionNames();
        CustomerController customerController = (CustomerController) context.getBean("customerController");
        CustomerRepository customerRepository = (CustomerRepository) context.getBean("customerRepository");
        customerRepository.findAll();

        DataSource dataSource = context.getBean(DataSource.class);
        EntityManager manager = context.getBean(EntityManager.class);

        List<?> q = manager.createQuery("SELECT c FROM Customer c")
            .getResultList();

        //2 items has been found.
        Assertions.assertEquals(2, q.size());
    }

}
