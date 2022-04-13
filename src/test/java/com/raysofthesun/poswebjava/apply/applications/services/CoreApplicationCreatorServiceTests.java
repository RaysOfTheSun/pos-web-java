package com.raysofthesun.poswebjava.apply.applications.services;

import com.raysofthesun.poswebjava.apply.applications.models.core.application.ApplicationCreationRequest;
import com.raysofthesun.poswebjava.apply.applications.models.core.application.ApplicationMeta;
import com.raysofthesun.poswebjava.apply.applications.models.core.application.ApplicationPaymentInfo;
import com.raysofthesun.poswebjava.apply.applications.services.marketCor.CorApplicationCreatorService;
import com.raysofthesun.poswebjava.apply.insureds.enums.InsuredRole;
import com.raysofthesun.poswebjava.apply.insureds.factories.ApplicationInsuredServiceFactory;
import com.raysofthesun.poswebjava.apply.insureds.models.core.insured.Insured;
import com.raysofthesun.poswebjava.apply.insureds.services.marketCor.CorApplicationInsuredService;
import com.raysofthesun.poswebjava.core.common.enums.Market;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CoreApplicationCreatorService")
@ExtendWith(MockitoExtension.class)
public class CoreApplicationCreatorServiceTests {

    @Mock
    ApplicationInsuredServiceFactory insuredServiceFactory;

    @Mock
    CorApplicationInsuredService applicationInsuredService;

    @InjectMocks
    CorApplicationCreatorService applicationCreatorService;

    private Insured createInsuredWithRole(InsuredRole insuredRole) {
        Insured insured = new Insured();
        insured.setRole(insuredRole);
        insured.setCustomerId(UUID.randomUUID().toString());

        return insured;
    }

    private ApplicationMeta createApplicationMeta() {
        ApplicationMeta applicationMeta = new ApplicationMeta();
        applicationMeta.setInsuredId("");
        applicationMeta.setDependentIds(new ArrayList<>());
        applicationMeta.setPaymentInfo(new ApplicationPaymentInfo());

        return applicationMeta;
    }

    private void setupInsuredServiceFactory() {
        when(insuredServiceFactory.getServiceForMarket(any(Market.class)))
                .thenReturn(this.applicationInsuredService);
    }

    @Nested
    @DisplayName("when creating an application from its metadata")
    public class FromApplicationMetaCreationTests {
        @Test
        @DisplayName("it should be able to create from an application's metadata and properly set owner when PO is insured")
        public void shouldCreateFromMeta() {
            setupInsuredServiceFactory();

            Insured expectedInsured = createInsuredWithRole(InsuredRole.IO);
            Insured expectedDependent = createInsuredWithRole(InsuredRole.OI);
            ApplicationMeta applicationMeta = createApplicationMeta();

            applicationMeta.setOwnerId(expectedInsured.getId());
            applicationMeta.setInsuredId(expectedInsured.getId());
            applicationMeta.getDependentIds().add(expectedDependent.getId());

            when(applicationInsuredService.getInsuredsWithApplicationMeta(any(ApplicationMeta.class)))
                    .thenReturn(Flux.just(expectedInsured, expectedDependent));

            StepVerifier
                    .create(applicationCreatorService.createApplicationFromMeta(applicationMeta, Market.COR))
                    .consumeNextWith((application -> assertAll(
                            () -> assertTrue(application.getDependents().contains(expectedDependent)),
                            () -> assertEquals(expectedInsured.getId(), application.getOwner().getId())
                    )))
                    .verifyComplete();
        }

        @Test
        @DisplayName("it should be able to create from an application's metadata and properly set owner when PO is not insured")
        public void shouldCreateFromMetaOwnerNotInsured() {
            setupInsuredServiceFactory();

            Insured expectedOwner = createInsuredWithRole(InsuredRole.PO);
            Insured expectedInsured = createInsuredWithRole(InsuredRole.PI);
            Insured expectedDependent = createInsuredWithRole(InsuredRole.OI);
            ApplicationMeta applicationMeta = createApplicationMeta();

            applicationMeta.setOwnerId(expectedOwner.getId());
            applicationMeta.setInsuredId(expectedInsured.getId());
            applicationMeta.getDependentIds().add(expectedDependent.getId());

            when(applicationInsuredService.getInsuredsWithApplicationMeta(any(ApplicationMeta.class)))
                    .thenReturn(Flux.just(expectedInsured, expectedDependent, expectedOwner));

            StepVerifier
                    .create(applicationCreatorService.createApplicationFromMeta(applicationMeta, Market.COR))
                    .consumeNextWith((application -> assertAll(
                            () -> assertEquals(expectedOwner.getId(), application.getOwner().getId()),
                            () -> assertEquals(expectedInsured.getId(), application.getInsured().getId())
                    )))
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("when creating an application from a creation request")
    public class FromApplicationCreationRequestApplicationCreationTests {

        @Test
        @DisplayName("it should be able to create an application with the owner as insured")
        public void shouldCreateApplicationFromRequestWhenOwnerIsInsured() {
            Insured expectedInsured = createInsuredWithRole(InsuredRole.IO);
            Insured expectedDependent = createInsuredWithRole(InsuredRole.OI);

            ApplicationCreationRequest request = new ApplicationCreationRequest();
            request.setDependentIds(List.of(expectedDependent.getCustomerId()));
            request.setPolicyOwnerId(expectedInsured.getCustomerId());
            request.setPrimaryInsuredId(expectedInsured.getCustomerId());

            setupInsuredServiceFactory();
            when(applicationInsuredService.getCustomersAsInsuredsFromRequest(any(ApplicationCreationRequest.class), anyString(), any(Market.class)))
                    .thenReturn(Flux.just(expectedInsured, expectedDependent));

            StepVerifier
                    .create(applicationCreatorService.createApplicationFromRequest(request, "001", Market.COR))
                    .consumeNextWith((application) -> {
                        assertAll(
                                () -> assertEquals(expectedInsured.getId(), application.getOwner().getId()),
                                () -> assertTrue(application.getDependents().contains(expectedDependent))
                        );
                    }).verifyComplete();


        }

        @Test
        @DisplayName("it should be able to create an application with owner and insured")
        public void shouldCreateApplicationFromRequestWhenOwnerIsNotInsured() {
            Insured expectedOwner = createInsuredWithRole(InsuredRole.PO);
            Insured expectedInsured = createInsuredWithRole(InsuredRole.PI);
            Insured expectedDependent = createInsuredWithRole(InsuredRole.OI);

            ApplicationCreationRequest request = new ApplicationCreationRequest();
            request.setDependentIds(List.of(expectedDependent.getCustomerId()));
            request.setPolicyOwnerId(expectedOwner.getCustomerId());
            request.setPrimaryInsuredId(expectedInsured.getCustomerId());

            setupInsuredServiceFactory();
            when(applicationInsuredService.getCustomersAsInsuredsFromRequest(any(ApplicationCreationRequest.class), anyString(), any(Market.class)))
                    .thenReturn(Flux.just(expectedOwner, expectedInsured, expectedDependent));

            StepVerifier
                    .create(applicationCreatorService.createApplicationFromRequest(request, "001", Market.COR))
                    .consumeNextWith((application) -> {
                        assertAll(
                                () -> assertTrue(application.getDependents().contains(expectedDependent)),
                                () -> assertEquals(expectedOwner.getId(), application.getOwner().getId()),
                                () -> assertEquals(expectedInsured.getId(), application.getInsured().getId())
                        );
                    }).verifyComplete();


        }
    }
}
