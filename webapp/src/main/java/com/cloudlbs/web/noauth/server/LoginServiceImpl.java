package com.cloudlbs.web.noauth.server;

import com.cloudlbs.web.noauth.client.LoginService;
import com.cloudlbs.web.noauth.shared.model.LoginCredentials;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

    @Override
    public boolean login(LoginCredentials credentials) {
        if (credentials.getUsername().equals("dan")) {
            return true;
        }
        return false;
    }

}
