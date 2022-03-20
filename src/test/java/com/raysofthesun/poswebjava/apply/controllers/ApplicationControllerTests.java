//package com.raysofthesun.poswebjava.apply.controllers;
//
//import com.raysofthesun.poswebjava.FeignConfig;
//import com.raysofthesun.poswebjava.TestSecurityConfig;
//import com.raysofthesun.poswebjava.apply2.feign.CustomerApiFeignClients;
//import com.raysofthesun.poswebjava.apply2.applications.models.core.application.Application;
//import com.raysofthesun.poswebjava.apply2.applications.models.core.application.ApplicationCreationRequest;
//import com.raysofthesun.poswebjava.apply2.applications.models.core.application.ApplicationMeta;
//import com.raysofthesun.poswebjava.apply2.applications.models.core.application.ApplicationPaymentInfo;
//import com.raysofthesun.poswebjava.apply2.insureds.models.core.insured.Insured;
//import com.raysofthesun.poswebjava.apply2.applications.services.core.ApplicationService;
//import org.junit.jupiter.api.BeforeEach;
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
//import reactor.core.publisher.Mono;
//
//import static org.mockito.Mockito.*;
//
//@ActiveProfiles("test")
//@Import({ApplicationService.class, CustomerApiFeignClients.class, FeignConfig.class, TestSecurityConfig.class})
//@ExtendWith(MockitoExtension.class)
//@WebFluxTest(ApplicationController.class)
//@DisplayName("ApplicationController")
//public class ApplicationControllerTests {
//	@Autowired
//	private WebTestClient webTestClient;
//
//	@MockBean
//	private ApplicationService applicationService;
//
//	private Application application;
//
//	@BeforeEach
//	public void setupMocks() {
//		final ApplicationMeta applicationMeta = new ApplicationMeta();
//		application = Application
//				.create(applicationMeta)
//				.withOwner(new Insured())
//				.withInsured(new Insured())
//				.withPaymentInfo(new ApplicationPaymentInfo())
//				.build();
//	}
//
////	@Test
////	@DisplayName("should respond with a 200 status if any applications were retrieved")
////	public void shouldBeAbleToRetrieveApplicationsWithCustomerId() {
////
////		when(applicationService.getAllApplicationsWithCustomerId(anyString()))
////				.thenReturn(Flux.just(application));
////
////		webTestClient
////				.get()
////				.uri("/v1/apply/customers/{customerId}/applications", "random-id")
////				.exchange()
////				.expectStatus()
////				.is2xxSuccessful()
////				.expectBody(List.class);
////	}
//
//	@Test
//	@DisplayName("should return a 200 if an application was successfully retrieved by its id")
//	public void shouldBeAbleToRetrieveApplicationById() {
//		when(applicationService.getApplicationWithId(anyString())).thenReturn(Mono.just(application));
//
//		webTestClient
//				.get()
//				.uri("/v1/apply/applications/{applicationId}", "random-id")
//				.exchange()
//				.expectStatus()
//				.is2xxSuccessful()
//				.expectBody(Application.class);
//	}
//
//	@Test
//	@DisplayName("should respond with a 200 if an application was successfully created")
//	public void shouldBeAbleToCreateApplication() {
//		when(applicationService
//				.createApplicationWithRequestAndAgentId(any(ApplicationCreationRequest.class), anyString()))
//				.thenReturn(Mono.just(application));
//
//		webTestClient
//				.post()
//				.uri("/v1/apply/agents/{agentId}/applications/create", "agentId")
//				.body(Mono.just(new ApplicationCreationRequest()), ApplicationCreationRequest.class)
//				.exchange()
//				.expectStatus()
//				.is2xxSuccessful()
//				.expectBody(Application.class);
//	}
//}
