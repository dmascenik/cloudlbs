package com.cloudlbs.core.utils.system;

import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudlbs.core.utils.remote.RestProtobufRemoteService;
import com.cloudlbs.platform.protocol.SearchProto.SearchResultMessage;
import com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage;
import com.googlecode.ehcache.annotations.Cacheable;

/**
 * Provides remote-side caching of system properties for a reasonable amount of
 * time (1m for example). Requires configuration of ehcache and a cache called
 * "systemPropertyCache"
 * 
 * @author Dan Mascenik
 * 
 */
public class CachingSystemPropertyRemoteService extends
		RestProtobufRemoteService<SystemPropertyMessage, SystemPropertyMessage>
		implements SystemPropertyRemoteService {

	public CachingSystemPropertyRemoteService() {
		super(null, SystemPropertyMessage.items);
	}

	@Override
	@Cacheable(cacheName = "systemPropertyCache")
	public Properties findByCategory(String category) {
		log.debug("Cache miss - calling remote service for "
				+ "system property category " + category);

		if (category == null) {
			category = "";
		}

		SearchResultMessage resultsMessage = secureRestTemplate.getForObject(
				getServiceURL() + "/query?cat=" + category,
				SearchResultMessage.class);

		Properties props = new Properties();
		List<SystemPropertyMessage> results = (List<SystemPropertyMessage>) resultsMessage
				.getExtension(SystemPropertyMessage.items);
		for (SystemPropertyMessage sp : results) {
			props.put(sp.getKey(), sp.getValue());
		}
		return props;
	}

	@Override
	@Cacheable(cacheName = "systemPropertyCache")
	public String getSystemProperty(String key) {
		log.debug("Cache miss - calling remote service for "
				+ "system property key " + key);
		SystemPropertyMessage sp = secureRestTemplate.getForObject(
				getServiceURL() + "/" + key, SystemPropertyMessage.class);
		return sp.getValue();
	}

	private Logger log = LoggerFactory.getLogger(getClass());

}
