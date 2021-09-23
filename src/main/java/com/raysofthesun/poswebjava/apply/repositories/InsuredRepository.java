package com.raysofthesun.poswebjava.apply.repositories;

import com.raysofthesun.poswebjava.apply.models.insured.Insured;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface InsuredRepository extends ReactiveCrudRepository<Insured, String> {
}
