package com.raysofthesun.poswebjava.apply.models.application;

import com.raysofthesun.poswebjava.apply.constants.ApplicationStatus;
import com.raysofthesun.poswebjava.propose.models.proposal.Proposal;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Document("applications")
public class ApplicationMeta {

	private ApplicationMeta() {
	}

	public static ApplicationMetaBuilder create(Proposal proposal) {
		return ApplicationMetaBuilder.createFromProposal(proposal);
	}

	private String id = UUID.randomUUID().toString();
	private String name;
	private String creationDate = Instant.now().toString();
	private ApplicationStatus status = ApplicationStatus.IN_PROGRESS;

	private String ownerId;
	private String insuredId;
	private List<String> dependentIds = new ArrayList<>();

	private ApplicationPaymentInfo paymentInfo = new ApplicationPaymentInfo();

	public static class ApplicationMetaBuilder {

		private final ApplicationMeta applicationMeta;

		private ApplicationMetaBuilder() {
			applicationMeta = new ApplicationMeta();
		}

		private ApplicationMetaBuilder(Proposal proposal) {
			this();
			applicationMeta.setName(proposal.getName());
		}

		public static ApplicationMetaBuilder createFromProposal(Proposal proposal) {
			return new ApplicationMetaBuilder(proposal);
		}

		public ApplicationMeta build() {
			return applicationMeta;
		}
	}
}
