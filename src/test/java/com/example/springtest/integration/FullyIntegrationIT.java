package com.example.springtest.integration;

import com.example.springtest.IntegrationTest;
import com.example.springtest.PostgresContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.example.springtest.HealthControllerTest.HEALTH_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class FullyIntegrationIT extends PostgresContainer {

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

    @Test
    public void testHealthEndpoint() throws Exception {
        this.mockMvc.perform(get(HEALTH_URL)
                /*.with(user(USER).password(PASS))*/)
                .andExpect(status()
                        .isOk());
    }


}
