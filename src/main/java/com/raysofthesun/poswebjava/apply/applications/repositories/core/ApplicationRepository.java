package com.raysofthesun.poswebjava.apply.applications.repositories.core;

import com.raysofthesun.poswebjava.apply.applications.models.core.application.ApplicationMeta;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface ApplicationRepository extends ReactiveMongoRepository<ApplicationMeta, String> {
	Flux<ApplicationMeta> findAllByCustomerId(String customerId, Pageable pageable);
	Mono<Integer> countApplicationMetasByCustomerId(String customerId);
}
