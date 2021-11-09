package com.raysofthesun.poswebjava.agent.repositories;

import com.raysofthesun.poswebjava.agent.models.customer.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Repository
public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
	Flux<Customer> findAllByAgentId(String agentId);
	Mono<Customer> findByIdAndAgentId(String id, String agentId);
	Mono<Boolean> existsCustomersByAgentIdAndId(String agentId, String id);
	Flux<Customer> findByAgentIdAndIdIn(String agentId, Collection<String> ids);
}
