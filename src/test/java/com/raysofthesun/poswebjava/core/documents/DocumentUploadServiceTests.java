package com.raysofthesun.poswebjava.core.documents;

import com.raysofthesun.poswebjava.core.document.services.DocumentUploadService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;

@ExtendWith(MockitoExtension.class)
@DisplayName("DocumentService")
public class DocumentUploadServiceTests {
    @Mock
    ReactiveGridFsTemplate gridFsTemplate;

    @InjectMocks
    TestDocumentUploadService documentUploadService;

    public class DocumentRetrievalTests {

    }

    @Nested
    @DisplayName("when replacing documents")
    public class DocumentReplacementTests {

    }
}
