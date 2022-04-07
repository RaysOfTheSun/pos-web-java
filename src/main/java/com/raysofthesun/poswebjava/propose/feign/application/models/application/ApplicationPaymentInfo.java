package com.raysofthesun.poswebjava.propose.feign.application.models.application;

import com.raysofthesun.poswebjava.apply.applications.emums.ApplicationPaymentMethod;
import com.raysofthesun.poswebjava.apply.applications.emums.ApplicationRenewalMethod;
import lombok.Data;

@Data
public class ApplicationPaymentInfo {
	private String totalPremium;
	private String initialPremium;
	private String subsequentPremium;
	private ApplicationPaymentMethod paymentMethod;
	private ApplicationRenewalMethod renewalMethod;
}
