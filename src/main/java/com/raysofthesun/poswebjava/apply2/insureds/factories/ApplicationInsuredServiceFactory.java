package com.raysofthesun.poswebjava.apply2.insureds.factories;

import com.raysofthesun.poswebjava.apply2.insureds.services.core.ApplicationInsuredService;
import com.raysofthesun.poswebjava.core.factories.PosWebServiceFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationInsuredServiceFactory extends PosWebServiceFactory<ApplicationInsuredService> {
    public ApplicationInsuredServiceFactory(List<ApplicationInsuredService> services) {
        super(services);
    }
}
