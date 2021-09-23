package com.raysofthesun.poswebjava.apply.models.application;

import lombok.Data;

import java.util.List;

@Data
public class ApplicationProgressInfo {
	private String forModule;
	private boolean complete;
	private List<ApplicationProgressChunk> chunks;
}
