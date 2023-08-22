package com.example.springtest;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

public class PostgresContainer {

    @Container
    public static PostgreSQLContainer postgres =
            new PostgreSQLContainer(DockerImageName.parse("postgres:alpine:3.18"))
                    .withUsername("springboot")
                    .withPassword("springboot")
                    .withDatabaseName("customer_info");


    @DynamicPropertySource
    static void setDataSourceProperties(DynamicPropertyRegistry registry) {
        postgres.withReuse(true);
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
    }


}
