package com.raysofthesun.poswebjava.apply.models.application;

import com.raysofthesun.poswebjava.apply.constants.ApplicationStatus;
import com.raysofthesun.poswebjava.apply.constants.PaymentFrequency;
import com.raysofthesun.poswebjava.apply.models.insured.Insured;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Application {

	private final String id = UUID.randomUUID().toString();
	private final String name;
	private final String creationDate;

	private final Insured owner;
	private final Insured insured;
	private final List<Insured> dependents;

	private final ApplicationStatus status;
	private final ApplicationPaymentInfo paymentInfo;
	private final List<ApplicationProgressInfo> progressInfo;

	private Application(Builder builder) {
		this.name = builder.getName();
		this.owner = builder.getOwner();
		this.status = builder.getStatus();
		this.insured = builder.getInsured();
		this.dependents = builder.getDependents();
		this.paymentInfo = builder.getPaymentInfo();
		this.creationDate = builder.getCreationDate();
		this.progressInfo = builder.getProgressInfo();
	}

	public static Builder create(ApplicationCreationRequest request) {
		return Builder.fromRequest(request);
	}

	@Getter
	public static class Builder {
		private String name;
		private String creationDate;

		private Insured owner;
		private Insured insured;
		private List<Insured> dependents;

		private ApplicationStatus status;
		private final ApplicationPaymentInfo paymentInfo;
		private List<ApplicationProgressInfo> progressInfo;

		private Builder() {
			paymentInfo = new ApplicationPaymentInfo();
		}

		private Builder(ApplicationCreationRequest request) {
			this();
			name = request.getName();
			creationDate = Instant.now().toString();
		}

		public static Builder fromRequest(ApplicationCreationRequest request) {
			return new Builder(request);
		}

		public Builder withOwner(Insured owner) {
			this.owner = owner;
			return this;
		}

		public Builder withDependents(List<Insured> insureds) {
			dependents = insureds;
			return this;
		}

		public Builder withInsured(Insured insured) {
			this.insured = insured;
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

			paymentInfo.setPaymentFrequency(paymentFrequency);
			progressInfo = new ArrayList<>();
			status = status == null ? ApplicationStatus.IN_PROGRESS : status;

			return new Application(this);
		}
	}
}
