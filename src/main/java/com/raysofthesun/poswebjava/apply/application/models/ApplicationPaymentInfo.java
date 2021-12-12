package com.raysofthesun.poswebjava.apply.application.models;

import com.raysofthesun.poswebjava.apply.application.constants.ApplicationPaymentMethod;
import com.raysofthesun.poswebjava.apply.application.constants.ApplicationRenewalMethod;
import com.raysofthesun.poswebjava.apply.application.constants.PaymentFrequency;
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
