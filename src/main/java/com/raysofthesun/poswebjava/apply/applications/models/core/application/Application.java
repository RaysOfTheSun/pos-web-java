package com.raysofthesun.poswebjava.apply.applications.models.core.application;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.raysofthesun.poswebjava.apply.applications.emums.ApplicationPaymentFrequency;
import com.raysofthesun.poswebjava.apply.insureds.enums.ApplicationStatus;
import com.raysofthesun.poswebjava.apply.insureds.models.core.insured.Insured;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@JsonDeserialize(builder = Application.Builder.class)
public class Application {
    private final String id;
    private final String name;
    private final String creationDate;
    private final Insured owner;
    private final Insured insured;
    private final List<Insured> dependents;
    private final ApplicationStatus status;
    private final ApplicationPaymentInfo paymentInfo;
    private final List<ApplicationProgressInfo> progressInfo;

    private Application(Builder builder) {
        this.id = builder.getId();
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

    public static Builder create(ApplicationMeta applicationMeta) {
        return Builder.fromMeta(applicationMeta);
    }

    @Getter
    @JsonPOJOBuilder
    public static class Builder {
        private String id;
        private String name;
        private String creationDate;

        private Insured owner;
        private Insured insured;
        private List<Insured> dependents;

        private ApplicationStatus status;
        private ApplicationPaymentInfo paymentInfo;
        private List<ApplicationProgressInfo> progressInfo;

        private Builder() {
            id = UUID.randomUUID().toString();
            paymentInfo = new ApplicationPaymentInfo();
        }

        private Builder(ApplicationCreationRequest request) {
            this();
            name = request.getName();
            paymentInfo.setTotalPremium(request.getTotalPremium());
            creationDate = Instant.now().toString();
        }

        private Builder(ApplicationMeta meta) {
            this();
            id = meta.getId();
            name = meta.getName();
            status = meta.getStatus();
            paymentInfo = meta.getPaymentInfo();
            creationDate = meta.getCreationDate();
        }

        public static Builder fromMeta(ApplicationMeta meta) {
            return new Builder(meta);
        }

        public static Builder fromRequest(ApplicationCreationRequest request) {
            return new Builder(request);
        }

        public Builder withOwner(Insured owner) {
            this.owner = owner;
            return this;
        }

        public Builder withPaymentInfo(ApplicationPaymentInfo paymentInfo) {
            this.paymentInfo = paymentInfo;
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
            final ApplicationPaymentFrequency paymentFrequency = paymentInfo.getPaymentFrequency() == null
                    ? ApplicationPaymentFrequency.ANNUAL
                    : paymentInfo.getPaymentFrequency();

            dependents = Optional.ofNullable(dependents).isPresent() ? dependents : new ArrayList<>();
            paymentInfo.setPaymentFrequency(paymentFrequency);
            progressInfo = new ArrayList<>();
            status = status == null ? ApplicationStatus.IN_PROGRESS : status;

            return new Application(this);
        }
    }
}
