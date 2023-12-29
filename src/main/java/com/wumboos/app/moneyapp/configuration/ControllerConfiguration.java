package com.wumboos.app.moneyapp.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilter;

@Configuration
@Component
public class ControllerConfiguration {
	Logger log = LoggerFactory.getLogger(ControllerConfiguration.class);

	@Bean
	WebFilter logFilter(String[] args) {
		WebFilter filter = (exchange, chain) -> {
			return chain.filter(exchange).doOnEach(signal -> {
				if (signal.isOnComplete() || signal.isOnError()) {
					log.info("log :: requestId: {}, ip: {}, method: {},path :{}, headers: {}, response :{}",
							exchange.getRequest().getId(), exchange.getRequest().getRemoteAddress(),
							exchange.getRequest().getMethod(), exchange.getRequest().getPath(),
							exchange.getRequest().getHeaders().entrySet().stream()
									.filter(stringListEntry -> !stringListEntry.getKey().equals("Authorization"))
									.toList(),
							exchange.getResponse().getStatusCode());
					if(signal.getThrowable() != null)
						log.error("Stack Trace: ",signal.getThrowable());
				}
			});
		};
		return filter;
	}

}
