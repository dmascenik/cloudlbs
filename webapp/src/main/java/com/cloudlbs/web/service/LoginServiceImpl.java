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

public class LoginServiceImpl implements LoginService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private AuthenticationManager authenticationManager;

    public LoginServiceImpl(AuthenticationManager authenticationManager) {
        Assert.notNull(authenticationManager);
        this.authenticationManager = authenticationManager;
    }

    @Override
    public boolean login(LoginCredentials credentials) {
        String username = credentials.getUsername();
        String password = credentials.getPassword();
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

}
