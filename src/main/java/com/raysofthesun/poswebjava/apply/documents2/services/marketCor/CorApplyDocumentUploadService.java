package com.raysofthesun.poswebjava.apply.documents2.services.marketCor;

import com.raysofthesun.poswebjava.apply.documents2.services.core.ApplyDocumentUploadService;
import com.raysofthesun.poswebjava.core.common.enums.Market;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;
import org.springframework.stereotype.Service;

@Service
public class CorApplyDocumentUploadService extends ApplyDocumentUploadService {
    public CorApplyDocumentUploadService(ReactiveGridFsTemplate reactiveGridFsTemplate) {
        super(reactiveGridFsTemplate);
    }


    @Override
    public Market getMarket() {
        return Market.COR;
    }
}
