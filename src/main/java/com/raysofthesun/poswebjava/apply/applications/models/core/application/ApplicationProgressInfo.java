package com.raysofthesun.poswebjava.apply.applications.models.core.application;

import lombok.Data;

import java.util.List;

@Data
public class ApplicationProgressInfo {
	private String forModule;
	private boolean complete;
	private List<ApplicationProgressChunk> chunks;
}
