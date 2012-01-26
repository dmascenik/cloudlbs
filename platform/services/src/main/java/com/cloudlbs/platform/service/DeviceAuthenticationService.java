package com.cloudlbs.platform.service;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudlbs.core.utils.search.Query;
import com.cloudlbs.core.utils.search.SearchResult;
import com.cloudlbs.core.utils.service.GenericService;
import com.cloudlbs.platform.domain.UserAccount;
import com.cloudlbs.sls.protocol.AuthenticationProto.AuthenticationMessage;
import com.cloudlbs.sls.protocol.AuthenticationProto.AuthenticationMessage.Builder;
import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.KeyGenerator;
import com.googlecode.ehcache.annotations.Property;
import com.googlecode.ehcache.annotations.TriggersRemove;
import com.googlecode.ehcache.annotations.key.HashCodeCacheKeyGenerator;

/**
 * Whether running against a local database connection or delegating to another
 * node, this service relies on a shared, coherent cache called
 * <code>userSessionCache</code>.
 * 
 * @author Dan Mascenik
 * 
 */
@Service
public class DeviceAuthenticationService implements
		GenericService<AuthenticationMessage> {

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private GenericService<UserAccount> userAccountService;

	/**
	 * Authenticates a user, returning a token that may be used for session
	 * authentication on all further HTTP calls.
	 * 
	 */
	@Override
	@Transactional
	public AuthenticationMessage createEntity(AuthenticationMessage authIn) {
		String username = authIn.getUsername();
		String passwordRaw = "";
		if (authIn.hasPassword()) {
			passwordRaw = authIn.getPassword();
		}
		String deviceUniqueId = authIn.getDeviceUniqueId();
		String appGuid = authIn.getAppGuid();

		Builder b = AuthenticationMessage.newBuilder(authIn);
		b.clearPassword();
		b.setSuccess(false);

		SearchResult<UserAccount> results = userAccountService
				.search(new Query("username: " + username, 0, 1));
		do {
			if (results.getTotalResults() == 0) {
				b.setBadCredentials(true);
				break;
			}

			UserAccount user = results.getValues().get(0);
			String password = passwordEncoder.encodePassword(passwordRaw,
					user.getGuid());
			if (!user.getPassword().equals(password)) {
				b.setBadCredentials(true);
				break;
			}

			// TODO verify the app for the device

			b.setToken(UUID.randomUUID().toString());
			b.setSuccess(true);
			break;
		} while (true);

		AuthenticationMessage auth = b.build();
		if (auth.getSuccess()) {
			// TODO associate the app with the user

			putInCache(auth.getToken(), auth);
		}
		return auth;
	}

	@Override
	@TriggersRemove(cacheName = "userSessionCache", keyGenerator = @KeyGenerator(name = "HashCodeCacheKeyGenerator", properties = { @Property(name = "includeMethod", value = "false") }))
	public void deleteEntity(String token) throws EntityNotFoundException {
		// Does nothing but remove the authentication object from cache.
	}

	@Override
	@Cacheable(cacheName = "userSessionCache", keyGenerator = @KeyGenerator(name = "HashCodeCacheKeyGenerator", properties = { @Property(name = "includeMethod", value = "false") }))
	public AuthenticationMessage retrieveEntity(String token)
			throws EntityNotFoundException {
		/*
		 * If the call got in here, there was nothing in the cache, so an
		 * invalid authentication message will be returned.
		 */
		Builder b = AuthenticationMessage.newBuilder();
		b.setAppGuid("");
		b.setUsername("");
		b.setDeviceUniqueId("");
		b.setSuccess(false);
		b.setInvalidToken(true);
		return b.build();
	}

	/**
	 * Manually inserts an element into the cache.
	 * 
	 * @param token
	 * @param auth
	 */
	private void putInCache(String token, AuthenticationMessage auth) {
		Cache authTokenCache = cacheManager.getCache("userSessionCache");
		HashCodeCacheKeyGenerator keyGen = new HashCodeCacheKeyGenerator(false,
				true);
		Long key = keyGen.generateKey(new Object[] { auth.getToken() });
		log.debug("Generated cache key " + key + " for Authentication for "
				+ auth.getUsername());
		Element element = new Element(key, auth);
		authTokenCache.put(element);
	}

	@Override
	public Class<AuthenticationMessage> entityClass() {
		return AuthenticationMessage.class;
	}

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public long count(Query arg0) {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public SearchResult<AuthenticationMessage> search(Query arg0) {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public AuthenticationMessage updateEntity(String arg0,
			AuthenticationMessage arg1, List<String> arg2)
			throws EntityNotFoundException {
		throw new UnsupportedOperationException("Not supported");
	}
}
