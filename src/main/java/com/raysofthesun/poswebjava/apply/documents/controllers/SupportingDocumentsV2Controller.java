package com.raysofthesun.poswebjava.apply.documents.controllers;

import com.raysofthesun.poswebjava.apply.documents.models.ApplicationDocumentMeta;
import com.raysofthesun.poswebjava.apply.documents.models.RawDocumentMetadata;
import com.raysofthesun.poswebjava.apply.documents.services.DocumentUploadService;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/v1/apply")
public class SupportingDocumentsV2Controller {
    private final DocumentUploadService documentUploadService;

    public SupportingDocumentsV2Controller(DocumentUploadService documentUploadService) {
        this.documentUploadService = documentUploadService;
    }

    @PostMapping("/documents")
    public Flux<String> uploadDocuments(@RequestPart("files") Flux<FilePart> filePartFlux,
                                        @RequestPart("metas") Flux<RawDocumentMetadata> metadataFlux) {

        return documentUploadService.uploadDocumentsWithMeta(filePartFlux, metadataFlux);
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
