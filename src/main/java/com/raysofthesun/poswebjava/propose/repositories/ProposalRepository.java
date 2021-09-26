package com.raysofthesun.poswebjava.propose.repositories;

import com.raysofthesun.poswebjava.propose.models.Proposal;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProposalRepository extends ReactiveMongoRepository<Proposal, String> {
	Flux<Proposal> findAllByAgentId(String agentId);
	Mono<Proposal> findByAgentIdAndId(String agentId, String id);

}
