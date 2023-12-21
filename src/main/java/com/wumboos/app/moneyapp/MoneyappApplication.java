package com.wumboos.app.moneyapp;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
public class MoneyappApplication {
	@Autowired Environment env;

	public static void main(String[] args) {
		SpringApplication.run(MoneyappApplication.class, args);
	}

//    @Bean
//    ConnectionFactoryInitializer initializer(@Qualifier("connectionFactory") ConnectionFactory connectionFactory) {
//        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
//        initializer.setConnectionFactory(connectionFactory);
//        ResourceDatabasePopulator resource = new ResourceDatabasePopulator(new ClassPathResource("init.sql"));
//        initializer.setDatabasePopulator(resource);
//        return initializer;
//    }

}
