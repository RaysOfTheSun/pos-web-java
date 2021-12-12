package com.raysofthesun.poswebjava.apply.documents.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationDocument<T, G> {

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String id = UUID.randomUUID().toString();

	private String src;

	private int fileSize;

	private String fileName;

	private String fileType;

	private T type;

	private G group;

	private String ownerId;

	@JsonIgnore
	private int index;

	@JsonIgnore
	private String agentId;

	@JsonIgnore
	private String applicationId;
}
