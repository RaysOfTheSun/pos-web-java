package com.raysofthesun.poswebjava.apply.repositories;

import com.raysofthesun.poswebjava.apply.models.insured.Insured;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface InsuredRepository extends ReactiveMongoRepository<Insured, String> {
}
