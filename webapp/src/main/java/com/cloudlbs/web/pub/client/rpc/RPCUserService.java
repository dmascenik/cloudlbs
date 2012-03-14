package com.cloudlbs.web.pub.client.rpc;

import com.cloudlbs.web.pub.shared.model.LoginCredentials;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("user")
public interface RPCUserService extends RemoteService {

    boolean login(LoginCredentials credentials);

}
