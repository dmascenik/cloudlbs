package com.cloudlbs.web.service;

import com.cloudlbs.web.pub.shared.model.LoginCredentials;
import com.cloudlbs.web.pub.shared.model.NewUserDetails;

public interface UserService {
    String SERVLET_ATTRIBUTE = "userService";

    boolean login(LoginCredentials credentials);
    boolean createUser(NewUserDetails details);
    
}
