package com.raysofthesun.poswebjava.apply.documents.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenericSuccessfulDocumentTransactionData {
    private String documentId;
    private String documentIndex;
}
