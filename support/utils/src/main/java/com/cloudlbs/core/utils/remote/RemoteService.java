package com.cloudlbs.core.utils.remote;

import java.security.PrivateKey;

import com.cloudlbs.core.utils.search.SearchResult;
import com.cloudlbs.core.utils.service.GenericService;
import com.cloudlbs.platform.protocol.SearchProto.QueryMessage;

/**
 * A {@link RemoteService} is an interface for calling typical CRUD/S operations
 * on a remote host. It is meant to be structurally similar to
 * {@link GenericService}, which is for local services. All the operations take
 * an optional sessionId parameter which may be used for authentication by the
 * implementation.
 * 
 * @see SecureRestTemplate
 * 
 * @author Dan Mascenik
 * 
 * @param <M> the over-the-wire type
 * @param <T> the deserialized type (may be the same as M)
 * 
 */
public interface RemoteService<M, T> {

	public void setResourceStub(String resourceStub);
	public void setServiceUrl(String serviceUrl);
	
	public T create(T obj);
	public T create(String sessionId, T obj);
	public T createPreAuthenticated(String username, T obj);
	public T create(String clientId, PrivateKey key, T obj);
	public T createFromMessage(M obj);
	public T createFromMessage(String sessionId, M obj);
	public T createFromMessagePreAuthenticated(String username, M obj);
	public T createFromMessage(String clientId, PrivateKey key, M obj);

	public T get(String guid);
	public T get(String sessionId, String guid);
	public T getPreAuthenticated(String username, String guid);
	public T get(String clientId, PrivateKey key, String guid);

	public T update(String guid, T obj);
	public T update(String sessionId, String guid, T obj);
	public T updatePreAuthenticated(String username, String guid, T obj);
	public T update(String clientId, PrivateKey key, String guid, T obj);
	public T updateFromMessage(String guid, M obj);
	public T updateFromMessage(String sessionId, String guid, M obj);
	public T updateFromMessagePreAuthenticated(String username, String guid, M obj);
	public T updateFromMessage(String clientId, PrivateKey key, String guid, M obj);

	public void delete(String guid);
	public void delete(String sessionId, String guid);
	public void deletePreAuthenticated(String username, String guid);
	public void delete(String clientId, PrivateKey key, String guid);

	public SearchResult<T> search(QueryMessage query);
	public SearchResult<T> search(String sessionId, QueryMessage query);
	public SearchResult<T> searchPreAuthenticated(String username, QueryMessage query);
	public SearchResult<T> search(String clientId, PrivateKey key, QueryMessage query);

}
