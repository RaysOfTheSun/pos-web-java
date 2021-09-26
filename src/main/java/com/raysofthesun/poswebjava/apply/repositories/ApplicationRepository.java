package com.raysofthesun.poswebjava.apply.repositories;

import com.raysofthesun.poswebjava.apply.models.application.ApplicationMeta;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends ReactiveMongoRepository<ApplicationMeta, String> {
}
