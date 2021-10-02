package com.raysofthesun.poswebjava.propose.feign_cients.applications.models.application;

import com.raysofthesun.poswebjava.apply.constants.ApplicationPaymentMethod;
import com.raysofthesun.poswebjava.apply.constants.ApplicationRenewalMethod;
import lombok.Data;

@Data
public class ApplicationPaymentInfo {
	private String totalPremium;
	private String initialPremium;
	private String subsequentPremium;
	private ApplicationPaymentMethod paymentMethod;
	private ApplicationRenewalMethod renewalMethod;
}