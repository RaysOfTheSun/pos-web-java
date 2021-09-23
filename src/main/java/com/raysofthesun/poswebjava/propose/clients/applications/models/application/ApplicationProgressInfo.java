package com.raysofthesun.poswebjava.propose.clients.applications.models.application;

import lombok.Data;

import java.util.List;

@Data
public class ApplicationProgressInfo {
	private String forModule;
	private boolean complete;
	private List<ApplicationProgressChunk> chunks;
}
