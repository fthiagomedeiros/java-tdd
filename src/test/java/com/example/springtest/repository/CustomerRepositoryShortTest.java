package com.example.springtest.repository;

import com.example.springtest.domain.Customer;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(properties = {
        "spring.test.database.replace = NONE",
        "spring.datasource.url = jdbc:tc:postgresql:15.3-alpine3.18:///springboot:customer_info"//,
        //"spring.flyway.enabled=false", disable Flyway
        //"spring.jpa.hibernate.ddl-auto=create-drop" Hibernate create ORM
}
)
public class CustomerRepositoryShortTest {

    @Autowired
    private CustomerRepository repository;

    @Test
    @Sql("/scripts/CREATE_CUSTOMER.sql")
    public void findSuccessfullyCustomerById() {
        //This test in not meaningful once we are testing the framework calling findById
        //(already testes by the framework team)
        List<Customer> customers = repository.findAll();
        assertEquals(2, customers.size());
    }

    @Test
    @Sql("/scripts/CREATE_CUSTOMER.sql")
    public void findSuccessfullyCustomersByFirstName() {
        List<Customer> customer = repository.findByFirstNameIn(List.of("Francisco"));
        assertEquals(1, customer.size());
    }

    @Test
    @Sql("/scripts/CREATE_CUSTOMER.sql")
    public void findNoCustomersByFirstName() {
        List<Customer> customer = repository.findByFirstNameIn(List.of("Joao"));
        assertEquals(0, customer.size());
    }

    @Test
    @Sql("/scripts/CREATE_CUSTOMER.sql")
    public void findSuccessfullySeveralCustomersByFirstName() {
        List<Customer> customer = repository.findByFirstNameIn(List.of("Francisco", "Alexandre"));
        assertEquals(2, customer.size());
    }

    @Test
    @Sql("/scripts/CREATE_CUSTOMER.sql")
    public void countCustomers() {
        long quantityCustomers = repository.count();
        assertEquals(2, quantityCustomers);
    }


    @Test
    public void insertSuccessfullyCustomerById() {
        //This test in not meaningful once we are testing the framework calling findById
        //(already testes by the framework team)

        Customer mCustomer = Customer.builder()
            .birth(LocalDateTime.now())
            .fullName("Joao Melo")
            .firstName("Joao")
            .lastName("Melo")
            .cpf("0000000000001")
            .username("joaomelo2023")
            .build();

        Customer customer = repository.save(mCustomer);

        assertThat(customer).isNotNull();
    }
}
