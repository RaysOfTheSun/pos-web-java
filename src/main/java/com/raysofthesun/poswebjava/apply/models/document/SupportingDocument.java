package com.raysofthesun.poswebjava.apply.models.document;

import com.raysofthesun.poswebjava.apply.constants.SupportingDocumentGroup;
import com.raysofthesun.poswebjava.apply.constants.SupportingDocumentType;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("supporting_documents")
public class SupportingDocument extends ApplicationDocument<SupportingDocumentType, SupportingDocumentGroup> {
}
