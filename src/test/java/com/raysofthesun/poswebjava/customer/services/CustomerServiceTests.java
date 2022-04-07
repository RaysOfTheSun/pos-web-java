//package com.raysofthesun.poswebjava.customer.services;
//
//
//import com.raysofthesun.poswebjava.customer2.exceptions.CustomerNotFoundException;
//import com.raysofthesun.poswebjava.customer2.enums.PersonGender;
//import com.raysofthesun.poswebjava.customer2.enums.Salutation;
//import com.raysofthesun.poswebjava.customer2.feign.application.ApplyApplicationsApi;
//import com.raysofthesun.poswebjava.customer2.feign.application.models.ApiApplicationMeta;
//import com.raysofthesun.poswebjava.customer2.models.Customer;
//import com.raysofthesun.poswebjava.customer2.models.RawCustomer;
//import com.raysofthesun.poswebjava.customer2.repositories.CustomerRepository;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.PageRequest;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import reactor.test.StepVerifier;
//
//import java.time.Instant;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@DisplayName("CustomerService")
//@ExtendWith({MockitoExtension.class})
//public class CustomerServiceTests {
//
//    private RawCustomer rawCustomer;
//
//    @Mock
//    private CustomerRepository repository;
//
//    @Mock
//    private ApplyApplicationsApi applyApplicationsApi;
//
//    @InjectMocks
//    private CustomerService customerService;
//
//    @BeforeEach
//    public void setupMocks() {
//        this.rawCustomer = new RawCustomer();
//        rawCustomer.setDateOfBirth(Instant.parse("1989-01-19T16:00:00.000Z").toString());
//        rawCustomer.setEmailAddress("mail@mail.com");
//        rawCustomer.setGender(PersonGender.MALE);
//        rawCustomer.setLastName("Last");
//        rawCustomer.setFirstName("First");
//        rawCustomer.setTitle(Salutation.MR);
//    }
//
//    @Nested
//    @DisplayName("when creating or updating customers")
//    public class CustomerUpdateAndCreateTests {
//        @Test
//        @DisplayName("should return the id of the created customer")
//        public void shouldReturnIdOfCreatedCustomer() {
//            when(repository.save(any(Customer.class)))
//                    .thenAnswer(invocationOnMock -> Mono.just(invocationOnMock.getArgument(0)));
//
//            StepVerifier
//                    .create(customerService.addCustomerWithAgentId(rawCustomer, "001"))
//                    .consumeNextWith(Assertions::assertNotNull)
//                    .verifyComplete();
//        }
//
//        @Test
//        @DisplayName("should throw an exception when attempting to update a non-existent customer")
//        public void shouldThrowIfUpdatingNonExistentCustomer() {
//            when(repository.existsCustomersByAgentIdAndId(anyString(), anyString()))
//                    .thenReturn(Mono.just(false));
//
//            StepVerifier
//                    .create(customerService.updateCustomer("0", "0", new RawCustomer()))
//                    .expectError(CustomerNotFoundException.class)
//                    .verify();
//        }
//    }
//
//    @Nested
//    @DisplayName("when deleting customers")
//    public class CustomerDeletionTests {
//        @Test
//        @DisplayName("should set the deleted property to TRUE given that the customer exists")
//        public void shouldSetDeletedTrue() {
//            Customer customer = Customer.fromRawCustomer(rawCustomer, "001").build();
//            when(repository.findByIdAndAgentId(anyString(), anyString()))
//                    .thenReturn(Mono.just(customer));
//            when(repository.save(any(Customer.class)))
//                    .thenAnswer(invocationOnMock -> Mono.just(invocationOnMock.getArgument(0)));
//
//            StepVerifier
//                    .create(customerService.deleteCustomerByIdAndAgentId("001", "0001232"))
//                    .consumeNextWith((isDeleted) -> assertAll(
//                            () -> assertTrue(isDeleted),
//                            () -> assertTrue(customer.isDeleted())
//                    )).verifyComplete();
//
//
//        }
//    }
//
//    @Nested
//    @DisplayName("when restoring customers")
//    public class CustomerRestorationTests {
//        @Test
//        @DisplayName("should set the deleted property to FALSE assuming the customer exists")
//        public void shouldSetDeletedFalse() {
//            Customer customer = Customer.fromRawCustomer(rawCustomer, "001").build();
//            when(repository.findByIdAndAgentId(anyString(), anyString()))
//                    .thenReturn(Mono.just(customer));
//            when(repository.save(any(Customer.class)))
//                    .thenAnswer(invocationOnMock -> Mono.just(invocationOnMock.getArgument(0)));
//
//            StepVerifier
//                    .create(customerService.restoreCustomerByIdAndAgentId("001", "000"))
//                    .consumeNextWith((isRestored) -> assertAll(
//                            () -> assertFalse(customer.isDeleted()),
//                            () -> assertTrue(isRestored)
//                    )).verifyComplete();
//
//        }
//    }
//
//    @Nested
//    @DisplayName("when retrieving customers")
//    public class CustomerRetrievalTests {
//
//        @Test
//        @DisplayName("should be able to properly return all the customers in a given page/division")
//        public void shouldReturnCustomersInPage() {
//            Customer customer = Customer.fromRawCustomer(rawCustomer, "001").build();
//            when(repository.findAllByAgentIdAndDeleted(anyString(), anyBoolean(), any(PageRequest.class)))
//                    .thenReturn(Flux.just(customer, customer));
//
//            StepVerifier
//                    .create(customerService.getAllCustomerSummariesByAgentIdAndPage("001", 0,
//                            20, false))
//                    .expectNextCount(2)
//                    .verifyComplete();
//        }
//
//        @Test
//        @DisplayName("should properly return the total customer count based on agentId and deleted status")
//        public void shouldReturnTotalCustomerCountByStatus() {
//            final int expectedCount = 100;
//            when(repository.countCustomerByAgentIdAndDeleted(anyString(), anyBoolean()))
//                    .thenReturn(Mono.just(expectedCount));
//
//            StepVerifier
//                    .create(customerService.getAllCustomerCountByAgentIdAndDeletedStatus("001", true))
//                    .consumeNextWith((count) -> assertEquals(expectedCount, count))
//                    .verifyComplete();
//        }
//
//        @Test
//        @DisplayName("should return a customer given that it exists")
//        public void shouldReturnCustomerIfExists() {
//            Customer customer = Customer.fromRawCustomer(rawCustomer, "001").build();
//            when(repository.findByIdAndAgentId(anyString(), anyString()))
//                    .thenReturn(Mono.just(customer));
//
//            StepVerifier
//                    .create(customerService.getCustomerByIdAndAgentId("002", "001"))
//                    .consumeNextWith(Assertions::assertNotNull)
//                    .verifyComplete();
//        }
//    }
//
//    @Nested
//    @DisplayName("when retrieving application information for a customer")
//    public class CustomerApplicationRetrievalTests {
//
//        @Test
//        @DisplayName("should properly return all the applications for the given customer id")
//        public void shouldReturnAllCustomerApplications() {
//            when(applyApplicationsApi.getApplicationsByCustomerId(anyString(), anyInt()))
//                    .thenReturn(Flux.just(new ApiApplicationMeta()));
//
//            StepVerifier
//                    .create(customerService.getApplicationsForCustomer("000", 0))
//                    .expectNextCount(1)
//                    .verifyComplete();
//        }
//
//        @Test
//        @DisplayName("should properly return the total number of applications for the given customer id")
//        public void shouldReturnTotalApplicationCountForCustomer() {
//            when(applyApplicationsApi.getTotalApplicationCountForCustomerById(anyString()))
//                    .thenReturn(Mono.just(100));
//
//            StepVerifier.create(customerService.getCustomerApplicationCount("00"))
//                    .consumeNextWith((applicationCount) -> assertEquals(100, applicationCount))
//                    .verifyComplete();
//        }
//    }
//}
