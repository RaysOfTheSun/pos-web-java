package com.raysofthesun.poswebjava.propose.feign_cients.applications.models.application;

import lombok.Data;

import java.util.List;

@Data
public class ApplicationProgressInfo {
	private String forModule;
	private boolean complete;
	private List<ApplicationProgressChunk> chunks;
}
