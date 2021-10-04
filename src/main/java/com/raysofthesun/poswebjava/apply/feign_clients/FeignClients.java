package com.raysofthesun.poswebjava.apply.feign_clients;

import com.raysofthesun.poswebjava.FeignConfig;
import com.raysofthesun.poswebjava.apply.feign_clients.agent.CustomerApi;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactivefeign.webclient.WebReactiveFeign;

@Component("ApplyFeignClients")
public class FeignClients {

	private final FeignConfig feignConfig;

	public FeignClients(FeignConfig feignConfig) {
		this.feignConfig = feignConfig;
	}

	@Bean
	CustomerApi customerApi() {
		return WebReactiveFeign
				.<CustomerApi>builder()
				.target(CustomerApi.class, feignConfig.getAgentServiceBaseUrl());
	}

}
