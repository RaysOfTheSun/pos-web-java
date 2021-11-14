package com.raysofthesun.poswebjava.agent.services;

import com.raysofthesun.poswebjava.agent.exception.CustomerNotFoundException;
import com.raysofthesun.poswebjava.agent.models.customer.Customer;
import com.raysofthesun.poswebjava.agent.repositories.CustomerRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

@Service
public class CustomerService {

	protected final CustomerRepository customerRepository;

	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	public Mono<String> addCustomerWithAgentId(Customer customer, String agentId) {

		Mono<Customer> updateCustomerProperties = Mono.fromSupplier(() -> {
			customer.setAgentId(agentId);
			customer.getPersonalInfo().setAge(getCustomerAge(customer));
			return customer;
		});

		return updateCustomerProperties
				.flatMap(customerRepository::save)
				.map(Customer::getId);
	}

	public Mono<String> updateCustomer(String customerId, String agentId, Customer customer) {
		return customerRepository
				.existsCustomersByAgentIdAndId(agentId, customerId)
				.flatMap((isExistingCustomer) -> isExistingCustomer ? Mono.just(customer)
						: Mono.error(new CustomerNotFoundException()))
				.flatMap(updatedCustomer -> {
					updatedCustomer.setId(customerId);
					updatedCustomer.setAgentId(agentId);
					updatedCustomer.getPersonalInfo().setAge(getCustomerAge(customer));
					return customerRepository.save(updatedCustomer);
				})
				.map(Customer::getId);
	}

	public Flux<Customer> getCustomersByIdWithAgentId(String agentId, Collection<String> customerIds) {
		return customerRepository
				.findByAgentIdAndIdIn(agentId, customerIds)
				.switchIfEmpty(Flux.error(new CustomerNotFoundException()));
	}

	public Flux<Customer> getAllCustomersWithAgentId(String agentId) {
		return customerRepository.findAllByAgentId(agentId);
	}

	public Flux<Customer> getAllCustomersWithAgentIdAndDeletedStatus(String agentId, int pageIndex,
	                                                                 int pageSize, boolean deleted) {
		return customerRepository.findAllByAgentIdAndDeleted(agentId, deleted, PageRequest.of(pageIndex, pageSize));
	}

	public Mono<Integer> getAllCustomerCountByAgentIdAndDeletedStatus(String agentId, boolean isDeleted) {
		return customerRepository.countCustomerByAgentIdAndDeleted(agentId, isDeleted);
	}

	public Mono<Boolean> deleteCustomerByIdAndAgentId(String agentId, String customerId) {
		return customerRepository
				.findByIdAndAgentId(customerId, agentId)
				.switchIfEmpty(Mono.error(new CustomerNotFoundException()))
				.map((customer -> {
					customer.setId(customerId);
					customer.setDeleted(true);
					return customer;
				}))
				.flatMap(customerRepository::save)
				.thenReturn(true);

	}

	public Mono<Boolean> restoreCustomerByIdAndAgentId(String agentId, String customerId) {
		return customerRepository
				.findByIdAndAgentId(customerId, agentId)
				.switchIfEmpty(Mono.error(new CustomerNotFoundException()))
				.map(customer -> {
					customer.setDeleted(false);
					return customer;
				})
				.flatMap(customerRepository::save)
				.thenReturn(true);
	}

	private int getCustomerAge(Customer customer) {

		if (customer.getPersonalInfo().getDateOfBirth() == null) {
			return -1;
		}

		Instant currDate = Instant.now();
		Instant customerDob = Instant.parse(customer.getPersonalInfo().getDateOfBirth());
		long dobAndCurrDateDiffInDays = customerDob.until(currDate, ChronoUnit.DAYS);

		return (int) (dobAndCurrDateDiffInDays / 365);
	}
}
