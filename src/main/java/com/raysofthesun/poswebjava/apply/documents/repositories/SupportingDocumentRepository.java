package com.raysofthesun.poswebjava.apply.documents.repositories;

import com.raysofthesun.poswebjava.apply.documents.models.SupportingDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SupportingDocumentRepository extends ReactiveMongoRepository<SupportingDocument, String> {
	Flux<SupportingDocument> findAllByOwnerIdAndApplicationIdOrderByIndex(String ownerId, String applicationId);
}
