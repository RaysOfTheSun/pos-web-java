package com.raysofthesun.poswebjava.propose.feign_cients;

import com.raysofthesun.poswebjava.FeignConfig;
import com.raysofthesun.poswebjava.propose.feign_cients.applications.ApplyApplicationApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactivefeign.webclient.WebReactiveFeign;

@Component
@Qualifier("ProposeFeignClients")
public class FeignClients {

	private final FeignConfig feignConfig;

	public FeignClients(FeignConfig feignConfig) {
		this.feignConfig = feignConfig;
	}

	@Bean
	public ApplyApplicationApi applyApplicationApi() {
		return WebReactiveFeign
				.<ApplyApplicationApi>builder()
				.target(ApplyApplicationApi.class, feignConfig.getApplyServiceBaseUrl());
	}
}
