package com.raysofthesun.poswebjava.propose.feign.application.models.insured;

import lombok.Data;

@Data
public class InsuredIdentificationInfo {
	private String type;
	private String idNumber;
	private String issueDate;
}
