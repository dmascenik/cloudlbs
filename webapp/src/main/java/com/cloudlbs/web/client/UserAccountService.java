package com.cloudlbs.web.client;

import java.util.List;

import com.cloudlbs.web.shared.dto.UserAccountDTO;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author Dan Mascenik
 * 
 */
@RemoteServiceRelativePath("springGwtServices/user")
public interface UserAccountService extends RemoteService {

	public List<UserAccountDTO> search(String query);

	public UserAccountDTO create(UserAccountDTO representation);
	
}
