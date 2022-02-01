package com.raysofthesun.poswebjava.propose.proposals.services;

import com.raysofthesun.poswebjava.propose.constants.*;
import com.raysofthesun.poswebjava.propose.feign.application.ApplyApplicationApi;
import com.raysofthesun.poswebjava.propose.feign.application.models.application.Application;
import com.raysofthesun.poswebjava.propose.proposals.constants.ProposalStatus;
import com.raysofthesun.poswebjava.propose.proposals.mappers.ProposalMapper;
import com.raysofthesun.poswebjava.propose.proposals.models.Proposal;
import com.raysofthesun.poswebjava.propose.proposals.repositories.ProposalRepository;
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
        return Mono
                .just(proposal)
                .map((proposal1 -> {
                    proposal.setAgentId(agentId);
                    return proposal;
                }))
                .flatMap(proposalRepository::save)
                .map(Proposal::getId);

    }

    public Flux<Proposal> getProposalsByAgentId(String agentId) {
        return proposalRepository.findAllByAgentId(agentId);
    }

    public Mono<Application> finalizeProposal(String agentId, String proposalId) {
        return proposalRepository
                .findByAgentIdAndId(agentId, proposalId)
                .switchIfEmpty(Mono.error(new CannotFindProposalException(proposalId)))
                .flatMap(this::makeFinalizedProposal)
                .map(ProposalMapper.PROPOSAL_MAPPER::proposalToApplicationCreationRequest)
                .flatMap((finalizedProposal) -> applyApplicationApi.createApplication(finalizedProposal, agentId))
                .onErrorResume((throwable -> throwable instanceof ProposeApiException
                        ? Mono.error(throwable)
                        : unFinalizeProposal(agentId, proposalId)));
    }

    protected Mono<Application> unFinalizeProposal(String agentId, String proposalId) {
        return proposalRepository
                .findByAgentIdAndId(agentId, proposalId)
                .map(proposal -> {
                    proposal.setStatus(ProposalStatus.DRAFT);
                    return proposal;
                })
                .flatMap(proposalRepository::save)
                .then(Mono.error(new FailedToFinalizeProposalException(proposalId)));
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
                .switchIfEmpty(Mono.error(new CannotFinalizeProposalException(proposalToFinalize)))
                .map((proposal -> {
                    proposal.setStatus(ProposalStatus.CONVERTED);
                    return proposal;
                }))
                .flatMap(proposalRepository::save);
    }
}