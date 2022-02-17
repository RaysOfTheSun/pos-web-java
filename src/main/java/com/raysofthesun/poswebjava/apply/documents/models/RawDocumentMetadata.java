package com.raysofthesun.poswebjava.apply.documents.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RawDocumentMetadata {
    private String ownerId;
    private String ownerRole;
    private String documentType;
    private String documentGroup;
    private String applicationId;
    private String documentIndex;
}
