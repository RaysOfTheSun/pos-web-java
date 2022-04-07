package com.raysofthesun.poswebjava.apply.documents2.factories;

import com.raysofthesun.poswebjava.apply.documents2.services.core.ApplyDocumentRequirementService;
import com.raysofthesun.poswebjava.core.common.factories.PosWebServiceFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplyDocumentRequirementServiceFactory extends PosWebServiceFactory<ApplyDocumentRequirementService> {
    public ApplyDocumentRequirementServiceFactory(List<ApplyDocumentRequirementService> services) {
        super(services);
    }
}
