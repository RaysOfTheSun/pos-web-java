package com.raysofthesun.poswebjava.agent.services;

import com.raysofthesun.poswebjava.agent.models.customer.Customer;
import com.raysofthesun.poswebjava.agent.repositories.CustomerRepository;
import com.raysofthesun.poswebjava.agent.services.CustomerService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
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


	@Nested
	@DisplayName("when creating customers")
	public class CustomerSavingTests {
		@Test
		@DisplayName("should be able to associate the current customer with the current agent and save it")
		public void shouldBeAbleToSaveCustomerWithGivenAgentId() {
			final String testAgentId = "AGENT_ID";
			when(customerRepository.save(any(Customer.class)))
					.thenAnswer((invocationOnMock -> Mono.just(invocationOnMock.getArgument(0))));

			StepVerifier
					.create(customerService.addCustomerWithAgentId(customer, testAgentId))
					.consumeNextWith((savedCustomerId) -> assertAll(
							() -> assertEquals(testAgentId, customer.getAgentId()),
							() -> assertEquals(customer.getId(), savedCustomerId)
					))
					.verifyComplete();

		}
	}

	@Nested
	@DisplayName("when updating customer data")
	public class CustomerUpdatingTests {

		@Test
		@DisplayName("should overwrite the current customer data and with the updated information")
		public void shouldSaveUpdatedCustomerInfo() {
			Customer updatedCustomer = new Customer();
			updatedCustomer.getPersonalInfo().setAge(-1);
			updatedCustomer.getPersonalInfo().setDateOfBirth(Instant.parse("2021-05-19T16:00:00.000Z").toString());

			when(customerRepository.existsCustomersByAgentIdAndId(anyString(), anyString()))
					.thenReturn(Mono.just(true));
			when(customerRepository.save(any(Customer.class)))
					.thenAnswer(invocationOnMock -> Mono.just(invocationOnMock.getArgument(0)));

			StepVerifier.create(customerService.updateCustomer("", "TEST-AGENT", updatedCustomer))
					.consumeNextWith(id -> assertAll(
							() -> assertNotEquals(-1, updatedCustomer.getPersonalInfo().getAge()),
							() -> assertEquals("TEST-AGENT", updatedCustomer.getAgentId())
					))
					.verifyComplete();
		}

		@Test
		@DisplayName("should throw an error when trying to update a non-existent customer")
		public void shouldThrowErrorWhenAttemptingToUpdateNonExistentCustomer() {
			when(customerRepository.existsCustomersByAgentIdAndId(anyString(), anyString()))
					.thenReturn(Mono.just(false));
			StepVerifier.create(customerService.updateCustomer("", "", new Customer()))
					.expectError()
					.verify();
		}
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

		@Test
		public void shouldBeAbleToRetrieveCustomerByDeletedStatus() {
			when(customerRepository.findAllByAgentIdAndDeleted(anyString(), anyBoolean(), any(PageRequest.class)))
					.thenReturn(Flux.just(new Customer()));
			StepVerifier
					.create(customerService.getAllCustomersWithAgentIdAndDeletedStatus("", 0, 20, false))
					.consumeNextWith(Assertions::assertNotNull)
					.verifyComplete();

		}

		@Test
		public void shouldBeAbleToRetrieveTotalCustomerCountByDeletedStatus() {
			when(customerRepository.countCustomerByAgentIdAndDeleted(anyString(), anyBoolean()))
					.thenReturn(Mono.just(1));
			StepVerifier.create(customerService.getAllCustomerCountByAgentIdAndDeletedStatus("", false))
					.consumeNextWith((count) -> assertEquals(1, count))
					.verifyComplete();
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

	@Nested
	@DisplayName("when restoring customers")
	public class CustomerRestorationTests {

		@Test
		@DisplayName("should set customer's deleted property to false")
		public void shouldSetDeletedToFalse() {
			customer.setDeleted(true);
			when(customerRepository.findByIdAndAgentId(anyString(), anyString()))
					.thenReturn(Mono.just(customer));
			when(customerRepository.save(any(Customer.class)))
					.thenAnswer((invocationOnMock -> Mono.just(invocationOnMock.getArgument(0))));

			StepVerifier.create(customerService.restoreCustomerByIdAndAgentId("", ""))
					.consumeNextWith((res) -> assertAll(
							() -> assertFalse(customer.isDeleted())
					))
					.verifyComplete();
		}
	}

}