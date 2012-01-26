package com.cloudlbs.platform.service.internal;

import java.util.Properties;

import com.cloudlbs.platform.core.InternalGenericService;
import com.cloudlbs.platform.domain.SystemProperty;
import com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage;

/**
 * This provides some interface methods that make the system property calls
 * cacheable via interceptors. An implementation of this class should annotate
 * the methods required by this interface as cacheable, and annotate any methods
 * that modify system properties as triggering removal from the cache.
 * 
 * @author Dan Mascenik
 * 
 */
public interface SystemPropertyService extends
		InternalGenericService<SystemPropertyMessage, SystemProperty> {

	public Properties getAsProperties(String category);

}
