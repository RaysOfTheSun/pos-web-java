package com.raysofthesun.poswebjava.propose.proposals.services.core;

import com.raysofthesun.poswebjava.core.services.PosWebService;
import com.raysofthesun.poswebjava.propose.proposals.exceptions.CannotFinalizeProposalException;
import com.raysofthesun.poswebjava.propose.feign.application.models.application.ApplicationCreationRequest;
import com.raysofthesun.poswebjava.propose.proposals.enums.ProposalStatus;
import com.raysofthesun.poswebjava.propose.proposals.mappers.ProposalMapper;
import com.raysofthesun.poswebjava.propose.proposals.models.Proposal;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.List;

public abstract class ProposalCreatorService implements PosWebService {
    public Proposal createProposal(Proposal proposal, String agentId) {
        proposal.setAgentId(agentId);
        return proposal;
    }

    public Tuple2<Proposal, ApplicationCreationRequest> createFinalizedProposalAndRequest(Proposal proposalToFinalize) {
        boolean canFinalize = List.of(ProposalStatus.DRAFT).contains(proposalToFinalize.getStatus());

        if (!canFinalize) {
            throw new CannotFinalizeProposalException(proposalToFinalize);
        }

        proposalToFinalize.setStatus(ProposalStatus.CONVERTED);
        ApplicationCreationRequest creationRequest = ProposalMapper.MAPPER
                .proposalToApplicationCreationRequest(proposalToFinalize);

        return Tuples.of(proposalToFinalize, creationRequest);
    }

    public Proposal createUnfinalizezdProposal(Proposal proposalToUnfinalize) {
        proposalToUnfinalize.setStatus(ProposalStatus.DRAFT);

        return proposalToUnfinalize;
    }
}
