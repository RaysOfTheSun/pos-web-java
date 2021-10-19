package com.raysofthesun.poswebjava.apply.controllers;

import com.raysofthesun.poswebjava.apply.models.application.ApplicationCreationRequest;
import com.raysofthesun.poswebjava.apply.models.application.Application;
import com.raysofthesun.poswebjava.apply.services.ApplicationService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("v1/apply/")
public class ApplicationController {
	protected ApplicationService applicationService;

	public ApplicationController(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	@PostMapping("/agents/{agentId}/applications/create")
	public Mono<Application> createApplicationFromProposal(@PathVariable String agentId,
	                                                       @RequestBody ApplicationCreationRequest request) {
		return applicationService.createApplicationWithRequestAndAgentId(request, agentId);
	}

	@GetMapping("/applications/{applicationId}")
	public Mono<Application> getApplicationById(@PathVariable String applicationId) {
		return applicationService.getApplicationWithId(applicationId);
	}

	@GetMapping("/customers/{customerId}/applications")
	public Flux<Application> getAllApplicationsWithCustomerId(@PathVariable String customerId) {
		return applicationService.getAllApplicationsWithCustomerId(customerId);
	}
}
