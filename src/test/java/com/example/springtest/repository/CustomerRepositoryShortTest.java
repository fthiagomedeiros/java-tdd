package com.example.springtest.repository;

import com.example.springtest.domain.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest( properties = {
        "spring.test.database.replace = NONE",
        "spring.datasource.url = jdbc:tc:postgresql:15.3-alpine3.18:///springboot:customer_info"
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
}
