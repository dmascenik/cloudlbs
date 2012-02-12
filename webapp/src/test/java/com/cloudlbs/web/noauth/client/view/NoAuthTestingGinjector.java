package com.cloudlbs.web.noauth.client.view;

import com.cloudlbs.web.noauth.client.NoAuthGinModule;
import com.cloudlbs.web.noauth.client.NoAuthGinjector;
import com.cloudlbs.web.noauth.shared.model.LoginCredentials;
import com.cloudlbs.web.noauth.shared.model.NewUserDetails;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.inject.client.GinModules;

@GinModules(NoAuthGinModule.class)
public interface NoAuthTestingGinjector extends NoAuthGinjector {

    LoginForm<LoginCredentials> getLoginForm();

    NewUserForm<NewUserDetails> getNewUserForm();

    HandlerManager getEventBus();
}
