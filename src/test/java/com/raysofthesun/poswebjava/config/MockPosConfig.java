package com.raysofthesun.poswebjava.config;

import com.raysofthesun.poswebjava.core.configuration.enums.PosDocumentType;
import com.raysofthesun.poswebjava.core.configuration.models.PosConfig;
import com.raysofthesun.poswebjava.core.configuration.models.PosDocumentRequirement;

import java.util.HashMap;
import java.util.Map;

public class MockPosConfig extends PosConfig {
    public MockPosConfig() {

        Map<String, PosDocumentRequirement> requirementMap = new HashMap<>();
        requirementMap.put(PosDocumentType.NATIONAL_ID.toString(), makeDocumentRequirement(PosDocumentType.NATIONAL_ID));
        requirementMap.put(PosDocumentType.PAYMENT_RECEIPT.toString(), makeDocumentRequirement(PosDocumentType.PAYMENT_RECEIPT));

        this.setDocumentRequirements(requirementMap);
    }

    private PosDocumentRequirement makeDocumentRequirement(PosDocumentType type) {
        PosDocumentRequirement requirement = new PosDocumentRequirement();
        requirement.setType(type.toString());

        return requirement;
    }
}
