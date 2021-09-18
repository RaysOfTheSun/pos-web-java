package com.raysofthesun.poswebjava.propose.repositories;

import com.raysofthesun.poswebjava.propose.models.prospect.Prospect;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProspectRepository extends ReactiveMongoRepository<Prospect, String> {
}
