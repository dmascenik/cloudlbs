package com.cloudlbs.web.server.noauth;

import javax.servlet.ServletException;

import com.cloudlbs.web.noauth.client.RPCLoginService;
import com.cloudlbs.web.noauth.shared.exception.EvilException;
import com.cloudlbs.web.noauth.shared.model.LoginCredentials;
import com.cloudlbs.web.service.LoginService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class RPCLoginServlet extends RemoteServiceServlet implements RPCLoginService {

    private LoginService loginService;

    @Override
    public void init() throws ServletException {
        loginService = (LoginService) getServletContext().getAttribute(LoginService.SERVLET_ATTRIBUTE);
    }

    @Override
    public boolean login(LoginCredentials credentials) throws EvilException {
        return loginService.login(credentials);
    }

}
