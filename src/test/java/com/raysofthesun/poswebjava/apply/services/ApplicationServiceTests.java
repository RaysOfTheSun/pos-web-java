package com.raysofthesun.poswebjava.apply.services;

import com.raysofthesun.poswebjava.apply.constants.ApplicationStatus;
import com.raysofthesun.poswebjava.apply.constants.InsuredRole;
import com.raysofthesun.poswebjava.apply.feign_clients.agent.CustomerApi;
import com.raysofthesun.poswebjava.apply.feign_clients.agent.models.Customer;
import com.raysofthesun.poswebjava.apply.models.application.Application;
import com.raysofthesun.poswebjava.apply.models.application.ApplicationCreationRequest;
import com.raysofthesun.poswebjava.apply.models.application.ApplicationMeta;
import com.raysofthesun.poswebjava.apply.models.insured.Insured;
import com.raysofthesun.poswebjava.apply.repositories.ApplicationRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ApplicationService")
@ExtendWith({MockitoExtension.class})
public class ApplicationServiceTests {
	private String testAgentId = "001";

	private Insured mockOwner;

	private Insured mockInsured;

	private Insured mockDependent;

	private Customer mockOwnerCustomer;

	private Customer mockInsuredCustomer;

	private Customer mockDependentCustomer;

	@Mock
	private CustomerApi customerApi;

	@Mock
	private ApplicationRepository applicationRepository;

	@InjectMocks
	private ApplicationService applicationService;

	private ApplicationCreationRequest mockApplicationCreationRequest;

	@BeforeEach
	public void initializeTestPrerequisites() {
		mockOwner = new Insured();
		mockInsured = new Insured();
		mockDependent = new Insured();

		mockOwnerCustomer = new Customer();
		mockInsuredCustomer = new Customer();
		mockDependentCustomer = new Customer();

		mockOwnerCustomer.setId("PO_CUSTOMER");
		mockInsuredCustomer.setId("PI_CUSTOMER");
		mockDependentCustomer.setId("OI_CUSTOMER");

		mockApplicationCreationRequest = new ApplicationCreationRequest();
		mockApplicationCreationRequest.setName("SAMPLE APPLICATION");
		mockApplicationCreationRequest.setProductType("MOCK_PRODUCT");
		mockApplicationCreationRequest.setTotalPremium(BigDecimal.TEN.toPlainString());
		mockApplicationCreationRequest.setPolicyOwnerId(mockOwnerCustomer.getId());
		mockApplicationCreationRequest.setDependentIds(List.of(mockDependentCustomer.getId()));

		when(applicationRepository.save(any(ApplicationMeta.class)))
				.thenReturn(Mono.just(ApplicationMeta.create().build()));
	}

	@Test
	@DisplayName("should be able to create a whole application based off an ApplicationCreationRequest")
	public void shouldBeAbleToCreateApplicationFromRequest() {
		mockApplicationCreationRequest.setPolicyOwnerId(mockOwnerCustomer.getId());
		mockApplicationCreationRequest.setDependentIds(List.of(mockDependentCustomer.getId()));
		mockApplicationCreationRequest.setPrimaryInsuredId(mockInsuredCustomer.getId());

		when(customerApi.getCustomersByIdAndAgentId(anyString(), anyList()))
				.thenReturn(Flux.just(mockOwnerCustomer, mockDependentCustomer, mockInsuredCustomer));

		StepVerifier
				.create(applicationService
						.createApplicationWithRequestAndAgentId(mockApplicationCreationRequest, testAgentId))
				.consumeNextWith((application -> assertAll(
						() -> assertEquals(mockApplicationCreationRequest.getTotalPremium(),
								application.getPaymentInfo().getTotalPremium()),
						() -> assertEquals(1, application.getDependents().size()),
						() -> assertNotNull(application.getOwner()),
						() -> assertNotNull(application.getInsured()),
						() -> assertEquals(ApplicationStatus.IN_PROGRESS, application.getStatus())
				)))
				.verifyComplete();
	}

	@Nested
	@DisplayName("when mapping insureds")
	class InsuredMappingTests {

		@BeforeEach
		public void setupCustomerApiMocks() {
			when(customerApi.getCustomersByIdAndAgentId(anyString(), anyList()))
					.thenReturn(Flux.just(mockOwnerCustomer, mockDependentCustomer, mockInsuredCustomer));
		}

		@Test
		@DisplayName("should create an application with no insured if the owner is also the insured")
		public void shouldBeAbleToCreateApplicationWithPolicyOwnerAsInsured() {
			mockApplicationCreationRequest.setPolicyOwnerId(mockOwnerCustomer.getId());
			mockApplicationCreationRequest.setPrimaryInsuredId(mockOwnerCustomer.getId());

			StepVerifier
					.create(applicationService
							.createApplicationWithRequestAndAgentId(mockApplicationCreationRequest, testAgentId))
					.consumeNextWith(application -> assertAll(
							() -> assertNull(application.getInsured()),
							() -> assertEquals(InsuredRole.IO, application.getOwner().getRole())
					))
					.verifyComplete();
		}

		@Test
		@DisplayName("should be able to create an application with dependents")
		public void shouldCreateApplicationWithDependents() {
			mockApplicationCreationRequest.setPolicyOwnerId(mockOwnerCustomer.getId());
			mockApplicationCreationRequest.setPrimaryInsuredId(mockOwnerCustomer.getId());
			mockApplicationCreationRequest.setDependentIds(List.of(mockDependentCustomer.getId()));

			StepVerifier
					.create(applicationService
							.createApplicationWithRequestAndAgentId(mockApplicationCreationRequest, testAgentId))
					.consumeNextWith((application -> assertEquals(InsuredRole.OI,
							application.getDependents().get(0).getRole())))
					.verifyComplete();
		}
	}
}
