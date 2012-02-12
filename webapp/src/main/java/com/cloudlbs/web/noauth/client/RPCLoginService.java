package com.cloudlbs.web.noauth.client;

import com.cloudlbs.web.noauth.shared.model.LoginCredentials;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("login")
public interface RPCLoginService extends RemoteService {

    boolean login(LoginCredentials credentials);

}
