package com.cloudlbs.web.server.pub;

import javax.servlet.ServletException;

import com.cloudlbs.web.pub.client.rpc.RPCUserService;
import com.cloudlbs.web.pub.shared.model.LoginCredentials;
import com.cloudlbs.web.pub.shared.model.NewUserDetails;
import com.cloudlbs.web.service.UserService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class RPCUserServiceImpl extends RemoteServiceServlet implements RPCUserService {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = (UserService) getServletContext().getAttribute(UserService.SERVLET_ATTRIBUTE);
    }

    @Override
    public boolean login(LoginCredentials credentials) {
        return userService.login(credentials);
    }

    @Override
    public boolean createUser(NewUserDetails details) {
        return userService.createUser(details);
    }

}
