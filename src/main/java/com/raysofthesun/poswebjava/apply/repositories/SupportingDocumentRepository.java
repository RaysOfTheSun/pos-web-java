package com.raysofthesun.poswebjava.apply.repositories;

import com.raysofthesun.poswebjava.apply.models.document.SupportingDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SupportingDocumentRepository extends ReactiveMongoRepository<SupportingDocument, String> {
	Flux<SupportingDocument> findAllByOwnerIdAndApplicationIdOrderByIndex(String ownerId, String applicationId);
}
