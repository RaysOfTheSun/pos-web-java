package com.raysofthesun.poswebjava.apply.models.application;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.raysofthesun.poswebjava.apply.constants.ApplicationStatus;
import com.raysofthesun.poswebjava.apply.models.insured.Insured;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Document("applications")
@JsonDeserialize(builder = ApplicationMeta.Builder.class)
@NoArgsConstructor
public class ApplicationMeta {
	private String id;
	private String name;
	private String ownerId;
	private String insuredId;
	private String customerId;
	private String creationDate;
	private List<String> dependentIds;
	private ApplicationStatus status;
	private ApplicationPaymentInfo paymentInfo;

	private ApplicationMeta(Builder builder) {
		this.id = builder.getId();
		this.name = builder.getName();
		this.status = builder.getStatus();
		this.ownerId = builder.getOwnerId();
		this.insuredId = builder.getInsuredId();
		this.customerId = builder.getCustomerId();
		this.paymentInfo = builder.getPaymentInfo();
		this.dependentIds = builder.getDependentIds();
		this.creationDate = builder.getCreationDate();
	}

	public static Builder create(Application application) {
		return Builder.create(application);
	}

	public static Builder create() {
		return Builder.create();
	}

	@Getter
	@JsonPOJOBuilder
	@NoArgsConstructor
	public static class Builder {
		private String id;
		private String name;
		private String ownerId;
		private String insuredId;
		private String customerId;
		private List<String> dependentIds = new ArrayList<>();
		private String creationDate;
		private ApplicationStatus status;
		private ApplicationPaymentInfo paymentInfo;

		private Builder(Application application) {
			this.id = application.getId();
			this.name = application.getName();
			this.status = application.getStatus();
			this.ownerId = application.getOwner().getId();
			this.insuredId = application.getInsured() == null
					? application.getOwner().getId()
					: application.getInsured().getId();
			this.customerId = application.getOwner().getCustomerId();
			this.paymentInfo = application.getPaymentInfo();
			this.creationDate = application.getCreationDate();
			this.dependentIds = getIdsFromInsureds(application.getDependents());
		}

		private List<String> getIdsFromInsureds(List<Insured> insureds) {

			if (insureds == null) {
				return new ArrayList<>();
			}

			return insureds
					.stream()
					.map(Insured::getId)
					.collect(Collectors.toList());
		}

		public static Builder create() {
			return new Builder();
		}

		public static Builder create(Application application) {
			return new Builder(application);
		}

		public ApplicationMeta build() {
			return new ApplicationMeta(this);
		}
	}
}
