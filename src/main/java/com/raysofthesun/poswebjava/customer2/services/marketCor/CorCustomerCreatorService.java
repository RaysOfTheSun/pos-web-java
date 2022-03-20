package com.raysofthesun.poswebjava.customer2.services.marketCor;

import com.raysofthesun.poswebjava.core.enums.Market;
import com.raysofthesun.poswebjava.customer2.services.core.CustomerCreatorService;
import org.springframework.stereotype.Service;

@Service
public class CorCustomerCreatorService extends CustomerCreatorService {
    @Override
    public Market getMarket() {
        return Market.COR;
    }
}
