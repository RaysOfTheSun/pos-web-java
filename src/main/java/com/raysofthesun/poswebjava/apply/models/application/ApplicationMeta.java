package com.raysofthesun.poswebjava.apply.models.application;

import com.raysofthesun.poswebjava.apply.constants.ApplicationStatus;
import com.raysofthesun.poswebjava.apply.models.insured.Insured;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Document("applications")
public class ApplicationMeta {

	private ApplicationMeta() {
	}

	public static Builder create(Application application) {
		return Builder.fromApplication(application);
	}

	public static Builder create() {
		return Builder.createRaw();
	}

	private String id;

	private String name;

	private String ownerId;

	private String insuredId;

	private String customerId;

	private List<String> dependentIds = new ArrayList<>();

	private String creationDate;

	private ApplicationStatus status;

	private ApplicationPaymentInfo paymentInfo;

	public static class Builder {
		private final ApplicationMeta meta = new ApplicationMeta();

		private Builder() {
		}

		private Builder(Application application) {
			meta.setId(application.getId());
			meta.setName(application.getName());
			meta.setStatus(application.getStatus());
			meta.setCustomerId(application.getOwner().getId());
			meta.setPaymentInfo(application.getPaymentInfo());
			meta.setCreationDate(application.getCreationDate());
			meta.setDependentIds(getIdsFromInsureds(application.getDependents()));
		}

		private List<String> getIdsFromInsureds(List<Insured> insureds) {
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
			return meta;
		}
	}
}
