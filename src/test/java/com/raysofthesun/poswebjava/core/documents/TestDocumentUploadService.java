package com.raysofthesun.poswebjava.core.documents;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.raysofthesun.poswebjava.core.document.services.DocumentUploadService;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;

import java.util.Map;

public class TestDocumentUploadService extends DocumentUploadService<Object, Object> {
    public TestDocumentUploadService(ReactiveGridFsTemplate reactiveGridFsTemplate) {
        super(reactiveGridFsTemplate);
    }

    @Override
    protected Object makeSuccessfulUploadResponse(Object documentMetadata, ObjectId objectId) {
        return Map.of("id", objectId.toHexString());
    }

    @Override
    protected Object makeDocumentMetadataFromFile(GridFSFile gridFSFile) {
        return Map.of("", "");
    }
}
