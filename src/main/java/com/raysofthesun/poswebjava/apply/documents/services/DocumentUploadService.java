package com.raysofthesun.poswebjava.apply.documents.services;

import com.raysofthesun.poswebjava.apply.documents.mappers.DocumentMapper;
import com.raysofthesun.poswebjava.apply.documents.models.ApplicationDocumentMeta;
import com.raysofthesun.poswebjava.apply.documents.models.GenericSuccessfulDocumentTransactionData;
import com.raysofthesun.poswebjava.apply.documents.models.RawDocumentMetadata;
import com.raysofthesun.poswebjava.apply.documents.models.SimpleSuccessfulDocumentTransactionData;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsResource;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Query.*;
import static org.springframework.data.mongodb.core.query.Criteria.*;

@Service
public class DocumentUploadService {
    private final ReactiveGridFsTemplate gridFsTemplate;

    public DocumentUploadService(ReactiveGridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }

    public Flux<GenericSuccessfulDocumentTransactionData> uploadDocumentsWithMeta(Flux<FilePart> fileParts, Flux<RawDocumentMetadata> fileMetadatas) {
        return Flux
                .zip(fileParts, fileMetadatas)
                .flatMap((filePartMetadataPair) -> {
                    FilePart filePart = filePartMetadataPair.getT1();
                    RawDocumentMetadata metadata = filePartMetadataPair.getT2();

                    return gridFsTemplate
                            .store(filePart.content(), filePart.filename(), metadata)
                            .map((objectId -> new GenericSuccessfulDocumentTransactionData(
                                    objectId.toHexString(), metadata.getDocumentIndex()))
                            );
                });
    }

    public Flux<SimpleSuccessfulDocumentTransactionData> deleteDocumentsById(Collection<String> documentIds) {
        return Flux
                .fromIterable(documentIds)
                .flatMap(documentId -> deleteDocumentById(documentId).map((b) -> documentId))
                .map(SimpleSuccessfulDocumentTransactionData::new);
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

    public Mono<Boolean> deleteDocumentById(String documentId) {
        return gridFsTemplate
                .delete(query(where("_id").is(documentId)))
                .thenReturn(true);
    }

    public Flux<Map<String, String>> reuploadFilesByDocumentId
            (Flux<FilePart> fileParts, Flux<String> targetDocumentIds) {
        return Flux
                .zip(fileParts, targetDocumentIds)
                .flatMap((fileAndMetaPair) -> {
                    FilePart filePart = fileAndMetaPair.getT1();
                    String documentId = fileAndMetaPair.getT2();

                    return replaceFieById(documentId, filePart);
                });
    }

    public Mono<Map<String, String>> replaceFieById(String fileToReplaceId, FilePart replacementFile) {
        return gridFsTemplate
                .findOne(query(where("_id").is(fileToReplaceId)))
                .map(DocumentMapper.MAPPER::mapGridFsFileToDocumentMeta)
                .flatMap(applicationDocumentMeta -> deleteDocumentById(fileToReplaceId)
                        .flatMap(deleteSuccess -> gridFsTemplate
                                .store(replacementFile.content(), replacementFile.filename(), applicationDocumentMeta)
                                .map(objectId -> Map.of("documentId", objectId.toHexString()))));
    }
}
