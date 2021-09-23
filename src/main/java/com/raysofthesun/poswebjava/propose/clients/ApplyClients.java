package com.raysofthesun.poswebjava.propose.clients;

import com.raysofthesun.poswebjava.propose.clients.applications.ApplyApplicationApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactivefeign.webclient.WebReactiveFeign;

@Configuration
public class ApplyClients {

	@Bean
	public ApplyApplicationApi applyApplicationApi() {
		return WebReactiveFeign
				.<ApplyApplicationApi>builder()
				.target(ApplyApplicationApi.class, "localhost:8080");
	}
}
