package com.cloudlbs.platform;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.cloudlbs.core.utils.dao.GenericDao;
import com.cloudlbs.platform.domain.UserAccount;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext-test.xml" })
@TransactionConfiguration(defaultRollback = true)
public abstract class BaseSpringContextTestCase extends Assert {

	@Autowired
	private GenericDao<UserAccount, Integer> userAccountDao;

	@Before
	public void initializeDatabase() {
		List<UserAccount> users = userAccountDao.findAll();
		assertTrue("Sample data failed to load", users.size() > 0);
	}

}
