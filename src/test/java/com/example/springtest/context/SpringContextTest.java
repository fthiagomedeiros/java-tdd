package com.example.springtest.context;

import com.example.springtest.controllers.CustomerController;
import com.example.springtest.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SpringContextTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void contextLoads() {
        assertNotNull(context);
        System.out.println(context);
        String[] definitions = context.getBeanDefinitionNames();
        CustomerController customerController = (CustomerController) context.getBean("customerController");
        CustomerRepository customerRepository = (CustomerRepository) context.getBean("customerRepository");
        customerRepository.findAll();
    }
}
