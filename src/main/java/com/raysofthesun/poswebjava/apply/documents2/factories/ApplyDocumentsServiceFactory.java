package com.raysofthesun.poswebjava.apply.documents2.factories;

import com.raysofthesun.poswebjava.apply.documents2.services.core.ApplyDocumentsService;
import com.raysofthesun.poswebjava.core.common.factories.PosWebServiceFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplyDocumentsServiceFactory extends PosWebServiceFactory<ApplyDocumentsService> {
    public ApplyDocumentsServiceFactory(List<ApplyDocumentsService> services) {
        super(services);
    }
}
