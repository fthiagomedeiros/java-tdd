package com.example.springtest.integration;

import com.example.springtest.IntegrationTest;
import com.example.springtest.PostgresContainer;
import com.example.springtest.controllers.CustomerController;
import com.example.springtest.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@IntegrationTest
public class SpringContextTest extends PostgresContainer {

    @Autowired
    private ApplicationContext context;

    @Test
    public void contextLoads() {
        assertNotNull(context);
        String[] definitions = context.getBeanDefinitionNames();
        CustomerController customerController = (CustomerController) context.getBean("customerController");
        CustomerRepository customerRepository = (CustomerRepository) context.getBean("customerRepository");
        customerRepository.findAll();
    }
}
