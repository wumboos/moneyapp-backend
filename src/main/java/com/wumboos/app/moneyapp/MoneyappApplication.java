package com.wumboos.app.moneyapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.modulith.events.core.EventPublicationRepository;
import org.springframework.modulith.events.core.EventSerializer;
import org.springframework.web.reactive.config.EnableWebFlux;

import com.wumboos.app.moneyapp.eventpublication.EventPublicationRepositoryImpl;
import com.wumboos.app.moneyapp.eventpublication.ReactiveEventPublicationRepository;


@EnableWebFlux
@EnableR2dbcRepositories
@SpringBootApplication
public class MoneyappApplication {
	@Autowired Environment env;

	public static void main(String[] args) {
		SpringApplication.run(MoneyappApplication.class, args);
	}
	
	@Bean
	EventPublicationRepository jpaEventPublicationRepository(ReactiveEventPublicationRepository reactiveEventPublicationRepository) {
		return new EventPublicationRepositoryImpl(reactiveEventPublicationRepository);
	}
}
