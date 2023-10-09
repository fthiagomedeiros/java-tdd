package com.example.springtest.extension;

import static java.lang.annotation.ElementType.PARAMETER;

import com.example.springtest.domain.CustomerDTO;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class ParameterCustomerResolverExtension implements ParameterResolver {

    public static final List<CustomerDTO> customers = List.of(
            CustomerDTO.builder()
                    .id(UUID.fromString("5801fc28-716e-4619-9814-4ef74c7c8898"))
                    .username("johndoe1")
                    .firstName("John")
                    .lastName("Doe")
                    .cpf("00000000001")
                    .birth(LocalDateTime.of(2001, 1, 1, 1, 1))
                    .build(),

            CustomerDTO.builder()
                    .id(UUID.fromString("5801fc28-716e-4619-9814-4ef74c7c8899"))
                    .username("fmeddoe2")
                    .firstName("Fmed")
                    .lastName("Doe")
                    .cpf("00000000002")
                    .birth(LocalDateTime.now())
                    .build(),

            CustomerDTO.builder()
                    .id(UUID.fromString("5801fc28-716e-4619-9814-4ef74c7c8899"))
                    .username("simaria123")
                    .firstName("Simaria")
                    .lastName("colegstwo")
                    .cpf("00000000003")
                    .birth(LocalDateTime.now())
                    .build()
    );

    @Retention(RetentionPolicy.RUNTIME)
    @Target({PARAMETER})
    public @interface RandomCustomer {

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.isAnnotated(RandomCustomer.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext,
                                   ExtensionContext extensionContext) throws ParameterResolutionException {
        return customers.get(ThreadLocalRandom.current().nextInt(0, customers.size()));
    }
}
