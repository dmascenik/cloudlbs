package com.cloudlbs.web.pub.client;

import com.cloudlbs.web.pub.shared.model.LoginCredentials;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("login")
public interface RPCLoginService extends RemoteService {

    boolean login(LoginCredentials credentials);

}
