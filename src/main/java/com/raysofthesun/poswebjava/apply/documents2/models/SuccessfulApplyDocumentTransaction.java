package com.raysofthesun.poswebjava.apply.documents2.models;

import com.raysofthesun.poswebjava.core.document.models.SimpleSuccessfulDocumentTransaction;
import lombok.Getter;

@Getter
public class SuccessfulApplyDocumentTransaction extends SimpleSuccessfulDocumentTransaction {
    private final String documentIndex;

    public SuccessfulApplyDocumentTransaction(String documentId, String documentIndex) {
        super(documentId);
        this.documentIndex = documentIndex;
    }
}
