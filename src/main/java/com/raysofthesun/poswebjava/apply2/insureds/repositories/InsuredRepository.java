package com.raysofthesun.poswebjava.apply2.insureds.repositories;

import com.raysofthesun.poswebjava.apply2.insureds.models.core.insured.Insured;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface InsuredRepository extends ReactiveMongoRepository<Insured, String> {
}
