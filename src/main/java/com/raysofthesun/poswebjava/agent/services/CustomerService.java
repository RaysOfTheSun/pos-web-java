package com.raysofthesun.poswebjava.agent.services;

import com.raysofthesun.poswebjava.agent.models.customer.Customer;
import com.raysofthesun.poswebjava.agent.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Service
public class CustomerService {

	protected final CustomerRepository customerRepository;

	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	public Mono<String> addCustomerWithAgentId(Customer customer, String agentId) {

		Mono<Customer> associateCustomerWithAgent$ = Mono.fromSupplier(() -> {
			customer.setAgentId(agentId);
			return customer;
		});

		return associateCustomerWithAgent$
				.flatMap(customerRepository::save)
				.map(Customer::getId);
	}

	public Flux<Customer> getCustomersByIdWithAgentId(String agentId, Collection<String> customerIds) {
		return customerRepository
				.findByAgentIdAndIdIn(agentId, customerIds)
				.switchIfEmpty(Flux.error(new RuntimeException("CANNOT FIND CUSTOMER")));
	}

	public Flux<Customer> getAllCustomersWithAgentId(String agentId) {
		return customerRepository.findAllByAgentId(agentId);
	}

	public Mono<Boolean> deleteCustomerByIdAndAgentId(String agentId, String customerId) {
		return customerRepository.existsCustomersByAgentIdAndId(agentId, customerId)
				.flatMap((doesCustomerExist) -> doesCustomerExist ? Mono.just(customerId) : Mono.empty())
				.switchIfEmpty(Mono.error(new RuntimeException("CUSTOMER NOT FOUND")))
				.flatMap((idOfCustomerToDelete) -> customerRepository.deleteById(customerId))
				.thenReturn(true);
	}
}
