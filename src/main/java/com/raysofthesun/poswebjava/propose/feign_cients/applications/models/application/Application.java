package com.raysofthesun.poswebjava.propose.feign_cients.applications.models.application;

import com.raysofthesun.poswebjava.apply.constants.ApplicationStatus;
import com.raysofthesun.poswebjava.propose.feign_cients.applications.models.insured.Insured;
import lombok.Data;

import java.util.List;

@Data
public class Application {
	private String id;
	private String name;
	private String creationDate;

	private Insured owner;
	private Insured insured;
	private List<Insured> dependents;

	private ApplicationStatus status;
	private ApplicationPaymentInfo paymentInfo;
	private ApplicationProgressInfo progressInfo;
}
