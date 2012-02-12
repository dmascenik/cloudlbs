package com.cloudlbs.web.service;

import com.cloudlbs.web.noauth.shared.exception.EvilException;
import com.cloudlbs.web.noauth.shared.model.LoginCredentials;

public interface LoginService {
    String SERVLET_ATTRIBUTE = "loginService";

    boolean login(LoginCredentials credentials) throws EvilException;
}
