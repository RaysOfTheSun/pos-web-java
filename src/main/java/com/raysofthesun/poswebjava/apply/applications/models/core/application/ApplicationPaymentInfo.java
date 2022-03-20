package com.raysofthesun.poswebjava.apply.applications.models.core.application;

import com.raysofthesun.poswebjava.apply.applications.emums.ApplicationPaymentMethod;
import com.raysofthesun.poswebjava.apply.applications.emums.ApplicationRenewalMethod;
import com.raysofthesun.poswebjava.apply.applications.emums.ApplicationPaymentFrequency;
import lombok.Data;

@Data
public class ApplicationPaymentInfo {
	private String totalPremium;
	private String initialPremium;
	private String subsequentPremium;
	private ApplicationPaymentFrequency paymentFrequency;
	private ApplicationPaymentMethod paymentMethod;
	private ApplicationRenewalMethod renewalMethod;
}
