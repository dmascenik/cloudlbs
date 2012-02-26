package com.cloudlbs.web.pub.client.view;

import com.cloudlbs.web.pub.client.PublicGinModule;
import com.cloudlbs.web.pub.client.PublicGinjector;
import com.cloudlbs.web.pub.client.view.LoginForm;
import com.cloudlbs.web.pub.client.view.NewUserForm;
import com.cloudlbs.web.pub.shared.model.LoginCredentials;
import com.cloudlbs.web.pub.shared.model.NewUserDetails;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.inject.client.GinModules;

@GinModules(PublicGinModule.class)
public interface PublicTestingGinjector extends PublicGinjector {

    LoginForm<LoginCredentials> getLoginForm();

    NewUserForm<NewUserDetails> getNewUserForm();

    HandlerManager getEventBus();
}
