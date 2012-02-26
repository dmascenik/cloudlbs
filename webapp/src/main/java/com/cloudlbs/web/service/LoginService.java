package com.cloudlbs.web.service;

import com.cloudlbs.web.pub.shared.model.LoginCredentials;

public interface LoginService {
    String SERVLET_ATTRIBUTE = "loginService";

    boolean login(LoginCredentials credentials);
}
