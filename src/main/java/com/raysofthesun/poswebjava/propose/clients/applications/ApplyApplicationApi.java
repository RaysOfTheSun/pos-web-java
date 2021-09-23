package com.raysofthesun.poswebjava.propose.clients.applications;

import com.raysofthesun.poswebjava.propose.clients.applications.models.application.Application;
import com.raysofthesun.poswebjava.propose.models.proposal.Proposal;
import feign.Param;
import feign.RequestLine;
import reactor.core.publisher.Mono;

public interface ApplyApplicationApi {
	@RequestLine("POST /apply/agents/{agentId}/applications/create")
	Mono<Application> createApplication(Proposal proposal, @Param("agentId") String agentId);
}
