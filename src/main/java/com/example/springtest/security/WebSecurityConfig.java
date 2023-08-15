//package com.example.springtest.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable();
//        return http.authorizeHttpRequests(request ->
//                request
//                        .requestMatchers(HttpMethod.GET, "/health")
//                        .permitAll()
//                        .anyRequest()
//                        .authenticated()
//                )
//                .httpBasic()
//                .and()
//                .build();
//    }
//
//}