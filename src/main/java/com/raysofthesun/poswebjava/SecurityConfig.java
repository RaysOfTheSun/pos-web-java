package com.raysofthesun.poswebjava;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
		return serverHttpSecurity
				.csrf()
				.disable()
				.authorizeExchange()
				.anyExchange()
				.permitAll()
				.and()
				.build();
	}
}
