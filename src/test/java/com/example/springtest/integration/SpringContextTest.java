package com.example.springtest.integration;

import com.example.springtest.IntegrationTest;
import com.example.springtest.PostgresContainer;
import com.example.springtest.controllers.CustomerController;
import com.example.springtest.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@IntegrationTest
public class SpringContextTest extends PostgresContainer {

    @Autowired
    private ApplicationContext context;

    @Test
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
