package com.raysofthesun.poswebjava.core.common.factories;

import com.raysofthesun.poswebjava.core.common.enums.Market;
import com.raysofthesun.poswebjava.core.common.exceptions.MarketNotSupportedException;
import com.raysofthesun.poswebjava.core.common.services.PosWebService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class PosWebServiceFactory<S extends PosWebService> {

    private final List<S> services;
    private Map<Market, S> serviceMap;

    public PosWebServiceFactory(List<S> services) {
        this.services = services;
    }

    @PostConstruct
    public void createServiceMap() {
        this.serviceMap = this
                .services
                .stream()
                .collect(Collectors.toMap(PosWebService::getMarket, (service) -> service));
    }

    public S getServiceForMarket(Market market) {
        S service = this.serviceMap.getOrDefault(market, null);

        if (service == null) {
            throw new MarketNotSupportedException();
        }

        return service;
    }
}
