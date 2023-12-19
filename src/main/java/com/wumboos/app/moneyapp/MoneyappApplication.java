package com.wumboos.app.moneyapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
public class MoneyappApplication {
	@Autowired Environment env;

	public static void main(String[] args) {
		SpringApplication.run(MoneyappApplication.class, args);
	}

}
