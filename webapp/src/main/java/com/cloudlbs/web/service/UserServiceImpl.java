package com.cloudlbs.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

import com.cloudlbs.web.pub.shared.model.LoginCredentials;
import com.cloudlbs.web.pub.shared.model.NewUserDetails;

public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private AuthenticationManager authenticationManager;

    public UserServiceImpl(AuthenticationManager authenticationManager) {
        Assert.notNull(authenticationManager);
        this.authenticationManager = authenticationManager;
    }

    @Override
    public boolean login(LoginCredentials credentials) {
        try {
            // Just for playing with the working indicator
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String username = credentials.getUsername();
        String password = credentials.getPassword();
        if ("error".equals(username)) {
            throw new RuntimeException("BOOM");
        }

        try {
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username,
                    password);
            Authentication authResult = authenticationManager.authenticate(authRequest);
            if (logger.isDebugEnabled()) {
                logger.debug("Authentication success. Updating SecurityContextHolder to contain: " + authResult);
            }
            SecurityContextHolder.getContext().setAuthentication(authResult);
            return true;
        } catch (AuthenticationException failed) {
            // Authentication failed
            SecurityContextHolder.clearContext();

            if (logger.isDebugEnabled()) {
                logger.debug("Authentication request failed: " + failed.toString());
                logger.debug("Updated SecurityContextHolder to contain null Authentication");
            }
            return false;
        }
    }

    @Override
    public boolean createUser(NewUserDetails details) {
        try {
            // Just for playing with the working indicator
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String username = details.getUsername();
        String password = details.getPassword();
        if ("error".equals(username)) {
            throw new RuntimeException("BOOM");
        }
        
        return true;
    }

}
