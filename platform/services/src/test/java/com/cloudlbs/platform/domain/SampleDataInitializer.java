package com.cloudlbs.platform.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class SampleDataInitializer implements
		ApplicationListener<ContextRefreshedEvent> {

	private static boolean isLoaded = false;

	@Autowired
	private SampleData data;

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public synchronized void onApplicationEvent(ContextRefreshedEvent event) {
		if (!isLoaded) {
			log.info("Context loaded - loading sample data...");
			data.load();
			log.info("Sample database loaded");
			isLoaded = true;
		}
	}

}
