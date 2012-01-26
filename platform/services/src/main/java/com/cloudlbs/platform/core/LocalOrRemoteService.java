package com.cloudlbs.platform.core;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.cloudlbs.core.utils.ReflectionUtils;
import com.cloudlbs.core.utils.StringUtils;
import com.cloudlbs.core.utils.dao.JpaGenericDao;
import com.cloudlbs.core.utils.domain.DatabaseEntity;
import com.cloudlbs.core.utils.protocol.MessageConverter;
import com.cloudlbs.core.utils.protocol.QueryMessageConverter;
import com.cloudlbs.core.utils.remote.RestProtobufRemoteService;
import com.cloudlbs.core.utils.remote.SecureRestTemplate;
import com.cloudlbs.core.utils.search.Query;
import com.cloudlbs.core.utils.search.SearchResult;
import com.cloudlbs.core.utils.service.JpaDaoGenericService;
import com.cloudlbs.platform.domain.SystemProperty;
import com.cloudlbs.platform.protocol.SearchProto.QueryMessage;
import com.cloudlbs.platform.protocol.SearchProto.SearchResultMessage;
import com.cloudlbs.platform.service.internal.SystemPropertyService;
import com.google.protobuf.GeneratedMessage.GeneratedExtension;
import com.google.protobuf.Message;

/**
 * Based on the <code>role</code> of this application instance (retrieved from
 * <code>System.getProperty("role")</code>), either execute the service call
 * locally, or delegate it to the remote service. Every service must have a
 * unique string identifier, such as "locations." System properties for this
 * particular service will be looked up as follows:<br/>
 * <br/>
 * <code>service.locations.<i>{role}</i>.isLocal</code> - value is true or
 * false</br> <code>service.locations.baseUrl</code> - if not local, the base
 * service URL</br> <code>service.locations.resourceStub</code> - if not local,
 * the URL stub of the given resource</br> <br/>
 * Note that the full qualified service URL should be the internal one (not from
 * outside the proxy). When this service delegates to an remote service, it will
 * handle authentication via a pre-authenticated header (just the username in
 * the HTTP header). From beyond the proxy, this will always fail.
 * 
 * @author Dan Mascenik
 * 
 * @param <T>
 * @param <ID>
 */
public abstract class LocalOrRemoteService<M extends Message, T extends DatabaseEntity, ID extends Serializable>
		extends JpaDaoGenericService<T, ID> implements InitializingBean,
		RemoteableService<M, T>, InternalGenericService<M, T> {

	public static final String ROLE_SYSTEM_PROPERTY = "role";

	private String role = null;
	private RestProtobufRemoteService<M, T> remoteService;
	private SystemPropertyService systemPropertyService;
	private boolean useRemote = false;
	private MessageConverter<M, T> messageConverter;
	private GeneratedExtension<SearchResultMessage, List<M>> extension;

	public LocalOrRemoteService(JpaGenericDao<T, ID> entityDao,
			SecureRestTemplate secureRestTemplate,
			MessageConverter<M, T> messageConverter,
			SystemPropertyService systemPropertyService,
			GeneratedExtension<SearchResultMessage, List<M>> extension) {
		this(entityDao, secureRestTemplate, messageConverter, extension);
		Assert.notNull(systemPropertyService);
		this.systemPropertyService = systemPropertyService;
	}

	public LocalOrRemoteService(JpaGenericDao<T, ID> entityDao,
			SecureRestTemplate secureRestTemplate,
			MessageConverter<M, T> messageConverter,
			GeneratedExtension<SearchResultMessage, List<M>> extension) {
		super(entityDao);
		Assert.notNull(extension);
		Assert.notNull(secureRestTemplate);
		this.extension = extension;

		role = System.getProperty(ROLE_SYSTEM_PROPERTY);

		Type[] typeArgs = ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments();
		@SuppressWarnings("unchecked")
		Class<M> messageClass = (Class<M>) typeArgs[0];
		@SuppressWarnings("unchecked")
		Class<T> typeClass = (Class<T>) typeArgs[1];

		this.messageConverter = messageConverter;
		this.remoteService = createRemoteService(messageConverter, extension,
				messageClass, typeClass, secureRestTemplate);
	}

	private RestProtobufRemoteService<M, T> createRemoteService(
			MessageConverter<M, T> messageConverter,
			GeneratedExtension<SearchResultMessage, List<M>> extension,
			Class<M> messageClass, Class<T> typeClass,
			SecureRestTemplate secureRestTemplate) {
		return new RestProtobufRemoteService<M, T>(messageConverter, extension,
				messageClass, typeClass, secureRestTemplate) {
		};
	}

	@Transactional
	protected T createEntityLocal(T entity) {
		return super.createEntity(entity);
	}

	@Transactional
	protected T retrieveEntityLocal(String guid) {
		return super.retrieveEntity(guid);
	}

	@Transactional
	protected T updateEntityLocal(String guid, T representation,
			List<String> unmodifiedFields) {
		return super.updateEntity(guid, representation, unmodifiedFields);
	}

	@Transactional
	protected void deleteEntityLocal(String guid) {
		super.deleteEntity(guid);
	}

	@Transactional
	protected void deleteEntityLocal(T entity) {
		super.deleteEntity(entity);
	}

	@Transactional
	protected SearchResult<T> searchLocal(Query query) {
		return super.search(query);
	}

	@Transactional
	protected long countLocal(Query query) {
		return super.count(query);
	}

	@Override
	public void setUseRemote(boolean useRemote) {
		this.useRemote = useRemote;
	}

	@Override
	public RestProtobufRemoteService<M, T> getRemoteService() {
		return remoteService;
	}

	@Override
	public MessageConverter<M, T> getMessageConverter() {
		return messageConverter;
	}

	@Override
	public GeneratedExtension<SearchResultMessage, List<M>> getMessageTypeExtension() {
		return extension;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (systemPropertyService == null
				&& !entityDao.getPersistentClass().equals(SystemProperty.class)) {
			throw new IllegalArgumentException(
					"systemPropertyService cannot be null");
		}
	}

	/**
	 * Creates an entity locally or via a remote service, depending on the
	 * configuration of this service. Default is for local execution.
	 */
	@Override
	@Transactional
	public T createEntity(T entity) {
		if (isLocalService()) {
			log.debug("Calling createEntity locally");
			createEntityLocal(entity);
		} else {
			log.debug("Calling createEntity on "
					+ remoteService.getServiceURL());
			String username = getUsername();

			T result;
			if (StringUtils.isBlank(username)) {
				log.debug(getClass().getName()
						+ ".createEntity called without "
						+ "authentication. Remote will not have "
						+ "authenticatioon either.");
				result = remoteService.create(entity);
			} else {
				result = remoteService.createPreAuthenticated(getUsername(),
						entity);
			}
			Assert.notNull(result.getGuid(), "GUID not set on creation");
			Assert.notNull(result.getCreateDate(),
					"Create date not set on creation");
			Assert.notNull(result.getVersion(),
					"Version number not set on creation");
			ReflectionUtils.setFieldValue(entity, result.getGuid(), "guid");
			ReflectionUtils.setFieldValue(entity, result.getCreateDate(),
					"createDate");
			ReflectionUtils.setFieldValue(entity, result.getVersion(),
					"version");

		}
		return entity;
	}

	/**
	 * Looks up an entity locally or via a remote service, depending on the
	 * configuration of this service. Default is for local execution.
	 */
	@Override
	@Transactional
	public T retrieveEntity(String guid) {
		T result;
		if (isLocalService()) {
			log.debug("Calling retrieveEntity locally");
			result = retrieveEntityLocal(guid);
		} else {
			log.debug("Calling retrieveEntity on "
					+ remoteService.getServiceURL());
			String username = getUsername();
			if (StringUtils.isBlank(username)) {
				log.debug(getClass().getName()
						+ ".retrieveEntity called without "
						+ "authentication. Remote will not have "
						+ "authenticatioon either.");
				result = remoteService.get(guid);
			} else {
				result = remoteService.getPreAuthenticated(username, guid);
			}
		}
		return result;
	}

	/**
	 * Updates an entity locally or via a remote service, depending on the
	 * configuration of this service. Default is for local execution.
	 */
	@Override
	@Transactional
	public T updateEntity(String guid, T representation,
			List<String> unmodifiedFields) {
		T result;
		if (isLocalService()) {
			log.debug("Calling updateEntity locally");
			result = updateEntityLocal(guid, representation, unmodifiedFields);
		} else {
			log.debug("Calling updateEntity on "
					+ remoteService.getServiceURL());
			String username = getUsername();
			if (StringUtils.isBlank(username)) {
				log.debug(getClass().getName()
						+ ".updateEntity called without "
						+ "authentication. Remote will not have "
						+ "authenticatioon either.");
				result = remoteService.update(guid, representation);
			} else {
				result = remoteService.updatePreAuthenticated(username, guid,
						representation);
			}
			ReflectionUtils.setFieldValue(representation, result.getGuid(),
					"guid");
			ReflectionUtils.setFieldValue(representation,
					result.getCreateDate(), "createDate");
			ReflectionUtils.setFieldValue(representation, result.getVersion(),
					"version");

		}
		return result;
	}

	@Override
	@Transactional
	public T updateEntity(T representation) {
		Assert.notNull(representation.getGuid(), "GUID cannot be null");
		return updateEntity(representation.getGuid(), representation,
				new ArrayList<String>());
	}

	/**
	 * Deletes an entity locally or via a remote service, depending on the
	 * configuration of this service. Default is for local execution.
	 */
	@Override
	@Transactional
	public void deleteEntity(String guid) {
		if (isLocalService()) {
			log.debug("Calling delete locally");
			deleteEntityLocal(guid);
		} else {
			String username = getUsername();
			if (StringUtils.isBlank(username)) {
				log.debug(getClass().getName()
						+ ".deleteEntity called without "
						+ "authentication. Remote will not have "
						+ "authenticatioon either.");
				remoteService.delete(guid);
			} else {
				remoteService.deletePreAuthenticated(username, guid);
			}
		}
	}

	/**
	 * Deletes an entity locally or via a remote service, depending on the
	 * configuration of this service. Default is for local execution.
	 */
	@Override
	@Transactional
	public void deleteEntity(T entity) {
		if (isLocalService()) {
			log.debug("Calling delete locally");
			deleteEntityLocal(entity);
		} else {
			deleteEntity(entity.getGuid());
		}
	}

	/**
	 * Performs a search for entities locally or via a remote service, depending
	 * on the configuration of this service. Default is for local execution.
	 */
	@Override
	@Transactional
	public SearchResult<T> search(Query query) {
		SearchResult<T> result;
		if (isLocalService()) {
			log.debug("Calling search locally");
			result = searchLocal(query);
		} else {
			log.debug("Calling search on " + remoteService.getServiceURL());
			String username = getUsername();
			QueryMessageConverter conv = new QueryMessageConverter();
			QueryMessage q = conv.toMessage(query);

			if (StringUtils.isBlank(username)) {
				log.debug(getClass().getName() + ".search called without "
						+ "authentication. Remote will not have "
						+ "authenticatioon either.");
				result = remoteService.search(q);
			} else {
				result = remoteService.searchPreAuthenticated(username, q);
			}
		}
		return result;
	}

	/**
	 * Counts entities locally or via a remote service, depending on the
	 * configuration of this service. Default is for local execution.
	 */
	@Override
	@Transactional
	public long count(Query query) {
		long count;
		if (isLocalService()) {
			log.debug("Calling count locally");
			count = countLocal(query);
		} else {
			log.debug("Calling count on " + remoteService.getServiceURL());
			String username = getUsername();
			QueryMessageConverter conv = new QueryMessageConverter();
			QueryMessage q = conv.toMessage(query);

			if (StringUtils.isBlank(username)) {
				log.debug(getClass().getName() + ".count called without "
						+ "authentication. Remote will not have "
						+ "authenticatioon either.");
				count = remoteService.search(q).getTotalResults();
			} else {
				count = remoteService.searchPreAuthenticated(username, q)
						.getTotalResults();
			}
		}
		return count;
	}

	protected boolean getUseRemote() {
		return useRemote;
	}

	private String getUsername() {
		String username = null;
		SecurityContext ctx = SecurityContextHolder.getContext();
		if (ctx != null && ctx.getAuthentication() != null) {
			username = ctx.getAuthentication().getName();
		}
		return username;
	}

	protected boolean isLocalService() {
		boolean isLocal = true;
		if (getUseRemote()) {
			log.debug("Service call forced to remote API");
			return false;
		}
		if (systemPropertyService == null) {
			log.warn("systemPropertyService is null - executing service call locally");
			return true;
		}
		if (StringUtils.isBlank(role)) {
			// no role defined - handle the call locally
		} else {
			// Look up the system configuration for this service/role

			/*
			 * These properties are (should be) cached, so don't worry about
			 * calling this for every service call.
			 */
			Properties props = systemPropertyService
					.getAsProperties(SystemProperty.CATEGORY_SERVICES);

			String isLocalStr = (String) props
					.get(SystemProperty.SERVICE_KEY_PREFIX
							+ getServiceNameSysPropKey() + "." + role
							+ SystemProperty.SERVICE_IS_LOCAL_SUFFIX);
			isLocal = (isLocalStr == null || Boolean.valueOf(isLocalStr));
		}
		return isLocal;
	}

	@Override
	public void setSystemPropertyService(SystemPropertyService service) {
		this.systemPropertyService = service;
	}

	protected Logger log = LoggerFactory.getLogger(getClass());
}
