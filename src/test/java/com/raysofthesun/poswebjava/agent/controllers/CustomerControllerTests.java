package com.raysofthesun.poswebjava.agent.controllers;

import com.raysofthesun.poswebjava.SecurityConfig;
import com.raysofthesun.poswebjava.agent.models.customer.Customer;
import com.raysofthesun.poswebjava.agent.services.CustomerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@Import({SecurityConfig.class, CustomerService.class})
@ExtendWith(MockitoExtension.class)
@WebFluxTest(CustomerController.class)
@DisplayName("CustomerController")
public class CustomerControllerTests {

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private CustomerService customerService;

	@Test
	@DisplayName("should respond with HTTP 201 if the customer is successfully added")
	public void shouldRespondWith201WhenAddingCustomers() {
		when(customerService.addCustomerWithAgentId(any(Customer.class), anyString()))
				.thenReturn(Mono.just(""));
		webTestClient.put()
				.uri("/v1/agents/001/customers")
				.body(BodyInserters.fromProducer(Mono.just(new Customer()), Customer.class))
				.exchange()
				.expectStatus()
				.isCreated();
	}


}