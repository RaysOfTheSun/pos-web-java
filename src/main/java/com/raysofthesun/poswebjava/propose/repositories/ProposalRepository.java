package com.raysofthesun.poswebjava.propose.repositories;

import com.raysofthesun.poswebjava.propose.models.proposal.Proposal;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProposalRepository extends ReactiveMongoRepository<Proposal, String> {
}
