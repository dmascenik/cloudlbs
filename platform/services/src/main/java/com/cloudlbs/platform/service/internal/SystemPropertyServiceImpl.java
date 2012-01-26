package com.cloudlbs.platform.service.internal;

import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudlbs.core.utils.StringUtils;
import com.cloudlbs.core.utils.dao.JpaGenericDao;
import com.cloudlbs.core.utils.protocol.MessageConverter;
import com.cloudlbs.core.utils.remote.SecureRestTemplate;
import com.cloudlbs.core.utils.search.Query;
import com.cloudlbs.core.utils.search.SearchResult;
import com.cloudlbs.platform.core.LocalOrRemoteService;
import com.cloudlbs.platform.domain.SystemProperty;
import com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage;
import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;

/**
 * Whether running against a local database connection or delegating to another
 * node, this service relies on a shared, coherent cache called
 * <code>systemPropertyCache</code>.
 * 
 * @author Dan Mascenik
 * 
 */
@Service("systemPropertyService")
public class SystemPropertyServiceImpl extends
		LocalOrRemoteService<SystemPropertyMessage, SystemProperty, Long>
		implements SystemPropertyService {

	public static final String SERVICE_NAME_KEY = "sysprop";

	/**
	 * @param systemPropertyDao
	 */
	@Autowired
	public SystemPropertyServiceImpl(
			JpaGenericDao<SystemProperty, Long> systemPropertyDao,
			SecureRestTemplate secureRestTemplate,
			MessageConverter<SystemPropertyMessage, SystemProperty> systemPropertyMessageConverter) {
		super(systemPropertyDao, secureRestTemplate,
				systemPropertyMessageConverter, SystemPropertyMessage.items);
	}

	/**
	 * Returns a collection of system properties. The results of this call are
	 * cached, and the cache is cleared when any SystemProperty is created or
	 * modified.
	 * 
	 */
	@Override
	@Transactional
	@Cacheable(cacheName = "systemPropertyCache")
	public Properties getAsProperties(String category) {
		log.debug("Cache miss - querying system properties for category "
				+ category);
		SearchResult<SystemProperty> results = findByCategory(category);
		Properties props = new Properties();
		List<SystemProperty> sProps = results.getValues();
		for (int i = 0; i < sProps.size(); i++) {
			SystemProperty sp = sProps.get(i);
			props.put(sp.getKey(), sp.getValue());
		}
		return props;
	}

	/**
	 * Returns a collection of system properties. The results of this call are
	 * cached, and the cache is cleared when any SystemProperty is created or
	 * modified.
	 * 
	 */
	@Transactional
	private SearchResult<SystemProperty> findByCategory(String category) {
		log.debug("Cache miss - retrieving system properties from the database for category "
				+ category);

		String q = "";
		if (!StringUtils.isBlank(category)) {
			q = "category: " + category + "*";
		}
		Query query = new Query(q, 0, Integer.MAX_VALUE);
		return search(query);
	}

	@Override
	@Transactional
	@TriggersRemove(cacheName = "systemPropertyCache", removeAll = true)
	public SystemProperty createEntity(SystemProperty entity) {
		return super.createEntity(entity);
	}

	@Override
	@Transactional
	@TriggersRemove(cacheName = "systemPropertyCache", removeAll = true)
	public SystemProperty updateEntity(String guid,
			SystemProperty representation, List<String> unmodifiedFields) {
		return super.updateEntity(guid, representation, unmodifiedFields);
	}

	@Override
	@Transactional
	@TriggersRemove(cacheName = "systemPropertyCache", removeAll = true)
	public void deleteEntity(String guid) {
		super.deleteEntity(guid);
	}

	@Override
	@Transactional
	@TriggersRemove(cacheName = "systemPropertyCache", removeAll = true)
	public void deleteEntity(SystemProperty entity) {
		super.deleteEntity(entity);
	}

	@Override
	public String getServiceNameSysPropKey() {
		return SERVICE_NAME_KEY;
	}

	private Logger log = LoggerFactory.getLogger(getClass());
}
