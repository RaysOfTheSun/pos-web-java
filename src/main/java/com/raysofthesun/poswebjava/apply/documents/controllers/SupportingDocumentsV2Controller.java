package com.raysofthesun.poswebjava.apply.documents.controllers;

import com.raysofthesun.poswebjava.apply.documents.models.ApplicationDocumentMeta;
import com.raysofthesun.poswebjava.apply.documents.models.GenericSuccessfulDocumentTransactionData;
import com.raysofthesun.poswebjava.apply.documents.models.RawDocumentMetadata;
import com.raysofthesun.poswebjava.apply.documents.models.SimpleSuccessfulDocumentTransactionData;
import com.raysofthesun.poswebjava.apply.documents.services.DocumentUploadService;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/v1/apply")
public class SupportingDocumentsV2Controller {
    private final DocumentUploadService documentUploadService;

    public SupportingDocumentsV2Controller(DocumentUploadService documentUploadService) {
        this.documentUploadService = documentUploadService;
    }

    @PostMapping("/documents")
    public Flux<GenericSuccessfulDocumentTransactionData> uploadDocuments(@RequestPart("files") Flux<FilePart> filePartFlux,
                                                                          @RequestPart("metas") Flux<RawDocumentMetadata> metadataFlux) {

        return documentUploadService.uploadDocumentsWithMeta(filePartFlux, metadataFlux);
    }

    @DeleteMapping("/documents")
    public Flux<SimpleSuccessfulDocumentTransactionData> deleteDocumentsById(@RequestParam Collection<String> documentIds) {
        return documentUploadService.deleteDocumentsById(documentIds);
    }

    @PatchMapping("/documents")
    public Flux<Map<String, String>> reuploadDocuments(@RequestPart("files") Flux<FilePart> filePartFlux,
                                                       @RequestPart("documentIds") Flux<String> documentIds) {

        return documentUploadService.reuploadFilesByDocumentId(filePartFlux, documentIds);
    }

    @GetMapping("/documents/owner/{ownerId}/metas")
    public Flux<ApplicationDocumentMeta> getDocumentMetasByOwnerId(@PathVariable String ownerId) {
        return documentUploadService.getDocumentMetasByOwnerId(ownerId);
    }

    @GetMapping("/documents/{documentId}")
    public Flux<Void> getDocumentSourceById(@PathVariable String documentId, ServerWebExchange exchange) {
        return documentUploadService
                .getDocumentResource(documentId)
                .flatMapMany((fsResource -> exchange.getResponse().writeWith(fsResource.getDownloadStream())));
    }
}
