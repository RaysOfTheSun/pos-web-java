//package com.raysofthesun.poswebjava.agent.controllers;
//
//import com.raysofthesun.poswebjava.TestSecurityConfig;
//import com.raysofthesun.poswebjava.customer.controllers.CustomerController;
//import com.raysofthesun.poswebjava.customer2.models.Customer;
//import com.raysofthesun.poswebjava.customer2.models.RawCustomer;
//import com.raysofthesun.poswebjava.customer.services.CustomerService;
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
//import org.springframework.web.reactive.function.BodyInserters;
//import reactor.core.publisher.Mono;
//
//import static org.mockito.Mockito.*;
//
//@ActiveProfiles("test")
//@Import({TestSecurityConfig.class, CustomerService.class})
//@ExtendWith(MockitoExtension.class)
//@WebFluxTest(CustomerController.class)
//@DisplayName("CustomerController")
//public class CustomerControllerTests {
//
//	@Autowired
//	private WebTestClient webTestClient;
//
//	@MockBean
//	private CustomerService customerService;
//
//	@Test
//	@DisplayName("should respond with HTTP 201 if the customer is successfully added")
//	public void shouldRespondWith201WhenAddingCustomers() {
//		when(customerService.addCustomerWithAgentId(any(RawCustomer.class), anyString()))
//				.thenReturn(Mono.just(""));
//		webTestClient.put()
//				.uri("/v1/agents/001/customers")
//				.body(BodyInserters.fromProducer(Mono.just(new Customer()), Customer.class))
//				.exchange()
//				.expectStatus()
//				.isCreated();
//	}
//
//
//}
