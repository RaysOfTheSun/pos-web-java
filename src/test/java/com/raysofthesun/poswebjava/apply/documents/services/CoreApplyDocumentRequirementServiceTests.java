package com.raysofthesun.poswebjava.apply.documents.services;

import com.raysofthesun.poswebjava.apply.applications.emums.ApplicationPaymentMethod;
import com.raysofthesun.poswebjava.apply.applications.models.core.application.ApplicationMeta;
import com.raysofthesun.poswebjava.apply.applications.models.core.application.ApplicationPaymentInfo;
import com.raysofthesun.poswebjava.apply.documents.services.marketCor.CorApplyDocumentRequirementService;
import com.raysofthesun.poswebjava.apply.insureds.enums.InsuredRole;
import com.raysofthesun.poswebjava.apply.insureds.models.core.insured.Insured;
import com.raysofthesun.poswebjava.config.MockPosConfig;
import com.raysofthesun.poswebjava.core.configuration.enums.PosDocumentType;
import com.raysofthesun.poswebjava.core.configuration.models.PosDocumentRequirement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CorApplyDocumentRequirementService")
public class CoreApplyDocumentRequirementServiceTests {

    @Spy
    MockPosConfig posConfig;

    @InjectMocks
    CorApplyDocumentRequirementService applyDocumentRequirementService;

    public ApplicationMeta makeApplicationMetaWithPaymentMethod(ApplicationPaymentMethod method) {
        ApplicationPaymentInfo paymentInfo = new ApplicationPaymentInfo();
        paymentInfo.setPaymentMethod(method);

        ApplicationMeta meta = new ApplicationMeta();
        meta.setPaymentInfo(paymentInfo);

        return meta;
    }

    public Insured makeInsuredWithRole(InsuredRole role) {
        Insured insured = new Insured();
        insured.setRole(role);

        return insured;
    }

    private PosDocumentRequirement getRequirementOfType(PosDocumentType type) {
        return posConfig
                .getDocumentRequirements()
                .get(type.toString());
    }

    @Test
    @DisplayName("it should include receipt when payment method is cash")
    public void shouldIncludeReceipt() {
        Insured applicationOwner = makeInsuredWithRole(InsuredRole.PO);
        Insured applicationDependent = makeInsuredWithRole(InsuredRole.OI);
        ApplicationMeta meta = makeApplicationMetaWithPaymentMethod(ApplicationPaymentMethod.CASH);
        PosDocumentRequirement paymentReq = getRequirementOfType(PosDocumentType.PAYMENT_RECEIPT);

        List<Insured> insureds = List.of(applicationOwner, applicationDependent);

        meta.setOwnerId(applicationOwner.getId());
        meta.setDependentIds(List.of(applicationDependent.getId()));

        Map<String, List<PosDocumentRequirement>> requirementMap = applyDocumentRequirementService
                .getRequiredDocsForApplication(meta, insureds);

        List<PosDocumentRequirement> ownerReqs = requirementMap.get(applicationOwner.getId());
        List<PosDocumentRequirement> dependentReqs = requirementMap.get(applicationDependent.getId());

        assertTrue(ownerReqs.contains(paymentReq));
        assertFalse(dependentReqs.contains(paymentReq));
    }

    @Test
    @DisplayName("it should set the NATIONAL_ID type as the default id requirement")
    public void haveNationalIdAsDefaultIdReq() {
        Insured applicationOwner = makeInsuredWithRole(InsuredRole.PO);
        ApplicationMeta meta = makeApplicationMetaWithPaymentMethod(ApplicationPaymentMethod.CREDIT);

        meta.setOwnerId(applicationOwner.getId());

        Map<String, List<PosDocumentRequirement>> requirementMap = applyDocumentRequirementService
                .getRequiredDocsForApplication(meta, List.of(applicationOwner));

        List<PosDocumentRequirement> ownerReqs = requirementMap.get(applicationOwner.getId());

        assertTrue(ownerReqs.contains(getRequirementOfType(PosDocumentType.NATIONAL_ID)));
    }
}
