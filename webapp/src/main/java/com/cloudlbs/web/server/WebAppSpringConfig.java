package com.cloudlbs.web.server;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.context.ServletContextAware;

import com.cloudlbs.web.service.UserService;
import com.cloudlbs.web.service.UserServiceImpl;

@Configuration
@ImportResource("classpath:/spring/spring-http-security.xml")
public class WebAppSpringConfig implements ServletContextAware {

    @Inject private AuthenticationManager authenticationManager;

    @Bean
    public UserService loginService() {
        return new UserServiceImpl(authenticationManager);
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        servletContext.setAttribute(UserService.SERVLET_ATTRIBUTE, loginService());
    }

}
