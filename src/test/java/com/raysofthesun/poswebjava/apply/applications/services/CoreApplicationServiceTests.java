package com.raysofthesun.poswebjava.apply.applications.services;

import com.raysofthesun.poswebjava.apply.applications.factories.ApplicationCreatorServiceFactory;
import com.raysofthesun.poswebjava.apply.applications.models.core.application.Application;
import com.raysofthesun.poswebjava.apply.applications.models.core.application.ApplicationCreationRequest;
import com.raysofthesun.poswebjava.apply.applications.models.core.application.ApplicationMeta;
import com.raysofthesun.poswebjava.apply.applications.models.core.application.ApplicationPaymentInfo;
import com.raysofthesun.poswebjava.apply.applications.repositories.core.ApplicationRepository;
import com.raysofthesun.poswebjava.apply.applications.services.marketCor.CorApplicationCreatorService;
import com.raysofthesun.poswebjava.apply.applications.services.marketCor.CorApplicationService;
import com.raysofthesun.poswebjava.apply.insureds.factories.ApplicationInsuredServiceFactory;
import com.raysofthesun.poswebjava.apply.insureds.models.core.insured.Insured;
import com.raysofthesun.poswebjava.apply.insureds.services.marketCor.CorApplicationInsuredService;
import com.raysofthesun.poswebjava.core.common.enums.Market;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CoreApplicationService")
@ExtendWith(MockitoExtension.class)
public class CoreApplicationServiceTests {

    @Mock
    ApplicationRepository applicationRepository;

    @Mock
    CorApplicationCreatorService applicationCreatorService;

    @Mock
    CorApplicationInsuredService applicationInsuredService;

    @Mock
    ApplicationCreatorServiceFactory applicationCreatorServiceFactory;

    @Mock
    ApplicationInsuredServiceFactory applicationInsuredServiceFactory;

    @InjectMocks
    CorApplicationService applicationService;

    private void setupCreatorServiceFactory() {
        when(applicationCreatorServiceFactory.getServiceForMarket(any(Market.class)))
                .thenReturn(this.applicationCreatorService);
    }

    private void setupInsuredServiceFactory() {
        when(applicationInsuredServiceFactory.getServiceForMarket(any(Market.class)))
                .thenReturn(this.applicationInsuredService);
    }

    @Nested
    @DisplayName("when retrieving application information")
    public class ApplicationDataRetrievalTests {

        @Test
        @DisplayName("it should be able to get the total application count based on an application's agent id")
        public void shouldReturnTotalCount() {

            when(applicationRepository.countApplicationMetasByCustomerId(anyString()))
                    .thenReturn(Mono.just(2));

            StepVerifier
                    .create(applicationService.getTotalApplicationCountForCustomerById("001"))
                    .expectNext(2)
                    .verifyComplete();
        }

        @Test
        @DisplayName("it should be able to fetch all application meta with the given customer id")
        public void shouldFetchAllMetasWithCustomerId() {
            when(applicationRepository.findAllByCustomerId(anyString(), any(Pageable.class)))
                    .thenReturn(Flux.just(new ApplicationMeta()));

            StepVerifier
                    .create(applicationService.getApplicationMetasByCustomerId("1", 0, 20))
                    .expectNextCount(1)
                    .verifyComplete();
        }

        @Test
        @DisplayName("it should be able to retrieve the application's meta and return the assembled application")
        public void getAssembledApplicationById() {
            ApplicationMeta meta = new ApplicationMeta();
            meta.setName("test application");
            meta.setPaymentInfo(new ApplicationPaymentInfo());

            Application expectedApplication = Application.Builder.fromMeta(meta).build();

            setupCreatorServiceFactory();
            when(applicationRepository.findById(anyString())).thenReturn(Mono.just(meta));
            when(applicationCreatorService.createApplicationFromMeta(any(ApplicationMeta.class), any(Market.class)))
                    .thenReturn(Mono.just(expectedApplication));

            StepVerifier
                    .create(applicationService.getApplicationById("id", Market.COR))
                    .expectNext(expectedApplication)
                    .verifyComplete();
        }

        @Test
        @DisplayName("it should be able to retrieve the application's meta and each insured in it based on the provided id")
        public void getApplicationAndInsureds() {
            ApplicationMeta applicationMeta = new ApplicationMeta();
            applicationMeta.setPaymentInfo(new ApplicationPaymentInfo());

            setupInsuredServiceFactory();
            when(applicationRepository.findById(anyString())).thenReturn(Mono.just(applicationMeta));
            when(applicationInsuredService.getInsuredsWithApplicationMeta(any(ApplicationMeta.class)))
                    .thenReturn(Flux.just(new Insured()));

            StepVerifier
                    .create(applicationService.getApplicationMetaAndInsuredsById("00", Market.COR))
                    .consumeNextWith(objects -> assertAll(() -> assertTrue(objects.getT2().size() > 0)))
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("when creating applications")
    public class ApplicationCreationTests {
        @Test
        @DisplayName("it should be able to create an application by request")
        public void shouldCreateApplicationFromRequest() {
            ApplicationCreationRequest request = new ApplicationCreationRequest();
            request.setName("Sample Name");
            request.setTotalPremium(BigDecimal.valueOf(1000).toPlainString());

            Application expectedApplication = Application
                    .create(request)
                    .withOwner(new Insured())
                    .build();

            setupCreatorServiceFactory();
            when(applicationRepository.save(any(ApplicationMeta.class)))
                    .thenAnswer((invocationOnMock -> Mono.just(invocationOnMock.getArgument(0))));
            when(applicationCreatorService
                    .createApplicationFromRequest(any(ApplicationCreationRequest.class), anyString(), any(Market.class)))
                    .thenReturn(Mono.just(expectedApplication));

            StepVerifier
                    .create(applicationService.createApplicationFromRequestAndAgentId(request, "001", Market.COR))
                    .consumeNextWith(application -> {
                        verify(applicationCreatorService, atLeastOnce())
                                .createApplicationFromRequest(request, "001", Market.COR);
                    })
                    .verifyComplete();
        }
    }
}
