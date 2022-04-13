package com.raysofthesun.poswebjava.apply.documents.factories;

import com.raysofthesun.poswebjava.apply.documents.services.core.ApplyDocumentUploadService;
import com.raysofthesun.poswebjava.core.common.factories.PosWebServiceFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplyDocumentUploadServiceFactory extends PosWebServiceFactory<ApplyDocumentUploadService> {
    public ApplyDocumentUploadServiceFactory(List<ApplyDocumentUploadService> services) {
        super(services);
    }
}
