package com.raysofthesun.poswebjava.apply.application.models;

import lombok.Data;

import java.util.List;

@Data
public class ApplicationProgressInfo {
	private String forModule;
	private boolean complete;
	private List<ApplicationProgressChunk> chunks;
}
