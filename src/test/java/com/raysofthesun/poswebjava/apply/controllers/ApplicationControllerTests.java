package com.raysofthesun.poswebjava.apply.controllers;

import com.raysofthesun.poswebjava.FeignConfig;
import com.raysofthesun.poswebjava.apply.feign_clients.FeignClients;
import com.raysofthesun.poswebjava.apply.models.application.Application;
import com.raysofthesun.poswebjava.apply.models.application.ApplicationCreationRequest;
import com.raysofthesun.poswebjava.apply.models.application.ApplicationMeta;
import com.raysofthesun.poswebjava.apply.models.application.ApplicationPaymentInfo;
import com.raysofthesun.poswebjava.apply.models.insured.Insured;
import com.raysofthesun.poswebjava.apply.services.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

@Import({ApplicationService.class, FeignClients.class, FeignConfig.class})
@ExtendWith(MockitoExtension.class)
@WebFluxTest(ApplicationController.class)
@DisplayName("ApplicationController")
public class ApplicationControllerTests {
	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private ApplicationService applicationService;

	private Application application;

	@BeforeEach
	public void setupMocks() {
		final ApplicationMeta applicationMeta = new ApplicationMeta();
		application = Application
				.create(applicationMeta)
				.withOwner(new Insured())
				.withInsured(new Insured())
				.withPaymentInfo(new ApplicationPaymentInfo())
				.build();
	}

	@Test
	@DisplayName("should respond with a 200 status if any applications were retrieved")
	public void shouldBeAbleToRetrieveApplicationsWithCustomerId() {

		when(applicationService.getAllApplicationsWithCustomerId(anyString()))
				.thenReturn(Flux.just(application));

		webTestClient
				.get()
				.uri("/apply/customers/{customerId}/applications", "random-id")
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(List.class);
	}

	@Test
	@DisplayName("should return a 200 if an application was successfully retrieved by its id")
	public void shouldBeAbleToRetrieveApplicationById() {
		when(applicationService.getApplicationWithId(anyString())).thenReturn(Mono.just(application));

		webTestClient
				.get()
				.uri("/apply/applications/{applicationId}", "random-id")
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(Application.class);
	}

	@Test
	@DisplayName("should respond with a 200 if an application was successfully created")
	public void shouldBeAbleToCreateApplication() {
		when(applicationService
				.createApplicationWithRequestAndAgentId(any(ApplicationCreationRequest.class), anyString()))
				.thenReturn(Mono.just(application));

		webTestClient
				.post()
				.uri("/apply/agents/{agentId}/applications/create", "agentId")
				.body(Mono.just(new ApplicationCreationRequest()), ApplicationCreationRequest.class)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(Application.class);
	}
}
