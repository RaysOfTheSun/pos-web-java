package com.raysofthesun.poswebjava.apply.documents2.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplySupportingDocumentMetadata {
    private String ownerId;
    private boolean isImage;
    private String fileName;
    private String documentId;
    private String uploadDate;
    private String documentType;
    private String documentGroup;
    private String documentIndex;
}
