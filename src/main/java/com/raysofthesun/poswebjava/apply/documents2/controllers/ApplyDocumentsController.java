package com.raysofthesun.poswebjava.apply.documents2.controllers;

import com.raysofthesun.poswebjava.apply.documents2.factories.ApplyDocumentsServiceFactory;
import com.raysofthesun.poswebjava.apply.documents2.models.ApplySupportingDocumentMetadata;
import com.raysofthesun.poswebjava.apply.documents2.models.SuccessfulApplyDocumentTransaction;
import com.raysofthesun.poswebjava.core.common.enums.Market;
import com.raysofthesun.poswebjava.core.configuration.models.PosDocumentRequirement;
import com.raysofthesun.poswebjava.core.document.models.SimpleSuccessfulDocumentTransaction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Api(tags = "Apply Document Related Processes")
@RestController
@RequestMapping("/v1/apply")
public class ApplyDocumentsController {
    private final ApplyDocumentsServiceFactory serviceFactory;

    public ApplyDocumentsController(ApplyDocumentsServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    @ApiOperation("Get the document requirements per insured")
    @GetMapping("/{market}/documents/requirements/{applicationId}")
    public Mono<Map<String, List<PosDocumentRequirement>>> getDocumentReqsByApplicationId(@PathVariable String applicationId,
                                                                                          @PathVariable Market market) {
        return this.serviceFactory
                .getServiceForMarket(market)
                .getDocumentReqsForInsuredsInApplication(applicationId, market);
    }

    @ApiOperation("Upload documents with metadata")
    @PutMapping("/{market}/documents")
    public Flux<SuccessfulApplyDocumentTransaction> uploadDocumentsWithMeta(@RequestPart("files") Flux<FilePart> files,
                                                                            @RequestPart("metas") Flux<ApplySupportingDocumentMetadata> metadata,
                                                                            @PathVariable Market market) {
        return this.serviceFactory.getServiceForMarket(market).uploadDocumentsWithMetadata(files, metadata, market);
    }

    @ApiOperation("Replace the resource associated with a given documentId")
    @PatchMapping("/{market}/documents")
    public Flux<SuccessfulApplyDocumentTransaction> replaceFilesById(@RequestPart("files") Flux<FilePart> files,
                                                                     @RequestPart("documentIds") Flux<String> documentIds,
                                                                     @PathVariable Market market) {
        return this.serviceFactory.getServiceForMarket(market).replaceFilesById(files, documentIds, market);
    }

    @ApiOperation("Delete a document by its id")
    @DeleteMapping("/{market}/documents")
    public Flux<SimpleSuccessfulDocumentTransaction> deleteDocumentsById(@RequestParam Collection<String> documentIds,
                                                                         @PathVariable Market market) {
        return this.serviceFactory.getServiceForMarket(market).deleteDocumentsById(documentIds, market);
    }

    @ApiOperation("Get the metadata of documents with a given owner id")
    @GetMapping("/{market}/documents/owners/{ownerId}")
    public Flux<ApplySupportingDocumentMetadata> getDocumentMetasWithOwnerId(@PathVariable String ownerId,
                                                                             @PathVariable Market market) {
        return this.serviceFactory.getServiceForMarket(market).getDocumentMetadataWithOwnerId(ownerId, market);
    }

    @ApiOperation("Get the file associated with a given documentId")
    @GetMapping("/{market}/documents/{documentId}")
    public Flux<Void> getDocumentResourceById(@PathVariable String documentId, @PathVariable Market market,
                                              @ApiIgnore ServerWebExchange exchange) {
        return this.serviceFactory.getServiceForMarket(market).getDocumentResourceById(documentId, exchange, market);
    }


}
