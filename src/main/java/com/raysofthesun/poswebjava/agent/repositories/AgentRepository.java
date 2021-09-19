package com.raysofthesun.poswebjava.agent.repositories;

import com.raysofthesun.poswebjava.agent.models.agent.Agent;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentRepository extends ReactiveCrudRepository<Agent, String> {
}
