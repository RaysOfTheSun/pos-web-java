package com.raysofthesun.poswebjava;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Profile("local")
@EnableWebFluxSecurity
public class LocalSecurityConfig {

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        return httpSecurity
                .csrf()
                .disable()
                .authorizeExchange()
                .anyExchange()
                .permitAll()
                .and()
                .build();
    }
}
