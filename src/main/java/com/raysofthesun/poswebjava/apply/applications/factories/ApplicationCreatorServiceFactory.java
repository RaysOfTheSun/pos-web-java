package com.raysofthesun.poswebjava.apply.applications.factories;

import com.raysofthesun.poswebjava.apply.applications.services.core.ApplicationCreatorService;
import com.raysofthesun.poswebjava.core.factories.PosWebServiceFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationCreatorServiceFactory extends PosWebServiceFactory<ApplicationCreatorService> {
    public ApplicationCreatorServiceFactory(List<ApplicationCreatorService> services) {
        super(services);
    }
}
