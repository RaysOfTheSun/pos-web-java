package com.raysofthesun.poswebjava.propose.services;

import com.raysofthesun.poswebjava.core.common.enums.Market;
import com.raysofthesun.poswebjava.propose.feign.application.ApplyApplicationApi;
import com.raysofthesun.poswebjava.propose.feign.application.models.application.Application;
import com.raysofthesun.poswebjava.propose.feign.application.models.application.ApplicationCreationRequest;
import com.raysofthesun.poswebjava.propose.proposals.enums.ProposalStatus;
import com.raysofthesun.poswebjava.propose.proposals.exceptions.CannotFinalizeProposalException;
import com.raysofthesun.poswebjava.propose.proposals.factories.ProposalCreatorServiceFactory;
import com.raysofthesun.poswebjava.propose.proposals.models.Proposal;
import com.raysofthesun.poswebjava.propose.proposals.repositories.ProposalRepository;
import com.raysofthesun.poswebjava.propose.proposals.services.marketCor.CorProposalCreatorService;
import com.raysofthesun.poswebjava.propose.proposals.services.marketCor.CorProposalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CoreProposalService")
@ExtendWith({MockitoExtension.class})
public class CoreProposalServiceTests {

    @Mock
    ProposalRepository proposalRepository;

    @Mock
    ApplyApplicationApi applyApplicationApi;

    @Mock
    CorProposalCreatorService proposalCreatorService;

    @Mock
    ProposalCreatorServiceFactory proposalCreatorServiceFactory;

    @InjectMocks
    CorProposalService proposalService;

    private void mockCreatorServiceFactory() {
        when(this.proposalCreatorServiceFactory.getServiceForMarket(any(Market.class)))
                .thenReturn(this.proposalCreatorService);
    }

    private void mockRepositorySaveMechanism() {
        when(this.proposalRepository.save(any(Proposal.class)))
                .thenAnswer(invocationOnMock -> Mono.just(invocationOnMock.getArgument(0)));
    }

    private Proposal makeProposalWithStatus(ProposalStatus proposalStatus) {
        Proposal proposal = new Proposal();
        proposal.setStatus(proposalStatus);
        proposal.setAgentId("001");
        proposal.setType("ULAYF");

        return proposal;
    }

    @Nested
    @DisplayName("when retrieving proposals")
    public class ProposalRetrievalTests {
        @Test
        @DisplayName("it should return the proposal associated with the provided id")
        public void shouldReturnProposal() {
            Proposal expectedProposal = new Proposal();

            when(proposalRepository.findByAgentIdAndId(anyString(), anyString()))
                    .thenReturn(Mono.just(expectedProposal));

            StepVerifier
                    .create(proposalService.getProposalByIdAndAgentId("1", "0"))
                    .expectNext(expectedProposal)
                    .verifyComplete();
        }

        @Test
        @DisplayName("it should throw an error when retrieving a proposal that does not exist")
        public void shouldThrowWhenProposalNotFound() {
            when(proposalRepository.findByAgentIdAndId(anyString(), anyString()))
                    .thenReturn(Mono.empty());

            StepVerifier
                    .create(proposalService.getProposalByIdAndAgentId("1", "0"))
                    .expectError()
                    .verify();
        }

        @Test
        @DisplayName("it should be able to fetch all proposals with a given agent id")
        public void shouldFetchAllProposalsWithAgentId() {
            when(proposalRepository.findAllByAgentId(anyString())).thenReturn(Flux.just(new Proposal()));
            StepVerifier
                    .create(proposalService.getProposalsByAgentId("001"))
                    .expectNextCount(1)
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("when creating proposals")
    public class ProposalCreationTests {

        @BeforeEach
        public void setupMocks() {
            mockCreatorServiceFactory();
        }

        @Test
        @DisplayName("it should return the id of the created one on success")
        public void shouldCreateProposalAndReturnId() {
            Proposal proposal = new Proposal();
            when(proposalCreatorService.createProposal(any(Proposal.class), anyString()))
                    .thenReturn(proposal);
            when(proposalRepository.save(any(Proposal.class)))
                    .thenAnswer(invocationOnMock -> Mono.just(invocationOnMock.getArgument(0)));

            StepVerifier
                    .create(proposalService.createProposalWithAgentId(new Proposal(), "001", Market.COR))
                    .expectNext(proposal.getId())
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("when finalizing proposals")
    public class ProposalFinalizationTests {

        @BeforeEach
        public void setupMocks() {
            mockCreatorServiceFactory();
        }

        @Test
        @DisplayName("it should return the created application")
        public void shouldReturnApplication() {
            Proposal proposalToFinalize = makeProposalWithStatus(ProposalStatus.DRAFT);
            Application expectedApplication = new Application();

            mockRepositorySaveMechanism();
            when(proposalRepository.findByAgentIdAndId(anyString(), anyString()))
                    .thenReturn(Mono.just(proposalToFinalize));
            when(applyApplicationApi.createApplication(any(ApplicationCreationRequest.class), anyString(), any(Market.class)))
                    .thenReturn(Mono.just(expectedApplication));
            when(proposalCreatorService.createFinalizedProposalAndRequest(any(Proposal.class)))
                    .thenCallRealMethod();


            StepVerifier
                    .create(proposalService.finalizeProposalByIdAndAgentId(proposalToFinalize.getId(), "001", Market.COR))
                    .expectNext(expectedApplication)
                    .verifyComplete();
        }

        @Test
        @DisplayName("it should throw an error if the proposal to finalize is already finalized")
        public void shouldThrowOnAlreadyFinalizedProposal() {
            Proposal proposalToFinalize = makeProposalWithStatus(ProposalStatus.CONVERTED);

            when(proposalRepository.findByAgentIdAndId(anyString(), anyString()))
                    .thenReturn(Mono.just(proposalToFinalize));
            when(proposalCreatorService.createFinalizedProposalAndRequest(any(Proposal.class)))
                    .thenThrow(new CannotFinalizeProposalException(proposalToFinalize));

            StepVerifier
                    .create(proposalService.finalizeProposalByIdAndAgentId(proposalToFinalize.getId(), "001", Market.COR))
                    .expectError(CannotFinalizeProposalException.class)
                    .verify();
        }

        @Test
        @DisplayName("it should revert the status of the given proposal to DRAFT on failure")
        public void shouldRevertProposalChangesOnFailure() {
            Proposal proposalToFinalize = makeProposalWithStatus(ProposalStatus.DRAFT);

            when(proposalRepository.findByAgentIdAndId(anyString(), anyString()))
                    .thenReturn(Mono.just(proposalToFinalize));
            when(proposalCreatorService.createFinalizedProposalAndRequest(any(Proposal.class)))
                    .thenCallRealMethod();
            when(proposalCreatorService.createUnfinalizezdProposal(any(Proposal.class)))
                    .thenCallRealMethod();

            StepVerifier
                    .create(proposalService.finalizeProposalByIdAndAgentId(proposalToFinalize.getId(), "001", Market.COR))
                    .consumeErrorWith(throwable -> {
                        assertAll(() -> assertEquals(ProposalStatus.DRAFT, proposalToFinalize.getStatus()));
                    }).verify();
        }
    }
}
