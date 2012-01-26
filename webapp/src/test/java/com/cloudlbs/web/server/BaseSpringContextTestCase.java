package com.cloudlbs.web.server;

import junit.framework.Assert;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml" })
@TransactionConfiguration(defaultRollback = true)
public abstract class BaseSpringContextTestCase extends Assert {

}
