package com.raysofthesun.poswebjava.propose.services;

import com.raysofthesun.poswebjava.propose.models.proposal.Proposal;
import com.raysofthesun.poswebjava.propose.repositories.ProposalRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProposalService {
	protected final ProposalRepository proposalRepository;

	public ProposalService(ProposalRepository proposalRepository) {
		this.proposalRepository = proposalRepository;
	}

	public Mono<String> createProposalWithAgentId(String agentId, Proposal proposal) {
		proposal.setAgentId(agentId);

		return proposalRepository
				.save(proposal)
				.map(Proposal::getId);
	}

	public Flux<Proposal> getProposalsByAgentId(String agentId) {
		return proposalRepository.findAllByAgentId(agentId);
	}
}
