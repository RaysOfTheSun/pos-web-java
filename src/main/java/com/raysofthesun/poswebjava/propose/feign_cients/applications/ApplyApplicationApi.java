package com.raysofthesun.poswebjava.propose.feign_cients.applications;

import com.raysofthesun.poswebjava.propose.feign_cients.applications.models.application.Application;
import com.raysofthesun.poswebjava.propose.feign_cients.applications.models.application.ApplicationCreationRequest;
import feign.Param;
import feign.RequestLine;
import reactor.core.publisher.Mono;

public interface ApplyApplicationApi {
	@RequestLine("POST /agents/{agentId}/applications/create")
	Mono<Application> createApplication(ApplicationCreationRequest request, @Param("agentId") String agentId);
}
