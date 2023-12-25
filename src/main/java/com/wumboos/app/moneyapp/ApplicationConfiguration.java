package com.wumboos.app.moneyapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.task.SimpleAsyncTaskSchedulerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;

public class ApplicationConfiguration {
	@Autowired AbstractApplicationContext appContext;

	CustomApplicationEventMulticaster applicationEventMulticaster() {
		CustomApplicationEventMulticaster applicationEventMulticaster = new CustomApplicationEventMulticaster(
				appContext.getBeanFactory());
		SimpleAsyncTaskSchedulerBuilder asyncTaskSchedulerBuilder = new SimpleAsyncTaskSchedulerBuilder();
		applicationEventMulticaster.setTaskExecutor(asyncTaskSchedulerBuilder.threadNamePrefix("wumboos").build());
		return applicationEventMulticaster;
	}
	

}
