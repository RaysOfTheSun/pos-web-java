package com.raysofthesun.poswebjava.apply.documents2.services.marketCor;

import com.raysofthesun.poswebjava.apply.documents2.services.core.ApplyDocumentRequirementService;
import com.raysofthesun.poswebjava.core.common.enums.Market;
import com.raysofthesun.poswebjava.core.configuration.models.PosConfig;
import org.springframework.stereotype.Service;

@Service
public class CorApplyDocumentRequirementService extends ApplyDocumentRequirementService {
    public CorApplyDocumentRequirementService(PosConfig posConfig) {
        super(posConfig);
    }

    @Override
    public Market getMarket() {
        return Market.COR;
    }
}
