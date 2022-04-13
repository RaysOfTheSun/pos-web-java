package com.raysofthesun.poswebjava.propose.controlllers;

import com.raysofthesun.poswebjava.LocalSecurityConfig;
import com.raysofthesun.poswebjava.core.common.enums.Market;
import com.raysofthesun.poswebjava.propose.feign.application.models.application.Application;
import com.raysofthesun.poswebjava.propose.proposals.advices.ProposalControllerAdvice;
import com.raysofthesun.poswebjava.propose.proposals.controllers.ProposalsController;
import com.raysofthesun.poswebjava.propose.proposals.exceptions.FailedToFinalizeProposalException;
import com.raysofthesun.poswebjava.propose.proposals.factories.ProposalServiceFactory;
import com.raysofthesun.poswebjava.propose.proposals.models.Proposal;
import com.raysofthesun.poswebjava.propose.proposals.services.marketCor.CorProposalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@WebFluxTest(ProposalsController.class)
@ExtendWith({MockitoExtension.class})
@Import({LocalSecurityConfig.class})
@ContextConfiguration(classes = {ProposalControllerAdvice.class, ProposalsController.class})
public class ProposalControllerTests {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    CorProposalService proposalService;

    @MockBean
    ProposalServiceFactory proposalServiceFactory;

    @BeforeEach
    public void setupFactory() {
        when(proposalServiceFactory.getServiceForMarket(any(Market.class)))
                .thenReturn(this.proposalService);
    }

    @Nested
    @DisplayName("when retrieving proposals")
    public class ProposalRetrievalTests {

        @Test
        @DisplayName("it should be able to retrieve all proposals with the provided agent id")
        public void shouldRetrieveProposalsWithAgentId() {
            Proposal expectedProposal = new Proposal();

            when(proposalService.getProposalsByAgentId(anyString()))
                    .thenReturn(Flux.just(expectedProposal));

            webTestClient
                    .get()
                    .uri("/v1/propose/COR/agents/001/proposals")
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBodyList(Proposal.class);
        }
    }

    @Nested
    @DisplayName("when creating proposals")
    public class ProposalCreationTests {
        @Test
        @DisplayName("it should return a HTTP 201 when an application is successfully created")
        public void shouldReturnCreatedStatusOnCreation() {
            Proposal proposalToCreate = new Proposal();

            when(proposalService.createProposalWithAgentId(any(Proposal.class), anyString(), any(Market.class)))
                    .thenReturn(Mono.just(proposalToCreate.getId()));

            webTestClient
                    .put()
                    .uri("/v1/propose/COR/agents/001/proposals")
                    .body(Mono.just(proposalToCreate), Proposal.class)
                    .exchange()
                    .expectStatus()
                    .isCreated()
                    .expectBody(String.class);
        }
    }

    @Nested
    @DisplayName("when finalizing proposals")
    public class ProposalFinalizationTests {
        @Test
        @DisplayName("it should return an application upon finalization")
        public void shouldFinalizeProposal() {
            when(proposalService.finalizeProposalByIdAndAgentId(anyString(), anyString(), any(Market.class)))
                    .thenReturn(Mono.just(new Application()));

            webTestClient
                    .post()
                    .uri("/v1/propose/COR/agents/001/proposals/0-1-2/finalize")
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(Application.class);
        }

        @Test
        @DisplayName("it should return an HTTP Response code of 400 on finalization failure")
        public void shouldReturnBadRequestOnFinalizationFailure() {
            when(proposalService.finalizeProposalByIdAndAgentId(anyString(), anyString(), any(Market.class)))
                    .thenThrow(FailedToFinalizeProposalException.class);

            webTestClient
                    .post()
                    .uri("/v1/propose/COR/agents/001/proposals/0-1-2/finalize")
                    .exchange()
                    .expectStatus()
                    .isBadRequest();
        }
    }
}
