package com.cloudlbs.web.service;

import com.cloudlbs.web.noauth.shared.model.LoginCredentials;

public class LoginServiceImpl implements LoginService {

    @Override
    public boolean login(LoginCredentials credentials) {
        if (credentials.getUsername().equals("dan")) {
            System.out.println("Login success");
            return true;
        } else if (credentials.getUsername().equals("error")) {
            throw new RuntimeException("BOOM");
        }
        System.out.println("Login failed");
        return false;
    }

}
