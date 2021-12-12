package com.raysofthesun.poswebjava.apply.application.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.raysofthesun.poswebjava.apply.application.constants.ApplicationStatus;
import com.raysofthesun.poswebjava.apply.insured.models.insured.Insured;
import com.raysofthesun.poswebjava.apply.insured.models.person.PersonalInfo;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Document("applications")
@JsonDeserialize(builder = ApplicationMeta.Builder.class)
@NoArgsConstructor
public class ApplicationMeta {
	private String id;
	private String name;
	private String ownerId;
	private String insuredId;
	private String customerId;
	private String insuredName;
	private String creationDate;
	private String lastModifiedDate;
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
		this.insuredName = builder.getInsuredName();
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
		private String insuredName;
		private List<String> dependentIds = new ArrayList<>();
		private String creationDate;
		private String lastModifiedDate;
		private ApplicationStatus status;
		private ApplicationPaymentInfo paymentInfo;
		private Insured applicationInsured;

		private Builder(Application application) {

			this.applicationInsured = application.getInsured() == null
					? application.getOwner()
					: application.getInsured();

			this.id = application.getId();
			this.name = application.getName();
			this.status = application.getStatus();
			this.ownerId = application.getOwner().getId();
			this.insuredId = applicationInsured.getId();
			this.customerId = application.getOwner().getCustomerId();
			this.insuredName = this.getInsuredName(applicationInsured);
			this.paymentInfo = application.getPaymentInfo();
			this.creationDate = application.getCreationDate();
			this.dependentIds = getIdsFromInsureds(application.getDependents());
			this.lastModifiedDate = Instant.now().toString();
		}

		private String getInsuredName(Insured insured) {
			final PersonalInfo personalInfo = insured.getPersonalInfo();
			return String.format("%s %s", personalInfo.getFirstName(), personalInfo.getLastName());
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
