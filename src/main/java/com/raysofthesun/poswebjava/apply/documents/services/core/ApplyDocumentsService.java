package com.raysofthesun.poswebjava.apply.documents.services.core;

import com.raysofthesun.poswebjava.apply.applications.factories.ApplicationServiceFactory;
import com.raysofthesun.poswebjava.apply.applications.models.core.application.ApplicationMeta;
import com.raysofthesun.poswebjava.apply.documents.factories.ApplyDocumentRequirementServiceFactory;
import com.raysofthesun.poswebjava.apply.documents.factories.ApplyDocumentUploadServiceFactory;
import com.raysofthesun.poswebjava.apply.documents.models.ApplySupportingDocumentMetadata;
import com.raysofthesun.poswebjava.apply.documents.models.SuccessfulApplyDocumentTransaction;
import com.raysofthesun.poswebjava.apply.insureds.models.core.insured.Insured;
import com.raysofthesun.poswebjava.core.common.enums.Market;
import com.raysofthesun.poswebjava.core.common.services.PosWebService;
import com.raysofthesun.poswebjava.core.configuration.models.PosDocumentRequirement;
import com.raysofthesun.poswebjava.core.document.models.SimpleSuccessfulDocumentTransaction;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsResource;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Query.*;
import static org.springframework.data.mongodb.core.query.Criteria.*;

public abstract class ApplyDocumentsService implements PosWebService {
    private final ApplicationServiceFactory applicationServiceFactory;
    private final ApplyDocumentUploadServiceFactory documentUploadServiceFactory;
    private final ApplyDocumentRequirementServiceFactory documentRequirementServiceFactory;

    public ApplyDocumentsService(
            ApplicationServiceFactory applicationServiceFactory,
            ApplyDocumentUploadServiceFactory documentUploadServiceFactory,
            ApplyDocumentRequirementServiceFactory documentRequirementServiceFactory) {
        this.applicationServiceFactory = applicationServiceFactory;
        this.documentUploadServiceFactory = documentUploadServiceFactory;
        this.documentRequirementServiceFactory = documentRequirementServiceFactory;
    }

    public Mono<Map<String, List<PosDocumentRequirement>>> getDocumentReqsForInsuredsInApplication(String applicationId,
                                                                                                   Market market) {
        return this.applicationServiceFactory.getServiceForMarket(market)
                .getApplicationMetaAndInsuredsById(applicationId, market)
                .map(metaAndInsureds -> {
                    List<Insured> insureds = metaAndInsureds.getT2();
                    ApplicationMeta applicationMeta = metaAndInsureds.getT1();

                    return this.documentRequirementServiceFactory
                            .getServiceForMarket(market)
                            .getRequiredDocsForApplication(applicationMeta, insureds);
                });
    }

    public Flux<SuccessfulApplyDocumentTransaction> uploadDocumentsWithMetadata(Flux<FilePart> files,
                                                                                Flux<ApplySupportingDocumentMetadata> metadata,
                                                                                Market market) {
        return this.documentUploadServiceFactory.getServiceForMarket(market).uploadWithMetadata(files, metadata);
    }

    public Flux<SuccessfulApplyDocumentTransaction> replaceFilesById(Flux<FilePart> replacementFiles,
                                                                     Flux<String> targetDocumentIds,
                                                                     Market market) {
        return this.documentUploadServiceFactory.getServiceForMarket(market)
                .replaceFilesById(replacementFiles, targetDocumentIds);
    }

    public Flux<ApplySupportingDocumentMetadata> getDocumentMetadataWithOwnerId(String ownerId, Market market) {
        return this.documentUploadServiceFactory
                .getServiceForMarket(market)
                .getDocumentMetadata(query(where("metadata.ownerId").is(ownerId)));
    }

    public Mono<ReactiveGridFsResource> getDocumentResourceById(String documentId, ServerWebExchange exchange, Market market) {
        return this.documentUploadServiceFactory.getServiceForMarket(market)
                .getDocumentResourceById(documentId);
    }

    public Flux<SimpleSuccessfulDocumentTransaction> deleteDocumentsById(Collection<String> documentIds, Market market) {
        return this.documentUploadServiceFactory.getServiceForMarket(market).deleteDocumentsById(documentIds);
    }
}
