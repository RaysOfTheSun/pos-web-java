package com.raysofthesun.poswebjava.propose.feign.application;

import com.raysofthesun.poswebjava.propose.feign.application.models.application.Application;
import com.raysofthesun.poswebjava.propose.feign.application.models.application.ApplicationCreationRequest;
import feign.Param;
import feign.RequestLine;
import reactor.core.publisher.Mono;

public interface ApplyApplicationApi {
	@RequestLine("POST /agents/{agentId}/applications/create")
	Mono<Application> createApplication(ApplicationCreationRequest request, @Param("agentId") String agentId);
}
