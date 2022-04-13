package com.raysofthesun.poswebjava.apply.documents.services.core;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.raysofthesun.poswebjava.apply.documents.mappers.DocumentMapper;
import com.raysofthesun.poswebjava.apply.documents.models.ApplySupportingDocumentMetadata;
import com.raysofthesun.poswebjava.apply.documents.models.SuccessfulApplyDocumentTransaction;
import com.raysofthesun.poswebjava.core.common.services.PosWebService;
import com.raysofthesun.poswebjava.core.document.services.DocumentUploadService;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;

public abstract class ApplyDocumentUploadService
        extends DocumentUploadService<ApplySupportingDocumentMetadata, SuccessfulApplyDocumentTransaction>
        implements PosWebService {

    public ApplyDocumentUploadService(ReactiveGridFsTemplate reactiveGridFsTemplate) {
        super(reactiveGridFsTemplate);
    }

    @Override
    protected SuccessfulApplyDocumentTransaction makeSuccessfulUploadResponse(ApplySupportingDocumentMetadata documentMetadata,
                                                                               ObjectId objectId) {
        return new SuccessfulApplyDocumentTransaction(objectId.toHexString(), documentMetadata.getDocumentIndex());
    }

    @Override
    public ApplySupportingDocumentMetadata makeDocumentMetadataFromFile(GridFSFile gridFSFile) {
        return DocumentMapper.MAPPER.mapGridFsFileToDocumentMeta(gridFSFile);
    }
}
