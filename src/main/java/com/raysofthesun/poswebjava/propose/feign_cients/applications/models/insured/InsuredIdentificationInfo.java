package com.raysofthesun.poswebjava.propose.feign_cients.applications.models.insured;

import lombok.Data;

@Data
public class InsuredIdentificationInfo {
	private String type;
	private String idNumber;
	private String issueDate;
}
