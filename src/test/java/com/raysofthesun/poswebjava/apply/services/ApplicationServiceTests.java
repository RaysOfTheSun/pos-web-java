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
import org.mockito.stubbing.Answer;
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
	private final String testAgentId = "001";
	private Insured mockOwner;

	private Insured mockInsured;

	private Insured mockDependent;

	private Customer mockOwnerCustomer;

	private Customer mockInsuredCustomer;

	private Customer mockDependentCustomer;

	@Mock
	private CustomerApi customerApi;

	@Mock
	private InsuredService insuredService;

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
		mockApplicationCreationRequest.setTotalPremium(BigDecimal.valueOf(20000).toString());
		mockApplicationCreationRequest.setPolicyOwnerId(mockOwnerCustomer.getId());
		mockApplicationCreationRequest.setDependentIds(List.of(mockDependentCustomer.getId()));


	}

	@Test
	@DisplayName("should be able to create a whole application based off an ApplicationCreationRequest")
	public void shouldBeAbleToCreateApplicationFromRequest() {
		mockApplicationCreationRequest.setPolicyOwnerId(mockOwnerCustomer.getId());
		mockApplicationCreationRequest.setDependentIds(List.of(mockDependentCustomer.getId()));
		mockApplicationCreationRequest.setPrimaryInsuredId(mockInsuredCustomer.getId());

		when(customerApi.getCustomersByIdAndAgentId(anyString(), anyList()))
				.thenReturn(Flux.just(mockOwnerCustomer, mockDependentCustomer, mockInsuredCustomer));

		when(applicationRepository.save(any(ApplicationMeta.class)))
				.thenReturn(Mono.just(ApplicationMeta.create().build()));

		when(insuredService.saveInsured(any(Insured.class)))
				.thenAnswer((Answer<Mono<Insured>>) mock -> Mono.just(mock.getArgument(0)));

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

	@Test
	@DisplayName("should be able to recreate an application based off its meta information")
	public void shouldBeAbleToCreateApplicationFromMeta() {
		final Application mockApplication = Application
				.create(mockApplicationCreationRequest)
				.withOwner(mockOwner)
				.withInsured(mockInsured)
				.withDependents(List.of(mockDependent))
				.build();

		final ApplicationMeta meta = ApplicationMeta
				.create(mockApplication)
				.build();

		when(insuredService.getInsuredsById(anyList()))
				.thenReturn(Flux.just(mockOwner, mockInsured, mockDependent));
		when(applicationRepository.findById(meta.getId())).thenReturn(Mono.just(meta));

		StepVerifier
				.create(applicationService.getApplicationWithId(meta.getId()))
				.consumeNextWith(application -> assertAll(
						() -> assertEquals(meta.getId(), application.getId()),
						() -> assertNotNull(application.getOwner()),
						() -> assertNotNull(application.getInsured()),
						() -> assertNotNull(application.getProgressInfo()),
						() -> assertEquals(meta.getPaymentInfo(), application.getPaymentInfo())
				))
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

			when(applicationRepository.save(any(ApplicationMeta.class)))
					.thenReturn(Mono.just(ApplicationMeta.create().build()));

			when(insuredService.saveInsured(any(Insured.class)))
					.thenAnswer((Answer<Mono<Insured>>) mock -> Mono.just(mock.getArgument(0)));

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

			when(applicationRepository.save(any(ApplicationMeta.class)))
					.thenReturn(Mono.just(ApplicationMeta.create().build()));

			when(insuredService.saveInsured(any(Insured.class)))
					.thenAnswer((Answer<Mono<Insured>>) mock -> Mono.just(mock.getArgument(0)));

			StepVerifier
					.create(applicationService
							.createApplicationWithRequestAndAgentId(mockApplicationCreationRequest, testAgentId))
					.consumeNextWith((application -> assertEquals(InsuredRole.OI,
							application.getDependents().get(0).getRole())))
					.verifyComplete();
		}
	}
}
