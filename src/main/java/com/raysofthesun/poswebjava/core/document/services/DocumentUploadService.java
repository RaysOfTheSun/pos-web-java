package com.raysofthesun.poswebjava.core.document.services;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.raysofthesun.poswebjava.core.document.models.SimpleSuccessfulDocumentTransaction;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsResource;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

import static org.springframework.data.mongodb.core.query.Query.*;
import static org.springframework.data.mongodb.core.query.Criteria.*;

public abstract class DocumentUploadService<M, U> {
    private final ReactiveGridFsTemplate reactiveGridFsTemplate;

    public DocumentUploadService(ReactiveGridFsTemplate reactiveGridFsTemplate) {
        this.reactiveGridFsTemplate = reactiveGridFsTemplate;
    }

    protected abstract U makeSuccessfulUploadResponse(M documentMetadata, ObjectId objectId);

    protected abstract M makeDocumentMetadataFromFile(GridFSFile gridFSFile);

    protected Query makeDocumentIdQuery(String documentId) {
        return query(where("_id").is(documentId));
    }

    public Flux<U> uploadWithMetadata(Flux<FilePart> files, Flux<M> metadatas) {
        return Flux.zip(files, metadatas).flatMap(fileAndMetadata -> {
            M metadata = fileAndMetadata.getT2();
            FilePart filePart = fileAndMetadata.getT1();

            return this.reactiveGridFsTemplate
                    .store(filePart.content(), filePart.filename(), metadata)
                    .map(objectId -> this.makeSuccessfulUploadResponse(metadata, objectId));
        });
    }

    public Flux<SimpleSuccessfulDocumentTransaction> deleteDocumentsById(Collection<String> documentIds) {
        return Flux.fromIterable(documentIds).flatMap(this::deleteDocumentById);
    }

    public Mono<ReactiveGridFsResource> getDocumentResourceById(String documentId) {
        return this.reactiveGridFsTemplate
                .findOne(this.makeDocumentIdQuery(documentId))
                .flatMap(this.reactiveGridFsTemplate::getResource);
    }

    public Flux<U> replaceFilesById(Flux<FilePart> replacementFiles, Flux<String> targetDocumentIds) {
        return Flux
                .zip(replacementFiles, targetDocumentIds)
                .flatMap(replacementAndTargetId -> this.replaceFileById(
                        replacementAndTargetId.getT2(), replacementAndTargetId.getT1()));

    }

    public Flux<M> getDocumentMetadata(Query query) {
        return this.reactiveGridFsTemplate
                .find(query)
                .map(this::makeDocumentMetadataFromFile);
    }

    protected Mono<SimpleSuccessfulDocumentTransaction> deleteDocumentById(String documentId) {
        return this.reactiveGridFsTemplate
                .delete(this.makeDocumentIdQuery(documentId))
                .then(Mono.just(new SimpleSuccessfulDocumentTransaction(documentId)));
    }

    protected Mono<U> replaceFileById(String idOfFileToReplace, FilePart replacementFile) {
        return this.reactiveGridFsTemplate
                .findFirst(this.makeDocumentIdQuery(idOfFileToReplace))
                .map(this::makeDocumentMetadataFromFile)
                .flatMap(metadata -> this.deleteDocumentById(idOfFileToReplace)
                        .flatMap(deletionIndicator -> {
                            Flux<FilePart> files = Flux.just(replacementFile);
                            Flux<M> documentMetadata = Flux.just(metadata);

                            return this.uploadWithMetadata(files, documentMetadata).last();
                        })
                );
    }
}
