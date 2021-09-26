package com.raysofthesun.poswebjava.apply.models.application;

import com.raysofthesun.poswebjava.apply.constants.ApplicationStatus;
import com.raysofthesun.poswebjava.apply.constants.PaymentFrequency;
import com.raysofthesun.poswebjava.apply.models.insured.Insured;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Application {

	private Application() {}

	public static Builder create(ApplicationCreationRequest request) {
		return Builder.fromRequest(request);
	}

	private String id = UUID.randomUUID().toString();
	private String name;
	private String creationDate;

	private Insured owner;
	private Insured insured;
	private List<Insured> dependents;

	private ApplicationStatus status;
	private ApplicationPaymentInfo paymentInfo;
	private ApplicationProgressInfo progressInfo;

	public static class Builder {
		private final Application application;
		private final ApplicationPaymentInfo paymentInfo;

		private Builder() {
			application = new Application();
			paymentInfo = new ApplicationPaymentInfo();
			application.setPaymentInfo(paymentInfo);
			application.setDependents(new ArrayList<>());
		}

		private Builder(ApplicationCreationRequest request) {
			this();
			application.setName(request.getName());
			application.setCreationDate(Instant.now().toString());
		}

		public static Builder fromRequest(ApplicationCreationRequest request) {
			return new Builder(request);
		}

		public Builder withName(String name) {
			application.setName(name);
			return this;
		}

		public Builder withOwner(Insured owner) {
			application.setOwner(owner);
			return this;
		}

		public Builder withDependents(List<Insured> insureds) {
			application.setDependents(insureds);
			return this;
		}

		public Builder withInsured(Insured insured) {
			application.setInsured(insured);
			return this;
		}

		public Builder withTotalPremium(String totalPremium) {
			paymentInfo.setTotalPremium(totalPremium);
			return this;
		}

		public Application build() {
			final PaymentFrequency paymentFrequency = paymentInfo.getPaymentFrequency() == null
					? PaymentFrequency.ANNUAL
					: paymentInfo.getPaymentFrequency();
			final ApplicationStatus applicationStatus = application.getStatus() == null
					? ApplicationStatus.IN_PROGRESS
					: application.getStatus();

			application.setStatus(applicationStatus);
			paymentInfo.setPaymentFrequency(paymentFrequency);

			return application;
		}
	}
}
