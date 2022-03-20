//package com.raysofthesun.poswebjava.propose.controllers;
//
//import com.raysofthesun.poswebjava.FeignConfig;
//import com.raysofthesun.poswebjava.TestSecurityConfig;
//import com.raysofthesun.poswebjava.propose.feign.application.ApplicationApiFeignClient;
//import com.raysofthesun.poswebjava.propose.feign.application.models.application.Application;
//import com.raysofthesun.poswebjava.propose.proposals.controllers.ProposalController;
//import com.raysofthesun.poswebjava.propose2.proposals.models.Proposal;
//import com.raysofthesun.poswebjava.propose.proposals.services.core.ProposalService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.reactive.server.WebTestClient;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//
//@ActiveProfiles("test")
//@WebFluxTest(value = {ProposalController.class})
//@ExtendWith(MockitoExtension.class)
//@Import({ApplicationApiFeignClient.class, ProposalService.class, FeignConfig.class, TestSecurityConfig.class})
//@DisplayName("ProposalController")
//public class ProposalControllerTests {
//
//	@Autowired
//	private WebTestClient webTestClient;
//
//	private final String testAgentId = "001";
//
//	private final Proposal mockProposal = new Proposal();
//
//	@MockBean
//	private ProposalService proposalService;
//
//	@Test
//	@DisplayName("should respond with a 201 for a successful proposal creation")
//	public void shouldBeAbleToCreateProposal() {
//
//		when(proposalService.createProposalWithAgentId(testAgentId, mockProposal))
//				.thenReturn(Mono.just(mockProposal.getId()));
//
//		webTestClient
//				.put()
//				.uri("/v1/propose/agents/{agentId}/proposals", testAgentId)
//				.body(Mono.just(new Proposal()), Proposal.class)
//				.exchange()
//				.expectStatus()
//				.isCreated()
//				.expectBody(String.class);
//	}
//
//	@Test
//	@DisplayName("should respond with a 200 for a successful retrieval of proposals owned by an agent")
//	public void shouldBeAbleToRetrieveAllProposalsByAgentId() {
//		when(proposalService.getProposalsByAgentId(testAgentId))
//				.thenReturn(Flux.just(new Proposal()));
//
//		webTestClient
//				.get()
//				.uri("/v1/propose/agents/{agentId}/proposals", testAgentId)
//				.exchange()
//				.expectStatus()
//				.is2xxSuccessful()
//				.expectBody(List.class);
//	}
//
//	@Test
//	@DisplayName("should respond with a 200 for a successful proposal finalization")
//	public void shouldBeAbleToFinalizeProposal() {
//
//		when(proposalService.finalizeProposal(testAgentId, mockProposal.getId()))
//				.thenReturn(Mono.just(new Application()));
//
//		webTestClient
//				.post()
//				.uri("/v1/propose/agents/{agentId}/proposals/{proposalId}/finalize", testAgentId, mockProposal.getId())
//				.body(Mono.just(mockProposal), Proposal.class)
//				.exchange()
//				.expectStatus()
//				.is2xxSuccessful()
//				.expectBody(Application.class);
//	}
//}
