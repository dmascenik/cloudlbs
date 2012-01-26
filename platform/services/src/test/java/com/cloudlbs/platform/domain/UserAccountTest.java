package com.cloudlbs.platform.domain;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.cloudlbs.core.utils.dao.JpaGenericDao;
import com.cloudlbs.core.utils.protocol.MessageConverter;
import com.cloudlbs.core.utils.protocol.ProtobufMessageConverter;
import com.cloudlbs.platform.BaseSpringContextTestCase;
import com.cloudlbs.platform.protocol.UserAccountProto.UserAccountMessage;

public class UserAccountTest extends BaseSpringContextTestCase {

	MessageConverter<UserAccountMessage, UserAccount> messageConverter = new ProtobufMessageConverter<UserAccountMessage, UserAccount>(
			UserAccountMessage.class, UserAccount.class) {
	};

	@Autowired
	private JpaGenericDao<UserAccount, Long> userAccountDao;

	@Test
	@Transactional
	public void testToMessage() {
		Criterion c = Restrictions.eq("username", "admin");
		List<UserAccount> results = userAccountDao.findByCriteria(c);
		UserAccount admin = results.get(0);
		assertTrue(admin.getGrantedAuthoritys().size() > 0);

		messageConverter.toMessage(admin);
	}

}
