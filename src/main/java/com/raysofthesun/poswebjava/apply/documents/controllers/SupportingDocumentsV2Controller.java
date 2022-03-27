package com.raysofthesun.poswebjava.apply.documents.controllers;

import com.raysofthesun.poswebjava.apply.documents.models.ApplicationDocumentMeta;
import com.raysofthesun.poswebjava.apply.documents.models.GenericSuccessfulDocumentTransactionData;
import com.raysofthesun.poswebjava.apply.documents.models.RawDocumentMetadata;
import com.raysofthesun.poswebjava.apply.documents.models.SimpleSuccessfulDocumentTransactionData;
import com.raysofthesun.poswebjava.apply.documents.services.DocumentUploadService;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collector;

@RestController
@RequestMapping("/v1/apply/COR")
public class SupportingDocumentsV2Controller {

    private class MultiValueMapCollector implements Collector<Tuple2<FilePart, RawDocumentMetadata>, MultipartBodyBuilder, MultiValueMap> {
        @Override
        public Supplier<MultipartBodyBuilder> supplier() {
            return MultipartBodyBuilder::new;
        }

        @Override
        public BiConsumer<MultipartBodyBuilder, Tuple2<FilePart, RawDocumentMetadata>> accumulator() {
            return ((multipartBodyBuilder, objects) -> {
                FilePart filePart = objects.getT1();
                RawDocumentMetadata rawDocumentMetadata = objects.getT2();

                multipartBodyBuilder
                        .asyncPart("metas", Mono.just(rawDocumentMetadata), RawDocumentMetadata.class)
                        .filename("");
                multipartBodyBuilder
                        .asyncPart("files", filePart.content(), DataBuffer.class)
                        .filename(filePart.filename());
            });
        }

        @Override
        public BinaryOperator<MultipartBodyBuilder> combiner() {
            return (multipartBodyBuilder, multipartBodyBuilder2) -> multipartBodyBuilder2;
        }

        @Override
        public Function<MultipartBodyBuilder, MultiValueMap> finisher() {
            return MultipartBodyBuilder::build;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Set.of(Characteristics.UNORDERED);
        }
    }

    private final DocumentUploadService documentUploadService;

    public SupportingDocumentsV2Controller(DocumentUploadService documentUploadService) {
        this.documentUploadService = documentUploadService;
    }

    @PostMapping("/documents2")
    public Flux<GenericSuccessfulDocumentTransactionData> s(
            @RequestPart("files") Flux<FilePart> filePartFlux,
            @RequestPart("metas") Flux<RawDocumentMetadata> metadataFlux
    ) {

//        filePartMetadataPair -> {
//            FilePart filePart = filePartMetadataPair.getT1();
//            RawDocumentMetadata metadata = filePartMetadataPair.getT2();
//
//            MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
//            bodyBuilder.asyncPart("files", filePart.content(), DataBuffer.class);
//            bodyBuilder.part("metas", metadata, MediaType.APPLICATION_JSON);
//
//            return bodyBuilder.build();
//        }

        return Flux
                .zip(filePartFlux, metadataFlux)
                .collect(new MultiValueMapCollector())
                .flatMapMany(multiValueMap -> WebClient
                        .create()
                        .post()
                        .uri("http://localhost:9000/v1/apply/COR/documents")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA.toString())
                        .body(BodyInserters.fromMultipartData(multiValueMap))
                        .retrieve()
                        .bodyToFlux(GenericSuccessfulDocumentTransactionData.class));
    }

    @PostMapping("/documents")
    public Flux<GenericSuccessfulDocumentTransactionData> uploadDocuments(@RequestPart("files") Flux<FilePart> filePartFlux,
                                                                          @RequestPart("metas") Flux<RawDocumentMetadata> metadataFlux
    ) {
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
