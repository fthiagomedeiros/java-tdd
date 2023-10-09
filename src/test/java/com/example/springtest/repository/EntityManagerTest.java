package com.example.springtest.repository;

import com.example.springtest.domain.Customer;
import com.example.springtest.domain.CustomerDTO;
import com.example.springtest.extension.ParameterCustomerResolverExtension;
import com.example.springtest.extension.ParameterCustomerResolverExtension.RandomCustomer;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest(properties = {
        "spring.test.database.replace = NONE",
        "spring.datasource.url = jdbc:tc:postgresql:15.3-alpine3.18:///springboot:CUSTOMER_INFO"
})
@ExtendWith(ParameterCustomerResolverExtension.class)
public class EntityManagerTest {

    @Autowired
    private EntityManager entityManager;
//    private TestEntityManager entityManager;

    @Test
    @Sql("/scripts/CREATE_CUSTOMER.sql")
    public void findSuccessfullyCustomerById() {
        //This test in not meaningful once we are testing the framework calling findById
        //(already testes by the framework team)
        Customer customers = entityManager.find(Customer.class, UUID.fromString("5801fc28-716e-4619-9814-4ef74c7c8898"));
        assertEquals("Francisco", customers.getFirstName());
    }


    @DisplayName("My repeated test sample")
    @RepeatedTest(value = 10)
    public void testJUnitExtension(@RandomCustomer CustomerDTO customer) {
        //Executes the test 10 times with the values provided by the RandomCustomer
        assertNotNull(customer);
    }


    @Test
    @Sql("/scripts/CREATE_CUSTOMER.sql")
    public void findSuccessfullyCustomersByCpfNaturalId() {
        Session session = entityManager.unwrap(Session.class);
        Optional<Customer> customer = session.bySimpleNaturalId(Customer.class)
                .loadOptional("00000000002");

        assertThat(customer.isPresent()).isTrue();
        assertThat(customer.get().getFirstName())
                .as("Customer loaded")
                .isEqualTo("Alexandre");

        assertThat(customer).isPresent();
    }

}
