package com.raysofthesun.poswebjava.apply.insured.repositories;

import com.raysofthesun.poswebjava.apply.insured.models.insured.Insured;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface InsuredRepository extends ReactiveMongoRepository<Insured, String> {
}
