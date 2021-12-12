package com.raysofthesun.poswebjava.apply.documents.repositories;

import com.raysofthesun.poswebjava.apply.documents.models.ApplicationDocumentSource;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface SupportingDocumentSourceRepository extends
		ReactiveMongoRepository<ApplicationDocumentSource, String> {
}
