package com.raysofthesun.poswebjava.apply.controllers;

import com.raysofthesun.poswebjava.apply.models.application.ApplicationCreationRequest;
import com.raysofthesun.poswebjava.apply.models.application.Application;
import com.raysofthesun.poswebjava.apply.services.ApplicationService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("apply/")
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

}
