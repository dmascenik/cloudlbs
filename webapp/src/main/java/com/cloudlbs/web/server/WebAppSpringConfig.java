package com.cloudlbs.web.server;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.context.ServletContextAware;

import com.cloudlbs.web.service.LoginService;
import com.cloudlbs.web.service.LoginServiceImpl;

@Configuration
public class WebAppSpringConfig implements ServletContextAware {

    @Inject private AuthenticationManager authenticationManager;

    @Bean
    public LoginService loginService() {
        return new LoginServiceImpl(authenticationManager);
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        servletContext.setAttribute(LoginService.SERVLET_ATTRIBUTE, loginService());
    }

}
