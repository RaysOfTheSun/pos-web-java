package com.raysofthesun.poswebjava.propose.services;

import com.raysofthesun.poswebjava.propose.clients.applications.ApplyApplicationApi;
import com.raysofthesun.poswebjava.propose.clients.applications.models.application.Application;
import com.raysofthesun.poswebjava.propose.constants.CannotFinalizeProposalException;
import com.raysofthesun.poswebjava.propose.constants.CannotFindProposalException;
import com.raysofthesun.poswebjava.propose.constants.ProposalStatus;
import com.raysofthesun.poswebjava.propose.models.proposal.Proposal;
import com.raysofthesun.poswebjava.propose.repositories.ProposalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@DisplayName("ProposalService")
@ExtendWith({MockitoExtension.class})
public class ProposalServiceTests {

	@Mock
	private ProposalRepository proposalRepository;

	@Mock
	private ApplyApplicationApi applyApplicationApi;

	@InjectMocks
	private ProposalService proposalService;

	private Proposal mockProposal;

	private Application mockApplication;

	private final String testAgentId = "001";

	@BeforeEach
	public void prepareMocks() {
		mockProposal = new Proposal();
		mockProposal.setId("mockProposal");
		mockProposal.setName("Mock Proposal");
		mockProposal.setStatus(ProposalStatus.DRAFT);

		mockApplication = new Application();
	}

	@Test
	@DisplayName("should be able to finalize a DRAFT proposal")
	public void shouldBeAbleToFinalizeProposal() {
		when(proposalRepository.findByAgentIdAndId(testAgentId, mockProposal.getId()))
				.thenReturn(Mono.just(mockProposal));
		when(proposalRepository.save(mockProposal)).thenReturn(Mono.just(mockProposal));

		when(applyApplicationApi.createApplication(mockProposal, testAgentId)).thenReturn(Mono.just(mockApplication));

		StepVerifier
				.create(proposalService.finalizeProposal(testAgentId, mockProposal.getId()))
				.expectNext(mockApplication)
				.verifyComplete();
	}

	@Test
	@DisplayName("should throw an exception if an EXPIRED proposal is to be finalized")
	public void shouldThrowWhenFinalizingExpiredProposal() {
		mockProposal.setStatus(ProposalStatus.EXPIRED);

		when(proposalRepository.findByAgentIdAndId(testAgentId, mockProposal.getId()))
				.thenReturn(Mono.just(mockProposal));

		StepVerifier
				.create(proposalService.finalizeProposal(testAgentId, mockProposal.getId()))
				.expectError(CannotFinalizeProposalException.class)
				.verify();
	}

	@Test
	@DisplayName("should throw an exception if an CONVERTED proposal is to be finalized")
	public void shouldThrowWhenFinalizingFinalizedProposal() {
		mockProposal.setStatus(ProposalStatus.CONVERTED);

		when(proposalRepository.findByAgentIdAndId(testAgentId, mockProposal.getId()))
				.thenReturn(Mono.just(mockProposal));

		StepVerifier
				.create(proposalService.finalizeProposal(testAgentId, mockProposal.getId()))
				.expectError(CannotFinalizeProposalException.class)
				.verify();
	}

	@Test
	@DisplayName("should throw an exception if the proposal to be finalized does not exist")
	public void shouldThrowWhenFinalizingNonExistentProposal() {
		when(proposalRepository.findByAgentIdAndId(testAgentId, mockProposal.getId()))
				.thenReturn(Mono.empty());

		StepVerifier
				.create(proposalService.finalizeProposal(testAgentId, mockProposal.getId()))
				.expectError(CannotFindProposalException.class)
				.verify();
	}

	@Test
	@DisplayName("should be able to save a given proposal")
	public void shouldBeAbleToSaveProposal() {
		when(proposalRepository.save(mockProposal))
				.thenReturn(Mono.just(mockProposal));

		StepVerifier
				.create(proposalService.createProposalWithAgentId(testAgentId, mockProposal))
				.expectNext(mockProposal.getId())
				.verifyComplete();
	}

	@Test
	@DisplayName("should be able to retrieve all the proposals assigned to a given agent")
	public void shouldBeAbleToRetrieveProposalsByAgentId() {
		when(proposalService.getProposalsByAgentId(testAgentId))
				.thenReturn(Flux.just(mockProposal));

		StepVerifier
				.create(proposalService.getProposalsByAgentId(testAgentId))
				.expectNext(mockProposal)
				.verifyComplete();
	}
}
