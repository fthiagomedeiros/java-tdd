package com.example.springtest.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import static com.example.springtest.HealthControllerTest.HEALTH_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class FullyIntegrationIT {

    @Autowired
    private WebApplicationContext context;

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
    public void testHealthEndpoint() throws Exception {
        this.mockMvc.perform(get(HEALTH_URL)
                /*.with(user(USER).password(PASS))*/)
                .andExpect(status()
                        .isOk());
    }


}
