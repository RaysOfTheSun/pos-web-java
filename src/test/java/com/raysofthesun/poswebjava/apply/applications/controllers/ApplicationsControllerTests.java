package com.raysofthesun.poswebjava.apply.applications.controllers;

import com.raysofthesun.poswebjava.LocalSecurityConfig;
import com.raysofthesun.poswebjava.apply.applications.factories.ApplicationServiceFactory;
import com.raysofthesun.poswebjava.apply.applications.models.core.application.Application;
import com.raysofthesun.poswebjava.apply.applications.models.core.application.ApplicationCreationRequest;
import com.raysofthesun.poswebjava.apply.applications.models.core.application.ApplicationMeta;
import com.raysofthesun.poswebjava.apply.applications.models.core.application.ApplicationPaymentInfo;
import com.raysofthesun.poswebjava.apply.applications.services.marketCor.CorApplicationService;
import com.raysofthesun.poswebjava.core.common.enums.Market;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@Import({LocalSecurityConfig.class})
@ExtendWith(MockitoExtension.class)
@DisplayName("ApplicationsController")
@WebFluxTest(ApplicationsController.class)
public class ApplicationsControllerTests {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    CorApplicationService applicationService;

    @MockBean
    ApplicationServiceFactory applicationServiceFactory;

    @BeforeEach
    public void setupApplicationServiceFactory() {
        when(applicationServiceFactory.getServiceForMarket(any(Market.class)))
                .thenReturn(this.applicationService);
    }

    @Nested
    @DisplayName("when retrieving application information")
    public class ApplicationInfoRetrievalTests {

        @Test
        @DisplayName("it should be able to get an application by its id")
        public void shouldGetApplicationById() {
            ApplicationMeta meta = new ApplicationMeta();
            meta.setPaymentInfo(new ApplicationPaymentInfo());
            Application testApplication = Application.create(meta).build();

            when(applicationService.getApplicationById(anyString(), any(Market.class)))
                    .thenReturn(Mono.just(testApplication));

            webTestClient
                    .get()
                    .uri("/v1/apply/COR/applications/001")
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(Application.class);
        }

        @Test
        @DisplayName("it should be able to retrieve applications with customer id")
        public void shouldGetApplicationMetasWithCustomerId() {
            when(applicationService.getApplicationMetasByCustomerId(anyString(), anyInt(), anyInt()))
                    .thenReturn(Flux.just(new ApplicationMeta()));

            webTestClient
                    .get()
                    .uri("/v1/apply/COR/customers/0/application-metas?index=0")
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBodyList(ApplicationMeta.class);
        }

        @Test
        @DisplayName("it should be able to get the total application count for a given customer's id")
        public void shouldGetTotalApplicationCountForCustomer() {
            when(applicationService.getTotalApplicationCountForCustomerById(anyString()))
                    .thenReturn(Mono.just(1));

            webTestClient
                    .get()
                    .uri("/v1/apply/COR/customers/0/application-count")
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(Integer.class);
        }
    }

    @Nested
    @DisplayName("when creating applications")
    public class ApplicationCreationTests {
        @Test
        @DisplayName("it should return a 201 on successful application creation")
        public void shouldCreateApplication() {
            ApplicationCreationRequest request = new ApplicationCreationRequest();
            Application application = Application.create(request).build();

            when(applicationService
                    .createApplicationFromRequestAndAgentId(any(ApplicationCreationRequest.class),anyString(), any(Market.class)))
                    .thenReturn(Mono.just(application));

            webTestClient
                    .post()
                    .uri("/v1/apply/COR/agents/001/applications/create")
                    .body(Mono.just(request), ApplicationCreationRequest.class)
                    .exchange()
                    .expectStatus()
                    .isCreated()
                    .expectBody(Application.class);
        }
    }
}
