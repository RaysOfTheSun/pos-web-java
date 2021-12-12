package com.raysofthesun.poswebjava.propose.proposals.controllers;

import com.raysofthesun.poswebjava.propose.feign.application.models.application.Application;
import com.raysofthesun.poswebjava.propose.proposals.models.Proposal;
import com.raysofthesun.poswebjava.propose.proposals.services.ProposalService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("v1/propose")
public class ProposalController {

	final protected ProposalService proposalService;

	public ProposalController(ProposalService proposalService) {
		this.proposalService = proposalService;
	}

	@PutMapping("/agents/{agentId}/proposals")
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<String> createProposal(@PathVariable String agentId, @RequestBody Proposal proposal) {
		return proposalService.createProposalWithAgentId(agentId, proposal);
	}

	@GetMapping("/agents/{agentId}/proposals")
	public Flux<Proposal> getProposalsByAgentId(@PathVariable String agentId) {
		return proposalService.getProposalsByAgentId(agentId);
	}

	@PostMapping("/agents/{agentId}/proposals/{proposalId}/finalize")
	public Mono<Application> finalizeProposal(@PathVariable String agentId, @PathVariable String proposalId) {
		return proposalService.finalizeProposal(agentId, proposalId);
	}
}
