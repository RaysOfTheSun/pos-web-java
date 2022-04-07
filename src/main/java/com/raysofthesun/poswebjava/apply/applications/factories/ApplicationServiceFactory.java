package com.raysofthesun.poswebjava.apply.applications.factories;

import com.raysofthesun.poswebjava.apply.applications.services.core.ApplicationServiceV2;
import com.raysofthesun.poswebjava.core.common.factories.PosWebServiceFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationServiceFactory extends PosWebServiceFactory<ApplicationServiceV2> {
    public ApplicationServiceFactory(List<ApplicationServiceV2> services) {
        super(services);
    }
}
