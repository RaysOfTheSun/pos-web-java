package com.raysofthesun.poswebjava;

import com.mongodb.ConnectionString;
import com.mongodb.reactivestreams.client.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.ReactiveMongoClientFactoryBean;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;

@Configuration
public class AppConfig {

	@Bean
	public ReactiveMongoClientFactoryBean mongoClientFactoryBean() {
		ReactiveMongoClientFactoryBean reactiveMongoClientFactoryBean = new ReactiveMongoClientFactoryBean();
		reactiveMongoClientFactoryBean.setHost("localhost");

		return reactiveMongoClientFactoryBean;
	}

	@Bean
	public SimpleReactiveMongoDatabaseFactory mongoOperations(MongoClient mongoClient) {
		return new SimpleReactiveMongoDatabaseFactory(mongoClient, "pos");
	}
}
