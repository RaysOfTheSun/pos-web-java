package com.raysofthesun.poswebjava.apply.applications.services.marketCor;

import com.raysofthesun.poswebjava.apply.applications.services.core.ApplicationDocumentService;
import com.raysofthesun.poswebjava.core.common.enums.Market;
import com.raysofthesun.poswebjava.core.configuration.models.PosConfig;
import org.springframework.stereotype.Service;

@Service
public class CorApplicationDocumentService extends ApplicationDocumentService {
    public CorApplicationDocumentService(PosConfig posConfig) {
        super(posConfig);
    }

    @Override
    public Market getMarket() {
        return Market.COR;
    }
}
