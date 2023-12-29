package com.wumboos.app.moneyapp;

import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;


@EnableWebFlux
@EnableJpaRepositories
@SpringBootApplication
public class MoneyappApplication {
	@Autowired Environment env;

	public static void main(String[] args) {
		SpringApplication.run(MoneyappApplication.class, args);
	}
	
    @Bean
    Scheduler jdbcScheduler(Environment env) {
        return Schedulers.fromExecutor(Executors.newFixedThreadPool(env.getRequiredProperty("jdbc.connection.pool.size", Integer.class)));
    }

}
