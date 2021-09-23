package com.raysofthesun.poswebjava.apply.repositories;

import com.raysofthesun.poswebjava.apply.models.application.ApplicationMeta;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends ReactiveCrudRepository<ApplicationMeta, String> {
}
