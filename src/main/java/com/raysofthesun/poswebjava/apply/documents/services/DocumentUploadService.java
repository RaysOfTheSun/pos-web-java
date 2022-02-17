package com.raysofthesun.poswebjava.apply.documents.services;

import com.raysofthesun.poswebjava.apply.documents.mappers.DocumentMapper;
import com.raysofthesun.poswebjava.apply.documents.models.ApplicationDocumentMeta;
import com.raysofthesun.poswebjava.apply.documents.models.RawDocumentMetadata;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsResource;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import static org.springframework.data.mongodb.core.query.Query.*;
import static org.springframework.data.mongodb.core.query.Criteria.*;

@Service
public class DocumentUploadService {
    private final ReactiveGridFsTemplate gridFsTemplate;

    public DocumentUploadService(ReactiveGridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }

    public Flux<String> uploadDocumentsWithMeta(Flux<FilePart> fileParts, Flux<RawDocumentMetadata> fileMetadatas) {
        return Flux
                .zip(fileParts, fileMetadatas)
                .flatMap((filePartMetadataPair) -> {
                    FilePart filePart = filePartMetadataPair.getT1();
                    RawDocumentMetadata metadata = filePartMetadataPair.getT2();

                    return gridFsTemplate
                            .store(filePart.content(), filePart.filename(), metadata)
                            .map((ObjectId::toHexString));
                });
    }

    public Flux<ApplicationDocumentMeta> getDocumentMetasByOwnerId(String ownerId) {
        return gridFsTemplate
                .find(query(where("metadata.ownerId").is(ownerId)))
                .map(DocumentMapper.MAPPER::mapGridFsFileToDocumentMeta);
    }

    public Mono<ReactiveGridFsResource> getDocumentResource(String documentId) {
        return gridFsTemplate
                .findOne(query(where("_id").is(documentId)))
                .flatMap(gridFsTemplate::getResource);
    }

}
