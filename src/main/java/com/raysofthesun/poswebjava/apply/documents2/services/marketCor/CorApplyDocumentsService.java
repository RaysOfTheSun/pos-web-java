package com.raysofthesun.poswebjava.apply.documents2.services.marketCor;

import com.raysofthesun.poswebjava.apply.applications.factories.ApplicationServiceFactory;
import com.raysofthesun.poswebjava.apply.documents2.factories.ApplyDocumentRequirementServiceFactory;
import com.raysofthesun.poswebjava.apply.documents2.factories.ApplyDocumentUploadServiceFactory;
import com.raysofthesun.poswebjava.apply.documents2.services.core.ApplyDocumentsService;
import com.raysofthesun.poswebjava.core.common.enums.Market;
import org.springframework.stereotype.Service;

@Service
public class CorApplyDocumentsService extends ApplyDocumentsService {
    public CorApplyDocumentsService(ApplicationServiceFactory applicationServiceFactory,
                                    ApplyDocumentUploadServiceFactory documentUploadServiceFactory,
                                    ApplyDocumentRequirementServiceFactory documentRequirementServiceFactory) {
        super(applicationServiceFactory, documentUploadServiceFactory, documentRequirementServiceFactory);
    }

    @Override
    public Market getMarket() {
        return Market.COR;
    }
}
