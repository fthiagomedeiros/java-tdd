package com.example.springtest.repository;

import com.example.springtest.domain.Customer;
import com.example.springtest.domain.CustomerDTO;
import com.example.springtest.extension.ParameterCustomerResolverExtension;
import com.example.springtest.extension.ParameterCustomerResolverExtension.RandomCustomer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest(properties = {
        "spring.test.database.replace = NONE",
        "spring.datasource.url = jdbc:tc:postgresql:15.3-alpine3.18:///springboot:CUSTOMER_INFO"
})
@ExtendWith(ParameterCustomerResolverExtension.class)
public class EntityManagerTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @Sql("/scripts/CREATE_CUSTOMER.sql")
    public void findSuccessfullyCustomerById() {
        //This test in not meaningful once we are testing the framework calling findById
        //(already testes by the framework team)
        Customer customers = entityManager.find(Customer.class, UUID.fromString("5801fc28-716e-4619-9814-4ef74c7c8898"));
        assertEquals("Francisco", customers.getFirstName());
    }


    @RepeatedTest(value = 10)
    public void testJUnitExtension(@RandomCustomer CustomerDTO customer) {
        System.out.println(customer);
        assertNotNull(customer);
    }

}
