package com.cloudlbs.platform.core;

import com.cloudlbs.core.utils.remote.RestProtobufRemoteService;
import com.cloudlbs.platform.initializer.ServiceInitializer;
import com.cloudlbs.platform.service.internal.SystemPropertyService;
import com.google.protobuf.Message;

/**
 * Interface for services that may be called locally or remotely.
 * 
 * @see ServiceInitializer
 * 
 * @author Dan Mascenik
 * 
 */
public interface RemoteableService<M extends Message, T> {

	public void setSystemPropertyService(SystemPropertyService service);

	public void setUseRemote(boolean useRemote);

	public RestProtobufRemoteService<M, T> getRemoteService();

	public String getServiceNameSysPropKey();

}
