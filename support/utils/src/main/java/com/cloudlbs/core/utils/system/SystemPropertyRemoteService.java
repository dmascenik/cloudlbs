package com.cloudlbs.core.utils.system;

import java.util.Properties;

/**
 * Provides access to the centrally managed system properties.
 * 
 * @author Dan Mascenik
 * 
 */
public interface SystemPropertyRemoteService {

	public Properties findByCategory(String category);

	public String getSystemProperty(String key);

}
