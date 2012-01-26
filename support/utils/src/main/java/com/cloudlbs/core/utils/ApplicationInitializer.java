package com.cloudlbs.core.utils;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.transaction.annotation.Transactional;

/**
 * Calls {@link Initializable#initialize()} on all the provided initializers
 * when the first {@link ContextRefreshedEvent} is received by the
 * {@link ApplicationListener}.
 * 
 * @author Dan Mascenik
 * 
 */
public class ApplicationInitializer implements
		ApplicationListener<ContextRefreshedEvent> {

	private static boolean initialized = false;

	private Logger log = LoggerFactory.getLogger(getClass());

	private List<Initializable> initializers;

	public ApplicationInitializer(List<Initializable> initializers) {
		if (initializers == null) {
			this.initializers = new ArrayList<Initializable>();
		} else {
			this.initializers = initializers;
		}
	}

	@Override
	@Transactional
	public synchronized void onApplicationEvent(ContextRefreshedEvent event) {
		if (!initialized) {
			log.debug("Context loaded - performing initializations");
			for (Initializable i : initializers) {
				try {
					i.initialize();
				} catch (Exception e) {
					throw new RuntimeException("Failed to initialize "
							+ i.getClass().getName(), e);
				}
			}
			initialized = true;
			log.debug("Application initialization complete");
		}
	}

}
