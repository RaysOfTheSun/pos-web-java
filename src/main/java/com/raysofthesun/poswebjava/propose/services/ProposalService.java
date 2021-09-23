package com.raysofthesun.poswebjava.propose.services;

import com.raysofthesun.poswebjava.propose.clients.applications.ApplyApplicationApi;
import com.raysofthesun.poswebjava.propose.clients.applications.models.application.Application;
import com.raysofthesun.poswebjava.propose.constants.ProposalStatus;
import com.raysofthesun.poswebjava.propose.models.proposal.Proposal;
import com.raysofthesun.poswebjava.propose.repositories.ProposalRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProposalService {
	protected final ProposalRepository proposalRepository;
	protected final ApplyApplicationApi applyApplicationApi;

	public ProposalService(ProposalRepository proposalRepository, ApplyApplicationApi applyApplicationApi) {
		this.proposalRepository = proposalRepository;
		this.applyApplicationApi = applyApplicationApi;
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

	public Mono<Application> finalizeProposal(String agentId, String proposalId) {
		return proposalRepository
				.findByAgentIdAndId(agentId, proposalId)
				.switchIfEmpty(Mono.error(new RuntimeException("PROPOSAL NOT FOUND")))
				.flatMap(this::makeFinalizedProposal)
				.flatMap((finalizedProposal) -> applyApplicationApi.createApplication(finalizedProposal, agentId));
	}

	protected Mono<Proposal> canProposalBeFinalized(Proposal proposal) {
		return Flux
				.just(ProposalStatus.DRAFT)
				.filterWhen((invalidProposalStatus) -> Mono.just(proposal.getStatus().equals(invalidProposalStatus)))
				.mapNotNull((proposalStatus) -> proposal)
				.next();
	}

	protected Mono<Proposal> makeFinalizedProposal(Proposal proposalToFinalize) {
		return canProposalBeFinalized(proposalToFinalize)
				.switchIfEmpty(Mono.error(new RuntimeException("CANNOT FINALIZE PROPOSAL WITH AN INVALID STATUS")))
				.map((proposal -> {
					proposal.setStatus(ProposalStatus.CONVERTED);
					return proposal;
				}))
				.flatMap(proposalRepository::save);
	}
}
