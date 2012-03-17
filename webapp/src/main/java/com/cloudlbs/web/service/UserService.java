package com.cloudlbs.web.service;

import com.cloudlbs.web.pub.shared.model.UsernamePasswordAuthentication;
import com.cloudlbs.web.pub.shared.model.NewUserDetails;

public interface UserService {
    String SERVLET_ATTRIBUTE = "userService";

    boolean login(UsernamePasswordAuthentication credentials);
    boolean createUser(NewUserDetails details);
    
}
