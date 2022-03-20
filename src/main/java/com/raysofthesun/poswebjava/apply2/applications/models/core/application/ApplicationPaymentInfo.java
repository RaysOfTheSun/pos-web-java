package com.raysofthesun.poswebjava.apply2.applications.models.core.application;

import com.raysofthesun.poswebjava.apply2.applications.emums.ApplicationPaymentMethod;
import com.raysofthesun.poswebjava.apply2.applications.emums.ApplicationRenewalMethod;
import com.raysofthesun.poswebjava.apply2.applications.emums.ApplicationPaymentFrequency;
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
