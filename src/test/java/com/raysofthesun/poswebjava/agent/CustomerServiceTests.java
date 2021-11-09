package com.raysofthesun.poswebjava.agent;

import com.raysofthesun.poswebjava.agent.models.customer.Customer;
import com.raysofthesun.poswebjava.agent.repositories.CustomerRepository;
import com.raysofthesun.poswebjava.agent.services.CustomerService;
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

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTests {

	@Mock
	private CustomerRepository customerRepository;

	@InjectMocks
	private CustomerService customerService;

	private Customer customer;

	@BeforeEach
	public void setupMocks() {
		customer = new Customer();
	}

	@Test
	@DisplayName("should be able to associate the current customer with the current agent and save it")
	public void shouldBeAbleToSaveCustomerWithGivenAgentId() {
		final String testAgentId = "AGENT_ID";
		when(customerRepository.save(any(Customer.class)))
				.thenAnswer((invocationOnMock -> Mono.just(invocationOnMock.getArgument(0))));

		StepVerifier
				.create(customerService.addCustomerWithAgentId(customer, testAgentId))
				.consumeNextWith((savedCustomerId) -> assertAll(
						() -> assertEquals(testAgentId, this.customer.getAgentId()),
						() -> assertEquals(this.customer.getId(), savedCustomerId)
				))
				.verifyComplete();

	}

	@Nested
	@DisplayName("when retrieving customers")
	public class CustomerRetrievalTests {

		@Test
		public void shouldBeAbleToRetrieveCustomersAssociatedWithAnAgent() {
			when(customerRepository.findAllByAgentId(anyString()))
					.thenReturn(Flux.just(customer));

			StepVerifier
					.create(customerService.getAllCustomersWithAgentId("agent-id"))
					.expectNext(customer)
					.verifyComplete();
		}

		@Test
		public void shouldThrowErrorIfCustomerWasNotFound() {
			when(customerRepository.findByAgentIdAndIdIn(anyString(), anyList()))
					.thenReturn(Flux.just(customer, null));

			StepVerifier
					.create(customerService.getCustomersByIdWithAgentId("001", List.of("1", "2")))
					.expectNextCount(1)
					.expectError()
					.verify();
		}

	}

	@Nested
	@DisplayName("when deleting customers")
	public class CustomerDeletionTests {
		@Test
		@DisplayName("should be able to mark a customer as deleted")
		public void shouldBeAbleToDeleteCustomer() {
			when(customerRepository.findByIdAndAgentId(anyString(), anyString()))
					.thenReturn(Mono.just(customer));
			when(customerRepository.save(any(Customer.class)))
					.thenAnswer((invocationOnMock -> Mono.just(invocationOnMock.getArgument(0))));
			StepVerifier
					.create(customerService.deleteCustomerByIdAndAgentId("001", "customer-id"))
					.consumeNextWith((isCustomerDeleted) -> {
						assertAll(
								() -> assertEquals(true, isCustomerDeleted),
								() -> assertTrue(customer.isDeleted())
						);
					})
					.verifyComplete();
		}
	}
}