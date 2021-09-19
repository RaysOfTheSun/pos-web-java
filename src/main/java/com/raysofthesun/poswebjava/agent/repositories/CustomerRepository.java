package com.raysofthesun.poswebjava.agent.repositories;

import com.raysofthesun.poswebjava.agent.models.customer.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, String> {
	Flux<Customer> findAllByAgentId(String agentId);
	Mono<Boolean> existsCustomersByAgentIdAndId(String agentId, String id);
}
