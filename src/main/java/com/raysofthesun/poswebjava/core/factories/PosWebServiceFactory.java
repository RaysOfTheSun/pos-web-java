package com.raysofthesun.poswebjava.core.factories;

import com.raysofthesun.poswebjava.core.enums.Market;
import com.raysofthesun.poswebjava.core.services.PosWebService;

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
        return this.serviceMap.get(market);
    }
}
