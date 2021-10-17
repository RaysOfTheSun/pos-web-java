package com.raysofthesun.poswebjava.apply.models.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document("document_sources")
@AllArgsConstructor
public class ApplicationDocumentSource {
	private String id;
	private String src;
}
