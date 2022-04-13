package com.raysofthesun.poswebjava.propose.services;

import com.raysofthesun.poswebjava.propose.feign.application.models.application.ApplicationCreationRequest;
import com.raysofthesun.poswebjava.propose.proposals.enums.ProposalStatus;
import com.raysofthesun.poswebjava.propose.proposals.exceptions.CannotFinalizeProposalException;
import com.raysofthesun.poswebjava.propose.proposals.models.Proposal;
import com.raysofthesun.poswebjava.propose.proposals.services.core.ProposalCreatorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.util.function.Tuple2;


import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CoreProposalCreatorService")
@ExtendWith({MockitoExtension.class})
public class CoreProposalCreatorServiceTests {

    @Spy
    public ProposalCreatorService proposalCreatorService;

    @Nested
    @DisplayName("when creating proposals")
    public class ProposalCreationTests {

        @Test
        @DisplayName("it should set the necessary properties for a given proposal")
        public void shouldSetProperties() {
            Proposal proposal = new Proposal();
            String expectedAgentId = "00X";

            Proposal createdProposal = proposalCreatorService.createProposal(proposal, expectedAgentId);

            assertEquals(expectedAgentId, createdProposal.getAgentId());
        }

        @Test
        @DisplayName("it should set the status of the given proposal to DRAFT when unfinalizing it")
        public void shouldSetCorrectStatusOnUnfinalize() {
            Proposal proposal = new Proposal();
            proposal.setStatus(ProposalStatus.CONVERTED);


            Proposal createdProposal = proposalCreatorService.createUnfinalizezdProposal(proposal);

            assertEquals(ProposalStatus.DRAFT, createdProposal.getStatus());
        }
    }

    @Nested
    @DisplayName("when finalizing proposals")
    public class ProposalFinalizationTests {
        @Test
        @DisplayName("it should return a proposal with an updated status")
        public void shouldCreateRequestAndUpdateProposalStatus() {
            Proposal proposalToFinalize = new Proposal();
            proposalToFinalize.setStatus(ProposalStatus.DRAFT);

            Tuple2<Proposal, ApplicationCreationRequest> applicationCreationRequestTuple =
                    proposalCreatorService.createFinalizedProposalAndRequest(proposalToFinalize);

            assertEquals(ProposalStatus.CONVERTED, applicationCreationRequestTuple.getT1().getStatus());
            assertNotNull(applicationCreationRequestTuple.getT2());
        }

        @Test
        @DisplayName("it should throw an error when the given proposal's status is invalid")
        public void shouldThrowWhenProposalStatusCannotFinalize() {
            Proposal proposalToFinalize = new Proposal();
            proposalToFinalize.setStatus(ProposalStatus.EXPIRED);

            assertThrows(CannotFinalizeProposalException.class,
                    () -> proposalCreatorService.createFinalizedProposalAndRequest(proposalToFinalize));
        }

    }
}
