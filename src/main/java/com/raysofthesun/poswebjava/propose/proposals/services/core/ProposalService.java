package com.raysofthesun.poswebjava.propose.proposals.services.core;

import com.raysofthesun.poswebjava.core.common.enums.Market;
import com.raysofthesun.poswebjava.core.common.services.PosWebService;
import com.raysofthesun.poswebjava.propose.proposals.exceptions.FailedToFinalizeProposalException;
import com.raysofthesun.poswebjava.propose.proposals.exceptions.ProposeApiException;
import com.raysofthesun.poswebjava.propose.feign.application.ApplyApplicationApi;
import com.raysofthesun.poswebjava.propose.feign.application.models.application.Application;
import com.raysofthesun.poswebjava.propose.feign.application.models.application.ApplicationCreationRequest;
import com.raysofthesun.poswebjava.propose.proposals.models.Proposal;
import com.raysofthesun.poswebjava.propose.proposals.repositories.ProposalRepository;
import com.raysofthesun.poswebjava.propose.proposals.factories.ProposalCreatorServiceFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class ProposalService implements PosWebService {
    protected final ProposalRepository proposalRepository;
    protected final ApplyApplicationApi applyApplicationApi;
    protected final ProposalCreatorServiceFactory proposalCreatorServiceFactory;

    public ProposalService(ProposalRepository proposalRepository,
                           ApplyApplicationApi applyApplicationApi,
                           ProposalCreatorServiceFactory proposalCreatorServiceFactory) {
        this.proposalRepository = proposalRepository;
        this.applyApplicationApi = applyApplicationApi;
        this.proposalCreatorServiceFactory = proposalCreatorServiceFactory;
    }

    public Flux<Proposal> getProposalsByAgentId(String agentId) {
        return proposalRepository.findAllByAgentId(agentId);
    }

    public Mono<String> createProposalWithAgentId(Proposal proposal, String agentId, Market market) {
        return Mono.fromSupplier(() -> this
                        .proposalCreatorServiceFactory
                        .getServiceForMarket(market)
                        .createProposal(proposal, agentId))
                .flatMap(this.proposalRepository::save)
                .map(Proposal::getId);
    }

    public Mono<Application> finalizeProposalByIdAndAgentId(String proposalId, String agentId, Market market) {
        return this.getProposalByIdAndAgentId(proposalId, agentId)
                .map(this.proposalCreatorServiceFactory
                        .getServiceForMarket(market)::createFinalizedProposalAndRequest)
                .flatMap(objects -> {
                    Proposal finalizedProposal = objects.getT1();
                    ApplicationCreationRequest creationRequest = objects.getT2();

                    return this.proposalRepository
                            .save(finalizedProposal)
                            .flatMap(id -> this.applyApplicationApi.createApplication(creationRequest, agentId, market));
                })
                .onErrorResume((throwable -> throwable instanceof ProposeApiException
                        ? Mono.error(throwable)
                        : this.unfinalizeProposalAfterError(proposalId, agentId, market)
                        .then(Mono.error(new FailedToFinalizeProposalException(proposalId))))
                );
    }

    public Mono<Proposal> getProposalByIdAndAgentId(String proposalId, String agentId) {
        return this.proposalRepository
                .findByAgentIdAndId(agentId, proposalId)
                .switchIfEmpty(Mono.error(new RuntimeException("proposal not found")));
    }

    protected Mono<Proposal> unfinalizeProposalAfterError(String proposalId, String agentId, Market market) {
        return this
                .getProposalByIdAndAgentId(proposalId, agentId)
                .map(this.proposalCreatorServiceFactory.getServiceForMarket(market)::createUnfinalizezdProposal)
                .flatMap(this.proposalRepository::save);
    }
}
