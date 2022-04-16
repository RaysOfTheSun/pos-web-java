package com.raysofthesun.poswebjava.apply.insureds.services;

import com.raysofthesun.poswebjava.apply.applications.models.core.application.ApplicationCreationRequest;
import com.raysofthesun.poswebjava.apply.applications.models.core.application.ApplicationMeta;
import com.raysofthesun.poswebjava.apply.feign.CustomerApi;
import com.raysofthesun.poswebjava.apply.feign.models.ApiCustomer;
import com.raysofthesun.poswebjava.apply.insureds.enums.InsuredRole;
import com.raysofthesun.poswebjava.apply.insureds.mappers.ApplicationInsuredMapper;
import com.raysofthesun.poswebjava.apply.insureds.models.core.insured.Insured;
import com.raysofthesun.poswebjava.apply.insureds.repositories.InsuredRepository;
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

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CoreApplicationInsuredService")
public class CoreApplicationInsuredServiceTests {

    @Mock
    CustomerApi customerApi;

    @Mock
    InsuredRepository insuredRepository;

    @InjectMocks
    CorApplicationInsuredService applicationInsuredService;

    @Nested
    @DisplayName("when converting customers to insureds")
    public class CustomerToInsuredConversionTests {

        @Test
        @DisplayName("it should correctly convert to all three basic roles")
        public void shouldCorrectlyConvertToAllRoles() {
            ApiCustomer ownerCustomer = new ApiCustomer();
            ApiCustomer insuredCustomer = new ApiCustomer();
            ApiCustomer dependentCustomer = new ApiCustomer();

            ownerCustomer.setId("0");
            insuredCustomer.setId("1");
            dependentCustomer.setId("2");

            Insured ownerInsured = ApplicationInsuredMapper.MAPPER.mapApiCustomerToInsured(ownerCustomer);
            Insured insuredInsured = ApplicationInsuredMapper.MAPPER.mapApiCustomerToInsured(insuredCustomer);
            Insured dependentInsured = ApplicationInsuredMapper.MAPPER.mapApiCustomerToInsured(dependentCustomer);

            ownerInsured.setRole(InsuredRole.PO);
            insuredInsured.setRole(InsuredRole.PI);
            dependentInsured.setRole(InsuredRole.OI);

            ApplicationCreationRequest request = new ApplicationCreationRequest();

            request.setDependentIds(List.of(dependentCustomer.getId()));
            request.setPolicyOwnerId(ownerCustomer.getId());
            request.setPrimaryInsuredId(insuredCustomer.getId());

            when(customerApi.getCustomersByIdAndAgentId(anyString(), anyList(), any(Market.class)))
                    .thenReturn(Flux.just(ownerCustomer, insuredCustomer, dependentCustomer));

            StepVerifier
                    .create(applicationInsuredService.getCustomersAsInsuredsFromRequest(request, "001", Market.COR))
                    .expectNext(ownerInsured, insuredInsured, dependentInsured)
                    .verifyComplete();
        }

        @Test
        @DisplayName("it should correctly convert a customer that is both owner and the insured")
        public void shouldCorrectlyConvertToInsuredAndOwner() {
            ApiCustomer ownerCustomer = new ApiCustomer();
            ownerCustomer.setId("1");

            Insured ownerInsured = ApplicationInsuredMapper.MAPPER.mapApiCustomerToInsured(ownerCustomer);
            ownerInsured.setRole(InsuredRole.IO);

            ApplicationCreationRequest request = new ApplicationCreationRequest();
            request.setPolicyOwnerId(ownerCustomer.getId());
            request.setPrimaryInsuredId(ownerCustomer.getId());

            when(customerApi.getCustomersByIdAndAgentId(anyString(), anyList(), any(Market.class)))
                    .thenReturn(Flux.just(ownerCustomer));

            StepVerifier
                    .create(applicationInsuredService.getCustomersAsInsuredsFromRequest(request, "001", Market.COR))
                    .expectNext(ownerInsured)
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("when retrieving insureds")
    public class InsuredRetrievalTests {
        @Test
        public void shouldRetrieveFromApplicationMeta() {
            Insured expectedInsured = new Insured();

            ApplicationMeta meta = new ApplicationMeta();
            meta.setOwnerId(expectedInsured.getId());
            meta.setInsuredId(expectedInsured.getId());
            meta.setDependentIds(List.of());

            when(insuredRepository.findAllById(anyList())).thenReturn(Flux.just(expectedInsured));

            StepVerifier.create(applicationInsuredService.getInsuredsWithApplicationMeta(meta))
                    .expectNext(expectedInsured)
                    .verifyComplete();
        }
    }
}
