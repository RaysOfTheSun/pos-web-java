package com.raysofthesun.poswebjava.apply.models.application;

import com.raysofthesun.poswebjava.apply.constants.ApplicationPaymentMethod;
import com.raysofthesun.poswebjava.apply.constants.ApplicationRenewalMethod;
import com.raysofthesun.poswebjava.apply.constants.PaymentFrequency;
import lombok.Data;

@Data
public class ApplicationPaymentInfo {
	private String totalPremium;
	private String initialPremium;
	private String subsequentPremium;
	private PaymentFrequency paymentFrequency;
	private ApplicationPaymentMethod paymentMethod;
	private ApplicationRenewalMethod renewalMethod;
}
