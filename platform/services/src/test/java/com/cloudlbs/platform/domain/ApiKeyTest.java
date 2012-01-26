package com.cloudlbs.platform.domain;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.cloudlbs.core.utils.dao.JpaGenericDao;
import com.cloudlbs.platform.BaseSpringContextTestCase;

public class ApiKeyTest extends BaseSpringContextTestCase {

	@Autowired
	JpaGenericDao<ApiKey, Long> apiKeyDao;

	@Test
	@Transactional
	public void testGetApiKeyAndApp() {
		Criterion apiKey = Restrictions.eq("keyString",
				"pbJS6bLTQD3oEieKQWl7ltrdvU");
		List<ApiKey> keys = apiKeyDao.findByCriteria(apiKey);
		assertEquals(1, keys.size());
		assertNotNull(keys.get(0).getApp());
		App app = keys.get(0).getApp();
		assertEquals("WhereRU?", app.getName());
	}

}
