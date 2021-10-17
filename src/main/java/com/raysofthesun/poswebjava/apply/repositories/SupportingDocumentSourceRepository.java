package com.raysofthesun.poswebjava.apply.repositories;

import com.raysofthesun.poswebjava.apply.models.document.ApplicationDocumentSource;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface SupportingDocumentSourceRepository extends
		ReactiveMongoRepository<ApplicationDocumentSource, String> {
}
