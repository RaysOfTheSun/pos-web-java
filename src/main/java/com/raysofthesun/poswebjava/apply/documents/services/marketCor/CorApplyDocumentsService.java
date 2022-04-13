package com.raysofthesun.poswebjava.apply.documents.services.marketCor;

import com.raysofthesun.poswebjava.apply.applications.factories.ApplicationServiceFactory;
import com.raysofthesun.poswebjava.apply.documents.factories.ApplyDocumentRequirementServiceFactory;
import com.raysofthesun.poswebjava.apply.documents.factories.ApplyDocumentUploadServiceFactory;
import com.raysofthesun.poswebjava.apply.documents.services.core.ApplyDocumentsService;
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
