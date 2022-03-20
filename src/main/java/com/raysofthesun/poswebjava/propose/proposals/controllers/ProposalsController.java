package com.raysofthesun.poswebjava.propose.proposals.controllers;

import com.raysofthesun.poswebjava.core.enums.Market;
import com.raysofthesun.poswebjava.propose.feign.application.models.application.Application;
import com.raysofthesun.poswebjava.propose.proposals.factories.ProposalServiceFactory;
import com.raysofthesun.poswebjava.propose.proposals.models.Proposal;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Api(tags = "Proposal Related Processes")
@RestController
@RequestMapping("/v1/propose")
public class ProposalsController {
    private final ProposalServiceFactory proposalServiceFactory;

    public ProposalsController(ProposalServiceFactory proposalServiceFactory) {
        this.proposalServiceFactory = proposalServiceFactory;
    }

    @PutMapping("/{market}/agents/{agentId}/proposals")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<String> createProposal(@PathVariable String agentId,
                                       @PathVariable Market market,
                                       @RequestBody Proposal proposal) {
        return this.proposalServiceFactory
                .getServiceForMarket(market)
                .createProposalWithAgentId(proposal, agentId, market);
    }

    @GetMapping("/{market}/agents/{agentId}/proposals")
    public Flux<Proposal> getProposalsByAgentId(@PathVariable String agentId, @PathVariable Market market) {
        return this.proposalServiceFactory.getServiceForMarket(market).getProposalsByAgentId(agentId);
    }

    @PostMapping("/{market}/agents/{agentId}/proposals/{proposalId}/finalize")
    public Mono<Application> finalizeProposal(@PathVariable String agentId,
                                              @PathVariable String proposalId,
                                              @PathVariable Market market) {
        return this.proposalServiceFactory
                .getServiceForMarket(market)
                .finalizeProposalByIdAndAgentId(proposalId, agentId, market);
    }
}
