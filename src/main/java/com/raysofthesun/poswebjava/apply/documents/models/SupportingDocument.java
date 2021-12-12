package com.raysofthesun.poswebjava.apply.documents.models;

import com.raysofthesun.poswebjava.apply.documents.constants.SupportingDocumentGroup;
import com.raysofthesun.poswebjava.apply.documents.constants.SupportingDocumentType;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("supporting_documents")
public class SupportingDocument extends ApplicationDocument<SupportingDocumentType, SupportingDocumentGroup> {
}
