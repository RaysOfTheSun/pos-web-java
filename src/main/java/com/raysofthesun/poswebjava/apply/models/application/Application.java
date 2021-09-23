package com.raysofthesun.poswebjava.apply.models.application;

import com.raysofthesun.poswebjava.apply.constants.ApplicationStatus;
import com.raysofthesun.poswebjava.apply.models.insured.Insured;
import lombok.Data;

import java.util.List;

@Data
public class Application {

	private Application() {}

	public static ApplicationBuilder create(ApplicationMeta applicationMeta) {
		return ApplicationBuilder.fromMeta(applicationMeta);
	}

	private String id;
	private String name;
	private String creationDate;

	private Insured owner;
	private Insured insured;
	private List<Insured> dependents;

	private ApplicationStatus status;
	private ApplicationPaymentInfo paymentInfo;
	private ApplicationProgressInfo progressInfo;

	private static class ApplicationBuilder {
		private final Application application;

		private ApplicationBuilder() {
			application = new Application();
		}

		private ApplicationBuilder(ApplicationMeta applicationMeta) {
			this();
			application.setName(applicationMeta.getName());
			application.setPaymentInfo(applicationMeta.getPaymentInfo());
			application.setCreationDate(applicationMeta.getCreationDate());
		}

		public static ApplicationBuilder fromMeta(ApplicationMeta meta) {
			return new ApplicationBuilder(meta);
		}

		public ApplicationBuilder withName(String name) {
			application.setName(name);
			return this;
		}

		public ApplicationBuilder withOwner(Insured owner) {
			application.setOwner(owner);
			return this;
		}

		public ApplicationBuilder withDependents(List<Insured> insureds) {
			application.setDependents(insureds);
			return this;
		}

		public ApplicationBuilder withInsured(Insured insured) {
			application.setInsured(insured);
			return this;
		}
	}
}
