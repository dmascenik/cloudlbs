package com.cloudlbs.sls.http;

/**
 * @author Dan Mascenik
 *
 */
public interface RemoteService<M> {

	public M get(String guid) throws Exception;
	public M create(M representation) throws Exception;
	public void delete(String guid) throws Exception;
	public M update(String guid, M representation) throws Exception;
	
}
