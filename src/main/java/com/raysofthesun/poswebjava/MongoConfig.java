package com.raysofthesun.poswebjava;

import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;

public class MongoConfig extends AbstractReactiveMongoConfiguration {

    private final MappingMongoConverter mappingMongoConverter;

    public MongoConfig(MappingMongoConverter converter) {
        this.mappingMongoConverter = converter;
    }

    @Bean
    public ReactiveGridFsTemplate reactiveGridFsTemplate() {
        return new ReactiveGridFsTemplate(reactiveMongoDbFactory(), mappingMongoConverter);
    }

    @Override
    protected String getDatabaseName() {
        return "pos";
    }
}
