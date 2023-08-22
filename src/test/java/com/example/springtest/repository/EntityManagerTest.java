package com.example.springtest.repository;

import com.example.springtest.domain.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(properties = {
        "spring.test.database.replace = NONE",
        "spring.datasource.url = jdbc:tc:postgresql:15.3-alpine3.18:///springboot:CUSTOMER_INFO"
})
public class EntityManagerTest {

    @Autowired
    private TestEntityManager entityManager;

    UUID uuid = UUID.randomUUID();

    @BeforeEach
    public void createEntities() {
        uuid = UUID.randomUUID();

        entityManager.merge(Customer.builder()
                .id(uuid)
                .firstName("John")
                .lastName("Doe")
                .cpf("11111111122")
                .username("johndoe")
                .build());
        entityManager.getEntityManager().getTransaction().commit();

    }

    @Test
    @Sql("/scripts/CREATE_CUSTOMER.sql")
    public void findSuccessfullyCustomerById() {
        //This test in not meaningful once we are testing the framework calling findById
        //(already testes by the framework team)
        Customer customers = entityManager.find(Customer.class, UUID.fromString("5801fc28-716e-4619-9814-4ef74c7c8898"));
        assertEquals("Francisco", customers.getFirstName());
    }

}
