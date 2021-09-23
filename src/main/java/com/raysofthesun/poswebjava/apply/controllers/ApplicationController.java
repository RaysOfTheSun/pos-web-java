package com.raysofthesun.poswebjava.apply.controllers;

import com.raysofthesun.poswebjava.apply.models.application.ApplicationMeta;
import com.raysofthesun.poswebjava.apply.services.ApplicationService;
import com.raysofthesun.poswebjava.propose.models.proposal.Proposal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("apply/")
public class ApplicationController {
	protected ApplicationService applicationService;
	public ApplicationController(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	@PostMapping("/agents/{agentId}/applications/create")
	public Mono<ApplicationMeta> createApplicationFromProposal(@RequestBody Proposal proposal) {
		return applicationService.createApplicationMetaFromProposal(proposal);
	}

}
