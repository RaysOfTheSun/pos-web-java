package com.raysofthesun.poswebjava.propose.feign.application.models.application;

import com.raysofthesun.poswebjava.apply.insureds.enums.ApplicationStatus;
import com.raysofthesun.poswebjava.propose.feign.application.models.insured.Insured;
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
	private List<ApplicationProgressInfo> progressInfo;
}
