package com.raysofthesun.poswebjava.apply2.applications.factories;

import com.raysofthesun.poswebjava.apply2.applications.services.core.ApplicationServiceV2;
import com.raysofthesun.poswebjava.core.factories.PosWebServiceFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationServiceFactory extends PosWebServiceFactory<ApplicationServiceV2> {
    public ApplicationServiceFactory(List<ApplicationServiceV2> services) {
        super(services);
    }
}
