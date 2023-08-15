package com.example.springtest.repository;

import com.example.springtest.domain.Customer;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerRepositoryTest {

    @Container
    public static PostgreSQLContainer postgres =
            new PostgreSQLContainer(DockerImageName.parse("postgres:alpine:3.18"))
                    .withUsername("springboot")
                    .withPassword("springboot")
                    .withDatabaseName("CUSTOMER_INFO");

    @DynamicPropertySource
    static void setDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
    }

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CustomerRepository repository;

    @Test
    public void checkContextLoadsSuccessfully() {
        assertNotNull(entityManager);
        assertNotNull(dataSource);
    }

    @Test
    public void findSuccessfullyCustomerById() {
        Customer saved = repository
                .save(Customer
                        .builder()
                        .firstName("firstName")
                        .lastName("lastName")
                        .username("username")
                        .cpf("05918363432")
                        .build());

        //This test in not meaningful once we are testing the framework calling findById
        //(already testes by the framework team)
        String uuid = saved.getId();
        Optional<Customer> customers = repository.findById(uuid);
        assertEquals(uuid, customers.get().getId());
    }

    @Test
    public void findSuccessfullyCustomersByFirstName() {
        Customer c1 = repository
                .save(Customer
                        .builder()
                        .firstName("Francisco")
                        .lastName("Medeiros")
                        .username("fthiagomedeiros")
                        .cpf("00000000001")
                        .build());

        Customer c2 = repository
                .save(Customer
                        .builder()
                        .firstName("Alexandre")
                        .lastName("Medeiros")
                        .username("alexmedeiros")
                        .cpf("00000000002")
                        .build());

        List<Customer> customer = repository.findByFirstNameIn(List.of("Francisco"));
        assertEquals(1, customer.size());
    }
}
