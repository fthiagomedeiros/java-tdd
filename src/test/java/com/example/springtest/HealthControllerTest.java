package com.example.springtest;

import com.example.springtest.controllers.HealthController;
//import com.example.springtest.security.WebSecurityConfig;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HealthController.class)
//@Import(WebSecurityConfig.class)
public class HealthControllerTest {

    public static final String HEALTH_URL = "/health";
    private static final String USER = "user";
    private static final String PASS = "pass";

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    public void shouldCheckServiceRunningSuccessfully() throws Exception {
        /*
            in order to test the security, you can change the WebSecurityConfig endpoint matchers for health
            to
            .requestMatchers(HttpMethod.GET, "/health")
                        .authenticated() and user and password will be mandatory
        */

        this.mockMvc
                .perform(get(HEALTH_URL)
                        /*.with(user(USER).password(PASS))*/)
                .andExpect(status().isOk())
                .andExpect(jsonPath("environment", Matchers.is("TST")))
                .andExpect(jsonPath("status", Matchers.is("UP")));
    }
}
