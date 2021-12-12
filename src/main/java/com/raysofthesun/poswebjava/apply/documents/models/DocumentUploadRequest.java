package com.raysofthesun.poswebjava.apply.documents.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DocumentUploadRequest {
	private List<String> ownerIds = new ArrayList<>();
	private List<SupportingDocument> documents = new ArrayList<>();
}
