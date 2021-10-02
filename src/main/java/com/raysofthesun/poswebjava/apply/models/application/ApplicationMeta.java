package com.raysofthesun.poswebjava.apply.models.application;

import com.raysofthesun.poswebjava.apply.constants.ApplicationStatus;
import com.raysofthesun.poswebjava.apply.models.insured.Insured;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Document("applications")
public class ApplicationMeta {
	private final String id;
	private final String name;
	private final String ownerId;
	private final String insuredId;
	private final String customerId;
	private final List<String> dependentIds;
	private final String creationDate;
	private final ApplicationStatus status;
	private final ApplicationPaymentInfo paymentInfo;

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
		return Builder.fromApplication(application);
	}

	public static Builder create() {
		return Builder.createRaw();
	}

	@Getter
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

		public static Builder createRaw() {
			return new Builder();
		}

		public static Builder fromApplication(Application application) {
			return new Builder(application);
		}

		public ApplicationMeta build() {
			return new ApplicationMeta(this);
		}
	}
}
