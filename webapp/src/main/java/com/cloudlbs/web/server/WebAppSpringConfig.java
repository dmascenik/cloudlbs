package com.cloudlbs.web.server;

import javax.servlet.ServletContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ServletContextAware;

import com.cloudlbs.web.service.LoginService;
import com.cloudlbs.web.service.LoginServiceImpl;

@Configuration
public class WebAppSpringConfig implements ServletContextAware {

    @Bean
    public LoginService loginService() {
        return new LoginServiceImpl();
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        servletContext.setAttribute(LoginService.SERVLET_ATTRIBUTE, loginService());
    }

}
