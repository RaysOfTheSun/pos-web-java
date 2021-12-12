package com.raysofthesun.poswebjava.apply.application.repositories;

import com.raysofthesun.poswebjava.apply.application.models.ApplicationMeta;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ApplicationRepository extends ReactiveMongoRepository<ApplicationMeta, String> {
	Flux<ApplicationMeta> findAllByCustomerId(String customerId);
}
